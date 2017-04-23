package com.rachel.updatelib.model;


import com.rachel.updatelib.enties.FileInfo;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface IUpdateModel {
    void download(FileInfo fileInfo);
    void onDestroy();
    void pause();
    void restart();
    void delete();
}
