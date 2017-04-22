package com.huanghj.mp3.entity;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class LoginEvent {
    private String mMsg;


    public LoginEvent(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;

    }
    public String getMsg(){
        return mMsg;
    }

}
