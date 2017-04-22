package com.huanghj.mp3.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.huanghj.mp3.LocalListActivity;
import com.huanghj.mp3.MainActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.SettingActivity;
import com.huanghj.mp3.activity.AboutActivity;
import com.huanghj.mp3.activity.CheckUpdateActivity;
import com.huanghj.mp3.activity.CollectActivity;
import com.huanghj.mp3.activity.DownLoadActivity;
import com.huanghj.mp3.activity.LoginActivity;
import com.huanghj.mp3.db.MusicTable;
import com.huanghj.mp3.db.MyDBAdapter;
import com.huanghj.mp3.db.NetMusicTable;
import com.huanghj.mp3.entity.LoginEvent;
import com.huanghj.mp3.util.SharePreferenceData;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * created by huanghj
 */
public class MyFragment extends Fragment implements View.OnClickListener{

    /**
     * 跳转登录页面
     */
    private RelativeLayout mLoginlLayout;

    /**
     * 跳转本地音乐layout
     */
    private RelativeLayout mLocalLayout;

    /**
     * 跳转下载页面layout
     */
    private RelativeLayout mDownloadLayout;

    /**
     * 跳转收藏页面layout
     */
    private RelativeLayout mCollectLayout;

    /**
     * 跳转设置页面layout
     */
    private RelativeLayout mSettingLayout;

    /**
     * 跳转更新页面的layout
     */
    private RelativeLayout mUpdateLayout;

    /**
     * 跳转关于页面的layout
     */
    private RelativeLayout mAboutLayout;

    private TextView songNum,downNum,collectNum,mUserName;

    private MyDBAdapter myDBAdapter;

    private EventBus eventBus;


    public MyFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my, container, false);

        mLoginlLayout= (RelativeLayout) view.findViewById(R.id.layout_login);
        mLocalLayout= (RelativeLayout) view.findViewById(R.id.layout_local);
        mUserName= (TextView) view.findViewById(R.id.lo_username);
        songNum= (TextView) view.findViewById(R.id.songnum);
        downNum= (TextView) view.findViewById(R.id.downloadNum);
        collectNum= (TextView) view.findViewById(R.id.collectNum);
        mDownloadLayout= (RelativeLayout) view.findViewById(R.id.layout_download);
        mCollectLayout= (RelativeLayout) view.findViewById(R.id.layout_collect);
        mSettingLayout= (RelativeLayout) view.findViewById(R.id.layout_setting);
        mAboutLayout= (RelativeLayout) view.findViewById(R.id.layout_about);
        mUpdateLayout= (RelativeLayout) view.findViewById(R.id.layout_update);
        mLoginlLayout.setOnClickListener(this);
        mLocalLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mCollectLayout.setOnClickListener(this);
        mDownloadLayout.setOnClickListener(this);
        mSettingLayout.setOnClickListener(this);
        mUpdateLayout.setOnClickListener(this);
        initData();
        return view;
    }

    private void initData(){
        myDBAdapter=new MyDBAdapter(getActivity());
        songNum.setText(myDBAdapter.fetchAll(MusicTable.TABLE_NAME).size()+"");
        downNum.setText("0");
        String use=SharePreferenceData.readUser(getActivity()).getString(SharePreferenceData.NAME,"");
        if(use!=null){
            mUserName.setText(use);
        }
        collectNum.setText(myDBAdapter.fetchCollectMusic().size() + "");
        if(eventBus==null){
            eventBus=EventBus.getDefault();
            eventBus.register(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_login:
                Toast.makeText(getActivity(), "登录", Toast.LENGTH_SHORT).show();
                Intent intent6=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent6);
                break;
            case R.id.layout_local:
                Toast.makeText(getActivity(), "本地", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), LocalListActivity.class);
                intent.putExtra(MainActivity.LIST,MainActivity.LOCAL);
                startActivity(intent);
                break;
            case R.id.layout_download:
                Toast.makeText(getActivity(),"下载",Toast.LENGTH_SHORT).show();
                Intent intent5=new Intent(getActivity(), DownLoadActivity.class);
                startActivity(intent5);
                break;
            case R.id.layout_collect:
                Toast.makeText(getActivity(),"收藏",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(getActivity(), CollectActivity.class);
                startActivity(intent1);
                break;
            case R.id.layout_setting:
                Toast.makeText(getActivity(),"设置",Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent2);
                break;
            case R.id.layout_update:
                Toast.makeText(getActivity(),"更新",Toast.LENGTH_SHORT).show();
                Intent intent3=new Intent(getActivity(), CheckUpdateActivity.class);
                startActivity(intent3);
                break;
            case R.id.layout_about:
                Toast.makeText(getActivity(),"关于",Toast.LENGTH_SHORT).show();
                Intent intent4=new Intent(getActivity(), AboutActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    public void onEventBackgroundThread(LoginEvent event) {
        String msg = event.getMsg();
        mUserName.setText(msg);
    }

    @Override
    public void onDestroy() {
        SharePreferenceData.saveUser(getActivity(),mUserName.getText().toString());
        super.onDestroy();
        if(eventBus!=null){
            eventBus.unregister(this);
        }
    }
}
