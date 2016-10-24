package com.app.googleplay.ui.fragments;

import android.view.View;

import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.http.protocol.AppProtocol;
import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.AppHolder;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.MyListView;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class AppFragment extends BaseFragment {
    ArrayList<AppInfo> data;
    private int index=0;
    @Override
    public View onCreateSuccessView() {
        MyListView listView=new MyListView(UIUtils.getContext());
        AppAdapter adapter=new AppAdapter(data);
        listView.setAdapter(adapter);
        return listView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        AppProtocol appProtocol=new AppProtocol();
        data=appProtocol.getData(0);
        return check(data);//检查数据
    }

    class AppAdapter extends MyBaseAdapter<AppInfo>{

        public AppAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new AppHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            AppProtocol appProtocol=new AppProtocol();
            ArrayList<AppInfo> moreData;
            index=index+20;
            if(index<=40){
                moreData= appProtocol.getData(index);
            }else{
                moreData= appProtocol.getData(0);
            }

            return moreData;
        }
    }
}
