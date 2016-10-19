package com.app.googleplay.ui.holder;

import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.utils.BitmapHelper;
import com.app.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by 14501_000 on 2016/10/18.
 */

public class GameHolder extends BaseHolder<AppInfo> {
    private View view;
    private TextView tv_des;
    private ImageView iv_icon;
    private TextView tv_name;
    private RatingBar rb_star;
    private TextView tv_size;

    private BitmapUtils mBitmapUtils;
    @Override
    public View initView() {
        view = View.inflate(UIUtils.getContext(), R.layout.home_list_item,null);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        rb_star = (RatingBar) view.findViewById(R.id.rb_star);
        tv_size = (TextView) view.findViewById(R.id.tv_size);
        tv_des = (TextView) view.findViewById(R.id.tv_des);
        mBitmapUtils= BitmapHelper.getBitmapUtils();//从单例获取对象
        return view;
    }

    @Override
    public void refreshData(AppInfo data) {
        tv_name.setText(data.name);
        rb_star.setRating(data.stars);
        tv_size.setText(Formatter.formatFileSize(UIUtils.getContext(),data.size));
        tv_des.setText(data.des);
        mBitmapUtils.display(iv_icon, HttpHelper.URL+data.iconUrl);
    }
}
