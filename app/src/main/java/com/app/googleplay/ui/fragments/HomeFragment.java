package com.app.googleplay.ui.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.http.protocol.HomeProtocol;
import com.app.googleplay.ui.activities.HomeDetailActivity;
import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.HomeHeaderHolder;
import com.app.googleplay.ui.holder.HomeHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.MyListView;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class HomeFragment extends BaseFragment {
    ArrayList<AppInfo> data;
    private int index = 0;
    private ArrayList<String> picUrlData;
    private HomeAdapter adapter;

    //运行在主线程
    @Override
    public View onCreateSuccessView() {
        MyListView listView = new MyListView(UIUtils.getContext());

        HomeHeaderHolder homeHeaderHolder = new HomeHeaderHolder();
        listView.addHeaderView(homeHeaderHolder.initView());
        homeHeaderHolder.setData(picUrlData);
        adapter = new HomeAdapter(data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo info = data.get(position - 1);//去掉头布局
                String packageName = info.packageName;
                if (info != null) {
                    Intent intent = new Intent(UIUtils.getContext(), HomeDetailActivity.class);
                    intent.putExtra("packageName", packageName);
                    startActivity(intent);
                }
            }
        });
        return listView;
    }

    //请求网络数据，运行在子线程，可直接进行耗时操作
    @Override
    public LoadingPage.ResultState onLoad() {
        HomeProtocol homeProtocol = new HomeProtocol();
        data = homeProtocol.getData(0);// 加载第一页数据
        picUrlData = homeProtocol.getPicUrlData();
        return check(data);//检查数据
    }


    class HomeAdapter<AppInfo> extends MyBaseAdapter {

        public HomeAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new HomeHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            HomeProtocol homeProtocol = new HomeProtocol();
            ArrayList<AppInfo> moreData;
            index = index + 20;
            if (index <= 60) {
                moreData = (ArrayList<AppInfo>) homeProtocol.getData(index);
            } else {
                moreData = (ArrayList<AppInfo>) homeProtocol.getData(0);
            }
            return moreData;

        }


    }
}
