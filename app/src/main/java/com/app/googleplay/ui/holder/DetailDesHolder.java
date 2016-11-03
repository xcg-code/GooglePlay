package com.app.googleplay.ui.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.googleplay.R;
import com.app.googleplay.domain.AppInfo;
import com.app.googleplay.utils.UIUtils;

/**
 * Created by 14501_000 on 2016/10/27.
 */

public class DetailDesHolder extends BaseHolder<AppInfo> {
    private TextView tvDes;
    private TextView tvAuthor;
    private ImageView ivArrow;
    private RelativeLayout rlToggle;
    @Override
    public View initView() {
        View view= UIUtils.inflate(R.layout.layout_detail_desinfo);

        tvDes = (TextView) view.findViewById(R.id.tv_detail_des);
        tvAuthor = (TextView) view.findViewById(R.id.tv_detail_author);
        ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        rlToggle = (RelativeLayout) view.findViewById(R.id.rl_detail_toggle);
        rlToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    private boolean isOpen=false;
    private LinearLayout.LayoutParams mParams;

    @Override
    public void refreshData(AppInfo data) {
        tvAuthor.setText(data.author);
        mParams= (LinearLayout.LayoutParams) tvDes.getLayoutParams();
        mParams.height=getShortHeight();
        tvDes.setLayoutParams(mParams);
        tvDes.setText(data.des);

    }


    private void toggle() {
        int shortHeight=getShortHeight();
        int wholeHeight=getWholeHeight();

        ValueAnimator animator=null;
        if(isOpen){
            isOpen=false;
            animator=ValueAnimator.ofInt(wholeHeight,shortHeight);
        }else{
            isOpen=true;
            animator=ValueAnimator.ofInt(shortHeight,wholeHeight);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer height= (Integer) animation.getAnimatedValue();
                mParams.height=height;
                tvDes.setLayoutParams(mParams);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isOpen) {
                    ivArrow.setImageResource(R.drawable.arrow_up);
                } else {
                    ivArrow.setImageResource(R.drawable.arrow_down);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(200);
        animator.start();

    }

    //默认7行数据的高度
    public int getShortHeight(){
        int width=tvDes.getMeasuredWidth();
        TextView textView=new TextView(UIUtils.getContext());
        textView.setText(getData().des);// 设置文字
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 文字大小一致
        textView.setMaxLines(4);// 最大行数为4行
        int widthMeasureSpec= View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec= View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        textView.measure(widthMeasureSpec,heightMeasureSpec);

        return textView.getMeasuredHeight();
    }

    //获取完整数据高度
    public int getWholeHeight(){
        int width=tvDes.getMeasuredWidth();
        TextView textView=new TextView(UIUtils.getContext());
        textView.setText(getData().des);// 设置文字
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);// 文字大小一致
        int widthMeasureSpec= View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec= View.MeasureSpec.makeMeasureSpec(2000, View.MeasureSpec.AT_MOST);
        textView.measure(widthMeasureSpec,heightMeasureSpec);

        return textView.getMeasuredHeight();
    }
}
