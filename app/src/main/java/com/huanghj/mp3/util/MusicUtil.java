package com.huanghj.mp3.util;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;



import com.huanghj.mp3.entity.DownloadEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by  myhug on 2016/3/14.
 */
public class MusicUtil {
    private  String Tag = "MusicUtils";
    private ArrayList<MusicInfo> musicList = null;
    private Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private Map<String,ArrayList<MusicInfo>> artists;
    private Map<String,ArrayList<MusicInfo>> albums;
    private Map<String,ArrayList<MusicInfo>> songMenu;
    private Map<String,ArrayList<MusicInfo>> files;
    private ArrayList<String> songMenuName;
    private ArrayList<MusicInfo> historyPlay;
    private ArrayList<MusicInfo> downloadMusic;
    private ArrayList<MusicInfo> collectMusic;
    private ArrayList<DownloadEntity> prepareDownload;
    private File historyFile = null;
    private  File downloadFile = null;
    private  File collectFile = null;
    private  File songMenuFile = null;
    private static MusicUtil musicUtil;
    private Context context;
    private MusicUtil(Context context){
        if(this.context==null)
            this.context = context.getApplicationContext();
        historyFile = new File(context.getFilesDir(), "history.txt");
        downloadFile = new File(context.getFilesDir(),"download.txt");
        collectFile = new File(context.getFilesDir(),"collect.txt");
        songMenuFile = new File(context.getFilesDir(),"songMenu.txt");
    }
    public static  MusicUtil getInstance(Context context){
        if(musicUtil==null){
            musicUtil = new MusicUtil(context);
        }
        return musicUtil;
    }
    private  ArrayList<MusicInfo> getAllMusic(){
        if(musicList==null){

            musicList = new ArrayList<>();
            context = context.getApplicationContext();
            Cursor audioCursor = context.getContentResolver().query(uri,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            if(!audioCursor.moveToFirst()){
                Log.d("musicUtils", "本地音乐文件为null");
                audioCursor.close();
                return musicList;
            }
            PinyinConv conv = new PinyinConv();
            do{
                int duration = audioCursor.getInt(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                if(duration>120000) {
                    MusicInfo musicInfo = new MusicInfo();
                    musicInfo.setId(audioCursor.getInt(audioCursor
                            .getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    musicInfo.setTitle(audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    String artist = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    if (artist.equals("<unknown>")){
                        artist = "未知艺术家";
                    }
                    musicInfo.setArtist(artist);
                    musicInfo.setArtist_id(audioCursor.getLong(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)));
                    String data = audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    musicInfo.setUrl(data);
                    musicInfo.setAlbum(audioCursor.getString(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                    musicInfo.setAlbum_id(audioCursor.getLong(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
                    musicInfo.setDuration(duration);
                    musicInfo.setSize(audioCursor.getLong(audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    musicInfo.setIsLove(0);

//                    File file = new File(data);
//                    data = file.getParentFile().getName();
//                    musicInfo.setData(data);
                    musicList.add(musicInfo);
                }
            }while (audioCursor.moveToNext());
            audioCursor.close();
        }
        for (int i=0;i<musicList.size();i++){
           Log.e("**************", musicList.get(i).toString());
        }
        return musicList;
    }
    private  Map<String,ArrayList<MusicInfo>> getMusicGroupByAlbum(){
        if(albums == null){
            albums = new HashMap<String,ArrayList<MusicInfo>>();
            if(musicList==null){
                getAllMusic();
            }
            ArrayList<MusicInfo> musics;
            for (MusicInfo musicInfo:musicList) {
                if(!albums.containsKey(musicInfo.getAlbum())){
                    musics = new ArrayList<>();
                    musics.add(musicInfo);
                    albums.put(musicInfo.getAlbum(),musics);
                }else {
                    musics = albums.get(musicInfo.getAlbum());
                    musics.add(musicInfo);
                }
            }
        }
        return albums;
    }
    private Map<String,ArrayList<MusicInfo>> getMusicGroupByFileName(){
        if(files == null){
            files = new HashMap<String,ArrayList<MusicInfo>>();
            if(musicList==null){
                getAllMusic();
            }
            ArrayList<MusicInfo> musics;
            for (MusicInfo musicInfo:musicList) {
                File file = new File(musicInfo.getUrl());
                String dataName=file.getParentFile().getName();
                if(!files.containsKey(dataName)){
                    musics = new ArrayList<>();
                    musics.add(musicInfo);
                    files.put(dataName,musics);
                }else {
                    musics = files.get(dataName);
                    musics.add(musicInfo);
                }
            }
        }
        return files;
    }
    private  Map<String,ArrayList<MusicInfo>> getMusicGroupByArtist(){
        if(artists == null){
            artists = new HashMap<String,ArrayList<MusicInfo>>();
            if(musicList==null){
                getAllMusic();
            }
            ArrayList<MusicInfo> musics;
            for (MusicInfo musicInfo:musicList) {
                if(!artists.containsKey(musicInfo.getArtist())){
                    musics = new ArrayList<>();
                    musics.add(musicInfo);
                    artists.put(musicInfo.getArtist(),musics);
                }else {
                    musics = artists.get(musicInfo.getArtist());
                    musics.add(musicInfo);
                }
            }
        }
        return artists;
    }
    private Map<String,ArrayList<MusicInfo>> getSongMenu(){
        if(songMenu!=null){
            return songMenu;
        }
        songMenu = FileUtil.readFile(songMenuFile);
        if(songMenu == null){
            songMenu = new LinkedHashMap<>();
            songMenu.put("我的收藏",new ArrayList<MusicInfo>());
        }
        return songMenu;
    }
    public synchronized void saveSongMenu(){
        FileUtil.saveFile(songMenuFile, songMenu);
    }
    public ArrayList<String> getSongMenuName(){
        if(songMenuName!= null)
            return songMenuName;
        songMenuName = new ArrayList<>();
        if(songMenu==null)
            getSongMenu();
        Set<String> stringSet = songMenu.keySet();
        for (String s:stringSet) {
            songMenuName.add(s);
        }
        return songMenuName;
    }
    public void addSongMenu(String name){
        if(songMenu==null)
            getSongMenu();
        ArrayList arrayList;
        if(!songMenu.containsKey(name)){
            if (songMenuName!=null)
                songMenuName.add(name);
            arrayList = new ArrayList();
            songMenu.put(name, arrayList);
        }
        saveSongMenu();
    }
    public void removeSongMenu(String name){
        if(songMenu==null)
            getSongMenu();
        if(songMenu.containsKey(name)){
            if (songMenuName!=null)
                songMenuName.remove(name);
            songMenu.remove(name);
            saveSongMenu();
        }
    }
    public void renameSongMenu(String oldName,String newName){
        if(songMenu==null)
            getSongMenu();
        ArrayList<MusicInfo> list = songMenu.get(oldName);
        songMenu.remove(oldName);
        songMenu.put(newName,list);
        int index = songMenuName.indexOf(oldName);
        songMenuName.remove(oldName);
        songMenuName.add(index,newName);
        saveSongMenu();
    }
    public ArrayList<MusicInfo> getMusicByType(int type){
        if(type == Constant.ALL_TYPE){
            return getAllMusic();
        }
        else if(type==Constant.LOAD_DOWNLOAD)
            return getDownload();
        else if(type==Constant.LOAD_HISTORY)
            return  getHistoryPlay();
        return null;
    }
    public Map<String,ArrayList<MusicInfo>> getMusicGroupByType(int type){
        if(type==Constant.ALBUM_TYPE)
            return getMusicGroupByAlbum();
        else if(type==Constant.ARTIST_TYPE)
            return getMusicGroupByArtist();
        else if(type==Constant.FILE_TYPE)
            return getMusicGroupByFileName();
        else if(type==Constant.SONG_MENU_TYPE)
            return getSongMenu();
        return null;
    }
    public void addHistory(final MusicInfo musicInfo){
        if(historyPlay==null)
            getHistoryPlay();
        if(historyPlay.contains(musicInfo)){
            historyPlay.remove(musicInfo);
        }
        historyPlay.add(0, musicInfo);
        FileUtil.saveFile(historyFile, historyPlay);
    }
    public void clearHistory(){
        if(historyPlay==null)
            getHistoryPlay();
        MusicInfo musicInfo = historyPlay.get(0);
        historyPlay.removeAll(historyPlay);
        historyPlay.add(musicInfo);
        FileUtil.saveFile(historyFile, historyPlay);
    }
    private ArrayList<MusicInfo> getDownload() {
        if(downloadMusic!=null){
            Log.e("sdddddd","1");
            return downloadMusic;
        }
        downloadMusic = FileUtil.readFile(downloadFile);
        Log.e("sdddddd","2");
        if(downloadMusic == null){
            downloadMusic = new ArrayList<>();
            Log.e("sdddddd","3");
        }
        boolean isCheck = false;
        for (MusicInfo m:downloadMusic) {
            File file = new File(m.getUrl());
            String dataName=file.getParentFile().getName();
            Log.e("sdddddd","4");
            if(dataName==null){
                if(musicList == null)
                    Log.e("sdddddd", "5");
                    getAllMusic();
                Log.e("sdddddd", "6");
                isCheck = true;
                MusicInfo music = musicList.get(musicList.indexOf(m));
               Log.e("----------------------",music.toString());
                m=music;
//                m.setDuration(music.getDuration());
//                m.setId(music.getId());
//                m.setTitle(music.getTitle());
//                m.setArtist(music.getArtist());
//                m.setAlbum(music.getAlbum());

            }
        }
        if (isCheck)
            Log.e("sdddddd", "6");
            FileUtil.saveFile(downloadFile,downloadMusic);
        Log.e("sdddddd", "7");
        for (int i=0;i<downloadMusic.size();i++){
            Log.e("---------------sss",downloadMusic.get(i).toString());
        }
        return downloadMusic;
//        return getObjFromFile(downloadFile,downloadMusic);
    }
    public synchronized void  addDownload(final MusicInfo musicInfo){
        if(downloadMusic==null){
            getDownload();
        }
        if(downloadMusic.contains(musicInfo)){
            return;
        }
        downloadMusic.add(0, musicInfo);
        FileUtil.saveFile(downloadFile, downloadMusic);
    }
    public void clearDownload(){
        if(downloadMusic==null){
            getDownload();
        }
        downloadMusic.clear();
        FileUtil.saveFile(downloadFile, downloadMusic);
    }
    //        addObjToFile(downloadMusic,downloadFile,musicInfo);
    private  ArrayList<MusicInfo> getHistoryPlay() {
        if(historyPlay!=null){
            return historyPlay;
        }
        historyPlay = FileUtil.readFile(historyFile);
        if(historyPlay == null){
            historyPlay = new ArrayList<>();
        }
        return historyPlay;
//        return getObjFromFile(historyFile,historyPlay);
    }
    public  void addCollect(final MusicInfo musicInfo){
        if(collectMusic.contains(musicInfo)){
            collectMusic.remove(musicInfo);
        }
        if(collectMusic!=null){
            collectMusic.add(0, musicInfo);
            FileUtil.saveFile(collectFile, collectMusic);
        }
//        addObjToFile(collectMusic,collectFile,musicInfo);
    }
    public  void saveCollect(){
        FileUtil.saveFile(collectFile, collectMusic);
    }
    private  ArrayList<MusicInfo> getCollect() {
        if(collectMusic!=null){
            return collectMusic;
        }
        collectMusic = FileUtil.readFile(collectFile);
        if(collectMusic == null)
            collectMusic = new ArrayList<>();
        return collectMusic;
//        return getObjFromFile(collectFile,collectMusic);
    }
    public void noticeAllRemove(MusicInfo musicInfo){
        if(musicList.contains(musicInfo))
            musicList.remove(musicInfo);
        if(historyPlay==null)
            getHistoryPlay();
        if(historyPlay.contains(musicInfo)){
            historyPlay.remove(musicInfo);
            FileUtil.saveFile(historyFile, historyPlay);
        }
        if (collectMusic==null)
            getCollect();
        if(collectMusic.contains(musicInfo)){
            collectMusic.remove(musicInfo);
            FileUtil.saveFile(collectFile, collectMusic);
        }
        if(downloadMusic==null)
            getDownload();
        if(downloadMusic.contains(musicInfo)){
            downloadMusic.remove(musicInfo);
            FileUtil.saveFile(downloadFile, downloadMusic);
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+musicInfo.getUrl())));
    }
}