package com.zhuxu.citypickerz.modules;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuxu.citypickerz.R;
import com.zhuxu.citypickerz.db.DBManager;
import com.zhuxu.citypickerz.interfaces.CommonBoolInterface;
import com.zhuxu.citypickerz.interfaces.CommonCityInterface;
import com.zhuxu.citypickerz.interfaces.CommonPickerXInterface;
import com.zhuxu.citypickerz.interfaces.CommonStringInterface;
import com.zhuxu.citypickerz.model.CityBean;
import com.zhuxu.citypickerz.model.CityPickerConfig;
import com.zhuxu.citypickerz.model.HeadModelConfig;
import com.zhuxu.citypickerz.utils.CityPickerXUtils;
import com.zhuxu.citypickerz.views.CustomHeadViews;
import com.zhuxu.citypickerz.views.SearchView;
import com.zhuxu.citypickerz.views.SideIndexBar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CityPickerXFragment extends DialogFragment {

    private static CityPickerXFragment cityPickerXFragment;

    public static CityPickerXFragment startShow(FragmentActivity activity, CityPickerConfig cityPickerConfig) {
        if (cityPickerXFragment == null) {
            cityPickerXFragment = CityPickerXFragment.newInstance(cityPickerConfig);
        }
        if (cityPickerXFragment.isAdded()) {
            activity.getSupportFragmentManager().beginTransaction().show(cityPickerXFragment).commitAllowingStateLoss();
        } else {
            activity.getSupportFragmentManager().beginTransaction().add(cityPickerXFragment, "cityPickerXActivity").commitAllowingStateLoss();
        }
        return cityPickerXFragment;
    }

    public static final String TAG = "CityPickerX";
    private static final String STR_CONFIG_HINT_ERROR = "解析失败,请检查启动调用是否合规!";

    private SearchView mZCitypickerSearchview;
    private NestedScrollView mZCitypickerScrollview;
    private LinearLayout mZCitypickerScrollviewHeadview;
    private RecyclerView mZCitypickerRecycleview;
    private SideIndexBar mZCitypickerSideindexbar;
    private TextView mCenterIndexHint;

    public static CityPickerXFragment newInstance(CityPickerConfig config) {
        final CityPickerXFragment fragment = new CityPickerXFragment();
        Bundle args = new Bundle();
        args.putSerializable("key", config);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_citylist_z_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    CityPickerConfig cityPickerConfig;
    List<CityBean> beanList;
    private DBManager dbManager;

    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            cityPickerConfig = (CityPickerConfig) args.getSerializable("key");
        } else {
            CityPickerXUtils.showToast(getActivity(), STR_CONFIG_HINT_ERROR);
            Log.e(TAG, STR_CONFIG_HINT_ERROR);
            if (pickerXInterface != null) {
                pickerXInterface.onDismiss();
            }
            dismiss();
            return;
        }

        if (!cityPickerConfig.useCustomListData()) {
            beanList = new ArrayList<>();
            dbManager = new DBManager(getActivity());
            beanList = dbManager.getAllCities();
            // 不初始化拼音内容
            // beanList = initPinyinData(beanList);
        } else {
            beanList = cityPickerConfig.getListData();
        }
    }

    CityListAdapter mAdapter;

    private void initViews() {
        findViews();
        initSearchView();
        initRl();
        initHeadViews();
        mZCitypickerSideindexbar.setLetterChoose("A");
    }

    private void findViews() {
        mZCitypickerSearchview = getView().findViewById(R.id.z_citypicker_searchview);
        mZCitypickerScrollview = getView().findViewById(R.id.z_citypicker_scrollview);
        mZCitypickerScrollviewHeadview = getView().findViewById(R.id.z_citypicker_scrollview_headview);
        mZCitypickerRecycleview = getView().findViewById(R.id.z_citypicker_scrollview_recycleview);
        mZCitypickerSideindexbar = getView().findViewById(R.id.z_citypicker_sideindexbar);
        mCenterIndexHint = getView().findViewById(R.id.layout_citylist_index_hint);
    }

    private void initSearchView() {
        mZCitypickerSearchview.setCloseListener(new CommonBoolInterface() {
            @Override
            public void result(boolean result) {
                if (pickerXInterface != null) {
                    pickerXInterface.onDismiss();
                }
                dismiss();
            }
        });
        mZCitypickerSearchview.setSearchListener(new CommonStringInterface() {
            @Override
            public void result(String result) {
                if (pickerXInterface != null) {
                    pickerXInterface.onSearch(result);
                }
            }
        });
        mZCitypickerSearchview.setResetListener(new CommonBoolInterface() {
            @Override
            public void result(boolean result) {
                if (pickerXInterface != null) {
                    pickerXInterface.onReset();
                }
            }
        });
        mZCitypickerSideindexbar.setOnLetterChangeListener(new SideIndexBar.OnLetterChangeListner() {
            @Override
            public void onLetterChanged(String letter) {
                mCenterIndexHint.setVisibility(View.VISIBLE);
                mCenterIndexHint.setText(letter);
                if (sideIndexList.size() > 0) {
                    for (String s : sideIndexList) {
                        Log.e(TAG, letter + " : " + s);
                        if (TextUtils.equals(letter, CityPickerXUtils.getSideIndexStr(s))) {
                            mAdapter.scrollToSectionHead();
                            return;
                        }
                    }
                }
                mAdapter.scrollToSection(letter);
            }

            @Override
            public void onLetterChoosed(String letter) {
                mCenterIndexHint.setVisibility(View.GONE);
            }
        });
    }

    private void initRl() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new CityListAdapter(getActivity(), beanList, layoutManager);
        mZCitypickerRecycleview.addItemDecoration(new StickHeaderDecoration(getActivity(), 2));
        mZCitypickerRecycleview.setLayoutManager(layoutManager);
        mZCitypickerRecycleview.setAdapter(mAdapter);
        mZCitypickerRecycleview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int _position = layoutManager.findFirstVisibleItemPosition();
                if (beanList.size() >= _position) {
                    String _index_str = beanList.get(_position).getPinyin();
                    mZCitypickerSideindexbar.setLetterChoose(_index_str);
                } else {
                    Log.e(TAG, "无法检索此条目!");
                }
            }
        });
        mAdapter.setClickListener(new CommonCityInterface() {
            @Override
            public void cityResult(CityBean cityBean) {
                if (pickerXInterface != null) {
                    pickerXInterface.onClick(cityBean);
                }
            }
        });
    }

    // 侧边导航栏新增部分内容
    List<String> sideIndexList = new ArrayList<>();
    List<View> headViews = new ArrayList<>();

    private void initHeadViews() {
        if (cityPickerConfig == null) {
            return;
        }
        headViews = new ArrayList<>();
        sideIndexList = new ArrayList<>();
        if (cityPickerConfig.getLocationConfig() != null && cityPickerConfig.getLocationConfig().isEnabled()) {
            headViews.add(getCustomModelItem(cityPickerConfig.getLocationConfig()));
            sideIndexList.add(cityPickerConfig.getLocationConfig().getTitle());
        }
        if (cityPickerConfig.getRecentConfig() != null && cityPickerConfig.getRecentConfig().isEnabled()) {
            headViews.add(getCustomModelItem(cityPickerConfig.getRecentConfig()));
            sideIndexList.add(cityPickerConfig.getRecentConfig().getTitle());
        }
        if (cityPickerConfig.getHotConfig() != null && cityPickerConfig.getHotConfig().isEnabled()) {
            headViews.add(getCustomModelItem(cityPickerConfig.getHotConfig()));
            sideIndexList.add(cityPickerConfig.getHotConfig().getTitle());
        }
        mAdapter.addHeadViews(headViews);
        mZCitypickerSideindexbar.addLetters(sideIndexList);
    }

    private View getCustomModelItem(HeadModelConfig headModelConfig) {
        CustomHeadViews customHeadViews = new CustomHeadViews(getContext());
        customHeadViews.setConfig(headModelConfig);
        customHeadViews.setClickListener(new CommonCityInterface() {
            @Override
            public void cityResult(CityBean cityBean) {
                if (pickerXInterface != null) {
                    pickerXInterface.onClick(cityBean);
                }
            }
        });
        return customHeadViews;
    }

    public void updateData(String tag, List<CityBean> _listBeans) {
        if (headViews != null && headViews.size() != 0) {
            for (View view : headViews) {
                CustomHeadViews customHeadViews = (CustomHeadViews) view;
                HeadModelConfig _config = customHeadViews.getConfig();
                if (TextUtils.equals(_config.getTag(), tag)) {
                    _config.setCityBeans(_listBeans);
                    customHeadViews.setConfig(_config);
                    return;
                }
            }
        } else {
            Log.e(TAG, "错误的调用，请检查你是否已经添加了头部view!");
        }
    }

    CommonPickerXInterface pickerXInterface;

    public void setPickerXInterface(CommonPickerXInterface commonPickerXInterface) {
        pickerXInterface = commonPickerXInterface;
    }
}