package com.zhuxu.citypickerz.utils;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zhuxu.citypickerz.model.CityBean;

import java.util.List;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/24
 * Description:
 */
public class CityDataInitUtils {

    /**
     * 初始化数据
     *
     * @param list          原始数据
     * @param needGotPinyin 是否需要获得首字母
     * @param needSort      是否需要排序
     * @return
     */
    public static List<CityBean> initData(List<CityBean> list, boolean needGotPinyin, boolean needSort) {
        if (needGotPinyin) {
            initPinyinData(list);
        }
        if (needSort){
            initSortData(list);
        }
        return list;
    }

    private static List<CityBean> initPinyinData(List<CityBean> list) {
        for (CityBean city : list) {
            if (TextUtils.isEmpty(city.getPinyin())) {
                // 自动获取首字母
                city.setPinyin(Pinyin.toPinyin(city.getName().charAt(0)));
            }
        }
        // 排序
        ChineseSortUtil.sortList(list);
        return list;
    }

    private static List<CityBean> initSortData(List<CityBean> list) {
        // 排序
        ChineseSortUtil.sortList(list);
        return list;
    }
}
