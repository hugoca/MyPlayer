package com.huanghj.mp3;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huanghj.mp3.activity.AboutActivity;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.db.MusicTable;
import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.db.NetMusicTable;
import com.huanghj.mp3.dialog.ItemDialog;
import com.huanghj.mp3.net.HttpUtil;
import com.huanghj.mp3.service.PlayService;

import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.GetResource;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.SharePreferenceData;
import com.huanghj.mp3.util.StatusBarCompat;

import java.util.ArrayList;


public class LocalListActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 传输的对象名
     */

    public static final String IS_PLAYING = "isPlaying";

    public static final String NAME = "name";

    /**
     * 用于展示音乐列表
     */
    private RecyclerView mRecyclerView;

    /**
     * recyclerView 处理适配器
     */
    private RecyclerViewAdapter mRecyclerViewAdapter;

    /**
     * 本地上下文
     */
    private Context mContext;

    /**
     * 页面图片按钮
     */
    private ImageView mPlayImage, mNextImage, mRefresh;

    /**
     * 底部布局
     */
    private RelativeLayout mLayout;

    /**
     * 歌曲名显示
     */
    private TextView nameText;

    /**
     * 当前于list中的位置
     */
    private int mPosition;

    /**
     * 音乐列表
     */
    private ArrayList<MusicInfo> mMusicList;

    /**
     * 是否在播放中
     */
    private boolean isPlaying;
    private boolean isPause;

    /**
     * 点击歌曲更多的对话框
     */
    private AlertDialog mDialog;

    private TextView mNOSong;

    private TextView mTitle;

    private Animation operatingAnim;
    private MyDBAdapter myDBAdapter;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        StatusBarCompat.compat(this);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_local);
        mContext = this;
        BackGutil.setBg(relativeLayout,mContext);
        isPlaying = false;
        isPause = false;
        initView();
        myDBAdapter=new MyDBAdapter(mContext);
        if(getIntent().getIntExtra(MainActivity.LIST,2)==MainActivity.NET){
            mMusicList=myDBAdapter.fetchAll(NetMusicTable.TABLE_NAME);
            mTitle.setText("网络音乐");
            mRefresh.setVisibility(View.INVISIBLE);
        }else {
            mMusicList=myDBAdapter.fetchAll(MusicTable.TABLE_NAME);
            mTitle.setText("本地音乐");
        }
        if (mMusicList != null) {
            initData();
            initRecyclerViewData();
        } else {
            mNOSong.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);

        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        mRefresh = (ImageView) findViewById(R.id.img_reflesh);
        mPlayImage = (ImageView) findViewById(R.id.bottom_play_bar_play_img);
        mNextImage = (ImageView) findViewById(R.id.bottom_play_bar_next_img);
        mLayout = (RelativeLayout) findViewById(R.id.Layout);
        nameText = (TextView) findViewById(R.id.songName_tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recycler_View);
        mNOSong = (TextView) findViewById(R.id.no_song);
        mTitle= (TextView) findViewById(R.id.textView_title);

    }


    /**
     * 初始化数据
     */
    private void initData() {
        //从share中获取数据
        SharedPreferences sp=SharePreferenceData.readData(mContext);
        nameText.setText(sp.getString(NAME, "").toString());
        mPosition = sp.getInt(PlayService.POSITION, 0);
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.reflesh_image);
        AccelerateInterpolator acc = new AccelerateInterpolator();
        operatingAnim.setInterpolator(acc);
    }

    private void initRecyclerViewData() {
        //给view设置监听
        mRefresh.setOnClickListener(this);
        mPlayImage.setOnClickListener(this);
        mNextImage.setOnClickListener(this);
        mLayout.setOnClickListener(this);

        //初始化recyclerView和设置监听

        mRecyclerViewAdapter = new RecyclerViewAdapter(this, mMusicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置recycerview的布局
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClick(new RecyclerViewAdapter.MusicListItemClickListenner() {
            @Override
            public void onMoreImageClick(MusicInfo musicInfo) {

                if(getIntent().getIntExtra(MainActivity.LIST,2)==MainActivity.NET){
                    LayoutInflater inflater1 = getLayoutInflater();
                    ItemDialog.init(inflater1, mContext, musicInfo, mDialog);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.more_dialog, null);
                    builder.setTitle("");
                    builder.setView(view);
                    TextView textMusicInfo = (TextView) view.findViewById(R.id.musicinfo);
                    TextView textAdd = (TextView) view.findViewById(R.id.add);
                    TextView textDelete = (TextView) view.findViewById(R.id.delete);
                    TextView textStore = (TextView) view.findViewById(R.id.restore);
                    TextView textShare = (TextView) view.findViewById(R.id.share);
                    DialogClick dialogClick = new DialogClick(musicInfo);
                    textMusicInfo.setOnClickListener(dialogClick);
                    textAdd.setOnClickListener(dialogClick);
                    textDelete.setOnClickListener(dialogClick);
                    textShare.setOnClickListener(dialogClick);
                    textStore.setOnClickListener(dialogClick);
                    mDialog = builder.create();
                    mDialog.show();
                }


            }

            @Override
            public void onMusicItemClick(int position, MusicInfo musicInfo, ArrayList<MusicInfo> list) {
                mPosition = position;
                play();
                Log.d("==============>", "调用播放服务");
            }
        });
    }

    /***
     * 对话框点击监听
     */
    class DialogClick implements View.OnClickListener {
        MusicInfo mMusicInfo;

        public DialogClick(MusicInfo musicInfo) {
            mMusicInfo = musicInfo;

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /**
                 * 对话框
                 */
                case R.id.musicinfo:
                    Log.i("============>", "歌曲信息");
                    Intent intent = new Intent(mContext, MusicInfoActivity.class);
                    intent.putExtra(PlayService.MUSICINFO, mMusicInfo);
                    startActivity(intent);
                    break;
                case R.id.add:
                    Log.i("============>", "添加到");
                    mMusicInfo.setIsLove(1);
                    myDBAdapter.updateData(mMusicInfo);
                    Toast.makeText(mContext,"添加歌曲到收藏",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delete:
                    Log.i("============>", "删除");
                    myDBAdapter.delete((int) mMusicList.get(mPosition).getId());
                    mMusicList=myDBAdapter.fetchAll(MusicTable.TABLE_NAME);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case R.id.restore:
                    Log.i("============>", "收藏");
                    mMusicInfo.setIsLove(1);
                    myDBAdapter.updateData(mMusicInfo);
                    Toast.makeText(mContext,"添加歌曲到收藏",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.share:
                    Log.i("============>", "分享");
                    Intent intent3=new Intent(mContext, AboutActivity.class);
                    intent3.putExtra("music", mMusicInfo);
                    startActivity(intent3);
                    break;
            }
            mDialog.dismiss();
        }
    }


    /**
     * 向服务发送广播，播放MP3 文件，并进行相关页面修改
     */
    public void play() {
        mPlayImage.setImageResource(R.mipmap.icon_bottom_play_bar_stop_normal);
        nameText.setText(mMusicList.get(mPosition).getTitle() + "-" + mMusicList.get(mPosition).getArtist());
        Intent intent = new Intent(mContext, PlayService.class);
        intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
        intent.putExtra(PlayService.POSITION, mPosition);
        intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicList);
        startService(intent);
        isPlaying = true;

    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, PlayActivity.class);
        switch (v.getId()) {

            case R.id.img_reflesh:
                if (operatingAnim != null) {
                    mRefresh.startAnimation(operatingAnim);
                }
                mRefresh.setImageResource(R.mipmap.icon_refresh_pressed);
                myDBAdapter.getLocalMusic(getContentResolver());
                mMusicList = myDBAdapter.fetchAll(MusicTable.TABLE_NAME);
                mRecyclerViewAdapter.notifyDataSetChanged();  //更新数据后刷新显示
                mRefresh.setImageResource(R.mipmap.icon_refresh_normal);
                break;
            /**
             * 点击下面的Layout,进入播放activity页面
             */
            case R.id.Layout:
                intent.putExtra(IS_PLAYING, isPlaying);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicList);
                intent.putExtra(PlayService.POSITION, mPosition);
                startActivity(intent);
                Log.e("=================","startintent");
                break;

            /**
             * 点击下一首按钮
             */
            case R.id.bottom_play_bar_next_img:
                next_music();
                break;

            /**
             * 点击播放/暂停 按钮
             */
            case R.id.bottom_play_bar_play_img:
                Intent intent1 = new Intent(this, PlayService.class);

                if (isPlaying) {
                    mPlayImage.setImageResource(R.mipmap.icon_bottom_play_bar_play_normal);
                    intent1.putExtra(PlayService.MSG, PlayService.PAUSE_MSG);
                    startService(intent1);

                } else {
                    mPlayImage.setImageResource(R.mipmap.icon_bottom_play_bar_stop_normal);
                    if (isPause) {
                        intent1.putExtra(PlayService.MSG, PlayService.CONTINUE_MSG);
                    } else {
                        intent1.putExtra(PlayService.POSITION, mPosition);
                        intent1.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                        intent1.putParcelableArrayListExtra(PlayService.MUSICLIST, mMusicList);
                    }
                    startService(intent1);
                }
                isPlaying = !isPlaying;
                isPause = !isPause;
                break;

        }

    }

    /**
     * 向share中保存数据，用于初次启动获取
     */
    @Override
    protected void onPause() {
        SharePreferenceData.saveData(mContext,nameText.getText().toString(),mPosition);
        super.onPause();
    }

    /**
     * 下一首
     */
    public void next_music() {
        if (mPosition + 1 <= mMusicList.size() - 1) {
            mPosition = mPosition + 1;
            nameText.setText(mMusicList.get(mPosition).getTitle() + "-" + mMusicList.get(mPosition).getArtist());
            if (isPlaying) {
                Intent intent = new Intent(this, PlayService.class);
                intent.putExtra(PlayService.MSG, PlayService.NEXT_MSG);
                startService(intent);
            } else {
                play();
            }
        } else {
            Toast.makeText(LocalListActivity.this, "没有下一首了", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
