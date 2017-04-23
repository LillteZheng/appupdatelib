package com.rachel.updatelib.presenter;


import com.rachel.updatelib.enties.DownloadInfo;

/**
 * Created by zhengshaorui on 2017/4/17.
 */

public interface IUpdatePresenter {
    void getDownloadInfo(DownloadInfo downloadInfo);
    void errorToast(String errorMsg);
    void success(String path);
    void onDestroy();
    void pause();
    void restart();
    void delete();
}
