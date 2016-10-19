package com.app.googleplay.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.domain.SubjectInfo;
import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.utils.BitmapHelper;
import com.app.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by 14501_000 on 2016/10/18.
 */

public class SubjectHolder extends BaseHolder<SubjectInfo> {
    private View view;
    private TextView tv_des;
    private ImageView iv_icon;
    private BitmapUtils mBitmapUtils;
    @Override
    public View initView() {
        view = View.inflate(UIUtils.getContext(), R.layout.subject_list_item,null);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_des = (TextView) view.findViewById(R.id.tv_des);

        mBitmapUtils= BitmapHelper.getBitmapUtils();//从单例获取对象
        return view;
    }

    @Override
    public void refreshData(SubjectInfo data) {
        mBitmapUtils.display(iv_icon, HttpHelper.URL+data.url);
        tv_des.setText(data.des);
    }
}
