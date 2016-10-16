package com.app.googleplay.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.app.googleplay.R;
import com.app.googleplay.ui.fragments.BaseFragment;
import com.app.googleplay.ui.fragments.FragmentFactory;
import com.app.googleplay.ui.view.PagerTab;
import com.app.googleplay.utils.UIUtils;

public class MainActivity extends BaseActivity {

    private PagerTab pagerTab;
    private ViewPager viewPager;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pagerTab = (PagerTab) findViewById(R.id.pager_tab);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerTab.setViewPager(viewPager);//将PageTab与ViewPager绑定

        pagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment fragment=FragmentFactory.creatFragment(position);
                fragment.loadData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyAdapter extends FragmentPagerAdapter {
        private String[] myTabNames;
        public MyAdapter(FragmentManager fm) {
            super(fm);
            //加载页面标签数组
            myTabNames = UIUtils.getStringArray(R.array.tab_names);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return myTabNames[position];
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = FragmentFactory.creatFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return myTabNames.length;
        }
    }
}
