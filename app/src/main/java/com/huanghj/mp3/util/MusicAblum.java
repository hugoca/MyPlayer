package com.huanghj.mp3.util;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobObject;


/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class MusicAblum extends BmobObject{
    private String name;
    private String bitmap1;
    private  String bitmap2;
    private  String bitmap3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBitmap1() {
        return bitmap1;
    }

    public void setBitmap1(String bitmap1) {
        this.bitmap1 = bitmap1;
    }

    public String getBitmap3() {
        return bitmap3;
    }

    public void setBitmap3(String bitmap3) {
        this.bitmap3 = bitmap3;
    }

    public String getBitmap2() {
        return bitmap2;
    }

    public void setBitmap2(String bitmap2) {
        this.bitmap2 = bitmap2;
    }
}
