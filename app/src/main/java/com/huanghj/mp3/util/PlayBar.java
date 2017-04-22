package com.huanghj.mp3.util;


import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.service.PlayService;


/**
 * Created by myhug on 2016/3/29.
 */
public class PlayBar extends RelativeLayout {
    /**
     * 播放/暂停
     */
    public ImageView mPlayImage;

    /**
     * 下一首图标
     */
    public ImageView mNextImage;

    /**
     * 整个布局
     */
    private RelativeLayout mLayout;

    /**
     * 歌曲名
     */
    public TextView mSongname;


    private Listener mListener;

    private Context mContext;

    public PlayBar(Context context) {
        super(context);
        mContext=context;
        init(context);
    }

    public PlayBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        init(context);
    }

    public PlayBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_playbar, this);
        mPlayImage = (ImageView) view.findViewById(R.id.bottom_play_bar_play_img);
        mNextImage = (ImageView) view.findViewById(R.id.bottom_play_bar_next_img);
        mLayout = (RelativeLayout) view.findViewById(R.id.Layout);
        mSongname= (TextView) view.findViewById(R.id.songName_tv);
        mListener=new Listener();
        mPlayImage.setOnClickListener(mListener);
        mNextImage.setOnClickListener(mListener);
        mLayout.setOnClickListener(mListener);
    }


    class Listener implements OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, PlayActivity.class);
            Intent buttonIntent = new Intent(PlayService.CTL_ACTION);
            switch (v.getId()){
                case R.id.bottom_play_bar_play_img:
                    buttonIntent.putExtra(PlayService.INTENT_BUTTONID_TAG, PlayService.PLAY_MSG);
                    mContext.sendBroadcast(buttonIntent);
                    break;
                case R.id.bottom_play_bar_next_img:
                    buttonIntent.putExtra(PlayService.INTENT_BUTTONID_TAG, PlayService.NEXT_MSG);
                    mContext.sendBroadcast(buttonIntent);
                    break;
                case R.id.Layout:

                    break;
                default:
                    break;
            }
        }
    }

    public void undateDate(MusicInfo musicInfo,boolean isPlaying){
        mSongname.setText(musicInfo.getTitle().toString());
        if(isPlaying){
            mPlayImage.setImageResource(R.mipmap.icon_bottom_play_bar_stop_normal);
        }else {
            mPlayImage.setImageResource(R.mipmap.icon_bottom_play_bar_play_normal);
        }
    }

}
