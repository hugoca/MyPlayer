package com.huanghj.mp3.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by  myhug on 2015/8/29.
 */
public class DiskCacheDir {
    //获取缓存
    public static String getStringDiskCacheDir(Context context,String uniqueName){
        String path = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            //私有目录（外部储存）
            path=context.getExternalCacheDir().getAbsolutePath();
        }else {
            //内部储存
            path=context.getCacheDir().getAbsolutePath();
        }
        path = path+ File.separator+uniqueName;
        return path;
    }
    public static File getStringDiskMusicCacheDir(Context context){
        File file = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        }else{
            file = context.getFilesDir();
        }

        return file;
    }
    public static String getStringDiskMusicLrcCacheDir(Context context){
        String file = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            file = Environment.getExternalStorageDirectory().getPath()+File.separator+"Musiclrc";
        }
        return file;
    }
    public static void cleanCache(Context context){
        String path = null;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())||!Environment.isExternalStorageRemovable()){
            path=context.getExternalCacheDir().getPath();
        }else {
            path=context.getCacheDir().getPath();
        }
        File dir = new File(path);
        deleteDir(dir);
    }

    private static void deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }

    /**
     *
     * @param context 获取
     * @param uniqueName
     * @return
     */
    public static File getFileDiskCacheDir(Context context,String uniqueName){
        return new File(getStringDiskCacheDir(context,uniqueName));
    }
}
