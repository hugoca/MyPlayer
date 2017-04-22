package com.huanghj.mp3.scollview;

/**
 * Created by Administrator on 2016/4/27 0027.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huanghj.mp3.R;

public class PageControlView extends LinearLayout implements ImageScrollView.ScrollToScreenCallback {
    /**
     * Context对象
     **/
    private Context context;
    /**
     * 圆圈的数量
     **/
    private int count;

    public PageControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 回调函数
     **/
    @Override
    public void callback(int currentIndex) {
        generatePageControl(currentIndex);
    }

    /**
     * 设置选中圆圈
     **/
    public void generatePageControl(int currentIndex) {
        this.removeAllViews();

        for (int i = 0; i < this.count; i++) {
            ImageView iv = new ImageView(context);
            if (currentIndex == i) {
                iv.setImageResource(R.mipmap.icon_dot_highlight);
            } else {
                iv.setImageResource(R.mipmap.icon_dot_normal);
            }
            iv.setLayoutParams(new LayoutParams(1, 0));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 8;
            layoutParams.rightMargin = 8;
            iv.setLayoutParams(layoutParams);
            this.addView(iv);
        }
    }

    /**
     * 设置圆圈数量
     **/
    public void setCount(int count) {
        this.count = count;
    }
}
