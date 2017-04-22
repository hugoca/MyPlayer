package com.huanghj.mp3.fragment;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.dialog.ItemDialog;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.Constant;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MusicUtil;
import com.huanghj.mp3.util.MyEvent;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter mAdapter;

    private ArrayList<MusicInfo> mList = new ArrayList<>();

    private ArrayList<Drawable> background;
    private AlertDialog mDialog;

    public ListFragment() {

    }

    @Override
    public void onEvent(MyEvent eventData) {
        if (eventData.eventType == PlayActivity.INFO_TYPE) {
            mList = eventData.list;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recycler_frag_View);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置recycerview的布局
        mAdapter = new RecyclerViewAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerViewAdapter.MusicListItemClickListenner() {
            @Override
            public void onMoreImageClick(MusicInfo musicInfo) {
                if(musicInfo.getUrl().startsWith("http")){
                    LayoutInflater inflater1 = getActivity().getLayoutInflater();
                    ItemDialog.init(inflater1, getActivity(), musicInfo, mDialog);
                }
            }

            @Override
            public void onMusicItemClick(int position, MusicInfo musicInfo, ArrayList<MusicInfo> list) {
                Intent intent = new Intent(getActivity(), PlayService.class);
                intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                intent.putExtra(PlayService.POSITION, position);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, mList);
                getActivity().startService(intent);
            }
        });
        return view;
    }


}
