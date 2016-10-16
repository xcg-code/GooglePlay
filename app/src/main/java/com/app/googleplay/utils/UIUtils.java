package com.app.googleplay.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import com.app.googleplay.GooglePlayApplication;

/**
 * 常用方法工具类
 * Created by 14501_000 on 2016/10/10.
 */

public class UIUtils {
    public static Context getContext(){
        return GooglePlayApplication.getContext();
    }
    public static Handler getHandler(){
        return GooglePlayApplication.getHandler();
    }
    public static int getMainThreadId(){
        return GooglePlayApplication.getMainThreadId();
    }

    /*--------------------------------加载资源文件----------------------------*/
    //获取字符串
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }

    //获取字符串数组
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }

    //获取图片
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }

    //获取颜色
    public static int getColor(int id){
        return getContext().getResources().getColor(id);
    }

    //获取尺寸，返回像素值
    public static int getDimen(int id){
        return  getContext().getResources().getDimensionPixelSize(id);
    }

    //dip转px
    public static int dip2px(float dip){
        float density=getContext().getResources().getDisplayMetrics().density;
        return (int) (dip*density+0.5f);
    }

    //px转dip
    public static float px2dip(int px){
        float density=getContext().getResources().getDisplayMetrics().density;
        return px/density;
    }

    //加载布局文件
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }

    //判断是否运行在主线程
    public static boolean isRunOnUIThread(){
        int currentThreadId=android.os.Process.myTid();
        return currentThreadId==getMainThreadId();
    }

    //使对象运行在主线程
    public static void runOnUIThread(Runnable r){
        if(isRunOnUIThread()){
            r.run();//已经在主线程直接运行
        }else{
            //如果是主线程，借助Handler运行在主线程
            getHandler().post(r);
        }
    }

    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id) {
        return getContext().getResources().getColorStateList(id);
    }
}
