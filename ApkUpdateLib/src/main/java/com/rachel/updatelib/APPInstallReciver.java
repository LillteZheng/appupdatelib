package com.rachel.updatelib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import com.rachel.updatelib.presenter.UpdatePresenter;
import com.rachel.updatelib.toolUtils.log;

import java.io.File;

/**
 * Created by Administrator on 2017/4/20.
 */

public class APPInstallReciver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_PACKAGE_ADDED)
                || action.equals(Intent.ACTION_PACKAGE_CHANGED)){
            //只有自己的更新了，才删除，毕竟是自己在线升级，其他应用的安装，不管
          //  if (intent.getDataString().contains("com.rachel.apkupdate")){
                String path = UpdatePresenter.mApkPath;
                log.d("path:  "+path);
                if (path != null){
                    File file = new File(path);
                    if (file.exists()){
                        file.delete();
                    }
                }

          //  }
        }
    }
}
