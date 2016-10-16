package com.app.googleplay.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/13.
 */

public class HomeHolder extends BaseHolder<String> {

    private View view;
    private TextView textView;

    @Override
    public View initView() {
        view = View.inflate(UIUtils.getContext(), R.layout.home_list_item,null);
        textView = (TextView) view.findViewById(R.id.text);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        return view;
    }

    @Override
    public void refreshData(String data) {
        textView.setText(data);
    }
}
