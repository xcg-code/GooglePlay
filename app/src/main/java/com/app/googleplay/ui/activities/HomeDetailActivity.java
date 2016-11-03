package com.app.googleplay.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import com.app.googleplay.R;
import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.http.protocol.HomeDetailProtocol;
import com.app.googleplay.ui.holder.DetailAppInfoHolder;
import com.app.googleplay.ui.holder.DetailDesHolder;
import com.app.googleplay.ui.holder.DetailPicsHolder;
import com.app.googleplay.ui.holder.DetailSafeHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/25.
 */

public class HomeDetailActivity extends BaseActivity {

    private LoadingPage loadingPage;
    private String packageName;
    private AppInfo data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingPage = new LoadingPage(this) {
            @Override
            public View onCreateSuccessView() {
                return HomeDetailActivity.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return HomeDetailActivity.this.onLoad();
            }
        };
        setContentView(loadingPage);
        packageName = getIntent().getStringExtra("packageName");
        //开始加载数据
        loadingPage.loadingData();
    }

    public View onCreateSuccessView() {
        View view = UIUtils.inflate(R.layout.home_detail_page);
        //APP信息
        FrameLayout flDetailAppInfo = (FrameLayout) view.findViewById(R.id.fl_detailAppinfo);
        DetailAppInfoHolder appInfoHolder = new DetailAppInfoHolder();
        flDetailAppInfo.addView(appInfoHolder.initView());
        appInfoHolder.setData(data);
        //安全模块
        FrameLayout flDetailSafeInfo = (FrameLayout) view.findViewById(R.id.fl_detail_safe);
        DetailSafeHolder appSafeInfoHolder = new DetailSafeHolder();
        flDetailSafeInfo.addView(appSafeInfoHolder.initView());
        appSafeInfoHolder.setData(data);
        //图片介绍
        HorizontalScrollView hsv_pic = (HorizontalScrollView) view.findViewById(R.id.hs_scroll);
        DetailPicsHolder picsHolder = new DetailPicsHolder();
        hsv_pic.addView(picsHolder.initView());
        picsHolder.setData(data);
        //应用描述
        FrameLayout rl_des = (FrameLayout) view.findViewById(R.id.fl_des);
        DetailDesHolder desHolder=new DetailDesHolder();
        rl_des.addView(desHolder.initView());
        desHolder.setData(data);
        return view;
    }

    public LoadingPage.ResultState onLoad() {
        //请求网络加载数据
        HomeDetailProtocol protocol = new HomeDetailProtocol(packageName);
        data = protocol.getData(0);
        if (data != null) {
            return LoadingPage.ResultState.STATE_SUCCESS;
        } else {
            return LoadingPage.ResultState.STATE_ERROR;
        }
    }
}
