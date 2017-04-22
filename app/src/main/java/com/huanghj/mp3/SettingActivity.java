package com.huanghj.mp3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.huanghj.mp3.fragment.BaseFragment;
import com.huanghj.mp3.util.BackGutil;
import com.huanghj.mp3.util.NetWork;

import static com.huanghj.mp3.R.id.switch_wifi;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener{
    /**
     * wifi状态下开关
     */
    private Switch mWifiSwitch;

    /**
     * 同时下载歌词和图片开关
     */
    private Switch mDownWordSwitch;
    private RelativeLayout relativeLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        relativeLayout= (RelativeLayout) findViewById(R.id.relayout_set);
        BackGutil.setBg(relativeLayout, this);

        mWifiSwitch= (Switch) findViewById(switch_wifi);
        mDownWordSwitch= (Switch) findViewById(R.id.down_words);
        mWifiSwitch.setOnCheckedChangeListener(this);
        mDownWordSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch_wifi:
                Log.d("Tag","仅在WiFi下试听/下载");
                NetWork.setIsWifiDown(mWifiSwitch.isChecked(),this);
                break;
            case R.id.down_words:
                Log.d("Tag","下载歌词和图片");
                break;
            default:
                break;

        }
    }
}
