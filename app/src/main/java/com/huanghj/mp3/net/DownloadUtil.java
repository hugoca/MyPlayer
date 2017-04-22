package com.huanghj.mp3.net;

import android.util.Log;


import com.huanghj.mp3.entity.DownloadEntity;
import com.huanghj.mp3.util.BusProvider;
import com.huanghj.mp3.util.MusicInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by myhug 2016/3/3.
 * 下载音乐工具类
 */
public class DownloadUtil {
    private final String Tag = "DownloadUtil";
    private static ExecutorService executorService = null;
//    private String downloadUrl;//下载地址
//    private String fileDir;//保存文件地址
    private LinkedList<DownloadEntity> downloadEntities = new LinkedList<>();
    private static DownloadUtil downloadUtil;
    public static DownloadUtil getInstance(){
        if(downloadUtil == null){
            synchronized (DownloadUtil.class){
                if(downloadUtil == null){
                    downloadUtil = new DownloadUtil();
                }
            }
        }
        return downloadUtil;
    }

    public LinkedList<DownloadEntity> getDownloadEntities() {
        return downloadEntities;
    }

    private DownloadUtil(){
        if(executorService==null)
            executorService = Executors.newFixedThreadPool(3);
    }

//    public String getFileDir() {
//        return fileDir;
//    }

//    private DownloadUtil(int maxThreads){
//        if(executorService==null)
//            executorService = Executors.newFixedThreadPool(maxThreads);
//    }
//    public DownloadUtil addDownloadUrl(String url){
//        return addDownloadUrl(url,null);
//    }
//    public DownloadUtil addDownloadUrl(String url,String fileName){
//        return addDownloadUrl(url,fileName,null);
//    }
    //
    public DownloadUtil addDownloadUrl(String filePath,String url,String fileName){
        for (DownloadEntity downloadEntity:downloadEntities) {
            if(downloadEntity.url.equals(url)){
                return downloadUtil;
            }
        }
        DownloadEntity downloadEntity = new DownloadEntity();
        downloadEntity.url = (url);
        downloadEntity.fileName = (fileName);
        downloadEntity.filePath = filePath+File.separator+fileName;
        downloadEntities.add(downloadEntity);
        start(downloadEntity);
        return downloadUtil;
    }

    public DownloadUtil addDownloadUrl(String filePath,String url,String fileName,String artistName,int duration){
        for (DownloadEntity downloadEntity:downloadEntities) {
            if(downloadEntity.url.equals(url)){
                return downloadUtil;
            }
        }
        DownloadEntity downloadEntity = new DownloadEntity();
        downloadEntity.url = (url);
        downloadEntity.fileName = (fileName);
        downloadEntity.artistName = artistName;
        downloadEntity.duration = duration;
        downloadEntity.filePath = filePath+File.separator+fileName;
        downloadEntities.add(downloadEntity);
        start(downloadEntity);
        return downloadUtil;
    }
    public void remove(DownloadEntity entity){
        downloadEntities.remove(entity);
    }
    public void remove(String url){
        DownloadEntity entity = new DownloadEntity();
        entity.url = url;
        downloadEntities.remove(entity);
    }

