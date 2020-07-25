package com.zhuxu.citypickerz.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/23
 * Description: 工具类
 */
public class CityPickerXUtils {

    public static void showToast(Context context, String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
    }

    /***
     * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
     * @return
     */
    public static String getSection(String pinyin) {
        if (TextUtils.isEmpty(pinyin)) {
            return "#";
        } else {
            String c = pinyin.substring(0, 1);
            Pattern p = Pattern.compile("[a-zA-Z]");
            Matcher m = p.matcher(c);
            if (m.matches()) {
                return c.toUpperCase();
            }
            //在添加定位和热门数据时设置的section就是‘定’、’热‘开头
//            else if (TextUtils.equals(c, "定") || TextUtils.equals(c, "热")) {
//                return pinyin;
//            } else {
//                return "#";
//            }
            else if (!TextUtils.isEmpty(pinyin)) {
                return pinyin;
            } else {
                return "#";
            }
        }
    }

    public static String getSideIndexStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "x";
        }
        if (str.length() > 2) {
            return str.substring(0, 2);
        } else {
            return str;
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
