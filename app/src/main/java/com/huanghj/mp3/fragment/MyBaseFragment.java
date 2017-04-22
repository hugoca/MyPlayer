package com.huanghj.mp3.fragment;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huanghj.mp3.R;
import com.huanghj.mp3.util.MyEvent;

/**
 *
 */
public abstract class MyBaseFragment extends Fragment {

    private View mContentView;//默认加载的布局

    protected MyBaseFragment(){
    }
    private ViewGroup container;
    protected Boolean isVisible = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        Log.d("BaseFragment", "onCreateView");
        if (mContentView == null) {
            Log.d("BaseFragment","initView");
            initView(savedInstanceState);
            lazyLoad();
            setListener();
            processLogic(savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()){
            isVisible = true;
            onVisible();
        }else {
            if(isVisible){
                isVisible =false;
                onUnvisible();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible){
            onVisible();
        }
    }

    //每次界面显示都会调用的 朋友
    protected abstract void onVisible();
    protected abstract void onUnvisible();
    //用于第一次加载布局时加载。
    protected abstract void lazyLoad();
    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(getActivity()).inflate(layoutResID, container,false);
    }
    /**
     * 初始化View控件
     */
    protected abstract void initView(Bundle savedInstanceState);
    /*
    为View设置监听器
     */
    protected abstract void setListener();
    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);
    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }
    /**
     * 当fragment对用户可见时，会调用该方法，可在该方法中懒加载网络数据
     */
    protected abstract void onUserVisible();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)mContentView.getParent()).removeView(mContentView);
    }
}


