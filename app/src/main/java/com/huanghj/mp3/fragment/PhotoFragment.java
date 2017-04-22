package com.huanghj.mp3.fragment;


import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.huanghj.mp3.PlayActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.util.Media;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.MyEvent;
import com.huanghj.mp3.util.PhotoUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends BaseFragment {
    /**
     * 旋转图片
     */
    private ImageView imageView;

    public int position;
    public boolean isPlay;


    public ArrayList<MusicInfo> list= new ArrayList<>();

    private Animation mOperatingAnim;

    public PhotoFragment() {

    }

    @Override
    public void onEvent(MyEvent eventData) {
        if (eventData.eventType == PlayActivity.INFO_TYPE) {
            position=eventData.position;
            list=eventData.list;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        imageView = (ImageView) view.findViewById(R.id.image_banner);
        MusicInfo musicInfo=list.get(position);
        Bitmap bitmap=PhotoUtil.getArtwork(getActivity(),musicInfo.getId(),musicInfo.getAlbum_id(),false,false);
        imageView.setImageBitmap(bitmap);
//        setImageRotate();
        return view;
    }

    private void setImageRotate() {
        /** 设置旋转动画 */
        mOperatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mOperatingAnim.setInterpolator(linearInterpolator);
        imageView.startAnimation(mOperatingAnim);
    }


}
