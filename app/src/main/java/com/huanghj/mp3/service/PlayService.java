package com.huanghj.mp3.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;

import com.huanghj.mp3.LocalListActivity;
import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.entity.LoginEvent;
import com.huanghj.mp3.entity.WordEvent;
import com.huanghj.mp3.fragment.WordsFragment;
import com.huanghj.mp3.util.LrcContent;
import com.huanghj.mp3.util.LrcProcess;
import com.huanghj.mp3.util.Media;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.PlayBar;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class PlayService extends Service {

    /**
     * 广播监听action
     */
    public static final String CTL_ACTION = "com.huanghaj.action.CTL_ACTION";
    public static final String WORD_ACTION = "WORD_ACTION";


    /**
     *通知栏按钮标记
     */
    public final static String INTENT_BUTTONID_TAG = "ButtonId";

    /**
     * 传送的对象名
     */
    public static final String POSITION = "position";
    public static final String MUSICLIST = "musicList";
    public static final String CURRENT_TIME = "currentTime";
    public static final String MUSICINFO = "musicInfo";


    /**
     * 播放模式
     */
    public static final String MODEL = "control";
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ALL = 2;
    public static final int ORDER = 3;
    public static final int SHUFFE = 4;

    /**
     * 操作方式，决定是播放、暂停、上一首、下一首，等等
     */
    public static final String MSG = "MSG";
    public static final int PLAY_MSG = 1; //播放
    public static final int PAUSE_MSG = 2; //暂停
    public static final int STOP_MSG = 3;  //停止
    public static final int CONTINUE_MSG = 4;  //继续暂停点播放
    public static final int PRIVIOUS_MSG = 5;  //上一首
    public static final int NEXT_MSG = 6;     //下一首
    public static final int PROGRESS_CHANGE = 7;   //进度

    private MediaPlayer mediaPlayer; // 媒体播放器对象
    private String path;            // 音乐文件路径
    private boolean isPause;        // 暂停状态
    private int current = 0;        // 记录当前正在播放的音乐
    private ArrayList<MusicInfo> mp3Infos;   //存放Mp3Info对象的集合

    private int status = 3;         //播放状态，默认为顺序播放
    private int currentTime;        //当前播放进度

    private MyReceiver myReceiver;  //自定义广播接收器
    private WordReceiver wordReceiver;  //自定义广播接收器


//    private int currentTime;		//当前播放进度
    private int duration;			//播放长度
    private LrcProcess mLrcProcess;	//歌词处理
    private List<LrcContent> lrcList = new ArrayList<LrcContent>(); //存放歌词列表对象
    private int index = 0;			//歌词检索值


    public PlayService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mediaPlayer = Media.getInstance();
        /**
         * 设置播放完成监听
         */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (status == REPEAT_ONE) { // 单曲循环
                    mediaPlayer.start();
                } else if (status == REPEAT_ALL) { // 全部循环
                    current++;
                    if (current > mp3Infos.size() - 1) {  //变为第一首的位置继续播放
                        current = 0;
                    }
                    play(0);
                } else if (status == ORDER) { // 顺序播放
                    current++;  //下一首位置
                    if (current <= mp3Infos.size() - 1) {
                        play(0);
                    } else {
                        mediaPlayer.seekTo(0);
                        current = 0;

                    }
                } else if (status == SHUFFE) {    //随机播放
                    current = (int) (Math.random()*(mp3Infos.size() - 1));
                    play(0);
                }
            }
        });

        IntentFilter mFilter01,mFilter02;
        mFilter01 = new IntentFilter(CTL_ACTION);
        mFilter02 = new IntentFilter(WORD_ACTION);
        myReceiver = new MyReceiver();
        wordReceiver = new WordReceiver();
        registerReceiver(myReceiver, mFilter01);
        registerReceiver(wordReceiver, mFilter02);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getIntExtra(MSG, -1)) {
            case PLAY_MSG:
                mp3Infos = intent.getParcelableArrayListExtra(MUSICLIST);
                current = intent.getIntExtra(POSITION, 0);   //当前播放歌曲的在mp3Infos的位置
                currentTime = intent.getIntExtra(CURRENT_TIME, 0);
                play(currentTime);
                break;
            case PAUSE_MSG:
                pause();
                break;
            case CONTINUE_MSG:
                resume();
                break;
            case PRIVIOUS_MSG:
                previous();
                break;
            case NEXT_MSG:
                next();
                break;
            case STOP_MSG:
                stop();
                break;
            case PROGRESS_CHANGE:
                currentTime = intent.getIntExtra(CURRENT_TIME, 0);
                play(currentTime);
        }
        return START_STICKY;
    }

    public void onTaskRemoved(Intent rootIntent) {
        startService(rootIntent);
    }

    /*** 播放音乐** @param*/
    private void play(int currentTime) {
        try {
            path = mp3Infos.get(current).getUrl();//歌曲路径
            EventBus.getDefault().post(new WordEvent(mediaPlayer,path));
            mediaPlayer.reset();// 把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare(); // 进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
            handler.sendEmptyMessage(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*** 暂停音乐*/
    private void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
            addNotification();
        }
    }

    /*** 继续播放*/
    private void resume() {
        if (isPause) {
            mediaPlayer.start();
            addNotification();
            isPause = false;
        }
    }

    /*** 上一首*/
    private void previous() {
        current = current - 1;
        Intent sendIntent = new Intent(PlayActivity.UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
        sendBroadcast(sendIntent);

        play(0);
        System.out.println("*****3*******");
    }

    /**
     * 下一首
     */
    private void next() {
        current = current + 1;
        Intent sendIntent = new Intent(PlayActivity.UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        // 发送广播，将被Activity组件中的BroadcastReceiver接收到
        sendBroadcast(sendIntent);
        play(0);
        System.out.println("*****3*******");
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unregisterReceiver(myReceiver);
        unregisterReceiver(wordReceiver);
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * handler用来接收消息，来发送广播更新播放时间
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (mediaPlayer != null) {
                    currentTime = mediaPlayer.getCurrentPosition(); // 获取当前音乐播放的位置
                    Intent intent = new Intent();
                    intent.setAction(PlayActivity.MUSIC_CURRENT);
                    intent.putExtra(CURRENT_TIME, currentTime);
                    sendBroadcast(intent); // 给PlayerActivity发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }

            }
        }


    };

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start(); // 开始播放
            addNotification();
            if (currentTime > 0) { // 如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
            }
            MusicInfo musicInfo = mp3Infos.get(current);
            Intent intent = new Intent();
            intent.setAction(PlayActivity.MUSIC_DURATION);
            intent.putExtra(MUSICINFO, musicInfo);
            sendBroadcast(intent);
        }
    }

    /**
     * 通知栏菜单
     */
    public void addNotification() {
        if (mediaPlayer == null) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        mRemoteViews.setImageViewResource(R.id.notify_img, R.mipmap.icon_notification_music);
        mRemoteViews.setTextViewText(R.id.tv_notify_song_name, mp3Infos.get(current).getTitle());
        mRemoteViews.setTextViewText(R.id.tv_notify_artist, mp3Infos.get(current).getArtist());

        if (mediaPlayer.isPlaying()) {
            mRemoteViews.setImageViewResource(R.id.img_notify_play, R.mipmap.notify_btn_pause_normal);
        } else {
            mRemoteViews.setImageViewResource(R.id.img_notify_play, R.mipmap.notify_btn_play_normal);
        }


        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(LocalListActivity.IS_PLAYING, mediaPlayer.isPlaying());
        intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mp3Infos);
        intent.putExtra(POSITION, current);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent buttonIntent = new Intent(CTL_ACTION);
        /* 播放/暂停  按钮 */
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, PLAY_MSG);
        PendingIntent intent_paly = PendingIntent.getBroadcast(this, 2, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.img_notify_play, intent_paly);

        buttonIntent.putExtra(INTENT_BUTTONID_TAG, NEXT_MSG);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 3, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.img_notify_next, intent_next);

        builder.setContent(mRemoteViews)
                .setContentIntent(pendingIntent)
                .setTicker("开始播放")
                .setSmallIcon(R.mipmap.stat_earphone);
        Notification notify = builder.build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        startForeground(200, notify);

    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra(MODEL, -1);
            status=control;
            int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
            switch (buttonId) {
                case PLAY_MSG:
                    addNotification();
                    if (mediaPlayer.isPlaying()) {
                        pause();
                    } else {
                        play(currentTime);
                    }
                    isPause = !isPause;
                    break;
                case NEXT_MSG:
                    next();
                default:
                    break;

            }

        }
    }




    public class WordReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(WORD_ACTION)){
                current = intent.getIntExtra("start", -1);
                if(current==1){
//                    initLrc();
                }
            }

        }
    }

}

