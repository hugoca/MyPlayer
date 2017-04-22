package com.huanghj.mp3.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.huanghj.mp3.service.PlayService;


/**
 * Created by myhug on 2016/4/18.
 */
public class SharePreferenceData {
    public static final String LAUNCH="launch";
    public static final String ISFIRST="isFirstLaunch";
    public static final String DATA="data";
    public static final String NAME="name";
    public static final String POSITION="position";


    /**
     * 是否是第一次启动
     */
    public static void setFirstLaunch(Context context,boolean isFirst){
        SharedPreferences sp =context.getSharedPreferences(LAUNCH, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ISFIRST,isFirst);
        editor.commit();
    }
    public static boolean geisFirstLaunch(Context context){
        SharedPreferences sp=context.getSharedPreferences(LAUNCH,0);
        Boolean isFirst=sp.getBoolean(ISFIRST, true);
        return isFirst;
    }


    /**
     * 保存上次退出时播放歌曲名和位置
     */
    public static void saveData(Context context,String name,int Position){
        SharedPreferences sp = context.getSharedPreferences(DATA, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NAME,name);
        editor.putInt(POSITION, Position);
        editor.commit();
    }
    public static SharedPreferences readData(Context context){
        SharedPreferences sp=context.getSharedPreferences(DATA,0);
        return sp;
    }

    /**
     * 保存上次退出时播放歌曲名和位置
     */
    public static void saveUser(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences("username", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(NAME,name);

        editor.commit();
    }
    public static SharedPreferences readUser(Context context){
        SharedPreferences sp=context.getSharedPreferences("username",0);
        return sp;
    }






}
