package com.zhuxu.citypickerz.modules;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.zhuxu.citypickerz.views.SearchView;
import com.zhuxu.citypickerz.views.SideIndexBar;

import java.util.ArrayList;
import java.util.List;

public class CityPickerZFragment extends Fragment {

    public static final String TAG = "CityPickerX";
    public static final String TAG_HEADP = "TAG_HEADP";
    private static final String STR_CONFIG_HINT_ERROR = "解析失败,请检查启动调用是否合规!";

    private SearchView mZCitypickerSearchview;
    private NestedScrollView mZCitypickerScrollview;
    private LinearLayout mZCitypickerScrollviewHeadview;
    private RecyclerView mZCitypickerRecycleview;
    private SideIndexBar mZCitypickerSideindexbar;
    private TextView mCenterIndexHint;
    private TextView tvEmpty;

    private LinearLayout layoutHeadP;
    private TextView tvHeadP;

    public static CityPickerZFragment newInstance(CityPickerConfig config) {
        final CityPickerZFragment fragment = new CityPickerZFragment();
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
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        int id = enter ? R.anim.cp_push_bottom_in : R.anim.cp_push_bottom_out;
        return AnimationUtils.loadAnimation(getContext(), id);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (cityPickerConfig != null) {
            initData();
            initViews();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.FragmentDialogAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    CityPickerConfig cityPickerConfig;
    List<CityBean> beanList;
    private DBManager dbManager;

    private void initData() {
        if (cityPickerConfig == null) {
            cityPickerConfig = new CityPickerConfig();
            beanList = cityPickerConfig.getListData();
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
//        List<CityBean> _beanList = new ArrayList<>();
//        for (CityBean cityBean : beanList){
//            _beanList.add(cityBean);
//            if (_beanList.size() > 100){
//                beanList = _beanList;
//                break;
//            }
//        }
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
        tvEmpty = getView().findViewById(R.id.z_citypicker_scrollview_recycleview_empty);
        layoutHeadP = getView().findViewById(R.id.layout_citylist_z_main_titlehead_p);
        tvHeadP = getView().findViewById(R.id.layout_citylist_z_main_titlehead);
        mZCitypickerRecycleview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mZCitypickerRecycleview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                Log.e("zhuxu", "=======加载完成=======");
                if (pickerXInterface != null) {
                    pickerXInterface.onInit();
                }
            }
        });
    }

    private void initSearchView() {
        mZCitypickerSearchview.setCloseListener(new CommonBoolInterface() {
            @Override
            public void result(boolean result) {
                hideDialog();
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
//                    pickerXInterface.onReset();
                }
            }
        });
        mZCitypickerSideindexbar.setOnLetterChangeListener(new SideIndexBar.OnLetterChangeListner() {
            @Override
            public void onLetterChanged(String letter) {
                mCenterIndexHint.setVisibility(View.VISIBLE);
                mCenterIndexHint.setText(letter);
//                if (sideIndexList.size() > 0) {
//                    for (String s : sideIndexList) {
//                        if (TextUtils.equals(letter, CityPickerXUtils.getSideIndexStr(s))) {
//                            mAdapter.scrollToSectionHead();
//                            return;
//                        }
//                    }
//                }
                mAdapter.scrollToSection(letter);
            }

            @Override
            public void onLetterChoosed(String letter) {
                mCenterIndexHint.setVisibility(View.GONE);
            }
        });
    }

    LinearLayoutManager layoutManager;

    private void initRl() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new CityListAdapter(getActivity(), beanList, layoutManager);
        mZCitypickerRecycleview.addItemDecoration(new StickHeaderDecoration(getActivity(), 2));
        mZCitypickerRecycleview.setLayoutManager(layoutManager);
        mZCitypickerRecycleview.setAdapter(mAdapter);
