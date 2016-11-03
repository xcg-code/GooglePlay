package com.app.googleplay.ui.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.googleplay.R;
import com.app.googleplay.http.HttpHelper;
import com.app.googleplay.utils.BitmapHelper;
import com.app.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/24.
 */

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {
    ArrayList<String> data;
    private ViewPager viewPager;
    private LinearLayout llContainer;
    private int mPreviousPos;// 上个圆点位置

    @Override
    public View initView() {
        // 创建根布局, 相对布局
        RelativeLayout rlRoot=new RelativeLayout(UIUtils.getContext());
        // 初始化布局参数, 根布局上层控件是listview, 所以要使用listview定义的LayoutParams
        AbsListView.LayoutParams rlParams=new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,UIUtils.dip2px(150));
        rlRoot.setLayoutParams(rlParams);

        //ViewPager
        viewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams vpParams=new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rlRoot.addView(viewPager,vpParams);

        //初始化指示器
        llContainer = new LinearLayout(UIUtils.getContext());
        llContainer.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams llParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        int padding=UIUtils.dip2px(10);
        llContainer.setPadding(padding,padding,padding,padding);
        // 添加规则, 设定展示位置
        llParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// 底部对齐
        llParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// 右对齐
        // 添加布局
        rlRoot.addView(llContainer,llParams);

        return rlRoot;
    }

    @Override
    public void refreshData(final ArrayList<String> data) {
        this.data=data;
        viewPager.setAdapter(new HomeHeaderAdapter());
        for(int i=0;i<data.size();i++){
            ImageView point=new ImageView(UIUtils.getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i==0){
                    point.setImageResource(R.drawable.indicator_selected);
            }else{
                point.setImageResource(R.drawable.indicator_normal);
                params.leftMargin = UIUtils.dip2px(4);// 左边距
            }
            point.setLayoutParams(params);
            llContainer.addView(point);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView point= (ImageView) llContainer.getChildAt(position);
                point.setImageResource(R.drawable.indicator_selected);
                // 上个点变为不选中
                ImageView prePoint = (ImageView) llContainer.getChildAt(mPreviousPos);
                prePoint.setImageResource(R.drawable.indicator_normal);
                mPreviousPos=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //轮播条自动播放
        HomeHeaderTask task=new HomeHeaderTask();
        task.start();

    }
    class HomeHeaderTask implements Runnable{

        public void start() {
            // 移除之前发送的所有消息, 避免消息重复
            UIUtils.getHandler().removeCallbacksAndMessages(null);
            UIUtils.getHandler().postDelayed(this, 3000);
        }

        @Override
        public void run() {
            int currentItem=viewPager.getCurrentItem();
            currentItem++;
            if(currentItem==data.size())currentItem=0;
            viewPager.setCurrentItem(currentItem);
            UIUtils.getHandler().postDelayed(this,3000);
        }
    }

    private class HomeHeaderAdapter extends PagerAdapter {
        private BitmapUtils mBitmapUtils;
        public HomeHeaderAdapter(){
            mBitmapUtils=  BitmapHelper.getBitmapUtils();
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url=data.get(position);
            ImageView view=new ImageView(UIUtils.getContext());
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            mBitmapUtils.display(view, HttpHelper.URL+url);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View) object);
        }
    }
}
