package com.huanghj.mp3.util;

import com.squareup.otto.Bus;

/**
 * Created by myhug on 2016/3/22.
 */
public class BusProvider {
    private final static Bus bus = new MainThreadBus();
    public static Bus getInstance(){
        return bus;
    };
    private BusProvider(){}
}
