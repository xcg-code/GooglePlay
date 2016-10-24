package com.app.googleplay.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.domain.CategoryInfo;
import com.app.googleplay.utils.UIUtils;

/**
 * 分类模块标题
 * Created by 14501_000 on 2016/10/23.
 */

public class TitleHolder extends BaseHolder<CategoryInfo> {
    public TextView tvTitle;
    @Override
    public View initView() {
        View view = UIUtils.inflate(R.layout.list_item_title);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void refreshData(CategoryInfo data) {
        tvTitle.setText(data.title);
    }
}
