package com.rachel.updatelib.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.rachel.updatelib.IDownloadView;
import com.rachel.updatelib.UpdateLibAppLication;
import com.rachel.updatelib.enties.DownloadInfo;
import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.model.IUpdateModel;
import com.rachel.updatelib.model.UpdateMedel;
import com.rachel.updatelib.toolUtils.log;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhengshaorui on 2017/4/15.
 */

public class UpdatePresenter implements IUpdatePresenter{
    private Context mContext = UpdateLibAppLication.getContext();
    private IUpdateModel mUpdateModel;
    private IDownloadView mIDownloadView;
    public static String mApkPath = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private UpdatePresenter(IDownloadView iDownloadView){

        mIDownloadView = iDownloadView;
        mUpdateModel = new UpdateMedel(this);

    };



    private volatile static UpdatePresenter sUpdatePresenter;

    public static UpdatePresenter getInstance(IDownloadView iDownloadView){
        if (sUpdatePresenter == null){
            synchronized (UpdatePresenter.class){
                if (sUpdatePresenter == null){
                    sUpdatePresenter = new UpdatePresenter(iDownloadView);
                }
            }
        }
        return sUpdatePresenter;
    }





    public void startDownload(FileInfo fileInfo){
        mUpdateModel.download(fileInfo);
    }


    @Override
    public void getDownloadInfo(final DownloadInfo downloadInfo) {
        //log.d("downloadInfo: "+downloadInfo);

        mHandler.post(new Runnable() { //由于数据是在线程返回，这里更新到UI，需要用主线程更新
            @Override
            public void run() {
                mIDownloadView.updateProgress(downloadInfo.getProgress());
                mIDownloadView.setDownloadSize(downloadInfo.getDwonloadSize());
                mIDownloadView.setFileSize(downloadInfo.getFileSize());
            }
        });

    }

    @Override
    public void errorToast(final String errorMsg) {
        log.d("errorMsg: "+errorMsg);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mIDownloadView.downloadFail(errorMsg);
            }
        });

    }

    @Override
    public void success(String path) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mIDownloadView.downloadSuccess();
            }
        });
        mApkPath = path;
        File file = new File(path);
        if (!file.exists()){
            return ;
        }

        mUpdateModel.onDestroy(); //当下载完成就取消绑定service，防止出错
      //  log.d("path: "+path);
        chmod(path); //需要修改权限，不然packageinstallactivity无法解析其他包下的apk
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true); //表明不是未知来源
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        UpdateLibAppLication.getContext().startActivity(intent);



    }

    /**
     * 添加权限
     * @param path
     */
    private void chmod(String path){
        if (path.contains("/data")){  //data下才需要改权限
            String[] paths = path.split("/");
            String splitpath = "/";
            for (String p : paths){
                if (!p.equals("")){
                    splitpath += p +"/";
                    String commond = "chmod 777 "+splitpath;
                    try {
                        Runtime.getRuntime().exec(commond);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            String commond = "chmod 777 "+path;
            try {
                Runtime.getRuntime().exec(commond);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void onDestroy(){
        if (mUpdateModel != null){
            mUpdateModel.onDestroy();
        }
    }

    @Override
    public void pause() {
        mUpdateModel.pause();
    }

    @Override
    public void restart() {
        mUpdateModel.restart();
    }

    @Override
    public void delete() {
        mUpdateModel.delete();
    }
}
