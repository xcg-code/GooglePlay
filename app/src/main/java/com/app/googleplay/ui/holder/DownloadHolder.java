package com.app.googleplay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.googleplay.R;
import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.domain.DownloadInfo;
import com.app.googleplay.manager.DownloadManager;
import com.app.googleplay.ui.view.ProgressHorizontal;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/11/4.
 */

public class DownloadHolder extends BaseHolder<AppInfo> implements DownloadManager.DownloadObserve,View.OnClickListener{

    private Button bt_load;
    private DownloadManager mDM;
    private FrameLayout fl_progress;
    private ProgressHorizontal pbProgress;
    private int mCurrentState;
    private float mProgress;

    @Override
    public View initView() {
        View view= UIUtils.inflate(R.layout.layout_detail_load);
        bt_load = (Button) view.findViewById(R.id.bt_load);
        bt_load.setOnClickListener(this);
        //初始化自定义进度条
        fl_progress = (FrameLayout) view.findViewById(R.id.fl_progress);
        pbProgress = new ProgressHorizontal(UIUtils.getContext());
        pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);//设置背景
        pbProgress.setProgressResource(R.drawable.progress_normal);//设置进度条图片
        pbProgress.setProgressTextColor(Color.WHITE);
        pbProgress.setProgressTextSize(UIUtils.dip2px(15));
        FrameLayout.LayoutParams params= new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        fl_progress.addView(pbProgress,params);
        fl_progress.setOnClickListener(this);

        mDM = DownloadManager.getInstance();
        mDM.registerObserve(this);//注册观察者，监听状态及进度的改变
        return view;
    }

    @Override
    public void refreshData(AppInfo data) {
        //判断是否当前应用是否已下载过
        DownloadInfo downloadInfo=mDM.getDownloadInfo(data);
        if(downloadInfo!=null){
            //之前下载过
            mCurrentState = downloadInfo.currentState;
            mProgress = downloadInfo.getProcess();
        }else{
            //之前未下载过
            mCurrentState=DownloadManager.STATE_UNDO;
            mProgress=0;
        }
        refreshUI(mCurrentState,mProgress,data.id);
    }

    // 根据当前的下载进度和状态来更新界面
    private void refreshUI(int currentState, float progress,String id) {
        // 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
        if (!getData().id.equals(id)) {
            return;
        }
        mCurrentState=currentState;
        mProgress=progress;
        switch(mCurrentState){
            case DownloadManager.STATE_UNDO://未下载
                fl_progress.setVisibility(View.GONE);
                bt_load.setVisibility(View.VISIBLE);
                bt_load.setText("下载");
                break;
            case DownloadManager.STATE_WAITING://等待下载
                fl_progress.setVisibility(View.GONE);
                bt_load.setVisibility(View.VISIBLE);
                bt_load.setText("等待中...");
                break;
            case DownloadManager.STATE_DOWNLOADING://下载中
                fl_progress.setVisibility(View.VISIBLE);
                bt_load.setVisibility(View.GONE);
                pbProgress.setCenterText("");
                pbProgress.setProgress(mProgress);// 设置下载进度
                break;
            case DownloadManager.STATE_PAUSE://下载暂停
                fl_progress.setVisibility(View.VISIBLE);
                bt_load.setVisibility(View.GONE);
                pbProgress.setCenterText("暂停");
                pbProgress.setProgress(mProgress);
                break;
            case DownloadManager.STATE_SUCCESS://下载成功
                fl_progress.setVisibility(View.GONE);
                bt_load.setVisibility(View.VISIBLE);
                bt_load.setText("安装");
                break;
            case DownloadManager.STATE_ERROR://下载失败
                fl_progress.setVisibility(View.GONE);
                bt_load.setVisibility(View.VISIBLE);
                bt_load.setText("下载失败");
                break;
            default:break;

        }

    }

    //主线程更新UI
    private void refreshUIOnMainThread(final DownloadInfo info) {
        //判断是否是当前应用
        AppInfo appinfo=getData();
        if(appinfo.id.equals(info.id)){
            UIUtils.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(info.currentState,info.getProcess(),info.id);
                }
            });

        }
    }

    @Override
    public void onDownloadStateChange(DownloadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onDownloadProgressChange(DownloadInfo info) {
        refreshUIOnMainThread(info);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_load:
            case R.id.fl_progress:
                //根据当前状态进行下一步
                if(mCurrentState==DownloadManager.STATE_UNDO
                        ||mCurrentState==DownloadManager.STATE_ERROR
                        ||mCurrentState==DownloadManager.STATE_PAUSE){
                    mDM.download(getData());//开始下载
                }else if(mCurrentState==DownloadManager.STATE_DOWNLOADING
                        ||mCurrentState==DownloadManager.STATE_WAITING){
                    mDM.pause(getData());//暂停下载
                }else if(mCurrentState==DownloadManager.STATE_SUCCESS){
                    mDM.install(getData());//开始安装
                }
                break;
            default:break;
        }
    }
}
