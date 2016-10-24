package com.app.googleplay.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by 14501_000 on 2016/10/23.
 */

public class DrawableUtils {
    public static GradientDrawable getGradientDrawable(int color, int radius){
        GradientDrawable shape=new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);//圆角半径
        shape.setColor(color);//颜色
        return shape;
    }

    public static StateListDrawable selector(Drawable normal, Drawable press){
        StateListDrawable selector=new StateListDrawable();
        selector.addState(new int[]{android.R.attr.state_pressed},press);//按下背景
        selector.addState(new int[]{},normal);//默认背景
        return selector;
    }

    public static StateListDrawable getSelector(int normal, int press, int radius){
        GradientDrawable bgNormal=getGradientDrawable(normal,radius);
        GradientDrawable bgPress=getGradientDrawable(press,radius);
        StateListDrawable selector=selector(bgNormal,bgPress);
        return selector;
    }
}
