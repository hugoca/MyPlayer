package com.huanghj.mp3.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by myhug on 2016/1/20.
 */
public class MusicInfo implements Parcelable {
    private long id;
    private String title;
    private String album;
    private long album_id;
    private int duration;
    private long size;
    private String artist;
    private long artist_id;
    private String url;
    private int isLove;

    public MusicInfo() {
    }

    public long getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(long artist_id) {
        this.artist_id = artist_id;
    }

    public long getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(long album_id) {
        this.album_id = album_id;
    }

    public MusicInfo(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getIsLove() {
        return isLove;
    }

    public void setIsLove(int isLove) {
        this.isLove = isLove;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", album_id=" + album_id +
                ", duration=" + duration +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", artist_id=" + artist_id +
                ", url='" + url + '\'' +
                ", isLove=" + isLove +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(artist);
        dest.writeLong(artist_id);
        dest.writeString(url);
        dest.writeLong(album_id);
        dest.writeInt(duration);
        dest.writeLong(size);
    }




    public static final Parcelable.Creator<MusicInfo>
            CREATOR = new Creator<MusicInfo>() {

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.setId(source.readLong());
            musicInfo.setTitle(source.readString());
            musicInfo.setAlbum(source.readString());
            musicInfo.setArtist(source.readString());
            musicInfo.setArtist_id(source.readLong());
            musicInfo.setUrl(source.readString());
            musicInfo.setAlbum_id(source.readLong());
            musicInfo.setDuration(source.readInt());
            musicInfo.setSize(source.readLong());
            return musicInfo;
        }
    };

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public static String formatSize(long size) {
        String strSize = null;
        if (size > 1024 * 1024) {
            strSize = size / 1024 / 1024 + " M " + size % 1024 + " KB";
        } else {
            strSize = size % 1024 / 1024 + "KB";
        }

        return strSize;
    }
}
