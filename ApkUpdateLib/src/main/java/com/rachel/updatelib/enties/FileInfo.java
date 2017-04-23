package com.rachel.updatelib.enties;

import java.io.File;
import java.io.Serializable;

/**
 * Created by zhengshaorui on 2017/4/20.
 */

public class FileInfo implements Serializable{
    private String filename;
    private int versioncode;
    private String versionname;
    private String versionmsg;
    private String fileurl;
    private long filesize;
    private File FileDir;


    public FileInfo() {
    }

    public FileInfo(String filename, int versioncode, String versionname, String versionmsg,
                    String fileurl, long filesize, File fileDir) {
        this.filename = filename;
        this.versioncode = versioncode;
        this.versionname = versionname;
        this.versionmsg = versionmsg;
        this.fileurl = fileurl;
        this.filesize = filesize;

        FileDir = fileDir;
    }



    public static class  Builder{
        private String filename;
        private int versioncode;
        private String versionname;
        private String versionmsg;
        private String fileurl;
        private long filesize;
        private File FileDir;
        public Builder setFileName(String filename){
            this.filename = filename;
            return this;
        }

    }

    public File getFileDir() {
        return FileDir;
    }

    public void setFileDir(File fileDir) {
        FileDir = fileDir;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }





    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getVersioncode() {
        return versioncode;
    }

    public void setVersioncode(int versioncode) {
        this.versioncode = versioncode;
    }

    public String getVersionname() {
        return versionname;
    }

    public void setVersionname(String versionname) {
        this.versionname = versionname;
    }

    public String getVersionmsg() {
        return versionmsg;
    }

    public void setVersionmsg(String versionmsg) {
        this.versionmsg = versionmsg;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "filename='" + filename + '\'' +
                ", versioncode=" + versioncode +
                ", versionname='" + versionname + '\'' +
                ", versionmsg='" + versionmsg + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", filesize=" + filesize +
                ", FileDir=" + FileDir +
                '}';
    }
}
