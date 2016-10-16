package com.app.googleplay.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/10.
 */

public abstract class BaseFragment extends Fragment {

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState onLoad() {
                return BaseFragment.this.onLoad();
            }
        };
        return loadingPage;
    }

    // 加载成功的布局, 必须由子类来实现
    public abstract View onCreateSuccessView();

    // 加载网络数据, 必须由子类来实现
    public abstract LoadingPage.ResultState onLoad();

    // 开始加载数据
    public void loadData() {
        if(loadingPage!=null){
            loadingPage.loadingData();
        }
    }
}
