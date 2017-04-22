package com.huanghj.mp3.Gson;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/4/18.
 */
public class GsonData {

    public ArrayList<Music> parseUserData(String strContent) {

        if (!TextUtils.isEmpty(strContent)) {
            try {
                Gson mgson = new Gson();
                NearDynamic mNearDynamic = mgson.fromJson(
                        strContent, NearDynamic.class);
                ArrayList<Music> mMusicList = mNearDynamic
                        .getArray();
                return mMusicList;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return null;
    }
}
