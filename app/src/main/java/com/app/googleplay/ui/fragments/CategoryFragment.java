package com.app.googleplay.ui.fragments;

import android.view.View;

import com.app.googleplay.domain.CategoryInfo;
import com.app.googleplay.http.protocol.CategoryProtocol;
import com.app.googleplay.ui.adapter.MyBaseAdapter;
import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.CategoryHolder;
import com.app.googleplay.ui.holder.TitleHolder;
import com.app.googleplay.ui.view.LoadingPage;
import com.app.googleplay.ui.view.MyListView;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/10.
 */
public class CategoryFragment extends BaseFragment {
    ArrayList<CategoryInfo> data;

    @Override
    public View onCreateSuccessView() {
        MyListView myListView = new MyListView(UIUtils.getContext());
        myListView.setAdapter(new CategoryAdapter(data));
        return myListView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        CategoryProtocol categoryProtocol = new CategoryProtocol();
        data = categoryProtocol.getData(0);
        return check(data);
    }

    class CategoryAdapter extends MyBaseAdapter<CategoryInfo> {

        public CategoryAdapter(ArrayList<CategoryInfo> data) {
            super(data);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;//在原来类型数量上+1
        }

        @Override
        public int getInnerType(int position) {
            // 判断是标题类型还是普通分类类型
            CategoryInfo info = data.get(position);

            if (info.isTitle) {
                // 返回标题类型
                return super.getInnerType(position)+1;// 原来类型基础上加1;
            } else {
                // 返回普通类型
                return super.getInnerType(position);
            }
        }

        @Override
        public BaseHolder<CategoryInfo> getHolder(int position) {
            // 判断是标题类型还是普通分类类型, 来返回不同的holder
            CategoryInfo info = data.get(position);

            if (info.isTitle) {
                return new TitleHolder();
            } else {
                return new CategoryHolder();
            }
        }

        @Override
        public boolean hasMore() {
            return false;
        }

        @Override
        public ArrayList<CategoryInfo> onLoadMore() {
            return null;
        }
    }
}
