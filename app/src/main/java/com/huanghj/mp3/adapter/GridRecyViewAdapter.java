package com.huanghj.mp3.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanghj.mp3.LocalListActivity;
import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.loader.AsynImageLoader;
import com.huanghj.mp3.scollview.ScrollImage;
import com.huanghj.mp3.util.MusicAblum;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class GridRecyViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<Bitmap> mList;

    private ArrayList<MusicAblum> mAblums;

    private int mWith;

    //建立枚举 2个item 类型
    public enum ITEM_TYPE {
        ITEM1,
        ITEM2
    }

    public GridRecyViewAdapter(Context context,ArrayList<Bitmap> list,ArrayList<MusicAblum> ablums,int width){
        mContext=context;
        mList=list;
        mAblums=ablums;
        mWith=width;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE.ITEM1.ordinal()){
            return new BannerHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.item_banner,parent, false));
        }else {
            return new GridHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.layout_grid_item,parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof  BannerHolder) {
            ((BannerHolder) holder).initScrollImage(mList,mWith);
        } else if (holder instanceof GridHolder) {
            ((GridHolder) holder).initGrid(mAblums,position);
        }
    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
//Enum类提供了一个ordinal()方法，返回枚举类型的序数，这里ITEM_TYPE.ITEM1.ordinal()代表0， ITEM_TYPE.ITEM2.ordinal()代表1
        return position == 0 ? ITEM_TYPE.ITEM1.ordinal() : ITEM_TYPE.ITEM2.ordinal();
    }


    @Override
    public int getItemCount() {
        return mAblums.size();
    }

    class GridHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView1;
        public ImageView imageView2;
        public ImageView imageView3;
        public TextView textView;
        public View view;


        public GridHolder(View itemView) {
            super(itemView);
            imageView1= (ImageView) itemView.findViewById(R.id.grid_image1);
            imageView2= (ImageView) itemView.findViewById(R.id.grid_image2);
            imageView3= (ImageView) itemView.findViewById(R.id.grid_image3);
            textView= (TextView) itemView.findViewById(R.id.grid_text);
            view=itemView.findViewById(R.id.grid_item);
            imageView1.setOnClickListener(this);
            imageView2.setOnClickListener(this);
            imageView3.setOnClickListener(this);
        }

        public void initGrid(ArrayList<MusicAblum> ablums,int i){
            AsynImageLoader asynImageLoader = new AsynImageLoader();
            asynImageLoader.showImageAsyn(imageView1,ablums.get(i).getBitmap1(),R.mipmap.nom);
            asynImageLoader.showImageAsyn(imageView2,ablums.get(i).getBitmap2(),R.mipmap.nom);
            asynImageLoader.showImageAsyn(imageView3,ablums.get(i).getBitmap3(),R.mipmap.nom);
            textView.setText(ablums.get(i).getName());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.grid_image1:
                   startIntent();
                    break;
                case R.id.grid_image2:
                   startIntent();
                    break;
                case R.id.grid_image3:
                    startIntent();
                    break;
                default:
                    break;
            }
        }
    }

    class BannerHolder extends RecyclerView.ViewHolder {
        public  ScrollImage scrollImage;


        public BannerHolder(View itemView) {
            super(itemView);
            scrollImage= (ScrollImage) itemView.findViewById(R.id.simage);
        }
        public void initScrollImage(ArrayList<Bitmap> mList,int mWith){
            scrollImage.setHeight((int) (1.0 * mWith *mList.get(0).getHeight() / mList.get(0).getWidth()));
            //设置scrollImage控件的集合值
            scrollImage.setBitmapList(mList);
            scrollImage.start(3000);
            scrollImage.setClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    int position = scrollImage.getPosition();
                    startIntent();

                }
            });
        }
    }



    public void startIntent(){
        Intent intent=new Intent(mContext, LocalListActivity.class);
        intent.putExtra(MainActivity.LIST, MainActivity.NET);
        mContext.startActivity(intent);
    }
}
