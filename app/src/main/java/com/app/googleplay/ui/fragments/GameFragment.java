package com.app.googleplay.ui.fragments;

import android.view.View;

import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.http.protocol.GameProtocol;
import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.GameHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.MyListView;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class GameFragment extends BaseFragment {
    ArrayList<AppInfo> data;
    private int index=0;
    @Override
    public View onCreateSuccessView() {
        MyListView listView=new MyListView(UIUtils.getContext());
        GameAdapter adapter=new GameAdapter(data);
        listView.setAdapter(adapter);
        return listView;
    }
    @Override
    public LoadingPage.ResultState onLoad() {
        GameProtocol gameProtocol=new GameProtocol();
        data=gameProtocol.getData(0);
        return check(data);//检查数据
    }
    class GameAdapter extends MyBaseAdapter<AppInfo>{

        public GameAdapter(ArrayList<AppInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder(int position) {
            return new GameHolder();
        }

        @Override
        public ArrayList<AppInfo> onLoadMore() {
            GameProtocol gameProtocol=new GameProtocol();
            ArrayList<AppInfo> moreData;
            index=index+20;
            if(index<=40){
                moreData= gameProtocol.getData(index);
            }else{
                moreData= gameProtocol.getData(0);
            }

            return moreData;
        }
    }
}
