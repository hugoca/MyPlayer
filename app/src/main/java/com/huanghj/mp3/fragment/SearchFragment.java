package com.huanghj.mp3.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.dialog.ItemDialog;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.net.SearchNetUtil;
import com.huanghj.mp3.util.NetWork;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchNetUtil.SearchMusicListener,View.OnClickListener {
    private EditText searchFrame;
    private LinearLayout searchBar;
    private ImageButton searchButton;
    private ArrayList<MusicInfo> infos = new ArrayList<>();
    private RecyclerView mRecyclerView;
    AlertDialog mDialog;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        searchFrame= (EditText) view.findViewById(R.id.search_frame);
        searchBar= (LinearLayout) view.findViewById(R.id.search_bar);
        searchButton= (ImageButton) view.findViewById(R.id.search_button);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.id_recycler_search_View);
        searchButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void success(ArrayList<MusicInfo> infos) {
        this.infos = infos;
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置recycerview的布局
        final RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(getActivity(), infos);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClick(new RecyclerViewAdapter.MusicListItemClickListenner() {
            @Override
            public void onMoreImageClick(MusicInfo musicInfo) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                ItemDialog.init(inflater, getActivity(), musicInfo, mDialog);
            }

            @Override
            public void onMusicItemClick(int position, MusicInfo musicInfo, ArrayList<MusicInfo> list) {
                Intent intent = new Intent(getActivity(), PlayService.class);
                intent.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
                intent.putExtra(PlayService.POSITION, position);
                intent.putParcelableArrayListExtra(PlayService.MUSICLIST, list);
                getActivity().startService(intent);
            }
        });

    }

    @Override
    public void fail() {
        Log.e("searchFragment","搜索失败");
    }

    @Override
    public void onClick(View v) {
        SearchNetUtil.getInstance().searchMusic(searchFrame.getText().toString(), this);

    }
}
