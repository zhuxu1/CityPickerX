package com.zhuxu.citypickerz.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhuxu.citypickerz.interfaces.CommonBoolInterface;
import com.zhuxu.citypickerz.interfaces.CommonStringInterface;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/23
 * Description: 搜索部分基类
 */
public abstract class BaseSearchView extends LinearLayout {
    public BaseSearchView(Context context) {
        super(context);
    }

    public BaseSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 关闭
    protected CommonBoolInterface closeListener;

    public void setCloseListener(CommonBoolInterface commonBoolInterface) {
        closeListener = commonBoolInterface;
    }

    // 搜索
    protected CommonStringInterface searchListener;

    public void setSearchListener(CommonStringInterface commonBoolInterface) {
        searchListener = commonBoolInterface;
    }

    // 重置
    protected CommonBoolInterface resetListener;

    public void setResetListener(CommonBoolInterface commonBoolInterface) {
        resetListener = commonBoolInterface;
    }

}
