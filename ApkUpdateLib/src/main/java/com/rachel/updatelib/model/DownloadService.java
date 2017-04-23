package com.rachel.updatelib.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.enties.ThreadInfo;
import com.rachel.updatelib.presenter.IUpdatePresenter;
import com.rachel.updatelib.toolUtils.ToolUtils;
import com.rachel.updatelib.toolUtils.log;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * Created by zhengshaorui on 2017/4/15.
 */

public class DownloadService extends Service {
    private Context mContext;
    private FileInfo mFileInfo;
    private DownloadTask mDownloadTask;
    private DownloadBinder mBinder = new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        mFileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");

        File file = new File(ToolUtils.getFilePath());
        if (!file.exists()){
            file.mkdir();
        }
        mFileInfo.setFileDir(file);
        mDownloadTask = new DownloadTask();

        return mBinder;
    }



    class DownloadBinder extends Binder{

        public void startDownload(IUpdatePresenter callback){
           if (mDownloadTask != null){
               if (!mDownloadTask.isThreadRunning){
                   mDownloadTask.isPause = false;
                   mDownloadTask.startDownload(mFileInfo,callback);
               }
           }
        }

        public void pause(){
            if (mDownloadTask != null){
                if (mDownloadTask.isThreadRunning){
                    mDownloadTask.isPause = true;
                }
            }
        }

        public void ReStart(IUpdatePresenter callback){
            log.d("running: "+mDownloadTask.isThreadRunning);
            log.d("fileinfo: "+mFileInfo);
            if (mDownloadTask != null){
                if (!mDownloadTask.isThreadRunning){
                    mDownloadTask.isPause = false;
                    mDownloadTask.startDownload(mFileInfo,callback);
                }
            }
        }


        public void delete(){
            if (mDownloadTask != null){
                mDownloadTask.isThreadRunning = false;
                mDownloadTask.isPause = true;
                DataSupport.deleteAll(ThreadInfo.class,"url = ?",mFileInfo.getFileurl());
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDownloadTask != null){
            mDownloadTask.isThreadRunning = false;
            mDownloadTask.isPause = true;
            DataSupport.deleteAll(ThreadInfo.class,"url = ?",mFileInfo.getFileurl());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


}
