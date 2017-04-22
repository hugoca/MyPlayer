package com.huanghj.mp3.Gson;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/4/18.
 */
public class Music {
    private int vip;
    private String Song_name;
    private int singer_id;
    private String singer_name;
    private int artist_flag;
    private int album_id;
    private String album_name;
    private int pick_count;
    private ArrayList<Audition> audition_list;

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getSong_name() {
        return Song_name;
    }

    public void setSong_name(String song_name) {
        Song_name = song_name;
    }

    public int getSinger_id() {
        return singer_id;
    }

    public void setSinger_id(int singer_id) {
        this.singer_id = singer_id;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public int getArtist_flag() {
        return artist_flag;
    }

    public void setArtist_flag(int artist_flag) {
        this.artist_flag = artist_flag;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getPick_count() {
        return pick_count;
    }

    public void setPick_count(int pick_count) {
        this.pick_count = pick_count;
    }

    public ArrayList<Audition> getAudition_list() {
        return audition_list;
    }

    public void setAudition_list(ArrayList<Audition> audition_list) {
        this.audition_list = audition_list;
    }
}
