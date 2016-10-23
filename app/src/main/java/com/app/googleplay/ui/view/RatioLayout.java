package com.app.googleplay.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.app.googleplay.R;

/**
 * Created by 14501_000 on 2016/10/19.
 */

public class RatioLayout extends FrameLayout {
    private float ratio;
    public RatioLayout(Context context) {
        super(context);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取属性值
        //attrs.getAttributeFloatValue("","ratio",-1);
        // 当自定义属性时, 系统会自动生成属性相关id, 此id通过R.styleable来引用
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.RatioLayout);
        // id = 属性名_具体属性字段名称 (此id系统自动生成)
        ratio=typedArray.getFloat(R.styleable.RatioLayout_ratio,-1);
        typedArray.recycle();//回收typearray, 提高性能
        System.out.println("ratio="+ratio);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);//获取宽度值
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);//获取宽度模式

        int height=MeasureSpec.getSize(heightMeasureSpec);//获取高度值
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);//获取高度模式
        //只有当宽度确定，高度不确定，ratio合法，才计算高度值
        if(widthMode==MeasureSpec.EXACTLY&&heightMode!=MeasureSpec.EXACTLY&&ratio>0){
            int imageWidth=width-getPaddingLeft()-getPaddingRight();//计算图片宽度
            int imageHeight= (int) (imageWidth/ratio);//计算图片高度
            height=imageHeight+getPaddingTop()+getPaddingBottom();//计算出自定义控件高度
        }
        //重新获得heightMeasureSpec
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
