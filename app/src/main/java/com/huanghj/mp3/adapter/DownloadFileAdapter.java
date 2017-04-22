package com.huanghj.mp3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.huanghj.mp3.R;
import com.huanghj.mp3.entity.DownloadEntity;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/1/25.
 */
public class DownloadFileAdapter extends RecyclerView.Adapter<DownloadFileAdapter.ViewHolder>{
    private ArrayList<DownloadEntity> infos;
    private OnItemOnClickListener onItemOnClickListener;
    public DownloadFileAdapter(){
        infos = new ArrayList<>();
    }

    public void setMusicInfos(ArrayList<DownloadEntity> downloadInfos){
        infos = downloadInfos;
        notifyDataSetChanged();
    }
    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
        this.onItemOnClickListener = onItemOnClickListener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloading_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return infos.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView singTitle;
        public TextView singProgress;
        public ViewHolder(View itemView) {
            super(itemView);
            singTitle = (TextView) itemView.findViewById(R.id.sing_title);
            singProgress = (TextView) itemView.findViewById(R.id.sing_progress);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemOnClickListener !=null){
                onItemOnClickListener.onItemClick(getAdapterPosition());
            }
        }
    }
    public interface OnItemOnClickListener{
        void onItemClick(int position);
    }
}
