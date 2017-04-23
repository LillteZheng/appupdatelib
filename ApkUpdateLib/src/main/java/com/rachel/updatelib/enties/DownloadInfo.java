package com.rachel.updatelib.enties;

/**
 * Created by zhengshaorui on 2017/4/17.
 */
public class DownloadInfo {
    private int progress;
    private String dwonloadSize;
    private String fileSize;


    public DownloadInfo() {
    }

    public DownloadInfo(int progress, String dwonloadSize,
                        String fileSize) {
        this.progress = progress;
        this.dwonloadSize = dwonloadSize;
        this.fileSize = fileSize;

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDwonloadSize() {
        return dwonloadSize;
    }

    public void setDwonloadSize(String dwonloadSize) {
        this.dwonloadSize = dwonloadSize;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }



    @Override
    public String toString() {
        return "DownloadInfo{" +
                "progress=" + progress +
                ", dwonloadSize='" + dwonloadSize + '\'' +
                ", fileSize='" + fileSize + '\'' +
                '}';
    }
}
