package com.huanghj.mp3.Gson;

import java.util.ArrayList;

/**
 * Created by myhug on 2016/4/18.
 */
public class NearDynamic {
    private Boolean next=false;
    private  int error;
    private ArrayList<Music> array = new ArrayList<Music>();
    public Boolean getNext() {
        return next;
    }
    public void setNext(Boolean next) {
        this.next = next;
    }
    public int getError() {
        return error;
    }
    public void setError(int error) {
        this.error = error;
    }
    public ArrayList<Music> getArray() {
        return array;
    }
    public void setArray(ArrayList<Music> array) {
        this.array = array;
    }
}