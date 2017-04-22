package com.huanghj.mp3.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.huanghj.mp3.entity.DownLoadMsg;
import com.huanghj.mp3.util.MusicInfo;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



/**
 * Created by xiefei on 2016/3/5.
 * 用来进行网络请求操作,搜索音乐
 * 进行get/post请求
 * 网址：http://music.163.com/api/search/suggest/web
 * 参数：s=丑八怪&limit=10
 * header:
 * Origin: http://music.163.com
   Referer:http://music.163.com/
   Content-Type: application/x-www-form-urlencoded

   音乐下载地址：
 http://music.163.com/api/song/detail?ids=%5B这里是ID%5D
 */
public class SearchNetUtil {
    private final String Tag = "SearchNetUtil";
    private final String searchMusicPath = "http://music.163.com/api/search/pc";
    private final String searchMusicLrcPath = "http://geci.me/api/lyric/";
    private final String downloadId = "http://music.163.com/api/song/detail?ids=%5Bid2%5D";
    private Gson gson;
    private static SearchNetUtil util;
    public static SearchNetUtil getInstance(){
        if(util==null){
            synchronized (SearchNetUtil.class){
                if(util==null){
                    util = new SearchNetUtil();
                }
            }
        }
        return util;
    }
    public SearchNetUtil() {
        gson = new Gson();
    }
    public void getUrlById(final int id, final GetDownloadUrlListener listener){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x123:
                        listener.success((DownLoadMsg) msg.obj);
                        break;
                    case 0x234:
                        listener.fail();
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(downloadId.replace("id2",id+""));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("Origin", "http://music.163.com");
                    connection.addRequestProperty("Referer","http://music.163.com/");
                    connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    if(connection.getResponseCode()>=200&&connection.getResponseCode()<300){
                        InputStream inputStream = connection.getInputStream();
                        StringBuilder sb = new StringBuilder();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len=inputStream.read(bytes))!=-1){
                            sb.append(new String(bytes,0,len));
                        }
                        Log.d(Tag,sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("songs");
                        jsonObject = jsonArray.getJSONObject(0);
                        DownLoadMsg downLoadMsg = new DownLoadMsg();
                        downLoadMsg.setMp3Url(jsonObject.getString("mp3Url"));
                        downLoadMsg.setName(jsonObject.getString("name"));
                        downLoadMsg.setDuration(jsonObject.getInt("duration"));
                        Message message = handler.obtainMessage();
                        message.what=0x123;
                        message.obj = downLoadMsg;
                        handler.sendMessage(message);
                    }else {
                        handler.sendEmptyMessage(0x234);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x234);
                }
            }
        }).start();
    }
    public void searchMusic(final String s, final SearchMusicListener listener){
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0x123:
                        listener.success((ArrayList<MusicInfo>) msg.obj);
                        break;
                    case 0x234:
                        listener.fail();
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(searchMusicPath);
                    Log.d(Tag, url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("Origin", "http://music.163.com");
                    connection.addRequestProperty("Referer","http://music.163.com/");
                    connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setConnectTimeout(5000);
                    connection.getOutputStream().write(("type=1&s=" + s + "&limit=10&offset=0").getBytes());
                    connection.connect();
                    if(connection.getResponseCode()>=200&&connection.getResponseCode()<300){
                        InputStream inputStream = connection.getInputStream();
                        StringBuilder sb = new StringBuilder();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len=inputStream.read(bytes))!=-1){
                            sb.append(new String(bytes,0,len));
                        }
                        Log.d(Tag, sb.toString());
                        JSONObject jsonObject = new JSONObject(sb.toString()).getJSONObject("result");
                        JSONArray jsonArray = jsonObject.getJSONArray("songs");
                        ArrayList<MusicInfo> infos = new ArrayList<MusicInfo>();
                        for (int i = 0;i<jsonArray.length();i++) {
                            MusicInfo m = new MusicInfo();
                            m.setAlbum_id(413516);
                            m.setAlbum("推荐");
                            m.setIsLove(0);
                            jsonObject =  jsonArray.getJSONObject(i);
                            m.setTitle(jsonObject.getString("name"));
                            m.setId(jsonObject.getLong("id"));
                            m.setUrl(jsonObject.getString("mp3Url"));
                            JSONObject jsonObject1 = jsonObject.getJSONObject("bMusic");
                            m.setDuration(jsonObject1.getInt("playTime"));
                            m.setSize(jsonObject1.getLong("size"));
                            jsonObject = jsonObject.getJSONArray("artists").getJSONObject(0);
                            m.setArtist(jsonObject.getString("name"));
                            m.setArtist_id(jsonObject.getLong("id"));
                            infos.add(m);
                        }
                        Log.d(Tag, infos.toString());
//                        jsonObject.
                        Message message = handler.obtainMessage();
                        message.what=0x123;
                        message.obj = infos;
                        handler.sendMessage(message);
                    }else {
                        handler.sendEmptyMessage(0x234);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0x234);
                }
            }
        }).start();
    }
//    public void searchMusicLrc(final SearchMusicLrcListener listener,final String... s){
//        Observable<String> observable = OnlineMusicUtil.getMusicLrc(s[0],s[1]);
//        observable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                if (s != null)
//                    listener.success(s);
//                else
//                    listener.fail();
//            }
//        });
//    }
    public interface SearchMusicListener{
        void success(ArrayList<MusicInfo> infos);
        void fail();
    }
    public interface SearchMusicLrcListener{
        void success(String lrc);
        void fail();
    }
    public interface GetDownloadUrlListener{
        void success(DownLoadMsg msg);
        void fail();
    }
}

