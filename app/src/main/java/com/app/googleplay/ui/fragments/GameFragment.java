package com.app.googleplay.ui.fragments;

import android.view.View;

import com.app.googleplay.ui.view.LoadingPage;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class GameFragment extends BaseFragment {
    @Override
    public View onCreateSuccessView() {
        return null;
    }
    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_EMPTY;
    }
}
