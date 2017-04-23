package com.rachel.updatelib;

/**
 * Created by Administrator on 2017/4/21.
 */

public abstract class DownloadViewAdapter implements IDownloadView {
    public void updateProgress(int progress){};
    public void setDownloadSize(String downloadSize){};
    public void setFileSize(String fileSize){};
    public void downloadSuccess(){};
    public void downloadFail(String errorMsg){};
}
