package com.app.googleplay.ui.fragments;


import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.googleplay.http.protocol.RecommendProtocol;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.fly.ShakeListener;
import com.app.googleplay.ui.view.fly.StellarMap;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class RecommendFragment extends BaseFragment {
    ArrayList<String> data;
    @Override
    public View onCreateSuccessView() {
        FrameLayout frameLayout=new FrameLayout(UIUtils.getContext());
        frameLayout.setBackgroundColor(Color.WHITE);
        final StellarMap stellarMap=new StellarMap(UIUtils.getContext());
        stellarMap.setAdapter(new RecommendAdapter());

        // 随机方式, 将控件划分为9行5列的的格子, 然后在格子中随机展示
        stellarMap.setRegularity(5, 9);
        // 设置内边距10dp
        int padding = UIUtils.dip2px(10);
        stellarMap.setInnerPadding(padding, padding, padding, padding);
        // 设置默认页面, 第一组数据
        stellarMap.setGroup(0, true);
        frameLayout.addView(stellarMap);

        //摇动手机跳转下一页数据
        ShakeListener shake=new ShakeListener(UIUtils.getContext());
        shake.setOnShakeListener(new ShakeListener.OnShakeListener() {
            @Override
            public void onShake() {
                stellarMap.zoomIn();
            }
        });
        return frameLayout;
    }
    @Override
    public LoadingPage.ResultState onLoad() {
        RecommendProtocol recommendProtocol=new RecommendProtocol();
        data=recommendProtocol.getData(0);
        return check(data);
    }

    class RecommendAdapter implements StellarMap.Adapter {

        //数据组的数量
        @Override
        public int getGroupCount() {
            return 2;
        }
        //每组数据个数
        @Override
        public int getCount(int group) {
            int count = data.size() / getGroupCount();
            if (group == getGroupCount() - 1) {
                // 最后一页, 将除不尽,余下来的数量追加在最后一页, 保证数据完整不丢失
                count += data.size() % getGroupCount();
            }
            return count;
        }

        //初始化布局
        @Override
        public View getView(int group, int position, View convertView) {
            // 因为position每组都会从0开始计数,
            // 所以需要将前面几组数据的个数加起来,才能确定当前组获取数据的下标位置
            position+=getCount(group-1);
            TextView textView=new TextView(UIUtils.getContext());
            final String keyword=data.get(position);
            textView.setText(keyword);
            Random random=new Random();
            //字体大小15-30
            int size=15+random.nextInt(16);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
            // 随机颜色
            // r g b, 0-255 -> 30-230, 颜色值不能太小或太大, 从而避免整体颜色过亮或者过暗
            int r = 30 + random.nextInt(200);
            int g = 30 + random.nextInt(200);
            int b = 30 + random.nextInt(200);
            textView.setTextColor(Color.rgb(r,g,b));

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(UIUtils.getContext(), keyword,
                            Toast.LENGTH_SHORT).show();
                }
            });

            return textView;
        }

        //返回下一组的id
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if(isZoomIn){
                //向下滑加载上一页
                if(group>0){
                   group--;
                }else{
                    group=getGroupCount()-1;
                }
            }else{
                //向上滑加载下一页
                if(group<(getGroupCount()-1)){
                    group++;
                }else{
                    group=0;
                }
            }
            return group;
        }
    }
}
