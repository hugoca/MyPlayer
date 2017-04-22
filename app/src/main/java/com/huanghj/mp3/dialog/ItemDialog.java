package com.huanghj.mp3.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huanghj.mp3.Gson.Music;
import com.huanghj.mp3.MusicInfoActivity;
import com.huanghj.mp3.R;
import com.huanghj.mp3.activity.AboutActivity;
import com.huanghj.mp3.db.MusicTable;
import com.huanghj.mp3.net.DownloadUtil;
import com.huanghj.mp3.net.SearchNetUtil;
import com.huanghj.mp3.service.PlayService;
import com.huanghj.mp3.util.DiskCacheDir;
import com.huanghj.mp3.util.MusicInfo;
import com.huanghj.mp3.util.NetWork;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class ItemDialog {

    public static void  init(LayoutInflater inflater,Context mContext,MusicInfo musicInfo,AlertDialog mDialog){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.search_more, null);
        builder.setTitle("");
        builder.setView(view);
        TextView textMusicInfo = (TextView) view.findViewById(R.id.musicinfo);
        TextView textPlay = (TextView) view.findViewById(R.id.play);
        TextView textDown = (TextView) view.findViewById(R.id.down);
        mDialog = builder.create();
        mDialog.show();

        DialogClick dialogClick = new DialogClick(musicInfo,mDialog,mContext);
        textMusicInfo.setOnClickListener(dialogClick);
        textPlay.setOnClickListener(dialogClick);
        textDown.setOnClickListener(dialogClick);



    }

    /***
     * 对话框点击监听
     */
    static class DialogClick implements View.OnClickListener {
       MusicInfo mMusicInfo;
        AlertDialog mDialog;
        Context mContext;

        public DialogClick(MusicInfo musicInfo, AlertDialog dialog, Context context) {
            mMusicInfo=musicInfo;
            mDialog = dialog;
            mContext = context;


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.musicinfo:
                    Log.i("============>", "歌曲信息");
                    Intent intent = new Intent(mContext, MusicInfoActivity.class);
                    intent.putExtra(PlayService.MUSICINFO, mMusicInfo);
                    mContext.startActivity(intent);
                    break;
                case R.id.play:
//                    Intent intent1 = new Intent(mContext, PlayService.class);
//                    intent1.putExtra(PlayService.MSG, PlayService.PLAY_MSG);
//                    intent1.putExtra(PlayService.POSITION, position);
//                    intent1.putParcelableArrayListExtra(PlayService.MUSICLIST, list);
//                    mContext.startService(intent1);
                    break;
                case R.id.down:
                        if(NetWork.isNetworkAvailable(mContext)){
                            Toast.makeText(mContext, "开始下载", Toast.LENGTH_SHORT).show();
                            DownloadUtil.getInstance().addDownloadUrl(DiskCacheDir.getStringDiskMusicCacheDir(mContext).
                                    getPath(), mMusicInfo.getUrl(), mMusicInfo.getTitle() + "-" +
                                    mMusicInfo.getArtist() + ".mp3", mMusicInfo.getArtist(), 0);
                        }else {
                            Toast.makeText(mContext, "系统设置仅在WiFi状态下下载，请前往处设置", Toast.LENGTH_SHORT).show();
                        }
                    break;
            }
            mDialog.dismiss();
        }
    }
}
