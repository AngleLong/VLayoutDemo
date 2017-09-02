package com.jinlong.vlayoutdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jinlong.vlayoutdemo.ItemHolder;
import com.jinlong.vlayoutdemo.R;

/**
 * 作者：贺金龙*
 * 创建时间：2017/9/1 19:11*
 * 类描述：* 线性的适配器
 * 修改人：*
 * 修改内容:*
 * 修改时间：*
 */

public class BaseItemsAdapter extends DelegateAdapter.Adapter<ItemHolder> {

    private Context mContext;
    private LayoutHelper mHelper;
    private String mColor;/*这个颜色值主要是为了区分样式的*/
    private int mCount;

    public BaseItemsAdapter(Context context, LayoutHelper helper, String color, int count) {
        mContext = context;
        mHelper = helper;
        this.mColor = color;
        this.mCount = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mHelper;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.items_linear, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.parseColor(mColor));
        holder.mTvContent.setText("这是基类的布局" + position);
    }

    @Override
    public int getItemCount() {
        return mCount;
    }
}
