package com.zhuxu.citypickerz.model;

import com.zhuxu.citypickerz.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/24
 * Description:
 */
public class HeadModelConfig implements Serializable {

    // 是否开启此模块
    private boolean isEnabled = true;
    // 自定义tag
    private String tag = "";
    // 标题
    private String title;
    // 内容
    private List<CityBean> cityBeans = new ArrayList<>();
    // 是否需要icon
    private boolean needIcon = false;
    // icon标题
    private String strIconTitle = "";
    // icon的背景
    private int iconBackRes = R.drawable.shape_back_icon;
    // item背景
    private int itemBackRes = R.drawable.shape_item_head;

    public HeadModelConfig(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 默认开启
     */
    public HeadModelConfig(String title, List<CityBean> cityBeans, boolean needIcon, String strIconTitle, int iconBackRes, int itemBackRes) {
        this.isEnabled = true;
        this.title = title;
        this.cityBeans = cityBeans;
        this.needIcon = needIcon;
        this.strIconTitle = strIconTitle;
        setIconBackRes(iconBackRes);
        setItemBackRes(itemBackRes);
    }

    public HeadModelConfig(String tag, String title, List<CityBean> cityBeans, boolean needIcon, String strIconTitle, int iconBackRes, int itemBackRes) {
        this.isEnabled = true;
        this.tag = tag;
        this.title = title;
        this.cityBeans = cityBeans;
        this.needIcon = needIcon;
        this.strIconTitle = strIconTitle;
        setIconBackRes(iconBackRes);
        setItemBackRes(itemBackRes);
    }

    public HeadModelConfig(String title, List<CityBean> cityBeans) {
        this.isEnabled = true;
        this.title = title;
        this.cityBeans = cityBeans;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CityBean> getCityBeans() {
        return cityBeans;
    }

    public void setCityBeans(List<CityBean> cityBeans) {
        this.cityBeans = cityBeans;
    }

    public boolean isNeedIcon() {
        return needIcon;
    }

    public void setNeedIcon(boolean needIcon) {
        this.needIcon = needIcon;
    }

    public String getStrIconTitle() {
        return strIconTitle;
    }

    public void setStrIconTitle(String strIconTitle) {
        this.strIconTitle = strIconTitle;
    }

    public int getIconBackRes() {
        return iconBackRes;
    }

    public void setIconBackRes(int iconBackRes) {
        if (iconBackRes == 0) {
            this.iconBackRes = R.drawable.shape_back_icon;
        } else {
            this.iconBackRes = iconBackRes;
        }
    }

    public int getItemBackRes() {
        return itemBackRes;
    }

    public void setItemBackRes(int itemBackRes) {
        if (itemBackRes == 0) {
            this.itemBackRes = R.drawable.shape_item_head;
        } else {
            this.itemBackRes = itemBackRes;
        }
    }
}
