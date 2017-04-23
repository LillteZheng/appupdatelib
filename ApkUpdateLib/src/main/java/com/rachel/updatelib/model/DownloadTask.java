package com.rachel.updatelib.model;



import com.rachel.updatelib.enties.DownloadInfo;
import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.enties.ThreadInfo;
import com.rachel.updatelib.presenter.IUpdatePresenter;
import com.rachel.updatelib.toolUtils.ToolUtils;
import com.rachel.updatelib.toolUtils.log;

import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhengshaorui on 2017/4/15.
 */

public class DownloadTask {
    private long downloadlength;
    private FileInfo mFileInfo;
    private static final int THREADCOUNT = 3; //下载线程数为3
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(3);
    List<DownloadTaskThread> mDownloadTaskThreadList;
    public volatile boolean isThreadRunning = false;
    public volatile boolean isPause = false;
    private IUpdatePresenter mIUpdatePresenter;


    public DownloadTask() {

    }

    public void startDownload(FileInfo fileInfo, IUpdatePresenter callback) {
        mFileInfo = fileInfo;
        mIUpdatePresenter = callback;
        List<ThreadInfo> threadInfos = DataSupport.where("url = ?",fileInfo.getFileurl()).find(ThreadInfo.class);
        if (threadInfos.size() == 0){ //此时数据库中并没有存在任何信息
            for (int i = 0; i < THREADCOUNT; i++) {
                int blocksize = (int)fileInfo.getFilesize()/THREADCOUNT;//每个线程分配的大小
                ThreadInfo threadInfo = new ThreadInfo(i,fileInfo.getFileurl(),0,blocksize*i,blocksize*(i+1)-1);
                if (i == THREADCOUNT -1){ //最后一个除不尽，则用文件的总大小填进去
                    threadInfo.setEndpos((int)fileInfo.getFilesize());
                }
                threadInfo.save();//保存到数据库中
                threadInfos.add(threadInfo);
            }

        }
        mDownloadTaskThreadList = new ArrayList<>();
        for (ThreadInfo threadInfo : threadInfos){
            DownloadTaskThread downloadTaskThread = new DownloadTaskThread(threadInfo);
            mExecutorService.execute(downloadTaskThread);
            log.d("thread: "+(threadInfo.getStartpos()+threadInfo.getThreadfinished())+" "+threadInfo.getEndpos());
            mDownloadTaskThreadList.add(downloadTaskThread); //管理起来，方便判断下载完成或者暂停等等
        }
    }

    /**
     * 任务是否下载完成判断
     */
    private synchronized void checkAllThreadFinished() {
        boolean allFinish = true;
        for (DownloadTaskThread downloadTaskThread : mDownloadTaskThreadList){
            if (!downloadTaskThread.isThreadFinished){
                allFinish = false;
                break;
            }
        }
        if (allFinish){
            log.d("all download finish");
            isThreadRunning = false;
            String filesize = ToolUtils.getFloatSize(mFileInfo.getFilesize());
            DownloadInfo downloadInfo = new DownloadInfo(100,filesize,filesize);
            mIUpdatePresenter.getDownloadInfo(downloadInfo);
            mIUpdatePresenter.success(ToolUtils.getFilePath()+mFileInfo.getFilename());
            //下载完成，则把线程信息删了
            DataSupport.deleteAll(ThreadInfo.class,"url = ?",mFileInfo.getFileurl());
        }

    }

    class DownloadTaskThread extends Thread{
        ThreadInfo threadInfo;
        public boolean isThreadFinished = false;//单个线程下载完成否
        public DownloadTaskThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }
        @Override
        public void run() {
            super.run();
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            BufferedInputStream bis = null;
            try {
                URL url = new URL(this.threadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                //加上这句是为了防止connection.getContentLength()获取不到
                conn.setRequestProperty(
                        "Accept",
                        "image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
                                "application/x-shockwave-flash, application/xaml+xml, " +
                                "application/vnd.ms-xpsdocument, application/x-ms-xbap, " +
                                "application/x-ms-application, application/vnd.ms-excel, " +
                                "application/vnd.ms-powerpoint, application/msword, */*");
                conn.setRequestProperty("Accept-Language", "zh-CN");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.setRequestMethod("GET");
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                int start = threadInfo.getStartpos() + threadInfo.getThreadfinished();
                int end = threadInfo.getEndpos();
                //设置下载位置
                conn.setRequestProperty("Range", "bytes="+start+"-"+end);
                //文件写入位置
                File file = new File(mFileInfo.getFileDir(),mFileInfo.getFilename());
                raf = new RandomAccessFile(file,"rwd");
                raf.seek(start);
                int len = -1;
                byte[] bytes = new byte[1024*4];
                bis = new BufferedInputStream(conn.getInputStream());
                long time = System.currentTimeMillis();
                while( (len = bis.read(bytes)) != -1 ){
                    raf.write(bytes,0,len);
                    downloadlength += len;

                    threadInfo.setThreadfinished(threadInfo.getThreadfinished()+len);
                    if (isPause){
                        //数据库保存暂停时的数据,用线程和id来作为标识
                        threadInfo.updateAll("url = ? and id = ?",threadInfo.getUrl(),threadInfo.getId()+"");
                        isThreadRunning = false;
                        return;
                    }
                    if (System.currentTimeMillis() -  time > 500){
                        int progress = (int)(downloadlength*100/mFileInfo.getFilesize());
                        log.d("progress: "+ progress);
                        String downloadsize = ToolUtils.getFloatSize(downloadlength);
                        String filesize = ToolUtils.getFloatSize(mFileInfo.getFilesize());
                        DownloadInfo downloadInfo = new DownloadInfo(progress,downloadsize,filesize);
                        mIUpdatePresenter.getDownloadInfo(downloadInfo);
                        time = System.currentTimeMillis();
                    }
                    isThreadRunning = true;

                }

                isThreadFinished = true;
                threadInfo.save();
                checkAllThreadFinished();

            } catch (Exception e) {
                e.printStackTrace();
                isPause = true;
                DataSupport.deleteAll(ThreadInfo.class,"url = ?",mFileInfo.getFileurl());
                mIUpdatePresenter.errorToast(e.toString());
            }finally {
                if (bis != null) try {
                    bis.close();
                    if (raf != null) raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (conn != null) conn.disconnect();
            }
        }
    }
}
