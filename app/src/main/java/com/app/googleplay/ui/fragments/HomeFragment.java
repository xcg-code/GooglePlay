package com.app.googleplay.ui.fragments;

import android.os.SystemClock;
import android.view.View;
import android.widget.ListView;

import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.HomeHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class HomeFragment extends BaseFragment {
    ArrayList data;
    //运行在主线程
    @Override
    public View onCreateSuccessView() {
        ListView listView=new ListView(UIUtils.getContext());
        listView.setAdapter(new HomeAdapter(data));
        return listView;
    }

    //运行在子线程，可直接进行耗时操作
    @Override
    public LoadingPage.ResultState onLoad() {
        data=new ArrayList<String>();
        for(int i=1;i<=20;i++){
            data.add("测试数据"+i);
        }
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    class HomeAdapter<String> extends MyBaseAdapter{

        public HomeAdapter(ArrayList<String> data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder() {
            return new HomeHolder();
        }

        @Override
        public ArrayList onLoadMore() {
            ArrayList<String> arrayList=new ArrayList<String>();
            for (int i=0;i<20;i++){
                arrayList.add((String) ("新增测试数据"+i));
            }
            SystemClock.sleep(2000);
            return arrayList;
        }


    }
}
