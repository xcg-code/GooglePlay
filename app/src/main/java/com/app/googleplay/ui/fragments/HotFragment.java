package com.app.googleplay.ui.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.googleplay.http.protocol.HotProtocol;
import com.app.googleplay.ui.view.FlowLayout;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.utils.DrawableUtils;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class HotFragment extends BaseFragment {
    ArrayList<String> data;
    private GradientDrawable bgNormal;
    private GradientDrawable bgPress;

    @Override
    public View onCreateSuccessView() {
        ScrollView scrollView=new ScrollView(UIUtils.getContext());
        FlowLayout flowLayout=new FlowLayout(UIUtils.getContext());
        //MyFlowLayout flowLayout=new MyFlowLayout(UIUtils.getContext());
        int padding=UIUtils.dip2px(10);
        flowLayout.setPadding(padding,padding,padding,padding);
        flowLayout.setHorizontalSpacing(UIUtils.dip2px(6));// 水平间距
        flowLayout.setVerticalSpacing(UIUtils.dip2px(8));// 竖直间距

        for(int i=0;i<data.size();i++){
            final String keyword=data.get(i);
            TextView textView=new TextView(UIUtils.getContext());
            textView.setText(keyword);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);// 18sp
            textView.setPadding(padding, padding, padding, padding);
            textView.setGravity(Gravity.CENTER);

            Random random=new Random();
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            int color = 0xffcecece;// 按下后偏白的背景色
//          bgNormal = getGradientDrawable(Color.rgb(r,g,b), UIUtils.dip2px(10));
//          bgPress = DrawableUtils.getGradientDrawable(color, UIUtils.dip2px(10));
            StateListDrawable selector=DrawableUtils.getSelector(Color.rgb(r,g,b),color,UIUtils.dip2px(10));
            textView.setBackgroundDrawable(selector);//状态选择器
            //只有设计了点击事件，状态选择器在起作用
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), keyword,
                            Toast.LENGTH_SHORT).show();
                }
            });
            flowLayout.addView(textView);
        }

        scrollView.addView(flowLayout);
        return scrollView;
    }
    @Override
    public LoadingPage.ResultState onLoad() {
        HotProtocol hotProtocol=new HotProtocol();
        data=hotProtocol.getData(0);
        return check(data);
    }
}
