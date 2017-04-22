package com.huanghj.mp3.entity;

/**
 * Created by xiefei on 2016/3/15.
 */
public class DownloadEntity {
    public String url;
    public volatile boolean isStart = false;
    public boolean isPause = false;
    public boolean isCancel = false;
    public boolean isFinish = false;
    public int lastSize = 0;
    public int currentSize = 0;
    public int fileSize;
    public String artistName;
    public String fileName;
    public boolean isFail = false;
    public String filePath;
    public int duration;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DownloadEntity that = (DownloadEntity) o;
        return fileName.equals(that.fileName);

    }

    @Override
    public int hashCode() {
        return fileName.hashCode();
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "url='" + url + '\'' +
                ", isStart=" + isStart +
                ", isPause=" + isPause +
                ", isCancel=" + isCancel +
                ", isFinish=" + isFinish +
                ", lastSize=" + lastSize +
                ", currentSize=" + currentSize +
                ", fileSize=" + fileSize +
                ", fileName='" + fileName + '\'' +
                ", isFail=" + isFail +
                '}';
    }
}
