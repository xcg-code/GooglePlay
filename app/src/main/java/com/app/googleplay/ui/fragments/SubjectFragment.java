package com.app.googleplay.ui.fragments;

import android.view.View;

import com.app.googleplay.domain.SubjectInfo;
import com.app.googleplay.http.protocol.SubjectProtocol;
import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.SubjectHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.MyListView;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class SubjectFragment extends BaseFragment {
    private ArrayList<SubjectInfo> data;
    private int index=0;
    @Override
    public View onCreateSuccessView() {
        MyListView listView=new MyListView(UIUtils.getContext());
        listView.setAdapter(new SubjectAdapter(data));
        return listView;
    }
    @Override
    public LoadingPage.ResultState onLoad() {
        SubjectProtocol subjectProtocol=new SubjectProtocol();
        data=subjectProtocol.getData(0);
        return check(data);//检查数据
    }

    class SubjectAdapter extends MyBaseAdapter<SubjectInfo>{

        public SubjectAdapter(ArrayList<SubjectInfo> data) {
            super(data);
        }

        @Override
        public BaseHolder getHolder() {
            return new SubjectHolder();
        }

        @Override
        public ArrayList<SubjectInfo> onLoadMore() {
            SubjectProtocol subjectProtocol=new SubjectProtocol();
            ArrayList<SubjectInfo> moreData;
            index=index+20;
            if(index<=60){
                moreData= subjectProtocol.getData(index);
            }else{
                moreData= subjectProtocol.getData(0);
            }

            return moreData;
        }
    }
}
