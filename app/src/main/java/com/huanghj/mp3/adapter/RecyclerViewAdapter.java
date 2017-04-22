package com.huanghj.mp3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huanghj.mp3.R;
import com.huanghj.mp3.util.MusicInfo;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/1/20.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<MusicInfo> mList;
    private MusicListItemClickListenner mListener;

    public RecyclerViewAdapter(Context context, ArrayList<MusicInfo> list) {
        mContext = context;
        mList = list;
    }

    /**
     * 设置Item点击事件
     *
     * @param listener listener
     */
    public void setOnItemClick(MusicListItemClickListenner listener) {
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.layout_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mList.get(position).getTitle().toString());
        holder.text_art.setText(mList.get(position).getArtist().toString());
        holder.bindData(position, mList.get(position));
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textView;
        public TextView text_art;
        public ImageView imageView;
        View view;
        public MusicInfo hMusicInfo;
        public int hPosition;


        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_show);
            text_art = (TextView) itemView.findViewById(R.id.tv_art);
            imageView = (ImageView) itemView.findViewById(R.id.more_igV);
            view = itemView.findViewById(R.id.item);
            imageView.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        public void bindData(int position, MusicInfo musicInfo) {
            this.hPosition = position;
            this.hMusicInfo = musicInfo;
        }

        @Override
        public void onClick(View v) {
            if (null == hMusicInfo) {
                return;
            }

            switch (v.getId()) {
                case R.id.more_igV:
                    if (null != mListener) {
                        mListener.onMoreImageClick(hMusicInfo);
                    }

                    break;
                case R.id.item:
                    if (null != mListener) {
                        mListener.onMusicItemClick(hPosition, hMusicInfo, mList);
                    }
                    break;
            }

        }
    }

    /**
     * 点击接口
     */
    public interface MusicListItemClickListenner {
        void onMoreImageClick(MusicInfo musicInfo);

        void onMusicItemClick(int position, MusicInfo musicInfo, ArrayList<MusicInfo> list);
    }
}
