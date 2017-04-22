package com.huanghj.mp3.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.huanghj.mp3.BaseActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.net.MyStringRequest;
import com.huanghj.mp3.util.BackGutil;

public class CheckUpdateActivity extends BaseActivity {
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_update);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_update);
        BackGutil.setBg(relativeLayout, this);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url="http://music.qq.com/musicbox/shop/v3/data/hit/hit_all.js";

        MyStringRequest stringRequest = new MyStringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(stringRequest);
    }
}
