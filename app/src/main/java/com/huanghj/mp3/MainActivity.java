package com.huanghj.mp3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.db.NetMusicTable;
import com.huanghj.mp3.fragment.ClassifyFragment;
import com.huanghj.mp3.fragment.MyFragment;
import com.huanghj.mp3.fragment.ReferFragment;
import com.huanghj.mp3.fragment.SearchFragment;
import com.huanghj.mp3.net.LoadMusic;
import com.huanghj.mp3.net.MusicList;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.PlayBar;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    public static final String NAME = "name";
    public static final String LIST="list";
    public static final int NET=1;
    public static final int LOCAL=2;

    /**
     * 各fragment标题
     */
    private TextView mReferTitle,mClassifyTitle,mSearchTitle,mMyMenuTitle;

    /**
     * title数组
     */
    private ArrayList<TextView> mTitles;

    private ViewPager mViewPager;

    private ArrayList<Fragment> mFragmentList;

//    private PlayBar mPlayBar;

    private Context mContext;


    private RelativeLayout relativeLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_main);
        BackGutil.setFirstBg(relativeLayout, mContext);

        myDBAdapter=new MyDBAdapter(mContext);
        Bmob.initialize(this, "b3cc8f005cc33e0aecd925eb39a96c0b");
        myDBAdapter.deleteNet();
        queryMajorData();
//        LoadMusic.queryAblumData(mContext);
        initTitle();
        initData();
//        initPlayBar();
    }


    public void onImageClick(View view){
        Intent intent=new Intent(this,LocalListActivity.class);
        intent.putExtra(LIST,NET);
        startActivity(intent);
    }



    /**
     * 此方法用于取得TextView并设置监听
     */
    private void initTitle(){
//        mPlayBar= (PlayBar) findViewById(R.id.playbar);
        mReferTitle= (TextView) findViewById(R.id.refer_title);
        mClassifyTitle= (TextView) findViewById(R.id.classify_title);
        mSearchTitle= (TextView) findViewById(R.id.search_title);
        mMyMenuTitle= (TextView) findViewById(R.id.mymenu_title);
        mReferTitle.setOnClickListener(this);
        mClassifyTitle.setOnClickListener(this);
        mSearchTitle.setOnClickListener(this);
        mMyMenuTitle.setOnClickListener(this);
        mTitles=new ArrayList<>();
        mTitles.add(mReferTitle);
        mTitles.add(mClassifyTitle);
        mTitles.add(mSearchTitle);
        mTitles.add(mMyMenuTitle);

    }



    private void initPlayBar(){
        SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
//        mPlayBar.mSongname.setText(sp.getString(NAME, "").toString());

    }

    private void queryMajorData(){
        BmobQuery<MusicList> query = new BmobQuery<MusicList>();
//查询playerName叫“比目”的数据

//返回50条数据，如果不加上这条语句，默认返回10条数据
//        query.setLimit(2);
//执行查询方法
        query.findObjects(mContext, new FindListener<MusicList>() {
            @Override
            public void onSuccess(List<MusicList> object) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "查询成功：共" + object.size() + "条数据。", Toast.LENGTH_SHORT).show();
                for (MusicList music : object) {

                    //获得数据的objectId信息
                    insertNetData(music);

                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext, "查询失败：" + msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void insertNetData(MusicList music){
        int duration = Integer.parseInt(music.getDuration().substring(0,2))*60*1000+Integer.parseInt(music.getDuration().substring(3,5))*1000;
        long artist_id = Long.parseLong(music.getArtist_id());
        long album_id = Long.parseLong(music.getAlbum_id());
        long size = Long.parseLong(music.getSize().substring(0,1))*1024 * 1024+ Long.parseLong(music.getSize().substring(2,4))*1024;
        myDBAdapter.insertData(NetMusicTable.TABLE_NAME, music.getSongname(), music.getArtist(),
                artist_id, music.getUrl(), music.getAlbum(), album_id, duration, size, 0);
    }


    @Override
    protected void onPause() {
//        String name = mPlayBar.mSongname.getText().toString();
//        SharedPreferences sp = mContext.getSharedPreferences("config", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(NAME, name);
//        editor.commit();
        super.onPause();

    }

    /**
     * 用于初始化相关数据
     */
    private void initData() {
        mViewPager = (ViewPager)findViewById(R.id.viewPage_View);
        mFragmentList=new ArrayList<>();
        ReferFragment referFragment=new ReferFragment();
        ClassifyFragment classifyFragment=new ClassifyFragment();
        SearchFragment searchFragment=new SearchFragment();
        MyFragment myFragment=new MyFragment();
        mFragmentList.add(referFragment);
        mFragmentList.add(classifyFragment);
        mFragmentList.add(searchFragment);
        mFragmentList.add(myFragment);


        /**
         *  给ViewPager设置适配器
         */

        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mReferTitle.setTextColor(getResources().getColor(R.color.textColor_focus));
        /**
         * 设置页面滑动改变监听
         */
        mViewPager.setOnPageChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        for(int i=0;i<mTitles.size();i++) {
            mTitles.get(i).setTextColor(getResources().getColor(R.color.textColor_normal));
        }
        switch (v.getId()){
            case R.id.refer_title:
                mReferTitle.setTextColor(getResources().getColor(R.color.textColor_focus));
                changeView(0);
                break;
            case R.id.classify_title:
                mClassifyTitle.setTextColor(getResources().getColor(R.color.textColor_focus));
                changeView(1);
                break;
            case R.id.search_title:
                mSearchTitle.setTextColor(getResources().getColor(R.color.textColor_focus));
                changeView(2);
                break;
            case R.id.mymenu_title:
                mMyMenuTitle.setTextColor(getResources().getColor(R.color.textColor_focus));
                changeView(3);
                break;
            default:
                break;
        }
    }

    /**
     * 用于改变当前页面
     * @param desTab
     */
    private void changeView(int desTab)
    {
        mViewPager.setCurrentItem(desTab, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     *  方法用于当页面改变进行的操作
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        for (int i=0;i<4;i++){
            mTitles.get(i).setTextColor(getResources().getColor(R.color.textColor_normal));
        }
        mTitles.get(position).setTextColor(getResources().getColor(R.color.textColor_focus));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//
            return super.instantiateItem(container, position);
        }
    }

}

