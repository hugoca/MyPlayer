package com.huanghj.mp3.util;

import android.media.MediaPlayer;

/**
 * Created by myhug on 2016/4/20.
 */
public class Media {

    static MediaPlayer mediaPlayer;
    public static MediaPlayer getInstance(){
        if(mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        return mediaPlayer;
    }
}
