package com.huanghj.mp3.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

import com.huanghj.mp3.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class BackGutil {
    public static final int[] bgs={R.drawable.background,R.drawable.background2,R.drawable.background3,R.drawable.background4};
    public static int index=0;

    public static void setFirstBg(RelativeLayout relativeLayout,Context context){
        Random random=new Random();
        index=random.nextInt(4);
        setBg(relativeLayout, context);
    }


    public static void setBg(RelativeLayout relativeLayout,Context context){
        relativeLayout.setBackground(context.getResources().getDrawable(bgs[index]));
    }
}
