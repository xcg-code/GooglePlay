package com.app.googleplay.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/14.
 */

public class MoreHolder extends BaseHolder<Integer> {
    /**
     * 加载更多几种状态：
     * 加载更多，加载失败，没有更多数据
     *
     */
    public static final int STATE_LOAD_MORE=1;
    public static final int STATE_LOAD_ERROR=2;
    public static final int STATE_LOAD_NONE=3;
    private TextView textView;
    private LinearLayout ll_load_more;

    public MoreHolder(boolean hasMore) {
        if(hasMore){
            setData(STATE_LOAD_MORE);
        }else{
            setData(STATE_LOAD_NONE);
        }
    }

    @Override
    public View initView() {
        View view=UIUtils.inflate(R.layout.list_item_more);
        textView = (TextView) view.findViewById(R.id.text);
        ll_load_more = (LinearLayout) view.findViewById(R.id.load_more);
        return view;
    }

    @Override
    public void refreshData(Integer data) {
        switch (data) {
            case STATE_LOAD_MORE:
                // 显示加载更多
                ll_load_more.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                break;
            case STATE_LOAD_NONE:
                // 隐藏加载更多
                ll_load_more.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                break;
            case STATE_LOAD_ERROR:
                // 显示加载失败的布局
                ll_load_more.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }
}
