package com.rachel.updatelib.enties;



import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by zhengshaorui on 2017/4/15.
 */

public class ThreadInfo extends DataSupport implements Serializable {
    private int id;
    private String url;
    private int threadfinished;
    private int startpos;
    private int endpos;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, int threadfinished, int startpos, int endpos) {
        this.id = id;
        this.url = url;
        this.threadfinished = threadfinished;
        this.startpos = startpos;
        this.endpos = endpos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getThreadfinished() {
        return threadfinished;
    }

    public void setThreadfinished(int threadfinished) {
        this.threadfinished = threadfinished;
    }

    public int getStartpos() {
        return startpos;
    }

    public void setStartpos(int startpos) {
        this.startpos = startpos;
    }

    public int getEndpos() {
        return endpos;
    }

    public void setEndpos(int endpos) {
        this.endpos = endpos;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", threadfinished=" + threadfinished +
                ", startpos=" + startpos +
                ", endpos=" + endpos +
                '}';
    }
}
