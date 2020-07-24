package com.zhuxu.citypickerz.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuxu.citypickerz.R;
import com.zhuxu.citypickerz.utils.CityPickerXUtils;

import androidx.annotation.Nullable;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/23
 * Description: 搜索部分view
 */
public class SearchView extends BaseSearchView {
    public SearchView(Context context) {
        super(context);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ImageView btn_close, btn_clear, img_icon;
    private EditText edt_search;
    private LinearLayout layout_back;
    private TextView btn_reset;

    private View rootView;

    private void init() {
        findViews();
        initListener();
        initViews();
    }

    private void initViews() {

    }

    private void initListener() {
        btn_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeListener != null) {
                    closeListener.result(true);
                }
            }
        });
        btn_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
            }
        });
        btn_reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                edt_search.clearFocus();
                CityPickerXUtils.hideKeyboard(edt_search);
                if (resetListener != null) {
                    resetListener.result(true);
                }
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    btn_clear.setVisibility(VISIBLE);
                    btn_reset.setVisibility(VISIBLE);
                    if (searchListener != null) {
                        searchListener.result(s.toString());
                    }
                } else {
                    btn_clear.setVisibility(GONE);
                    btn_reset.setVisibility(GONE);
                }
            }
        });
    }

    private void findViews() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.act_search_view, this);
        btn_close = rootView.findViewById(R.id.x_citypicker_search_close);
        btn_clear = rootView.findViewById(R.id.x_citypicker_search_clear);
        img_icon = rootView.findViewById(R.id.x_citypicker_search_icon);
        edt_search = rootView.findViewById(R.id.x_citypicker_search_edt);
        layout_back = rootView.findViewById(R.id.x_citypicker_search_p);
        btn_reset = rootView.findViewById(R.id.x_citypicker_search_reset);
    }
}
