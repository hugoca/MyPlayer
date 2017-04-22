package com.huanghj.mp3.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.entity.LoginEvent;
import com.huanghj.mp3.entity.WordEvent;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.LrcContent;
import com.huanghj.mp3.util.LrcProcess;
import com.huanghj.mp3.util.LrcView;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MyEvent;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends BaseFragment {

    /**
     * Called when the activity is first created.
     */
    public  LrcView lrcView;
    public  LrcProcess mLrcProcess;

    private List<LrcContent> lrcList = new ArrayList<LrcContent>(); //存放歌词列表对象


    public int position;

    public static WordsFragment wordsFragment;

    public MediaPlayer mediaPlayer;
    public String url;



    /**
     * 播放广播，用于处理播放进度
     */



    public ArrayList<MusicInfo> list= new ArrayList<>();



    public WordsFragment() {

    }

    public static WordsFragment getInstance(){
        if(wordsFragment==null){
            wordsFragment=new WordsFragment();
        }
        return wordsFragment;
    }


    @Override
    public void onEvent(MyEvent eventData) {
        if (eventData.eventType == PlayActivity.INFO_TYPE) {
            position=eventData.position;
            list=eventData.list;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_words, container, false);
        lrcView = (LrcView) view.findViewById(R.id.lrcShowView);
        Intent intent = new Intent(PlayService.WORD_ACTION);
        intent.putExtra("start", 1);
        getActivity().sendBroadcast(intent);
        return view;
    }

    public void onEventBackgroundThread(WordEvent event) {

        mediaPlayer=event.mediaPlayer;
        url=event.url;
        initLrc();
    }

    /**
     * 初始化歌词配置
     */

    public void initLrc(){
        mLrcProcess = new LrcProcess();
        //读取歌词文件
        mLrcProcess.readLRC(url);
        if(mLrcProcess==null){
           Log.e("===============","mlrcPrcess为空");
            return;
        }
        //传回处理后的歌词文件
        lrcList = mLrcProcess.getLrcList();

        if(lrcView==null){
            Log.e("===============","lrcView为空");
            return;
        }
        lrcView.setmLrcList(lrcList);
        Log.e("===============", "lrcView设置歌词列表");
        //切换带动画显示歌词
        lrcView.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_z));
        Log.e("===============", "切换带动画显示歌词");
        lrcView.setIndex(lrcIndex());
        lrcView.invalidate();

//        handler.post(mRunnable);

    }
    Runnable mRunnable = new Runnable() {

        @Override
        public void run() {

            Log.e("===============", "设置进度");

            Log.e("===============", "初始化");
//            handler.postDelayed(mRunnable, 100);
            Log.e("===============", "延迟发送");
        }
    };

    /**
     * 根据时间获取歌词显示的索引值
     * @return
     */

    private  int currentTime;
    private int duration;
    private int index;
    public int lrcIndex() {
        if(mediaPlayer.isPlaying()) {
            currentTime = mediaPlayer.getCurrentPosition();
            duration = mediaPlayer.getDuration();
        }
        if(currentTime < duration) {
            for (int i = 0; i < lrcList.size(); i++) {
                if (i < lrcList.size() - 1) {
                    if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
                        index = i;
                    }
                    if (currentTime > lrcList.get(i).getLrcTime()
                            && currentTime < lrcList.get(i + 1).getLrcTime()) {
                        index = i;
                    }
                }
                if (i == lrcList.size() - 1
                        && currentTime > lrcList.get(i).getLrcTime()) {
                    index = i;
                }
            }
        }
        return index;
    }




}