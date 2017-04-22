package com.huanghj.mp3.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.huanghj.mp3.util.MusicAblum;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by myhug on 2016/2/1.
 */
public class LoadMusic {

    public static String getHttpClient() {
        return null;
    }

    /**
     * 判断网络可用的情况下是否是wifi连接
     * true 是，false 否
     * @param context
     * @return
     */
    public boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null && networkINfo.isConnected()
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static  void queryAblumData(Context mContext){
        final ArrayList<MusicAblum> ablums=new ArrayList<>();
        BmobQuery<MusicAblum> query = new BmobQuery<MusicAblum>();
//查询playerName叫“比目”的数据

//返回50条数据，如果不加上这条语句，默认返回10条数据
//        query.setLimit(2);
//执行查询方法
        query.findObjects(mContext, new FindListener<MusicAblum>() {
            @Override
            public void onSuccess(List<MusicAblum> object) {
                // TODO Auto-generated method stub
                for (MusicAblum musicAblum : object) {
                    //获得数据的objectId信息
                    ablums.add(musicAblum);
                    Log.e("============",musicAblum.getName());
                }
            }

            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub

            }
        });

    }


}
