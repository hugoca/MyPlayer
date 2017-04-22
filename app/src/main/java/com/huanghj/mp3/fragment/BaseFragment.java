package com.huanghj.mp3.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huanghj.mp3.R;
import com.huanghj.mp3.util.MyEvent;

/**
 *
 */
public abstract class BaseFragment extends Fragment {

    public BaseFragment() {
    }

    public abstract void onEvent(MyEvent eventData);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }


}
