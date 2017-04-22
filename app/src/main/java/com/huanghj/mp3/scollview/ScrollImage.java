package com.huanghj.mp3.scollview;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.huanghj.mp3.R;

public class ScrollImage extends RelativeLayout {

    private ImageScrollView imageScrollView = null;
    private PageControlView pageControlView = null;

    public ScrollImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.scroll_image, ScrollImage.this);

        imageScrollView = (ImageScrollView) this.findViewById(R.id.myImageScrollView);

        pageControlView = (PageControlView) this.findViewById(R.id.myPageControlView);
    }

    /**
     * 锟斤拷锟斤拷锟斤拷示图片
     * @param list 图片锟斤拷锟斤拷
     */
    public void setBitmapList(List<Bitmap> list){
        int num = list.size();
        imageScrollView.removeAllViews();
        for(int i = 0; i < num; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setImageBitmap(list.get(i));
            imageScrollView.addView(imageView);
        }
        /** 锟斤拷锟斤拷圆圈锟斤拷锟斤拷锟斤拷 **/
        pageControlView.setCount(imageScrollView.getChildCount());
        /** 锟斤拷始锟斤拷圆圈 **/
        pageControlView.generatePageControl(0);
        /** 锟斤拷锟斤拷锟斤拷图锟叫伙拷锟截碉拷锟斤拷锟斤拷实锟斤拷 **/
        imageScrollView.setScrollToScreenCallback(pageControlView);
    }

    /**
     * 锟斤拷锟斤拷锟斤拷锟斤拷母锟�
     * @param height 锟斤拷
     */
    public void setHeight(int height){
        android.view.ViewGroup.LayoutParams la = getLayoutParams();
        la.height = height;
        android.view.ViewGroup.LayoutParams lap = imageScrollView.getLayoutParams();
        lap.height = height;
    }

    /**
     *
     */
    public void setWidth(int width){
        android.view.ViewGroup.LayoutParams la = getLayoutParams();
        la.width = width;
        android.view.ViewGroup.LayoutParams lap = imageScrollView.getLayoutParams();
        lap.width = width;
    }

    /**
     * 图片锟斤拷始锟斤拷锟斤拷
     * @param time 锟斤拷锟斤拷频锟绞ｏ拷锟斤拷位锟斤拷锟斤拷锟斤拷
     */
    public void start(int time){
        imageScrollView.start(time);
    }

    /**
     * 图片停止锟斤拷锟斤拷
     */
    public void stop(){
        imageScrollView.stop();
    }

    public int getPosition(){
        return imageScrollView.getCurrentScreenIndex();
    }

    /**
     * 锟斤拷锟矫硷拷锟斤拷
     * @param clickListener
     */
    public void setClickListener(OnClickListener clickListener) {
        imageScrollView.setClickListener(clickListener);
    }
}
