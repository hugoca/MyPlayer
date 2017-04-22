package com.huanghj.mp3.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huanghj.mp3.Gson.Music;
import com.huanghj.mp3.LocalListActivity;
import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.adapter.GridRecyViewAdapter;
import com.huanghj.mp3.adapter.RecyclerViewAdapter;
import com.huanghj.mp3.scollview.ScrollImage;
import com.huanghj.mp3.util.MusicAblum;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferFragment extends Fragment {

    private ScrollImage scrollImage;

    private RecyclerView mGridRecyclerView;

    private  ArrayList<Bitmap> list;

    ArrayList<MusicAblum> ablums;

    public ReferFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_refer, container, false);

        scrollImage = (ScrollImage) view.findViewById(R.id.simage);
        mGridRecyclerView= (RecyclerView) view.findViewById(R.id.id_grid_recycler_View);
//        initScrollImage();
        queryAblumData(getActivity());
        return view;
    }



    public void initRecyView(){
        list = new ArrayList<Bitmap>();
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banne2));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner1));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner4));
        list.add(BitmapFactory.decodeResource(getResources(), R.mipmap.banner3));
//        ArrayList<MusicAblum> ablums=new ArrayList<>();

        //取得屏幕宽度
        int width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        mGridRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//设置recycerview的布局
        mGridRecyclerView.setAdapter(new GridRecyViewAdapter(getActivity(), list, ablums, width));
    }

    public  void queryAblumData(Context mContext){
        ablums=new ArrayList<>();
        BmobQuery<MusicAblum> query = new BmobQuery<MusicAblum>();
//查询playerName叫“比目”的数据

//返回50条数据，如果不加上这条语句，默认返回10条数据
//        query.setLimit(2);
//执行查询方法
        query.findObjects(mContext, new FindListener<MusicAblum>() {
            @Override
            public void onSuccess(List<MusicAblum> object) {
                // TODO Auto-generated method stub
                for (MusicAblum musicAblum : object) {
                    //获得数据的objectId信息
                    ablums.add(musicAblum);
                    initRecyView();

                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub

            }
        });

    }

}
