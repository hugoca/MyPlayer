package com.huanghj.mp3.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huanghj.mp3.R;
import com.huanghj.mp3.activity.DownLoadActivity;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.util.Constant;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MusicUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownFragment extends MyBaseFragment {
    private final String Tag = "DownloadMusicFrg";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter  adapter;
    private ArrayList<MusicInfo> musicInfos;
    private DownLoadActivity activity;
    private PopupWindow popupWindow;
    private View popupContentView;
    private int currentClickPosition;
    private ListView songMenuListView;
    private AlertDialog alertDialog;
    private ArrayList<String> songMenuName;
    @Override
    protected void onVisible() {
        musicInfos = MusicUtil.getInstance(getContext()).getMusicByType(Constant.LOAD_DOWNLOAD);
        if(adapter == null){
            adapter = new RecyclerViewAdapter(getActivity(),musicInfos);
//            adapter.setOnItemOnClickListener(this);
        }
//        adapter.setMusicInfos(musicInfos);
    }

    @Override
    protected void onUnvisible() {

    }

    @Override
    protected void lazyLoad() {

    }
//    private void initPopupWindow(){
//        if(popupWindow == null){
//            popupContentView = getActivity().getLayoutInflater().inflate(R.layout.popup_list_layout,null);
//            ListView listView = (ListView) popupContentView.findViewById(R.id.list);
//            popupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.simple_item,new String[]{"收 藏","播 放","删 除","添加到"});
//            listView.setAdapter(arrayAdapter);
//            listView.setOnItemClickListener(this);
//            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popupwindow_bg));
//        }
//    }
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_down);
        recyclerView = getViewById(R.id.id_recycler_down_View);
//        initPopupWindow();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new ItemDivider(getContext(), R.drawable.recycle_divider));
        if(adapter == null){
            adapter = new  RecyclerViewAdapter(getActivity(),musicInfos);
//            adapter.setOnItemOnClickListener(this);
        }
        songMenuName = MusicUtil.getInstance(getContext()).getSongMenuName();
        recyclerView.setAdapter(adapter);
        activity = (DownLoadActivity) getActivity();
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

//    @Override
//    public void onItemClick(int position) {
////        activity.musicPlayer.setPlayList(Constant.LOAD_DOWNLOAD, null);
////        activity.musicPlayer.start(position,0);
//    }
//
//    @Override
//    public void onItemMoreClick(int position) {
//        popupWindow.showAtLocation(recyclerView, Gravity.CENTER,0,0);
//        currentClickPosition = position;
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//        if(parent==songMenuListView){
//            if(position == songMenuName.size()){
//                final EditText editText = new EditText(getContext());
////            editText.setBackgroundResource(R.drawable.search_bg);
//                new android.app.AlertDialog.Builder(getContext()).setMessage("输入新的歌单名").setView(editText).setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MusicUtil.getInstance(getContext()).addSongMenu(editText.getText().toString());
//                        MusicUtil.getInstance(getContext()).getMusicGroupByType(Constant.SONG_MENU_TYPE).get(musicInfos.get(position)).add(musicInfos.get(currentClickPosition));
//                        MusicUtil.getInstance(getContext()).saveSongMenu();
//                    }
//                }).setPositiveButton("取消",null).show();
//            }else {
//                MusicUtil.getInstance(getContext()).getMusicGroupByType(Constant.SONG_MENU_TYPE).get(songMenuName.get(position)).add(musicInfos.get(currentClickPosition));
//                MusicUtil.getInstance(getContext()).saveSongMenu();
//            }
//            alertDialog.dismiss();
//            return;
//        }
//        switch (position){
//            case 0:
//                activity.musicPlayer.collectMusic(Constant.LOAD_DOWNLOAD,null,currentClickPosition);
//                break;
//            case 1:
//                activity.musicPlayer.setPlayList(Constant.LOAD_DOWNLOAD,null);
//                activity.musicPlayer.start(currentClickPosition, 0);
//                break;
//            case 2:
//                new AlertDialog.Builder(getContext()).setTitle("提示").setMessage("你确定要删除这首歌吗？（内存里面删除哦）").
//                        setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        FileUtil.deleteFile(musicInfos.get(currentClickPosition).getUrl());
//                        MusicUtil.getInstance(getContext()).noticeAllRemove(musicInfos.get(currentClickPosition));
//                    }
//                }).create().show();
//                break;
//            case 3:
//                songMenuListView = new ListView(getContext());
//                songMenuListView.setOnItemClickListener(this);
//                String[] strings = new String[songMenuName.size()+1];
//                songMenuName.toArray(strings);
//                strings[songMenuName.size()]="新建歌单";
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.simple_item,strings);
//                songMenuListView.setAdapter(arrayAdapter);
//                alertDialog =  new AlertDialog.Builder(getContext()).setMessage("加入歌单").setView(songMenuListView).create();
//                alertDialog.show();
//                break;
//        }
//        popupWindow.dismiss();
//    }
}
