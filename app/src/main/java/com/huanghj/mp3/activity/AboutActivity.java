package com.huanghj.mp3.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.loader.AsynImageLoader;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.PhotoUtil;

public class AboutActivity extends AppCompatActivity {
    private ImageView imageView;
    private MusicInfo musicInfo;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_about);
        BackGutil.setBg(relativeLayout, this);
        imageView= (ImageView) findViewById(R.id.iamg);

        AsynImageLoader asynImageLoader = new AsynImageLoader();
        String url="http://f.hiphotos.baidu.com/image/h%3D200/sign=3853eb794f540923b569647ea259d1dc/50da81cb39dbb6fde784f07c0f24ab18962b3788.jpg";
        asynImageLoader.showImageAsyn(imageView, url, R.mipmap.nom);

    }
}
