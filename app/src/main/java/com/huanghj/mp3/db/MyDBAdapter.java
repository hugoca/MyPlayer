
package com.huanghj.mp3.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;
import android.util.Log;

import com.huanghj.mp3.util.MusicInfo;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;


/**
 * Created by myhug on 2016/1/4.
 */
public class MyDBAdapter {

    private static final int DAY_OF_MONTH = 31;
    /**
     * 定义Helper对象,用于创建数据库
     */
    private BaseHelper mHelper;

    /**
     * 数据库对象
     */
    private SQLiteDatabase mDB;
    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 接收查询到的数据
     */
    private ArrayList<MusicInfo> musicInfos;

    private Cursor mCursor ;

    public MyDBAdapter(Context mContext) {
        this.mContext = mContext;
        mHelper = BaseHelper.getInstance(mContext); //取得单例helper
        mDB = mHelper.getWritableDatabase();//初始化数据库
        mDB.execSQL(BaseHelper.CREATE_TABLE_TIME);
        mDB.execSQL(BaseHelper.CREATE_NET_TABLE_TIME);
        Log.e("----------------", "创建数据表成功");
    }

    /**
     * 插入音乐数据
     * @param title
     * @param artist
     * @param url
     * @param album
     * @param duration
     * @param size
     * @param isLove
     */
    public void insertData(String table,String title, String artist,long artist_id, String url, String album,long album_id, int duration, long size,int isLove) {
        long createResult = 0;
        ContentValues initialValues = new ContentValues();
        initialValues.put(MusicTable.TITLE, title);
        initialValues.put(MusicTable.ARTIST, artist);
        initialValues.put(MusicTable.ARTIST_ID, artist_id);
        initialValues.put(MusicTable.URL, url);
        initialValues.put(MusicTable.ALBUN, album);
        initialValues.put(MusicTable.ALBUN_ID, album_id);
        initialValues.put(MusicTable.DURATION, duration);
        initialValues.put(MusicTable.SIZE, size);
        initialValues.put(MusicTable.ISLOVE, isLove);
        try {
            createResult = mDB.insert(table, null, initialValues);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * 更新音乐属性
     *
     * @param musicInfo
     */
    public void updateData(MusicInfo musicInfo) {
        String selection = MusicTable.KEY_id+"=" + musicInfo.getId();
        String[] str = {MusicTable.TITLE, MusicTable.ARTIST};
        mDB.delete(MusicTable.TABLE_NAME, selection, null);
        insertData(MusicTable.TABLE_NAME, musicInfo.getTitle(), musicInfo.getArtist(), musicInfo.getArtist_id(), musicInfo.getUrl(),
                musicInfo.getAlbum(), musicInfo.getAlbum_id(), musicInfo.getDuration(), musicInfo.getSize(),
                musicInfo.getIsLove());
    }

    /**
     * 删除音乐文件
     * @param id
     */
    public void delete(int id) {
        String whereClause= MusicTable.KEY_id+"="+id;
        mDB.delete(MusicTable.TABLE_NAME, whereClause, null);
    }

    /**
     * 删除网络数据
     */
    public void deleteNet(){
        mDB.delete(NetMusicTable.TABLE_NAME, null, null);
    }


    /**
     * 查询单首音乐
     * @param title
     * @return
     */
    public MusicInfo fetchMusic(String table,String title) {
        MusicInfo musicInfo=new MusicInfo();
        String selection = MusicTable.TITLE+"=" + title;
        String[] str = {MusicTable.TITLE, MusicTable.ARTIST, MusicTable.URL, MusicTable.ALBUN,
                MusicTable.DURATION, MusicTable.SIZE, MusicTable.ISLOVE};
        mCursor = mDB.query(table, str, selection, null, null, null, null);
        if (mCursor.moveToFirst()) {
            musicInfo.setId(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.KEY_id)));
            musicInfo.setTitle(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.TITLE)));
            musicInfo.setArtist(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ARTIST)));
            musicInfo.setUrl(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.URL)));
            musicInfo.setAlbum(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ALBUN)));
            musicInfo.setDuration(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.DURATION)));
            musicInfo.setSize(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.SIZE)));
            musicInfo.setIsLove(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.ISLOVE)));
        }
        if (mCursor != null) {
            mCursor.close();
            // 当没有查询到数据时，返回null
        }
        return musicInfo;
    }


    /**
     * 查询收藏音乐
     *
     * @return
     */
    public ArrayList<MusicInfo> fetchCollectMusic() {
        musicInfos = new ArrayList<>();
        String selection = MusicTable.ISLOVE+"=" + 1;
        String[] str = {MusicTable.KEY_id,MusicTable.TITLE, MusicTable.ARTIST, MusicTable.ARTIST_ID,MusicTable.URL, MusicTable.ALBUN,
               MusicTable.ALBUN_ID, MusicTable.DURATION, MusicTable.SIZE, MusicTable.ISLOVE};
        mCursor = mDB.query(MusicTable.TABLE_NAME, str, selection, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setId(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.KEY_id)));
                musicInfo.setTitle(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.TITLE)));
                musicInfo.setArtist(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ARTIST)));
                musicInfo.setArtist_id(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.ARTIST_ID)));
                musicInfo.setUrl(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.URL)));
                musicInfo.setAlbum(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ALBUN)));
                musicInfo.setAlbum_id(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.ALBUN_ID)));
                musicInfo.setDuration(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.DURATION)));
                musicInfo.setSize(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.SIZE)));
                musicInfo.setIsLove(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.ISLOVE)));
                musicInfos.add(musicInfo);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
        }
        return musicInfos;
    }


    /**
     * 查询所有音乐
     *
     * @return
     */
    public ArrayList<MusicInfo> fetchAll(String table) {
        musicInfos = new ArrayList<>();
        String[] str = {MusicTable.KEY_id,MusicTable.TITLE, MusicTable.ARTIST,MusicTable.ARTIST_ID, MusicTable.URL, MusicTable.ALBUN,
                MusicTable.ALBUN_ID,MusicTable.DURATION, MusicTable.SIZE, MusicTable.ISLOVE};
        mCursor = mDB.query(table, str, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setId(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.KEY_id)));
                musicInfo.setTitle(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.TITLE)));
                musicInfo.setArtist(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ARTIST)));
                musicInfo.setArtist_id(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.ARTIST_ID)));
                musicInfo.setUrl(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.URL)));
                musicInfo.setAlbum(mCursor.getString(mCursor.getColumnIndexOrThrow(MusicTable.ALBUN)));
                musicInfo.setAlbum_id(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.ALBUN_ID)));
                musicInfo.setDuration(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.DURATION)));
                musicInfo.setSize(mCursor.getLong(mCursor.getColumnIndexOrThrow(MusicTable.SIZE)));
                musicInfo.setIsLove(mCursor.getInt(mCursor.getColumnIndexOrThrow(MusicTable.ISLOVE)));
                musicInfos.add(musicInfo);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
        }
        Log.e("=============","读取数据成功");
        return musicInfos;
    }

    /**
     * 获取本地的MP3 文件
     *
     * @return 返回 Arraytlist音乐列表
     */
    public void  getLocalMusic(ContentResolver mContentResolver) {
        mDB.delete(MusicTable.TABLE_NAME,null,null);
        Cursor cursor = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (cursor.moveToNext()) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    musicInfo.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    musicInfo.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    musicInfo.setArtist_id(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
                    musicInfo.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    musicInfo.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    musicInfo.setAlbum_id(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                    musicInfo.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    musicInfo.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    musicInfo.setIsLove(0);
                    insertData(MusicTable.TABLE_NAME,musicInfo.getTitle(), musicInfo.getArtist(), musicInfo.getArtist_id(), musicInfo.getUrl(),
                            musicInfo.getAlbum(),musicInfo.getAlbum_id() ,musicInfo.getDuration(), musicInfo.getSize(),
                            musicInfo.getIsLove());
                }
            }
            cursor.close();
        }

    }

}

