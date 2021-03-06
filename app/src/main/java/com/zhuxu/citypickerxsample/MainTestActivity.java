package com.zhuxu.citypickerxsample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhuxu.citypickerz.db.DBManager;
import com.zhuxu.citypickerz.interfaces.CommonPickerXInterface;
import com.zhuxu.citypickerz.model.CityBean;
import com.zhuxu.citypickerz.model.CityPickerConfig;
import com.zhuxu.citypickerz.model.HeadModelConfig;
import com.zhuxu.citypickerz.modules.CityPickerXFragment;
import com.zhuxu.citypickerz.modules.CityPickerZFragment;

import java.util.ArrayList;
import java.util.List;

public class MainTestActivity extends AppCompatActivity {

    CityPickerXFragment cityPickerXFragment;

    private DBManager dbManager;

    private static String STR_TITLE_HEAD_LOCATION = "定位信息";
    private static String STR_TITLE_HEAD_RECENT = "历史信息";
    private static String STR_TITLE_HEAD_HOT = "热门信息";

    CityPickerZFragment cityPickerZFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_test);
        dbManager = new DBManager(MainTestActivity.this);

        cityPickerZFragment = (CityPickerZFragment) getSupportFragmentManager().findFragmentById(R.id.z_fragment);
        cityPickerZFragment.setConfigData(getCityPickerConfig());
//        cityPickerZFragment = CityPickerZFragment.newInstance(getCityPickerConfig());
        cityPickerZFragment.setPickerXInterface(new CommonPickerXInterface() {
            @Override
            public void onClick(CityBean cityBean) {
                // 在此实现你的点击逻辑
                Toast.makeText(getApplicationContext(), "you clicked " + cityBean.getName() + " , this is a " + cityBean.getType(), Toast.LENGTH_SHORT).show();
                cityPickerZFragment.hideDialog();
            }

            @Override
            public void onDismiss() {
                // 在此实现dismiss触发逻辑
                getSupportFragmentManager().beginTransaction().hide(cityPickerZFragment).commitAllowingStateLoss();
                Toast.makeText(getApplicationContext(), "dismiss", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearch(String s) {
                // 在此实现你的搜索逻辑
                Toast.makeText(getApplicationContext(), "you search " + s, Toast.LENGTH_SHORT).show();
                if (TextUtils.isEmpty(s)) {
                    List<CityBean> beanList = dbManager.getAllCities();
                    cityPickerZFragment.updateListData(beanList, true);
                } else {
                    List<CityBean> beanList = dbManager.searchCity(s);
                    cityPickerZFragment.updateListData(beanList, false);
                }
            }

            @Override
            public void onInit() {
                // 初始化完成后的请求
                // 例如定位，更新
            }
        });
        getSupportFragmentManager().beginTransaction().hide(cityPickerZFragment).commitAllowingStateLoss();

        findViewById(R.id.jump_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("zhuxu","=============startShow==============");
                getSupportFragmentManager().beginTransaction().show(cityPickerZFragment).commitAllowingStateLoss();
            }
        });
        findViewById(R.id.jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("zhuxu","=============startShow==============");
                cityPickerXFragment = CityPickerXFragment.startShow(MainTestActivity.this, getCityPickerConfig());
//                cityPickerXFragment = CityPickerXFragment.startShow(MainTestActivity.this, new CityPickerConfig());
                cityPickerXFragment.setPickerXInterface(new CommonPickerXInterface() {
                    @Override
                    public void onClick(CityBean cityBean) {
                        // 在此实现你的点击逻辑
                        Toast.makeText(getApplicationContext(), "you clicked " + cityBean.getName() + " , this is a " + cityBean.getType(), Toast.LENGTH_SHORT).show();
                        cityPickerXFragment.hideDialog();
                    }

                    @Override
                    public void onDismiss() {
                        // 在此实现dismiss触发逻辑
                        Toast.makeText(getApplicationContext(), "dismiss", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSearch(String s) {
                        // 在此实现你的搜索逻辑
                        Toast.makeText(getApplicationContext(), "you search " + s, Toast.LENGTH_SHORT).show();
                        if (TextUtils.isEmpty(s)) {
                            List<CityBean> beanList = dbManager.getAllCities();
                            cityPickerXFragment.updateListData(beanList, true);
                        } else {
                            List<CityBean> beanList = dbManager.searchCity(s);
                            cityPickerXFragment.updateListData(beanList, false);
                        }
                    }

                    @Override
                    public void onInit() {
                        // 初始化完成后的请求
                        // 例如定位，更新
                        Log.e("zhuxu","=============initShow==============");
                    }
                });
            }
        });
    }

    private CityPickerConfig getCityPickerConfig() {
        List<CityBean> listLocation = new ArrayList<>();
        listLocation.add(new CityBean("济南", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));
        listLocation.add(new CityBean("黄河路", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));
        listLocation.add(new CityBean("长江路", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));
        listLocation.add(new CityBean("腊山路", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));

        List<CityBean> listRecent = new ArrayList<>();
        listRecent.add(new CityBean("济南", "山东", "0546", "最近", CityBean.TYPE_STR_RECENT));
        listRecent.add(new CityBean("深圳", "山东", "0546", "最近", CityBean.TYPE_STR_RECENT));
        listRecent.add(new CityBean("上海", "山东", "0546", "最近", CityBean.TYPE_STR_RECENT));

        List<CityBean> listHot = new ArrayList<>();
        listHot.add(new CityBean("北京", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("深圳", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("上海", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("成都", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("广州", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("天津", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
        listHot.add(new CityBean("大连", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));

        HeadModelConfig locationConfig = new HeadModelConfig(STR_TITLE_HEAD_LOCATION, listLocation);
        locationConfig.setTag(STR_TITLE_HEAD_LOCATION);
        HeadModelConfig recentConfig = new HeadModelConfig(STR_TITLE_HEAD_RECENT, listRecent, true, "近", 0, 0);
        recentConfig.setTag(STR_TITLE_HEAD_RECENT);
        HeadModelConfig hotConfig = new HeadModelConfig(STR_TITLE_HEAD_HOT, listHot, true, "热", 0, 0);
        hotConfig.setTag(STR_TITLE_HEAD_HOT);
        CityPickerConfig cityPickerConfig = new CityPickerConfig(locationConfig, recentConfig, hotConfig, null);
//        CityPickerConfig cityPickerConfig = new CityPickerConfig(null, null, null, null);
        return cityPickerConfig;
    }
}