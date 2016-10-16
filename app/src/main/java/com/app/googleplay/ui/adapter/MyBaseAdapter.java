package com.app.googleplay.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.app.googleplay.ui.holder.BaseHolder;
import com.app.googleplay.ui.holder.MoreHolder;
import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/13.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter{
    private static final  int TYPE_NORMAL=0;
    private static final int TYPE_MORE=1;
    ArrayList<T> data;//定义一个泛型数据集合
    public MyBaseAdapter(ArrayList<T> data){
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size()+1;//加一为加载更多布局
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return 2;//两种布局：普通布局 和 加载更多布局
    }
    @Override
    public int getItemViewType(int position) {
        if(position==(getCount()-1)){//最后一个条目
            return TYPE_MORE;
        }else{
            return getInnerType();
        }
    }
    public int getInnerType(){//便于子类重写拓展类型
        return TYPE_NORMAL;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if(convertView==null){
            if(getItemViewType(position)==TYPE_MORE){
                holder=new MoreHolder(hasMore());
            }else{
                holder=getHolder();//子类返回具体对象
            }

        }else{
            holder= (BaseHolder) convertView.getTag();
        }

        if(getItemViewType(position)==TYPE_NORMAL){
            holder.setData(getItem(position));//传递数据并处理
        }else{
            //加载更多数据
            MoreHolder moreHolder= (MoreHolder) holder;
            // 一旦加载更多布局展示出来, 就开始加载更多
            // 只有在有更多数据的状态下才加载更多
            if(moreHolder.getData()==MoreHolder.STATE_LOAD_MORE){
                loadMore(moreHolder);
            }
        }


        return holder.getRootView();
    }
    public boolean hasMore(){
        return true;
    }
    public abstract BaseHolder getHolder();
    private boolean isLoadMore=false;

    public void loadMore(final MoreHolder holder){
        if(!isLoadMore){
            isLoadMore = true;
            new Thread(){
                @Override
                public void run() {
                    final ArrayList<T> moreData=onLoadMore();
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if(moreData!=null){
                                // 每一页有20条数据, 如果返回的数据小于20条, 就认为到了最后一页了
                                if(moreData.size()<20){
                                    holder.setData(MoreHolder.STATE_LOAD_NONE);
                                    Toast.makeText(UIUtils.getContext(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                                }else{
                                    holder.setData(MoreHolder.STATE_LOAD_MORE);
                                }
                                // 将更多数据追加到当前集合中
                                data.addAll(moreData);
                                //刷新界面
                                MyBaseAdapter.this.notifyDataSetChanged();
                            }else{
                                // 加载更多失败
                                holder.setData(MoreHolder.STATE_LOAD_ERROR);
                            }
                            isLoadMore=false;
                        }
                    });

                }
            }.start();
        }

    }

    // 加载更多数据, 必须由子类实现
    public abstract ArrayList<T> onLoadMore();
}
