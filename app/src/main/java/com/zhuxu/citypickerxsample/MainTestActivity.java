package com.zhuxu.citypickerxsample;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zhuxu.citypickerz.db.DBManager;
import com.zhuxu.citypickerz.interfaces.CommonPickerXInterface;
import com.zhuxu.citypickerz.model.CityBean;
import com.zhuxu.citypickerz.model.CityPickerConfig;
import com.zhuxu.citypickerz.model.HeadModelConfig;
import com.zhuxu.citypickerz.modules.CityPickerXFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainTestActivity extends AppCompatActivity {

    CityPickerXFragment cityPickerXFragment;

    private DBManager dbManager;

    private static String STR_TITLE_HEAD_LOCATION = "定位信息";
    private static String STR_TITLE_HEAD_RECENT = "历史信息";
    private static String STR_TITLE_HEAD_HOT = "热门信息";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DBManager(MainTestActivity.this);
        findViewById(com.zhuxu.citypickerz.R.id.jump_btn).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 模拟更新数据
                        List<CityBean> listLocation = new ArrayList<>();
                        listLocation.add(new CityBean("济南n", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));
                        listLocation.add(new CityBean("黄河路n", "山东", "0546", "定位", CityBean.TYPE_STR_LOCATION));

                        List<CityBean> listRecent = new ArrayList<>();
                        listRecent.add(new CityBean("济南n", "山东", "0546", "最近", CityBean.TYPE_STR_RECENT));
                        listRecent.add(new CityBean("深圳n", "山东", "0546", "最近", CityBean.TYPE_STR_RECENT));

                        List<CityBean> listHot = new ArrayList<>();
                        listHot.add(new CityBean("北京n", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));
                        listHot.add(new CityBean("深圳n", "山东", "0546", "热门", CityBean.TYPE_STR_LIST));

                        cityPickerXFragment.updateData(STR_TITLE_HEAD_RECENT, listRecent);
                        cityPickerXFragment.updateData(STR_TITLE_HEAD_LOCATION, listLocation);
                        cityPickerXFragment.updateData(STR_TITLE_HEAD_HOT, listHot);

                        Toast.makeText(getApplicationContext(), "10秒后数据自动变化", Toast.LENGTH_SHORT).show();
                    }
                }, 10000);
                return false;
            }
        });
        findViewById(com.zhuxu.citypickerz.R.id.jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityPickerXFragment = CityPickerXFragment.startShow(MainTestActivity.this, getCityPickerConfig());
                cityPickerXFragment.setPickerXInterface(new CommonPickerXInterface() {
                    @Override
                    public void onClick(CityBean cityBean) {
                        // 在此实现你的点击逻辑
                        Toast.makeText(getApplicationContext(), "you clicked " + cityBean.getName() + " , this is a " + cityBean.getType(), Toast.LENGTH_SHORT).show();
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
                    public void onReset() {
                        // 在此实现reset触发逻辑
                        Toast.makeText(getApplicationContext(), "reset", Toast.LENGTH_SHORT).show();
                        List<CityBean> beanList = dbManager.getAllCities();
                        cityPickerXFragment.updateListData(beanList, true);
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
        return cityPickerConfig;
    }
}