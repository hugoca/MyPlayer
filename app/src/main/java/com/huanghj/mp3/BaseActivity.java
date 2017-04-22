package com.huanghj.mp3;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.db.NetMusicTable;
import com.huanghj.mp3.net.MusicList;
import com.huanghj.mp3.util.BackGutil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class BaseActivity extends AppCompatActivity {


    private Activity mActivity;
    private ArrayList<MusicList > lists=new ArrayList<>();
    MyDBAdapter myDBAdapter;
    private RelativeLayout relativeLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mActivity = this;
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_base);
        BackGutil.setBg(relativeLayout, mActivity);

    }



}
