package com.huanghj.mp3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by myhug on 2016/4/9.
 */
public class BaseHelper extends SQLiteOpenHelper {

    /**
     * 数据库名
    */
    public static final String DB_NAME = "MusicDatabase.db";

    /**
     * 数据库版本
     */
    public static final int DB_VERSION = 1;

    /**
     * 创建baseHelper对象
     */
    private static BaseHelper mInstance;

    /**
     * 创建表语句
     */
    public static final String CREATE_TABLE_TIME = "CREATE TABLE if not exists "
            + MusicTable.TABLE_NAME + " ( " + MusicTable.KEY_id
            + " integer PRIMARY KEY autoincrement," +
            MusicTable.TITLE + " TEXT," +
            MusicTable.ARTIST + " TEXT," +
            MusicTable.ARTIST_ID + " DECIMAL," +
            MusicTable.URL + " TEXT," +
            MusicTable.ALBUN + " TEXT," +
            MusicTable.ALBUN_ID + " DECIMAL," +
            MusicTable.DURATION + " INTEGER," +
            MusicTable.SIZE + " DECIMAL," +
            MusicTable.ISLOVE + " INTEGER );";

    /**
     * 创建表语句
     */
    public static final String CREATE_NET_TABLE_TIME = "CREATE TABLE if not exists "
            + NetMusicTable.TABLE_NAME + " ( " + NetMusicTable.KEY_id
            + " integer PRIMARY KEY autoincrement," +
            NetMusicTable.TITLE + " TEXT," +
            NetMusicTable.ARTIST + " TEXT," +
            NetMusicTable.ARTIST_ID + " DECIMAL," +
            NetMusicTable.URL + " TEXT," +
            NetMusicTable.ALBUN + " TEXT," +
            NetMusicTable.ALBUN_ID + " DECIMAL," +
            NetMusicTable.DURATION + " INTEGER," +
            NetMusicTable.SIZE + " DECIMAL," +
            NetMusicTable.ISLOVE + " INTEGER );";

    /**
     * 以单例模式创建helper
     *
     * @param context
     * @return basehelper
     */
    static public BaseHelper getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new BaseHelper(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
        }
        return mInstance;
    }

    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 创建方法，可以在这里创建数据表
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TIME);
        db.execSQL(CREATE_NET_TABLE_TIME);

        Log.e("TAG","创建表成功");
    }

    /**
     * 版本更新方法
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
