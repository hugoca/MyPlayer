package com.huanghj.mp3.entity;

/**
 * Created by xiefei on 2016/3/5.
 * 获取歌曲下载信息的实体类
 */
public class DownLoadMsg {

    private String mp3Url;
    private String name;
    private int duration;

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }
}