    public void pause(String url){
        for (DownloadEntity downloadEntity:downloadEntities) {
            if(downloadEntity.url.equals(url)){
//                downloadEntities.remove(downloadEntity);
                downloadEntity.isPause = true;
                start(downloadEntity);
                break;
            }
        }

    }
    public void cancel(String url){
        for (DownloadEntity downloadEntity:downloadEntities) {
            if(downloadEntity.url.equals(url)){
                downloadEntity.isCancel = true;
                downloadEntities.remove(downloadEntity);
                break;
            }
        }
    }
//    public DownloadUtil setFileDir(String fileDir) {
//        this.fileDir  = fileDir;
//        return downloadUtil;
//    }
//    private Observable downloadObservable = Observable.interval(1, TimeUnit.SECONDS).map(new Func1<Long,LinkedList<DownloadEntity>>() {
//        @Override
//        public LinkedList<DownloadEntity> call(Long aLong) {
//            return downloadEntities;
//        }
//    });
//    public rx.Observable getObservable(){
//        return downloadObservable;
//    }
    private void start(final DownloadEntity entity){
        synchronized (entity){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    OutputStream outputStream = null;
                    HttpURLConnection httpURLConnection = null;
//                    String download currentFileUrl = entity.getUrl();
                    try {
                        url = new URL(entity.url);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
//                        httpURLConnection.setDoOutput(false);
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(5 * 1000);
//                        httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                        httpURLConnection.setRequestProperty(
                                "Accept",
                                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                        httpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
                        httpURLConnection.setRequestProperty("Referer", entity.url);
                        httpURLConnection.setRequestProperty("Charset", "UTF-8");
                        httpURLConnection.setRequestProperty(
                                "User-Agent",
                                "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                        if(entity.fileName == null){
                            entity.fileName = getFileName(entity.url);
                        }
                        File file = new File(entity.filePath);
                        entity.fileSize = (getRemoteFileSize(entity.url));
                        if(entity.duration == 0){
                            entity.duration = entity.fileSize;
                        }
                        if (file.exists()) {
//                            handler.sendEmptyMessage(GOON);
                            //进行下载长度的设置
                            if(file.length()==entity.fileSize){
                                entity.isFinish = true;
                                MusicInfo musicInfo = new MusicInfo();
                                musicInfo.setUrl(entity.filePath);
                                musicInfo.setTitle(entity.fileName);
                                musicInfo.setArtist(entity.artistName);
                                musicInfo.setDuration(entity.duration);
                                BusProvider.getInstance().post(musicInfo);
                                downloadEntities.remove(entity);
                            }
                            httpURLConnection.setRequestProperty("Range", "bytes=" + file.length() + "-" + entity.fileSize);
                            entity.currentSize = ((int) file.length());
                        } else {
//                            file.createNewFile();
                            httpURLConnection.setRequestProperty("Range", "bytes=" + 0 + "-" +entity.fileSize);
                            entity.currentSize  = 0;
                        }
                        httpURLConnection.connect();
                        Log.d(Tag,"respondCode->"+httpURLConnection.getResponseCode());
                        if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode() < 300) {
                            entity.isStart = true;
                            InputStream inputStream = httpURLConnection.getInputStream();
                            byte[] bytes = new byte[1024];
                            int len = -1;
                            if(!file.exists())
                                file.createNewFile();
                            entity.filePath = file.getPath();
                            outputStream = new FileOutputStream(file, true);
                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    BusProvider.getInstance().post(entity);
                                }
                            };
                            timer.schedule(timerTask,1000,1000);
                            while ((len = inputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                                entity.lastSize = entity.currentSize;
                                entity.currentSize +=  len;
                                if (entity.isPause) {
                                    timer.cancel();
                                    break;
                                } else if (entity.isCancel) {
                                    file.delete();
                                    timer.cancel();
                                    downloadEntities.remove(entity);
                                    break;
                                }
                                else if (entity.currentSize == entity.fileSize) {
                                    entity.isFinish = true;
                                    timer.cancel();
                                        MusicInfo musicInfo = new MusicInfo();
                                        musicInfo.setUrl(entity.filePath);
                                        musicInfo.setTitle(entity.fileName);
                                        musicInfo.setArtist(entity.artistName);
                                        musicInfo.setDuration(entity.duration);
                                        BusProvider.getInstance().post(musicInfo);
                                    downloadEntities.remove(entity);
                                    Log.d(Tag, "downloadSuccess");
                                break;
                                }
                            }

                        } else {
                            entity.isFail = true;
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                    }
                }
            });
        }
    }
    private String getFileName(String url){
        String result =null;
        String[] names = url.split("/");
        result = names[names.length-1];
//        if(!result.endsWith(".mp3")){
//            result += ".mp3";
//        }
        return result;
    }
    private int getRemoteFileSize(String url){
        int size = -1;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            httpURLConnection.setConnectTimeout(5 * 1000);
            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode()>=200&&httpURLConnection.getResponseCode()<300){
                size = httpURLConnection.getContentLength();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }
}
