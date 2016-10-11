package com.app.googleplay.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 14501_000 on 2016/10/10.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCreateSuccessView();
    }

    // 加载成功的布局, 必须由子类来实现
    public abstract View onCreateSuccessView();

    // 加载网络数据, 必须由子类来实现
    //public abstract ResultState onLoad();
}
