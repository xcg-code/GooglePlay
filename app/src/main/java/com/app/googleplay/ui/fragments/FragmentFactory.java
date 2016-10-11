package com.app.googleplay.ui.fragments;

import java.util.HashMap;

/**
 * Created by 14501_000 on 2016/10/10.
 */

public class FragmentFactory {
    private static HashMap<Integer,BaseFragment> mFragmentsMap=new HashMap<Integer,BaseFragment>();

    public static BaseFragment creatFragment(int pos){
        // 先从集合中取, 如果没有,才创建对象, 提高性能
        BaseFragment fragment=mFragmentsMap.get(pos);
        if(fragment==null){
            switch (pos) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new AppFragment();
                    break;
                case 2:
                    fragment = new GameFragment();
                    break;
                case 3:
                    fragment = new SubjectFragment();
                    break;
                case 4:
                    fragment = new RecommendFragment();
                    break;
                case 5:
                    fragment = new CategoryFragment();
                    break;
                case 6:
                    fragment = new HotFragment();
                    break;

                default:
                    break;
            }
        }
        mFragmentsMap.put(pos,fragment);// 将fragment保存在集合中
        return fragment;
    }
}
