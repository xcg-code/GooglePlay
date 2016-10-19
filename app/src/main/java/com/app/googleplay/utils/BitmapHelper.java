package com.app.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;

/**
 * BitmapUtils单例模式
 * Created by 14501_000 on 2016/10/18.
 */

public class BitmapHelper {
    private static BitmapUtils bitmapUtils=null;

    public static BitmapUtils getBitmapUtils(){
        if(bitmapUtils==null){
            synchronized (BitmapHelper.class){
                if(bitmapUtils==null){
                    bitmapUtils=new BitmapUtils(UIUtils.getContext());
                }
            }
        }
        return bitmapUtils;
    }
}
