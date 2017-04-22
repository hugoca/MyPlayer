//package com.huanghj.mp3.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//
//import com.huanghj.mp3.R;
//import com.huanghj.mp3.util.MusicInfo;
//
//import java.util.ArrayList;
//
///**
// * Created by myhug on 2016/1/25.
// */
//public class OrderBySingAdapter extends RecyclerView.Adapter<OrderBySingAdapter.ViewHolder>{
//    private ArrayList<MusicInfo> musicInfos;
//    private OnItemOnClickListener onItemOnClickListener;
//    public OrderBySingAdapter(){
//        musicInfos = new ArrayList<>();
//    }
//    public void setMusicInfos(ArrayList<MusicInfo> musicInfos){
//        this.musicInfos = musicInfos;
//        notifyDataSetChanged();
//    }
//    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener){
//        this.onItemOnClickListener = onItemOnClickListener;
//    }
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
//        ViewHolder viewHolder = new ViewHolder(view);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        MusicInfo musicInfo = musicInfos.get(position);
//        holder.singTitle.setText(musicInfo.getTitle());
//        holder.singArticle.setText(musicInfo.getArtist());
//    }
//
//    @Override
//    public int getItemCount() {
//        return musicInfos.size();
//    }
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        public TextView singTitle;
//        public TextView singArticle;
//        public ImageButton more;
//        public ViewHolder(View itemView) {
//            super(itemView);
//            singTitle = (TextView) itemView.findViewById(R.id.sing_title);
//            singArticle = (TextView) itemView.findViewById(R.id.sing_article);
//            more = (ImageButton) itemView.findViewById(R.id.more);
//            itemView.setOnClickListener(this);
//            more.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            if(onItemOnClickListener !=null){
//            if(v.getId()==R.id.more){
//                onItemOnClickListener.onItemMoreClick(getAdapterPosition());
//                return;
//            }
//                onItemOnClickListener.onItemClick(getAdapterPosition());
//            }
//        }
//    }
//    public interface OnItemOnClickListener{
//        void onItemClick(int position);
//        void onItemMoreClick(int position);
//    }
//}
