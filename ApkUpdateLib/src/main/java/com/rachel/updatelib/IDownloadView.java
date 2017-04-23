package com.rachel.updatelib;

/**
 * Created by Administrator on 2017/4/21.
 */

public interface IDownloadView {
    void updateProgress(int progress);
    void setDownloadSize(String downloadSize);
    void setFileSize(String fileSize);
    void downloadSuccess();
    void downloadFail(String errorMsg);

}
