package com.zhuxu.citypickerz.interfaces;

import com.zhuxu.citypickerz.model.CityBean;

/**
 * craated by Zhuxu _email:zhuxu@chenyushunjia.com
 * Creat time : 2020/7/25
 * Description:
 */
public interface CommonPickerXInterface {

    void onClick(CityBean cityBean);

    void onDismiss();

    void onSearch(String s);

    void onInit();

}
