package com.huanghj.mp3.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/3/7.
 * <p/>
 * 该类用于获取一些本地资源
 */
public class GetResource {

    /**
     * 获取本地的MP3 文件
     *
     * @return 返回 Arraytlist音乐列表
     */
    public static ArrayList<MusicInfo> getLocalMusic(ContentResolver mContentResolver) {
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        ArrayList<MusicInfo> list = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    musicInfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    musicInfo.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    musicInfo.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    musicInfo.setSize(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    musicInfo.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    musicInfo.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    musicInfo.setIsLove(0);
                    list.add(musicInfo);
                }
            }
            cursor.close();
        }
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    public static int getLocalSongNum(ContentResolver mContentResolver){
        int size=getLocalMusic(mContentResolver).size();
        return size;
    }

}
