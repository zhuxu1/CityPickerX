package com.zhuxu.citypickerz.modules;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuxu.citypickerz.R;
import com.zhuxu.citypickerz.interfaces.CommonCityInterface;
import com.zhuxu.citypickerz.model.CityBean;
import com.zhuxu.citypickerz.model.HeadPlaceBean;
import com.zhuxu.citypickerz.views.CustomHeadViews;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zhuxu.citypickerz.model.CityBean.TYPE_STR_HEAD;

/**
 * craated by Zhuxu _email:332525966@qq.com
 * Creat time : 2020/7/24
 * Description:
 */
public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEWTYPE_HEAD = 88;
    public static final int VIEWTYPE_LIST = 99;

    private Context mContext;
    private List<CityBean> mList;
    private LinearLayoutManager mLayoutManager;
    private List<View> headViewList = new ArrayList<>();

    public CityListAdapter(Context context, List<CityBean> list, LinearLayoutManager _mLayoutManager) {
        this.mContext = context;
        this.mList = list;
        this.mLayoutManager = _mLayoutManager;
    }

    public void setmList(List<CityBean> mList) {
        this.mList = mList;
        if (mList == null || mList.size() == 0) {
            return;
        } else {
            if (!mList.get(0).getType().equals(TYPE_STR_HEAD)) {
                mList.add(0, new HeadPlaceBean());
            }
        }
        notifyDataSetChanged();
    }

    public List<CityBean> getmList() {
        return mList;
    }

    public void addHeadViews(List<View> _headViewList) {
        headViewList = _headViewList;
        if (mList != null && mList.size() > 0) {
            int maxIndex = mList.size() > 3 ? (mList.size() - 1) : (mList.size());
            for (; maxIndex >= 0; maxIndex--) {
                CityBean cityBean = mList.get(maxIndex);
                if (TextUtils.equals(cityBean.getType(), TYPE_STR_HEAD)) {
                    mList.remove(cityBean);
                }
            }
            for (int index = _headViewList.size() - 1; index >= 0; index--) {
                String pinyin = ((CustomHeadViews) _headViewList.get(index)).getConfig().getTitle();
                Log.e("zhuxu", "addHeadViews " + pinyin);
                mList.add(0, new HeadPlaceBean(pinyin));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEWTYPE_HEAD:
                return new HeadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.act_head_view_p, parent, false));
            default:
                return new ListViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_text, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mList.get(position).getType(), TYPE_STR_HEAD)) {
            return VIEWTYPE_HEAD;
        } else {
            return VIEWTYPE_LIST;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            LinearLayout layout = ((HeadViewHolder) holder).layoutPP;
            if (headViewList != null && headViewList.size() > 0) {
                if (layout.getChildCount() != 0) {
                    layout.removeAllViews();
                }
//                for (View view : headViewList) {
//                    layout.addView(view);
//                }
                if (position >= headViewList.size() - 1) {
                    ((HeadViewHolder) holder).spacing.setVisibility(View.VISIBLE);
                } else {
                    ((HeadViewHolder) holder).spacing.setVisibility(View.GONE);
                }
                layout.addView(headViewList.get(position));
            }
        } else {
            String text = mList.get(position).getName();
            final CityBean cityBean = mList.get(position);
            ((ListViewHolder) holder).mTextView.setText(text);
            ((ListViewHolder) holder).mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickInterface != null) {
                        clickInterface.cityResult(cityBean);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 判断position对应的Item是否是组的第一项
     *
     * @param position
     * @return
     */
    public boolean isItemHeader(int position) {
        if (position == 0) {
            return true;
        } else {
            String lastGroupName = mList.get(position - 1).getPinyin();
            String currentGroupName = mList.get(position).getPinyin();
            //判断上一个数据的组别和下一个数据的组别是否一致，如果不一致则是不同组，也就是为第一项（头部）
            if (lastGroupName.equals(currentGroupName)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 获取position对应的Item组名
     *
     * @param position
     * @return
     */
    public String getGroupName(int position) {
        return mList.get(position).getPinyin();
    }

    public boolean isCutomHead(int position) {
        return TextUtils.equals(mList.get(position).getType(), TYPE_STR_HEAD);
    }

    /**
     * 自定义ViewHolder
     */
    class ListViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public ListViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_item_text);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutPP;
        View spacing;

        public HeadViewHolder(View itemView) {
            super(itemView);
            layoutPP = itemView.findViewById(R.id.act_head_view_p_p);
            spacing = itemView.findViewById(R.id.act_head_view_p_p_spacing);
        }
    }

    public void scrollToSectionHead() {
        if (mLayoutManager != null) {
            mLayoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    /**
     * 滚动RecyclerView到索引位置
     *
     * @param index
     */
    public void scrollToSection(String index) {
        if (mList == null || mList.isEmpty()) return;
        if (TextUtils.isEmpty(index)) return;
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(index.substring(0, 1), mList.get(i).getPinyin())) {
                if (mLayoutManager != null) {
                    mLayoutManager.scrollToPositionWithOffset(i, 0);
//                    if (TextUtils.equals(index.substring(0, 1), "定")) {
//                        //防止滚动时进行刷新
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (stateChanged) notifyItemChanged(0);
//                            }
//                        }, 1000);
//                    }
                    return;
                }
            }
        }
    }

    CommonCityInterface clickInterface;

    public void setClickListener(CommonCityInterface clickListener) {
        clickInterface = clickListener;
    }
}
