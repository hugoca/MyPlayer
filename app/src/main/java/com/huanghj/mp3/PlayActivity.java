package com.huanghj.mp3;

import android.app.Activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huanghj.mp3.adapter.PLayViewPagerAdapter;
import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.fragment.ListFragment;
import com.huanghj.mp3.fragment.PhotoFragment;
import com.huanghj.mp3.fragment.WordsFragment;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.LrcView;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MyEvent;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class PlayActivity extends BaseActivity implements View.OnClickListener {

    public static final String UPDATE_ACTION = "com.huanghj.action.UPDATE_ACTION";  //更新动作
            //控制动作
    public static final String MUSIC_CURRENT = "com.huanghj.action.MUSIC_CURRENT";  //音乐当前时间改变动作
    public static final String MUSIC_DURATION = "com.huanghj.action.MUSIC_DURATION";

    public static final int INFO_TYPE = 1;

    /**
     * 播放/暂停 、上一首、下一首
     */
    private ImageView mSwitchImage, mPreviousImage, mNextImage;

    /**
     * 播放模式 单曲循环、随机、顺序
     */

    private ImageView mPlayModel;

    /**
     * 歌曲进度
     */
    private SeekBar mProgressBar;
    private TextView mCurrentProgress;   //当前进度消耗的时间
    private TextView mFinalProgress;     //歌曲时间
    private int currentTime;

    /**
     * 歌曲名称、作者名称
     */
    private TextView mMusicTitle;
    private TextView mMusicArtist;

    /**
     * viewPager用于切换歌词和歌手照片
     */
    private ImageView photoImage, normalImage, wordImge;
    private ViewPager mViewPager;
    private ArrayList<ImageView> images;

    private ArrayList<Fragment> mFragmentArrayList;
    private ListFragment mListFragment;

    private PhotoFragment mPhotoFragment;

    private WordsFragment mWordsFragment;

    /**
     * 歌曲信息
     */
    private String url;         //歌曲路径
    private int mPosition;   //播放歌曲在mp3Infos的位置
    private int mFlag;           //播放标识
    private int mDuration = 0;

    /**
     * 播放状态
     */
    private int model = 3;                      //播放模式
    private boolean isPlaying;              // 正在播放
    private boolean isPause;                // 暂停

    /**
     * 歌曲列表
     */
    private ArrayList<MusicInfo> mMusicInfos;

    /**
     * 播放广播，用于处理播放进度
     */
    private PlayerReceiver playerReceiver;

    /**
     * 存放各种图片
     */
    private int[] models = {R.mipmap.menu_loop_one_normal,
            R.mipmap.menu_loop_normal,
            R.mipmap.menu_order_normal,
            R.mipmap.menu_shuffle_normal};

    private Context mContext;

    private EventBus eventBus;

    private MyDBAdapter myDBAdapter;

    /**
     * 添加到自己喜欢的歌曲
     */
    private ImageView mLoveImage;

    private boolean isCollect = false; //是否已添加该歌曲到我的喜欢歌曲列表

    private FragmentManager mFragmentManager;

    private RelativeLayout relativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        mContext = this;
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_play);
        BackGutil.setBg(relativeLayout, mContext);




        playerReceiver = new PlayerReceiver();
        myDBAdapter=new MyDBAdapter(this);
        findViewById();
        initData();
        initViewPager();
        initSeekBar();
        setViewOnclickListener();

    }



    /**
     * 从界面上根据id获取按钮
     */
    private void findViewById() {
        mListFragment = new ListFragment();
        mPhotoFragment = new PhotoFragment();
        mWordsFragment = new WordsFragment();
        mMusicTitle = (TextView) findViewById(R.id.tv_songname);
        mMusicArtist = (TextView) findViewById(R.id.tv_artist);
        mPreviousImage = (ImageView) findViewById(R.id.previous_mg);
        mPlayModel = (ImageView) findViewById(R.id.imgv_model);
        mSwitchImage = (ImageView) findViewById(R.id.play_img);
        mNextImage = (ImageView) findViewById(R.id.next_img);
        mProgressBar = (SeekBar) findViewById(R.id.seekbar);
        mCurrentProgress = (TextView) findViewById(R.id.tv_current);
        mFinalProgress = (TextView) findViewById(R.id.tv_sumtime);
        mLoveImage = (ImageView) findViewById(R.id.image_love);
        photoImage = (ImageView) findViewById(R.id.dot_one);
        normalImage = (ImageView) findViewById(R.id.dot_two);
        wordImge = (ImageView) findViewById(R.id.dot_three);
        mViewPager = (ViewPager) findViewById(R.id.vPager);

    }

    /**
     * 给每一个按钮设置监听器
     */
    private void setViewOnclickListener() {
        mPreviousImage.setOnClickListener(this);
        mNextImage.setOnClickListener(this);
        mSwitchImage.setOnClickListener(this);
        mPlayModel.setOnClickListener(this);
        mLoveImage.setOnClickListener(this);
        mProgressBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

    }

    /**
     * 初始化数据
     */
    private void initData() {

        isPause = false;
        Intent intent = getIntent();
        isPlaying = intent.getBooleanExtra(LocalListActivity.IS_PLAYING, false);
        mMusicInfos = intent.getParcelableArrayListExtra(PlayService.MUSICLIST);
        mPosition = intent.getIntExtra(PlayService.POSITION, 0);
        mMusicTitle.setText(mMusicInfos.get(mPosition).getTitle().toString());
        mMusicArtist.setText(mMusicInfos.get(mPosition).getArtist().toString());
        mDuration = mMusicInfos.get(mPosition).getDuration();
        url = mMusicInfos.get(mPosition).getUrl();
        mPlayModel.setImageResource(models[model]);
        if(mMusicInfos.get(mPosition).getIsLove()==0){
            mLoveImage.setImageResource(R.mipmap.icon_collect_normal);
        }else {
            isCollect=true;
            mLoveImage.setImageResource(R.mipmap.icon_collect_selected);
        }

        if (isPlaying) {
            mSwitchImage.setImageResource(R.mipmap.btn_playback_pause_normal);
        } else {
            mSwitchImage.setImageResource(R.mipmap.btn_playback_play_normal);
        }
    }

    /**
     * 初始化进度条
     */
    private void initSeekBar() {
        mCurrentProgress.setText(MusicInfo.formatTime(0));
        mFinalProgress.setText(MusicInfo.formatTime(mDuration));
        mProgressBar.setEnabled(true);
        mProgressBar.setProgress(1);
        mProgressBar.setMax(mDuration);
    }

    /**
     * 初始化ViewPager和fragment
     */
    private void initViewPager() {

        images = new ArrayList<>();
        mFragmentArrayList = new ArrayList<>();
        images.add(photoImage);
        images.add(normalImage);
        images.add(wordImge);
        mFragmentArrayList.add(mListFragment);
        mFragmentArrayList.add(mPhotoFragment);
        mFragmentArrayList.add(mWordsFragment);

        eventBus = new EventBus();
        eventBus.register(mListFragment);//注册订阅者
        eventBus.register(mWordsFragment);//注册订阅者
        eventBus.register(mPhotoFragment);//注册订阅者

        MyEvent myEvent = new MyEvent();
        myEvent.eventType = INFO_TYPE;
        myEvent.list = mMusicInfos;
        myEvent.position = mPosition;
        myEvent.isPlay = isPlaying;
        eventBus.post(myEvent);//发布消息


        mViewPager.setAdapter(new PLayViewPagerAdapter(getSupportFragmentManager(), mFragmentArrayList));

        mViewPager.setCurrentItem(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < images.size(); i++) {
                    images.get(i).setImageResource(R.mipmap.icon_dot_normal);
                }
                images.get(position).setImageResource(R.mipmap.icon_dot_highlight);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 注册receiver
     */
    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        registerReceiver(playerReceiver, filter);
        super.onResume();
    }


    /**
     * 反注册广播
     */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(playerReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击播放/暂停 按钮
             */
            case R.id.play_img:
                play();

                break;

            /**
             * 点击上一首
             */
            case R.id.previous_mg:
                previous_music();
                break;

            /**
             * 点击下一首歌曲
             */
            case R.id.next_img:
                next_music();
                break;

            /**
             * 添加到喜爱列表
             */
            case R.id.image_love:
                MusicInfo musicInfo=mMusicInfos.get(mPosition);
                if (musicInfo.getIsLove()==1) {
                    musicInfo.setIsLove(0);
                    myDBAdapter.updateData(musicInfo);
                    mLoveImage.setImageResource(R.mipmap.icon_collect_normal);
                    Toast.makeText(mContext,"取消收藏",Toast.LENGTH_SHORT).show();
                } else {
                    musicInfo.setIsLove(1);
                    myDBAdapter.updateData(musicInfo);
                    mLoveImage.setImageResource(R.mipmap.icon_collect_selected);
                    Toast.makeText(mContext,"添加歌曲到收藏",Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 播放模式
             */
            case R.id.imgv_model:
                setModel();
                break;
        }

    }

    /**
     * 实现监听Seekbar的类
     *
     * @author wwj
     */
    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        int cutrrentTime = 0;

        // 触发操作，拖动
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mCurrentProgress.setText(MusicInfo.formatTime(progress));
            MyEvent myEvent = new MyEvent();
            myEvent.eventType = 2;
            myEvent.position = progress;
            myEvent.isPlay = isPlaying;
            eventBus.post(myEvent);
        }

        // 表示进度条刚开始拖动，开始拖动时候触发的操作
        public void onStartTrackingTouch(SeekBar seekBar) {
            Intent intent = new Intent(mContext, PlayService.class);
            intent.putExtra(PlayService.MSG, PlayService.STOP_MSG);
            startService(intent);
        }

        // 停止拖动时候
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

            Intent intent = new Intent(mContext, PlayService.class);
            intent.putExtra(PlayService.CURRENT_TIME, seekBar.getProgress());
            intent.putExtra(PlayService.MSG, PlayService.PROGRESS_CHANGE);
            startService(intent);

        }
    }

    /**
     * 单曲循环
     */
    public void repeat_one() {
        Intent intent = new Intent(PlayService.CTL_ACTION);
        intent.putExtra(PlayService.MODEL, PlayService.REPEAT_ONE);
        sendBroadcast(intent);
    }

    /**
     * 随机播放
     */
    public void shuffleMusic() {
        Intent intent = new Intent(PlayService.CTL_ACTION);
        intent.putExtra(PlayService.MODEL, PlayService.SHUFFE);
        sendBroadcast(intent);
    }

    /**
     * 全部循环
     */
    public void repeat_all() {
        Intent intent = new Intent(PlayService.CTL_ACTION);
        intent.putExtra(PlayService.MODEL, PlayService.REPEAT_ALL);
        sendBroadcast(intent);
    }

    /**
     * 顺序播放
     */
    public void repeat_order() {
        Intent intent = new Intent(PlayService.CTL_ACTION);
        intent.putExtra(PlayService.MODEL, PlayService.ORDER);
        sendBroadcast(intent);
    }

    /**
     * 上一首
     */
    public void previous_music() {
        if (mPosition - 1 >= 0) {
            mPosition = mPosition - 1;
            MusicInfo mp3Info = mMusicInfos.get(mPosition);   //上一首MP3
            mMusicTitle.setText(mp3Info.getTitle());
            mMusicArtist.setText(mp3Info.getArtist());
            mSwitchImage.setImageResource(R.mipmap.btn_playback_pause_normal);
            Intent intent = new Intent(this, PlayService.class);
            if (isPlaying) {
                intent.putExtra(PlayService.MSG, PlayService.PRIVIOUS_MSG);

            } else {
                intent.putExtra(PlayService.POSITION, mPosition);
                intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicInfos);
                isPlaying = true;

            }

            startService(intent);

        } else {
            Toast.makeText(PlayActivity.this, "已是第一首", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 下一首
     */
    public void next_music() {
        if (mPosition + 1 <= mMusicInfos.size() - 1) {
            mPosition = mPosition + 1;
            MusicInfo mp3Info = mMusicInfos.get(mPosition);
            mMusicTitle.setText(mp3Info.getTitle());
            mMusicArtist.setText(mp3Info.getArtist());
            mSwitchImage.setImageResource(R.mipmap.btn_playback_pause_normal);
            Intent intent = new Intent(this, PlayService.class);
            if (isPlaying) {
                intent.putExtra(PlayService.MSG, PlayService.NEXT_MSG);
            } else {
                intent.putExtra(PlayService.POSITION, mPosition);
                intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicInfos);
                isPlaying = true;
            }
            startService(intent);
        } else {
            Toast.makeText(PlayActivity.this, "没有下一首了", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 播放暂停方法
     */
    public void play() {
        Intent intent = new Intent(this, PlayService.class);
        if (isPlaying) {
            mSwitchImage.setImageResource(R.mipmap.btn_playback_play_normal);
            intent.putExtra(PlayService.MSG, PlayService.PAUSE_MSG);
            startService(intent);
            isPlaying = false;
            isPause = true;

        } else {
            mSwitchImage.setImageResource(R.mipmap.btn_playback_pause_normal);
            if (isPause) {
                intent.putExtra(PlayService.MSG, PlayService.CONTINUE_MSG);
            } else {
                intent.putExtra(PlayService.POSITION, mPosition);
                intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicInfos);
            }
            startService(intent);

            isPause = false;
            isPlaying = true;
        }
    }

    /**
     * 设置模式
     */
    private void setModel() {
        Intent intent = new Intent(PlayService.CTL_ACTION);
        if (model < models.length - 1) {
            model++;
        } else {
            model = 0;
        }
        mPlayModel.setImageResource(models[model]);
        switch (model + 1) {
            case PlayService.REPEAT_ONE: // 单曲循环
                Toast.makeText(PlayActivity.this, "单曲循环",
                        Toast.LENGTH_SHORT).show();

                intent.putExtra(PlayService.MODEL, PlayService.REPEAT_ONE);
                sendBroadcast(intent);
                break;
            case PlayService.REPEAT_ALL: // 全部循环
                Toast.makeText(PlayActivity.this, "列表循环",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra(PlayService.MODEL, PlayService.REPEAT_ALL);
                sendBroadcast(intent);
                break;
            case PlayService.ORDER:
                Toast.makeText(PlayActivity.this, "顺序播放",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra(PlayService.MODEL, PlayService.ORDER);
                sendBroadcast(intent);
                break;
            case PlayService.SHUFFE: // 无重复
                Toast.makeText(PlayActivity.this, "随机播放",
                        Toast.LENGTH_SHORT).show();
                intent.putExtra(PlayService.MODEL, PlayService.SHUFFE);
                sendBroadcast(intent);
                break;
        }
    }

    /**
     * 用来接收从service传回来的广播的内部类,处理播放进度
     *
     * @author huanghj
     */
    public class PlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MUSIC_CURRENT)) {
                currentTime = intent.getIntExtra("currentTime", -1);
                mCurrentProgress.setText(MusicInfo.formatTime(currentTime));
                mProgressBar.setProgress(currentTime);

                MyEvent myEvent = new MyEvent();
                myEvent.eventType = 2;
                myEvent.position = currentTime;
                myEvent.isPlay = isPlaying;
                eventBus.post(myEvent);
            }
            if (action.equals(MUSIC_DURATION)) {
                MusicInfo musicInfo = intent.getParcelableExtra(PlayService.MUSICINFO);
                mDuration = musicInfo.getDuration();
                mMusicTitle.setText(musicInfo.getTitle());
                mMusicArtist.setText(musicInfo.getArtist());
                mFinalProgress.setText(MusicInfo.formatTime(mDuration));
            }

        }

    }

}
