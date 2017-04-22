package com.huanghj.mp3.net;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huanghj.mp3.Gson.Music;
import com.huanghj.mp3.Gson.NearDynamic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by myhug on 2016/4/18.
 */
public class HttpUtil {

    public static String getJsonContent(String urlStr) {
        try {// 获取HttpURLConnection连接对象
            URL url = new URL(urlStr);
            HttpURLConnection httpConn = null;
            try {
                httpConn = (HttpURLConnection) url
                        .openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 设置连接属性
            httpConn.setConnectTimeout(3000);
            httpConn.setDoInput(true);
            httpConn.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpConn.getResponseCode();
            if (respCode == 200) {
                return ConvertStream2Json(httpConn.getInputStream());
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    private static String ConvertStream2Json(InputStream inputStream) {
        String jsonStr = "";
        // ByteArrayOutputStream相当于内存输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        // 将输入流转移到内存输出流中
        try {
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                out.write(buffer, 0, len);
            }
            // 将内存流转换为字符串
            jsonStr = new String(out.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * 使用泛型获取javaBean（核心函数）
     */
    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return t;
    }

    public static ArrayList<Music> parseUserData(String strContent) {

        if (!TextUtils.isEmpty(strContent)) {
            try {
                Gson mgson = new Gson();
                NearDynamic mNearDynamic = mgson.fromJson(
                        strContent, NearDynamic.class);
                ArrayList<Music> mMusicList = mNearDynamic
                        .getArray();
                for(int i=0;i<mMusicList.size();i++){
                    Log.e("++++++++++++++++",mMusicList.get(i).getSong_name());
                }
                return mMusicList;
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return null;
    }

//    public static ArrayList<Music> getPersons(String jsonString, Music cls) {
//        ArrayList<Music> list = new ArrayList<Music>();
//        try {
//            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {
//            }.getType());
//        } catch (Exception e) {
//        }
//        return list;
//    }
//
//    public static ArrayList<Map<String, Object>> listKeyMaps(String jsonString) {
//        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        try {
//            Gson gson = new Gson();
//            list = gson.fromJson(jsonString,
//                    new TypeToken<ArrayList<Map<String, Object>>>() {
//                    }.getType());
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//        return list;
//    }
}