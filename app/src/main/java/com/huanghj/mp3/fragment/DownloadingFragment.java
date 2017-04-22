package com.huanghj.mp3.fragment;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huanghj.mp3.R;
import com.huanghj.mp3.activity.DownLoadActivity;
import com.huanghj.mp3.adapter.DownloadFileAdapter;
import com.huanghj.mp3.entity.DownloadEntity;
import com.huanghj.mp3.net.DownloadUtil;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.BusProvider;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MyEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import rx.Observable;

public class DownloadingFragment extends MyBaseFragment {
    private final String Tag = "DownloadingMusicFrg";
    private RecyclerView recyclerView;
    private DownloadFileAdapter adapter;
    private ArrayList<MusicInfo> musicInfos;
    private boolean isLoadData = false;
    private DownLoadActivity  activity;
    private LinkedList<DownloadEntity> downloads;
    private Observable observable;
    private LinearLayout container;
    private HashMap<String,View> hashMap;
    @Override
    protected void onVisible() {
//        if(observable!=null)
//            observable.subscribe(subscriber);
        Log.d(Tag,"onvisible");
        BusProvider.getInstance().register(this);

    }

    @Override
    public void onPause() {
        super.onPause();
//        subscriber.unsubscribe();
//        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe
    public void getDownloadEntity(DownloadEntity entity){
        Log.d(Tag, entity.toString());
        if(hashMap.containsKey(entity.fileName)){
//            freshView(hashMap.get(entity.fileName), entity);
            if(entity.fileSize!=0)
                ((TextView)hashMap.get(entity.fileName).findViewById(R.id.download_btn)).setText(entity.currentSize*100/entity.fileSize+"%");
        }
    }
    @Override
    protected void onUnvisible() {
        Log.d(Tag,"unvisible");
        BusProvider.getInstance().unregister(this);
    }

    @Override
    protected void lazyLoad() {

    }
    @Subscribe
    public void finish(MusicInfo musicInfo){
        if(hashMap.containsKey(musicInfo.getTitle()))
            container.removeView(hashMap.get(musicInfo.getTitle()));
    }
    private void initContainerView(){
        Log.d(Tag,downloads.toString());
        for (DownloadEntity d:downloads) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.downloading_list_item,null);
            hashMap.put(d.fileName,view);
            freshView(view,d);
            container.addView(view);
        }
    }
    private void freshView(View view,DownloadEntity downloadEntity){
        ((TextView)view.findViewById(R.id.sing_title)).setText(downloadEntity.fileName);
        ((TextView)view.findViewById(R.id.sing_progress)).setText(downloadEntity.artistName);
        if(downloadEntity.fileSize!=0)
            ((TextView)view.findViewById(R.id.download_btn)).setText(downloadEntity.currentSize*100/downloadEntity.fileSize+"%");
    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_downloading);
        container = getViewById(R.id.container);
        hashMap = new HashMap<>();
        downloads = DownloadUtil.getInstance().getDownloadEntities();
        initContainerView();
//        recyclerView = getViewById(R.id.recycle_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.recycle_divider));
//        if(adapter == null){
//            adapter = new DownloadFileAdapter();
//            adapter.setOnItemOnClickListener(this);
//        }
//        downloads = new ArrayList<>();
//        adapter.setMusicInfos(downloads);
//        recyclerView.setAdapter(adapter);
//        activity = (DownloadAty) getActivity();
//        observable = activity.download.getObservable();
//        observable = DownloadUtil.getInstance().getObservable();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    protected void onUserVisible() {

    }


}
