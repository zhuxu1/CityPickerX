package com.zhuxu.citypickerz.model;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;
import com.zhuxu.citypickerz.utils.CityPickerXUtils;

import java.io.Serializable;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/23
 * Description:
 */
public class CityBean implements Serializable {

    public static final String TYPE_STR_LOCATION = "TYPE_STR_LOCATION";
    public static final String TYPE_STR_HOT = "TYPE_STR_HOT";
    public static final String TYPE_STR_RECENT = "TYPE_STR_RECENT";
    public static final String TYPE_STR_LIST = "TYPE_STR_LIST";
    public static final String TYPE_STR_HEAD = "TYPE_STR_HEAD";

    // 名字
    private String name;
    // 省份
    private String province;
    // 城市编码
    private String code;
    // 拼音
    private String pinyin;
    // 类型
    private String type;
    // 用于拓展的自定义字段
    private String tag;

    public CityBean(String name, String province, String code, String pinyin, String type) {
        this.name = name;
        this.province = province;
        this.code = code;
        this.pinyin = pinyin;
        this.type = type;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPinyin() {
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(pinyin)) {
            // 自动获取首字母
            setPinyin(Pinyin.toPinyin(getName().charAt(0)));
        }
        return CityPickerXUtils.getSection(pinyin);
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", code='" + code + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", type='" + type + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
