package com.zhuxu.citypickerz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuxu.citypickerz.R;
import com.zhuxu.citypickerz.interfaces.CommonCityInterface;
import com.zhuxu.citypickerz.model.CityBean;
import com.zhuxu.citypickerz.model.HeadModelConfig;

import androidx.annotation.Nullable;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/24
 * Description: 自定义头部布局
 */
public class CustomHeadViews extends LinearLayout {

    public CustomHeadViews(Context context) {
        super(context);
        init();
    }

    public CustomHeadViews(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomHeadViews(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private LinearLayout parentRoot;
    private TextView tvTitle;
    private AutoLinefeedLayout childLayout;

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.act_custom_head_view, this);
        parentRoot = findViewById(R.id.act_custom_head_view_p);
        tvTitle = findViewById(R.id.act_custom_head_view_title);
        childLayout = findViewById(R.id.act_custom_head_view_childlayout);
    }

    HeadModelConfig config;

    public HeadModelConfig getConfig() {
        return config;
    }

    public void setConfig(HeadModelConfig _config) {
        if (_config == null && !_config.isEnabled()) {
            return;
        }
        config = _config;
        tvTitle.setText(config.getTitle());
        if (config.getCityBeans() != null && config.getCityBeans().size() > 0) {
            if (childLayout.getChildCount() != 0) {
                childLayout.removeAllViews();
            }
            for (CityBean cityBean : config.getCityBeans()) {
                childLayout.addView(getCustomChildView(config, cityBean));
            }
        }
    }

    CommonCityInterface clickInterface;

    public void setClickListener(CommonCityInterface clickListener) {
        clickInterface = clickListener;
    }

    private View getCustomChildView(HeadModelConfig config, final CityBean city) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_cutom_child, null);
        LinearLayout layoutP = view.findViewById(R.id.item_custom_child_p);
        layoutP.setBackgroundResource(config.getItemBackRes());
        layoutP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("zhuxu", "city bean is " + city.toString());
                if (clickInterface != null) {
                    clickInterface.cityResult(city);
                }
            }
        });
        if (config.isNeedIcon()) {
            TextView childIconTv = view.findViewById(R.id.item_custom_child_icon);
            childIconTv.setVisibility(VISIBLE);
            childIconTv.setText(config.getStrIconTitle());
            childIconTv.setBackgroundResource(config.getIconBackRes());
        }

        TextView childTv = view.findViewById(R.id.item_custom_child_tv);
        childTv.setText(city.getName());
        return view;
    }
}
