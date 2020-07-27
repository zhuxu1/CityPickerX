package com.zhuxu.citypickerz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/23
 * Description: 参数类
 */
public class CityPickerConfig implements Serializable {

    // 定位模块配置
    private HeadModelConfig locationConfig;
    // 最近访问模块配置
    private HeadModelConfig recentConfig;
    // 热门城市模块配置
    private HeadModelConfig hotConfig;

    // 城市列表数据 null为不使用
    private List<CityBean> listData = new ArrayList<>();

    public CityPickerConfig() {
        this.locationConfig = new HeadModelConfig(false);
        this.recentConfig = new HeadModelConfig(false);
        this.hotConfig = new HeadModelConfig(false);
        this.listData = new ArrayList<>();
    }

    /**
     * @param locationConfig 定位模块配置
     * @param recentConfig   最近模块配置
     * @param hotConfig      热门模块配置
     * @param listData       列表数据配置
     */
    public CityPickerConfig(HeadModelConfig locationConfig, HeadModelConfig recentConfig, HeadModelConfig hotConfig, List<CityBean> listData) {
        this.locationConfig = locationConfig;
        this.recentConfig = recentConfig;
        this.hotConfig = hotConfig;
        this.listData = listData;
    }

    public HeadModelConfig getLocationConfig() {
        return locationConfig;
    }

    public void setLocationConfig(HeadModelConfig locationConfig) {
        this.locationConfig = locationConfig;
    }

    public HeadModelConfig getRecentConfig() {
        return recentConfig;
    }

    public void setRecentConfig(HeadModelConfig recentConfig) {
        this.recentConfig = recentConfig;
    }

    public HeadModelConfig getHotConfig() {
        return hotConfig;
    }

    public void setHotConfig(HeadModelConfig hotConfig) {
        this.hotConfig = hotConfig;
    }

    public List<CityBean> getListData() {
        return listData;
    }

    public void setListData(List<CityBean> listData) {
        this.listData = listData;
    }

    public boolean useCustomListData() {
        if (listData == null || listData.size() == 0) {
            return false;
        }
        return true;
    }
}
