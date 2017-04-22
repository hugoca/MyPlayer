package com.huanghj.mp3.entity;

import android.media.MediaPlayer;

/**
 * Created by Administrator on 2016/6/3 0003.
 */
public class WordEvent {
    public MediaPlayer mediaPlayer;
    public String url;

    public WordEvent(MediaPlayer mediaPlayer,String url) {
        this.mediaPlayer = mediaPlayer;
        this.url=url;
    }
}
