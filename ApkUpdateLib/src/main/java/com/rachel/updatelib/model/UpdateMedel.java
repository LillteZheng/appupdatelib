package com.rachel.updatelib.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


import com.rachel.updatelib.UpdateLibAppLication;
import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.presenter.IUpdatePresenter;
import com.rachel.updatelib.toolUtils.log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/14.
 */

public class UpdateMedel implements IUpdateModel {
    private Context mContext = UpdateLibAppLication.getContext();
    private IUpdatePresenter mIUpdatePresenter;
    private DownloadService.DownloadBinder mBinder;

    public UpdateMedel(IUpdatePresenter mIUpdatePresenter) {
        this.mIUpdatePresenter = mIUpdatePresenter;
    }

    @Override
    public void download(FileInfo fileInfo) {
        Intent intent = new Intent(mContext,DownloadService.class);
        intent.putExtra("fileinfo",fileInfo);
        mContext.bindService(intent,conn,Context.BIND_AUTO_CREATE);
        Timer timer = new Timer(); //绑定需要时间，隔500ms后再连接
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mBinder.startDownload(mIUpdatePresenter);
            }
        },500);

    }


    private ServiceConnection conn  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (DownloadService.DownloadBinder) service;
            log.d("binder");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
            log.d("unbinder");
        }
    };


    public void onDestroy(){
        if (conn != null) {
            mContext.unbindService(conn);
        }
    }

    @Override
    public void pause() {
        if (mBinder != null){
            mBinder.pause();
        }
    }

    @Override
    public void restart() {
        if (mBinder != null){
            mBinder.ReStart(mIUpdatePresenter);
        }
    }

    @Override
    public void delete() {
        if (mBinder != null){
            mBinder.delete();
        }
    }


}
