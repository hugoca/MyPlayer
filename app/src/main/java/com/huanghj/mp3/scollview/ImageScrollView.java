package com.huanghj.mp3.scollview;

/**
 * Created by Administrator on 2016/4/27 0027.
 */

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ImageScrollView extends ViewGroup {
    /** 滚动对象Scroller **/
    private Scroller scroller = null;
    /** 手势识别对象GestureDetector **/
    private GestureDetector gestureDetector = null;
    /** 当前屏幕索引 **/
    private int currentScreenIndex = 0;
    /** 设置一个标志位，防止底层的onTouch事件重复处理UP事件 **/
    private boolean fling = false;

    Handler handler;
    int time;
    OnClickListener clickListener;

    public ImageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        /** 设置布局，将子视图顺序横屏排列 **/
        for ( int i = 0; i < getChildCount(); i++ ){
            View child = getChildAt(i);
            child.setVisibility(View.VISIBLE);
            child.measure(right - left, bottom - top);
            child.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }

    /** 初始化 **/
    private void initView(final Context context) {
        this.scroller = new Scroller(context);
        handler = new Handler();

        this.gestureDetector = new GestureDetector(new OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                if(clickListener!=null){
                    clickListener.onClick(ImageScrollView.this);
                }

                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // 防止移动过最后一页
                if((distanceX > 0 && getScrollX() < getWidth() * (getChildCount() - 1))
                        || (distanceX < 0 && getScrollX() > 0)){// 防止向第一页之前移动
                    scrollBy((int) distanceX, 0);
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 判断是否达到最小轻松速度，取绝对值的
                if(Math.abs(velocityX) > ViewConfiguration.get(context)
                        .getScaledMinimumFlingVelocity()){
                    if(velocityX > 0 && currentScreenIndex >= 0){
                        fling = true;
                        scrollToScreen((currentScreenIndex - 1 + getChildCount()) % getChildCount());
                    } else if(velocityX < 0 && currentScreenIndex <= getChildCount() - 1){
                        fling = true;
                        scrollToScreen((currentScreenIndex + 1 + getChildCount()) % getChildCount());
                    }
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
        });
    }

    /** 切换到指定屏 **/
    public void scrollToScreen(int whichScreen) {
        if(whichScreen != currentScreenIndex && getFocusedChild() != null
                && getFocusedChild() == getChildAt(currentScreenIndex)){
            getFocusedChild().clearFocus(); // 清除焦点
        }

        final int delta = whichScreen * getWidth() - getScrollX();
        int show = 0;
        if(Math.abs(delta) < getWidth()*(getChildCount()-1) - getWidth()/2)
            show = Math.abs(delta) * 2;
        scroller.startScroll(getScrollX(), 0, delta, 0, show);
        invalidate();

        currentScreenIndex = whichScreen; // 设置当前屏幕索引
        if(scrollToScreenCallback != null){ // 刷新圆圈
            scrollToScreenCallback.callback(currentScreenIndex);
        }
    }

    @Override
    public void computeScroll() {
        // 当滚动没有完成
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_UP){

            handler.removeCallbacks(next);
            if(time>=500)
                handler.postDelayed(next, time);

            if(!fling){
                // 当用户停止拖动
                snapToDestination();
            }
            fling = false;
        }
        return true;
    }

    /** 根据当前x坐标位置确定切换到第几屏 **/
    private void snapToDestination() {
        scrollToScreen((getScrollX() + (getWidth() / 2)) / getWidth());
    }

    /** 底部圆圈显示回调接口 **/
    interface ScrollToScreenCallback {
        public void callback(int currentIndex);
    }

    /** ScrollToScreenCallback回调对象 **/
    private ScrollToScreenCallback scrollToScreenCallback;

    /** 设置回调函数对象 **/
    public void setScrollToScreenCallback(ScrollToScreenCallback scrollToScreenCallback) {
        this.scrollToScreenCallback = scrollToScreenCallback;
    }

    public void start(int time){
        if(time>500)
            this.time = time;
        else
            this.time = 500;
        handler.postDelayed(next, time);
    }

    public void stop(){
        handler.removeCallbacks(next);
    }

    Runnable next = new Runnable() {

        @Override
        public void run() {
            scrollToScreen((currentScreenIndex+1)%getChildCount());
            handler.postDelayed(next, time);
        }
    };

    public int getCurrentScreenIndex() {
        return currentScreenIndex;
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}