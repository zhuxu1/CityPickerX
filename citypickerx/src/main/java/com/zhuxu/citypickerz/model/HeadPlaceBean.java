package com.zhuxu.citypickerz.model;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/24
 * Description: 头部占位
 */
public class HeadPlaceBean extends CityBean {
    public HeadPlaceBean() {
        super("", "", "", "", TYPE_STR_HEAD);
    }

    public HeadPlaceBean(String pinyin) {
        super("", "", "", pinyin, TYPE_STR_HEAD);
    }
}
