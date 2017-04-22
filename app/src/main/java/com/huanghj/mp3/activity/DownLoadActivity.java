package com.huanghj.mp3.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.fragment.DownFragment;
import com.huanghj.mp3.fragment.DownloadingFragment;
import com.huanghj.mp3.fragment.ListFragment;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.MusicInfo;

import java.util.ArrayList;

public class DownLoadActivity extends AppCompatActivity {

//    private RecyclerView mRecyclerView;
//
//    private RecyclerViewAdapter mAdapter;
//
//    private ArrayList<MusicInfo> mList = new ArrayList<>();
//
    private RelativeLayout relativeLayout;

    private final String Tag = "DownloadAty";

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private  TextView title;


    private Fragment downloadMusicFrg, downloadingMusicFrg;
    private Fragment[] fragments;
    private String[] titles = {"歌曲", "正在下载"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        relativeLayout= (RelativeLayout) findViewById(R.id.lay_down);
        BackGutil.setBg(relativeLayout, this);
       findViewById();
        title.setText("我的下载");

        downloadMusicFrg = new DownFragment();
        downloadingMusicFrg = new DownloadingFragment();
        fragments = new Fragment[]{downloadMusicFrg, downloadingMusicFrg};
        FragmentPagerAdapter adapter = new MyViewPagerAapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void findViewById(){
        tabLayout= (TabLayout) findViewById(R.id.tab_layout);
        viewPager= (ViewPager) findViewById(R.id.view_pager);
        title= (TextView) findViewById(R.id.title);


    }

    class MyViewPagerAapter extends FragmentPagerAdapter {
        public MyViewPagerAapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

}
