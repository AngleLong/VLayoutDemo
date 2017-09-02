package com.jinlong.vlayoutdemo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * 作者：贺金龙*
 * 创建时间：2017/9/1 23:13*
 * 类描述：*
 * 修改人：*
 * 修改内容:*
 * 修改时间：*
 */

public class ItemHolder extends RecyclerView.ViewHolder {


    public TextView mTvContent;

    public ItemHolder(View itemView) {
        super(itemView);

        mTvContent = itemView.findViewById(R.id.tv_content);
    }
}
