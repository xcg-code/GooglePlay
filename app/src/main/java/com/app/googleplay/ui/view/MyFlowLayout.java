package com.app.googleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.app.googleplay.utils.UIUtils;

import java.util.ArrayList;

/**
 * Created by 14501_000 on 2016/10/30.
 */

public class MyFlowLayout extends ViewGroup {

    private int usedWidth;
    private Line mLine;//当前行
    private int mHorizontalSpacing = UIUtils.dip2px(6);// 水平间距
    private int mVerticalSpacing = UIUtils.dip2px(8);// 竖直间距
    private static final int MAX_LINE = 100;// 最大行数是100行
    private ArrayList<Line> lineList = new ArrayList<Line>();

    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = l + getPaddingLeft();
        int top =t+getPaddingTop();
        // 遍历所有行对象, 设置每行位置
        for(int i=0;i<lineList.size();i++){
            Line line=lineList.get(i);
            line.onLayout(left,top);
            top+=line.maxHeight+mVerticalSpacing;// 更新top值
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取有效宽度，及宽度模式
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取有效高度，及高度模式
        int height = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int childCount = getChildCount();//所有子控件数量
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, (widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : widthMode);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, (heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST : heightMode);
            //开始测量
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            // 如果当前行对象为空, 初始化一个行对象
            if (mLine == null) {
                mLine = new Line();
            }
            int childWidth = childView.getMeasuredWidth();
            usedWidth += childWidth;//已使用的宽度
            if (usedWidth < width) {// 判断是否超出了边界
                mLine.addView(childView);// 为当前行对象添加子控件
                usedWidth += mHorizontalSpacing;
                if (usedWidth > width) {
                    // 增加水平间距之后, 就超出了边界, 此时也需要换行
                    if (!nextLine()) {
                        break;
                    }
                }
            } else {
                //需要换行
                if (mLine.getChildCount() == 0) { //1.当前没有任何控件, 一旦添加当前子控件, 就超出边界(子控件很长)
                    mLine.addView(childView);
                    if (!nextLine()) {
                        break;
                    }
                } else {// 2.当前有控件, 一旦添加, 超出边界
                    if (!nextLine()) {
                        break;
                    }
                    mLine.addView(childView);
                    usedWidth += childWidth + mHorizontalSpacing;// 更新已使用宽度

                }
            }
        }
        // 保存最后一行的行对象
        if (mLine != null && mLine.getChildCount() != 0 && !lineList.contains(mLine)) {
            lineList.add(mLine);
        }
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);//控件整体宽度
        int totalHeight = 0;//控件整体高度
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);
            totalHeight += line.maxHeight;
        }
        totalHeight += (lineList.size() - 1) * mVerticalSpacing;// 增加竖直间距
        totalHeight += getPaddingTop() + getPaddingBottom();// 增加上下边距

        // 根据最新的宽高来测量整体布局的大小
        setMeasuredDimension(totalWidth, totalHeight);
    }

    private boolean nextLine() {
        lineList.add(mLine);// 保存上一行数据
        if (lineList.size() < MAX_LINE) {
            mLine = new Line();
            usedWidth = 0;// 已使用宽度清零
            return true;
        }
        return false;

    }

    //每一行对象封装
    class Line {
        private ArrayList<View> childViewList = new ArrayList<View>();
        private int totalWidth;//已占用的总宽度
        private int maxHeight;//控件最大高度

        public void addView(View view) {
            childViewList.add(view);
            totalWidth += view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            maxHeight = (maxHeight < height) ? height : maxHeight;
        }

        private int getChildCount() {
            return childViewList.size();
        }

        public void onLayout(int left, int top) {
            int childCount = getChildCount();
            //有效宽度
            int vaildWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            //剩余宽度
            int surplusWidth = vaildWidth - totalWidth - (childCount - 1) * mHorizontalSpacing;
            if (surplusWidth > 0) {
                int space = (int) ((float) surplusWidth / childCount + 0.5f);
                //重新测量子控件
                for (int i = 0; i < childCount; i++) {
                    View childView = childViewList.get(i);
                    int childWidth = childView.getMeasuredWidth();
                    int childHeight = childView.getMeasuredHeight();
                    childWidth += space;// 宽度增加
                    int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                    // 重新测量控件
                    childView.measure(widthMeasureSpec, heightMeasureSpec);
                    // 当控件比较矮时,需要居中展示, 竖直方向需要向下有一定偏移
                    int topOffset = (maxHeight - heightMeasureSpec) / 2;
                    if (topOffset < 0) topOffset = 0;
                    // 设置子控件位置
                    childView.layout(left, top + topOffset, left + childWidth, top + topOffset + childHeight);
                    left += childWidth + mHorizontalSpacing;// 更新left值
                }
            } else {
                // 这个控件很长, 占满整行
                View childView = childViewList.get(0);
                childView.layout(left, top,
                        left + childView.getMeasuredWidth(),
                        top + childView.getMeasuredHeight());

            }
        }
    }
}
