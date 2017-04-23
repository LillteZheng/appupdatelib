package com.rachel.updatelib.enties;

/**
 * Created by zhengshaorui on 2017/4/20.
 */

public class LocalInfo {
    private int versioncode;
    private String versionname;

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

    @Override
    public String toString() {
        return "LocalInfo{" +
                "versioncode=" + versioncode +
                ", versionname='" + versionname + '\'' +
                '}';
    }
}
