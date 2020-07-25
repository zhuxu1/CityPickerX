package com.zhuxu.citypickerz.modules;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.zhuxu.citypickerz.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义装饰器（实现分组+吸顶效果）
 * Create by: chenWei.li
 * Date: 2018/11/2
 * Time: 上午1:14
 * Email: lichenwei.me@foxmail.com
 */
public class StickHeaderDecoration extends RecyclerView.ItemDecoration {

    //头部的高
    private int mItemHeaderHeight;
    private int mTextPaddingLeft;

    //画笔，绘制头部和分割线
    private Paint mItemHeaderPaint;
    private Paint mItemHeaderTextPaint;
    private Paint mLinePaint;

    private Rect mTextRect;

    private float mLineSpacingHeight = 2;


    public StickHeaderDecoration(Context context, float _lineSpacingHeight) {
        mLineSpacingHeight = _lineSpacingHeight;
        mItemHeaderHeight = dp2px(context, 40);
        mTextPaddingLeft = dp2px(context, 20);

        mTextRect = new Rect();

        mItemHeaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mItemHeaderPaint.setColor(Color.WHITE);

        mItemHeaderTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mItemHeaderTextPaint.setTextSize(46);
        mItemHeaderTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mItemHeaderTextPaint.setColor(Color.BLACK);
        mItemHeaderTextPaint.setColor(context.getColor(R.color.z_color_black_txt));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setColor(context.getColor(R.color.z_color_spacing_ed));

    }

    /**
     * 绘制Item的分割线和组头
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof CityListAdapter) {
            CityListAdapter adapter = (CityListAdapter) parent.getAdapter();
            int count = parent.getChildCount();//获取可见范围内Item的总数
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildLayoutPosition(view);
                if (adapter.isCutomHead(position)) {
                    continue;
                }
                boolean isHeader = adapter.isItemHeader(position);
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                if (isHeader) {
                    c.drawRect(left, view.getTop() - mItemHeaderHeight, right, view.getTop(), mItemHeaderPaint);
                    mItemHeaderTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                    c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft,
                            (view.getTop() - mItemHeaderHeight) + mItemHeaderHeight / 2 + mTextRect.height() / 2, mItemHeaderTextPaint);
                    c.drawRect(left + mTextPaddingLeft, view.getTop() - mItemHeaderHeight - mLineSpacingHeight,
                            right, view.getTop() - mItemHeaderHeight, mLinePaint);
                } else {
                    c.drawRect(left + mTextPaddingLeft, view.getBottom() + mLineSpacingHeight, right, view.getBottom(), mLinePaint);
                }
                c.drawRect(left + mTextPaddingLeft, view.getTop() - mLineSpacingHeight, right, view.getTop(), mLinePaint);
            }
        }
    }

    /**
     * 绘制Item的顶部布局（吸顶效果）
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getAdapter() instanceof CityListAdapter) {
            CityListAdapter adapter = (CityListAdapter) parent.getAdapter();
            int position = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
            if (position == 0) {
                return;
            }
            if (parent == null || parent.findViewHolderForAdapterPosition(position) == null || parent.findViewHolderForAdapterPosition(position).itemView == null) {
                Log.e("zhuxu", "onDrawOver some thing is null");
                return;
            }
            View view = parent.findViewHolderForAdapterPosition(position).itemView;
            boolean isHeader = adapter.isItemHeader(position + 1);
            int top = parent.getPaddingTop();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            if (isHeader) {
                int bottom = Math.min(mItemHeaderHeight, view.getBottom());
                c.drawRect(left, top + view.getTop() - mItemHeaderHeight, right, top + bottom, mItemHeaderPaint);
                mItemHeaderTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft,
                        top + mItemHeaderHeight / 2 + mTextRect.height() / 2 - (mItemHeaderHeight - bottom), mItemHeaderTextPaint);
                c.drawRect(left + mTextPaddingLeft, top + bottom - mLineSpacingHeight, right, top + bottom, mLinePaint);
            } else {
                c.drawRect(left, top, right, top + mItemHeaderHeight, mItemHeaderPaint);
                mItemHeaderTextPaint.getTextBounds(adapter.getGroupName(position), 0, adapter.getGroupName(position).length(), mTextRect);
                c.drawText(adapter.getGroupName(position), left + mTextPaddingLeft,
                        top + mItemHeaderHeight / 2 + mTextRect.height() / 2, mItemHeaderTextPaint);
                c.drawRect(left + mTextPaddingLeft, top + mItemHeaderHeight - mLineSpacingHeight, right, top + mItemHeaderHeight, mLinePaint);
            }
            c.save();
        }

    }

    /**
     * 设置Item的间距
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State
            state) {
        if (parent.getAdapter() instanceof CityListAdapter) {
            CityListAdapter adapter = (CityListAdapter) parent.getAdapter();
            int position = parent.getChildLayoutPosition(view);
            if (adapter.isCutomHead(position)) {
                outRect.top = 1;
                return;
            }
            boolean isHeader = adapter.isItemHeader(position);
            if (isHeader) {
                outRect.top = mItemHeaderHeight;
            } else {
                outRect.top = 1;
            }
        }
    }


    /**
     * dp转换成px
     */
    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}