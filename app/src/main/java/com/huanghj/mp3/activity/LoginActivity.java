package com.huanghj.mp3.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huanghj.mp3.R;
import com.huanghj.mp3.entity.LoginEvent;
import com.huanghj.mp3.fragment.MyFragment;
import com.huanghj.mp3.util.BackGutil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import de.greenrobot.event.EventBus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String NAME="name";

    private EditText mUserName,mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RelativeLayout relativeLayout= (RelativeLayout) findViewById(R.id.relayout_login);
        BackGutil.setBg(relativeLayout, this);
        init();
    }

    private void init(){
        mUserName= (EditText) findViewById(R.id.edit_user);
        mPassword= (EditText) findViewById(R.id.edit_pwd);
        mLogin= (Button) findViewById(R.id.bt_login);
        mLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        checkNetworkState();
    }



    private boolean checkNetworkState() {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (flag) {
            login();
        } else {
            Toast.makeText(LoginActivity.this, "网络没连接，请先连接网络", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }

    public void login(){
        BmobUser  bu2 = new BmobUser();
        bu2.setUsername(mUserName.getText().toString());
        bu2.setPassword(mPassword.getText().toString());
        bu2.login(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                BmobUser user =  BmobUser.getCurrentUser(LoginActivity.this, BmobUser.class);
                EventBus.getDefault().post(new LoginEvent(user.getUsername()));
               LoginActivity.this.finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

