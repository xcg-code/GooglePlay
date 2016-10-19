package com.app.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.app.googleplay.R;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/11.
 */

public abstract class LoadingPage extends FrameLayout {
    private static final int STATE_LOAD_UNDO = 1;// 未加载
    private static final int STATE_LOAD_LOADING = 2;// 正在加载
    private static final int STATE_LOAD_ERROR = 3;// 加载失败
    private static final int STATE_LOAD_EMPTY = 4;// 数据为空
    private static final int STATE_LOAD_SUCCESS = 5;// 加载成功

    private int mCurrentState = STATE_LOAD_UNDO;// 当前状态

    private View mLoadingPage;
    private View mErrorPage;
    private View mEmptyPage;
    private View mSuccessPage;

    public LoadingPage(Context context) {
        super(context);
        initView();
    }
    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if(mLoadingPage==null){
            mLoadingPage = UIUtils.inflate(R.layout.page_loading);
            addView(mLoadingPage);
        }
        if(mErrorPage==null){
            mErrorPage = UIUtils.inflate(R.layout.page_error);
            Button bt_error= (Button) mErrorPage.findViewById(R.id.bt_error);
            bt_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingData();
                }
            });
            addView(mErrorPage);
        }
        if(mEmptyPage==null){
            mEmptyPage = UIUtils.inflate(R.layout.page_empty);
            addView(mEmptyPage);
        }
        //根据当前状态选择正确布局
        showRightPage();

    }

    private void showRightPage() {
        if(mCurrentState==STATE_LOAD_UNDO||mCurrentState==STATE_LOAD_LOADING){
            mLoadingPage.setVisibility(View.VISIBLE);
        }else{
            mLoadingPage.setVisibility(View.GONE);
        }
        if(mCurrentState==STATE_LOAD_ERROR){
            mErrorPage.setVisibility(View.VISIBLE);
        }else{
            mErrorPage.setVisibility(View.GONE);
        }

        if(mCurrentState==STATE_LOAD_EMPTY){
            mEmptyPage.setVisibility(View.VISIBLE);
        }else{
            mEmptyPage.setVisibility(View.GONE);
        }
        if(mSuccessPage==null&&mCurrentState==STATE_LOAD_SUCCESS){
            mSuccessPage = onCreateSuccessView();
            if(mSuccessPage!=null){
                addView(mSuccessPage);
            }
        }
        if (mSuccessPage != null) {
            if(mCurrentState == STATE_LOAD_SUCCESS){
                mSuccessPage.setVisibility(View.VISIBLE);
            }else{
                mSuccessPage.setVisibility(View.GONE);
            }

        }
    }

    //加载数据
    public void loadingData(){
        if(mCurrentState!=STATE_LOAD_LOADING){
            mCurrentState=STATE_LOAD_LOADING;
            new Thread(){
                @Override
                public void run() {
                    final ResultState resultState=onLoad();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(resultState!=null){
                                mCurrentState=resultState.getState();
                                System.out.println("状态"+mCurrentState);
                                //根据最新状态刷新页面
                                showRightPage();
                            }
                        }
                    });

                }
            }.start();
        }

    }

    // 加载成功的布局, 由调用者来实现
    public abstract View onCreateSuccessView();

    //加载网络数据, 返回值表示请求网络结束后的状态
    public abstract ResultState onLoad();

    public enum ResultState{
        STATE_SUCCESS(STATE_LOAD_SUCCESS),
        STATE_EMPTY(STATE_LOAD_EMPTY),
        STATE_ERROR(STATE_LOAD_ERROR);
        private int state;
        private ResultState(int state){
            this.state=state;
        }
        public int getState(){
            return state;
        }
    }
}
