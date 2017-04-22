package com.huanghj.mp3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.MusicInfo;

public class MusicInfoActivity extends AppCompatActivity {
    private TextView mName, mArtist, mSize, mDuration, mURl, mAblum;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_info);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_musicInfo);
        BackGutil.setBg(relativeLayout, this);
        initView();
        initData();
    }

    private void initView() {
        mName = (TextView) findViewById(R.id.tv_songname);
        mAblum = (TextView) findViewById(R.id.tv_ablum);
        mArtist = (TextView) findViewById(R.id.tv_artist);
        mDuration = (TextView) findViewById(R.id.tv_duration);
        mSize = (TextView) findViewById(R.id.tv_size);
        mURl = (TextView) findViewById(R.id.tv_url);
    }

    private void initData() {
        Intent intent = getIntent();
        MusicInfo musicInfo = intent.getParcelableExtra(PlayService.MUSICINFO);
        mName.setText(musicInfo.getTitle());
        mArtist.setText(musicInfo.getArtist());
        mAblum.setText(musicInfo.getAlbum());
        mSize.setText(MusicInfo.formatSize(musicInfo.getSize()));
        mDuration.setText(MusicInfo.formatTime(musicInfo.getDuration()));
        mURl.setText(musicInfo.getUrl());
    }
}
