package com.app.googleplay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.domain.DownloadInfo;
import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.manager.DownloadManager;
import com.app.googleplay.ui.view.ProgressArc;
import com.app.googleplay.utils.BitmapHelper;
import com.app.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by 14501_000 on 2016/10/13.
 */

public class HomeHolder extends BaseHolder<AppInfo> implements View.OnClickListener,DownloadManager.DownloadObserve{

    private View view;
    private TextView tv_des;
    private ImageView iv_icon;
    private TextView tv_name;
    private RatingBar rb_star;
    private TextView tv_size;

    private BitmapUtils mBitmapUtils;
    private ProgressArc progressArc;//I定义的进度条
    private DownloadManager mDM;
    private int mCurrentState;
    private float mProgress;
    private TextView tvDownload;

    @Override
    public View initView() {
        view = View.inflate(UIUtils.getContext(), R.layout.home_list_item,null);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        rb_star = (RatingBar) view.findViewById(R.id.rb_star);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        tvDownload = (TextView) view.findViewById(R.id.tv_download);
        mBitmapUtils= BitmapHelper.getBitmapUtils();//从单例获取对象

        //初始化进度条
        FrameLayout flProgress= (FrameLayout) view.findViewById(R.id.fl_download);
        flProgress.setOnClickListener(this);
        progressArc = new ProgressArc(UIUtils.getContext());
        //设置圆形进度条直径
        progressArc.setArcDiameter(UIUtils.dip2px(26));
        //设置进度条颜色
        progressArc.setProgressColor(UIUtils.getColor(R.color.progress));
        // 设置进度条宽高布局参数
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(
                UIUtils.dip2px(27), UIUtils.dip2px(27));
        flProgress.addView(progressArc,params);

        mDM = DownloadManager.getInstance();
        mDM.registerObserve(this);// 注册观察者, 监听状态和进度变化
        return view;
    }

    @Override
    public void refreshData(AppInfo data) {
        tv_name.setText(data.name);
        rb_star.setRating(data.stars);
        tv_size.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));
        tv_des.setText(data.des);
        mBitmapUtils.display(iv_icon, HttpHelper.URL+data.iconUrl);

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
    /**
     * 刷新界面
     *
     * @param progress
     * @param state
     */
    private void refreshUI(int state, float progress, String id) {
        // 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
        if (!getData().id.equals(id)) {
            return;
        }

        mCurrentState = state;
        mProgress = progress;
        switch (state) {
            case DownloadManager.STATE_UNDO:
                // 自定义进度条背景
                progressArc.setBackgroundResource(R.drawable.ic_download);
                // 没有进度
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载");
                break;
            case DownloadManager.STATE_WAITING:
                progressArc.setBackgroundResource(R.drawable.ic_download);
                // 等待模式
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                tvDownload.setText("等待");
                break;
            case DownloadManager.STATE_DOWNLOADING:
                progressArc.setBackgroundResource(R.drawable.ic_pause);
                // 下载中模式
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                progressArc.setProgress(progress, true);
                tvDownload.setText((int) (progress * 100) + "%");
                break;
            case DownloadManager.STATE_PAUSE:
                progressArc.setBackgroundResource(R.drawable.ic_resume);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                break;
            case DownloadManager.STATE_ERROR:
                progressArc.setBackgroundResource(R.drawable.ic_redownload);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("下载失败");
                break;
            case DownloadManager.STATE_SUCCESS:
                progressArc.setBackgroundResource(R.drawable.ic_install);
                progressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                tvDownload.setText("安装");
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_download:
                // 根据当前状态来决定下一步操作
                if (mCurrentState == DownloadManager.STATE_UNDO
                        || mCurrentState == DownloadManager.STATE_ERROR
                        || mCurrentState == DownloadManager.STATE_PAUSE) {
                    mDM.download(getData());// 开始下载
                } else if (mCurrentState == DownloadManager.STATE_DOWNLOADING
                        || mCurrentState == DownloadManager.STATE_WAITING) {
                    mDM.pause(getData());// 暂停下载
                } else if (mCurrentState == DownloadManager.STATE_SUCCESS) {
                    mDM.install(getData());// 开始安装
                }
                break;
            default:
                break;
        }

    }

    // 主线程更新ui 3-4
    private void refreshUIOnMainThread(final DownloadInfo info) {
        // 判断下载对象是否是当前应用
        AppInfo appInfo = getData();
        if (appInfo.id.equals(info.id)) {
            UIUtils.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                    refreshUI(info.currentState, info.getProcess(), info.id);
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
}
