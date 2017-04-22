package com.huanghj.mp3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.db.MusicTable;
import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.MusicInfo;

import java.util.ArrayList;

public class CollectActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    private ArrayList<MusicInfo> mList = new ArrayList<>();

    private MyDBAdapter myDBAdapter;
    private TextView mShow;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_collect);
        BackGutil.setBg(relativeLayout, this);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_recycler_collect);
        mShow= (TextView) findViewById(R.id.no_collect_show);
        myDBAdapter=new MyDBAdapter(this);
        mList=myDBAdapter.fetchCollectMusic();

        if(mList==null||mList.size()==0){
            mShow.setVisibility(View.VISIBLE);
            return;
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//设置recycerview的布局
        mAdapter = new RecyclerViewAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerViewAdapter.MusicListItemClickListenner() {
            @Override
            public void onMoreImageClick(MusicInfo musicInfo) {

            }

            @Override
            public void onMusicItemClick(int position, MusicInfo musicInfo, ArrayList<MusicInfo> list) {

            }
        });


    }
}
