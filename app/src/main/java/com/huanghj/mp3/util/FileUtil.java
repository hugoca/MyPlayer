package com.huanghj.mp3.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by xiefei on 2016/3/8.
 * 用来保存数据对象。
 */
public class FileUtil {
    /**
     *  @param file 文件
     *  @param t 泛型,强制指定存储对象类型
     *  @return 返回状态信息
     */
    public static <T>boolean saveFile(final File file, final T t){
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(t);
            objectOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     *@param  file 文件名
     *@return 返回数据
     */
    public static <T>T readFile(final File file){
        Log.d("readFile","read->"+file.getPath());
        if(!file.exists()){
            return null;
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            T t  = (T) objectInputStream.readObject();
            return t;
        }  catch (Exception e) {
            return null;
        }
    }
    public static void deleteFile(String filePath){
        File file = new File(filePath);
        if(file.isFile()){
            deleteFile(file);
        }else {
            File[] files = file.listFiles();
            for (File file1: files){
                deleteFile(file1);
            }
        }
    }
    /*
    删除文件
     */
    public static void deleteFile(File file){
        if(file.exists()){
            file.delete();
        }
    }
}