//        mZCitypickerRecycleview.setHasFixedSize(true);
//        mZCitypickerRecycleview.setItemViewCacheSize(300);
        mZCitypickerRecycleview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int _position = layoutManager.findFirstVisibleItemPosition();
                List<CityBean> _beanList = mAdapter.getmList();
                if (_position < 0 || _position >= _beanList.size()) {
                    Log.e(TAG, "无法检索条目!");
                    return;
                }
                if (_beanList.size() >= _position) {
                    String _index_str = _beanList.get(_position).getPinyin();
                    String _name_str = _beanList.get(_position).getName();
//                    Log.e("zhuxu", "bean : " + _beanList.get(_position).toString());
//                    Log.e("zhuxu", "_position : " + _position + " , _index_str : " + _index_str + " , _name_str : " + _name_str);
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
        tvEmpty.setVisibility(View.GONE);
    }

    // 侧边导航栏新增部分内容
    List<String> sideIndexList = new ArrayList<>();
    List<HeadModelConfig> headViews = new ArrayList<>();

    private void initHeadViews() {
        headViews = new ArrayList<>();
        sideIndexList = new ArrayList<>();
        if (cityPickerConfig != null) {
            if (cityPickerConfig.getLocationConfig() != null && cityPickerConfig.getLocationConfig().isEnabled()) {
                headViews.add(cityPickerConfig.getLocationConfig());
                sideIndexList.add(cityPickerConfig.getLocationConfig().getTitle());
            }
            if (cityPickerConfig.getRecentConfig() != null && cityPickerConfig.getRecentConfig().isEnabled()) {
                headViews.add(cityPickerConfig.getRecentConfig());
                sideIndexList.add(cityPickerConfig.getRecentConfig().getTitle());
            }
            if (cityPickerConfig.getHotConfig() != null && cityPickerConfig.getHotConfig().isEnabled()) {
                headViews.add(cityPickerConfig.getHotConfig());
                sideIndexList.add(cityPickerConfig.getHotConfig().getTitle());
            }
        }
        mAdapter.addHeadViews(headViews);
        mZCitypickerSideindexbar.addLetters(sideIndexList);
    }

    public LinearLayout getLayoutHeadP() {
        return layoutHeadP;
    }

    public void updateHeadP(String text) {
        layoutHeadP.setVisibility(View.VISIBLE);
        tvHeadP.setText(text);
    }

    public void updateData(String tag, List<CityBean> _listBeans) {
        if (headViews != null && headViews.size() != 0) {
            for (HeadModelConfig _config : headViews) {
                if (TextUtils.equals(_config.getTag(), tag)) {
                    _config.setCityBeans(_listBeans);
                    mAdapter.addHeadViews(headViews);
                    return;
                }
            }
        } else {
            Log.e(TAG, "错误的调用，请检查你是否已经添加了头部view!");
        }
    }

    /**
     * 更新列表数据
     *
     * @param _listBeans
     * @param isALL      是否需要添加头部显示
     */
    public void updateListData(List<CityBean> _listBeans, boolean isALL) {
        if (_listBeans == null || _listBeans.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
        }
        if (mAdapter != null) {
            mAdapter.setmList(_listBeans);
            int _position = layoutManager.findFirstVisibleItemPosition();
            if (_position < 0 || _position >= _listBeans.size()) {
                Log.e(TAG, "无法检索条目!");
            } else {
                if (_listBeans.size() >= _position) {
                    String _index_str = _listBeans.get(_position).getPinyin();
                    mZCitypickerSideindexbar.setLetterChoose(_index_str);
                } else {
                    Log.e(TAG, "无法检索此条目!");
                }
            }
        } else {
            beanList = _listBeans;
        }
        if (isALL) {
            mAdapter.addHeadViews(headViews);
        }
        mAdapter.scrollToSection(mZCitypickerSideindexbar.getmLetters()[0]);
//        mZCitypickerSideindexbar.setLetterChoose("A");
    }

    CommonPickerXInterface pickerXInterface;

    public void setPickerXInterface(CommonPickerXInterface commonPickerXInterface) {
        pickerXInterface = commonPickerXInterface;
    }

    public void setConfigData(CityPickerConfig config) {
        cityPickerConfig = config;
        initData();
        initViews();
    }

    public void hideDialog() {
//        if (getDialog() != null) {
//            getDialog().hide();
//        }
        if (pickerXInterface != null) {
            pickerXInterface.onDismiss();
        }
    }
//
//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        if (pickerXInterface != null) {
//            pickerXInterface.onDismiss();
//        }
//    }
}