package com.jinlong.vlayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.FixLayoutHelper;
import com.alibaba.android.vlayout.layout.FloatLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.jinlong.vlayoutdemo.adapter.BaseItemsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于展示VLayout的Demo演示
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycleView = (RecyclerView) findViewById(R.id.recycleView);

        /*创建LayoutManager*/
        VirtualLayoutManager manager = new VirtualLayoutManager(this);
        mRecycleView.setLayoutManager(manager);

        /*创建管理所有Adapter的DelegateAdapter对象*/
        DelegateAdapter delegateAdapter = new DelegateAdapter(manager, true);

        /*创建一个集合管理所有的Adapter适配器*/
        List<DelegateAdapter.Adapter> adapters = new ArrayList<>();

        /*添加一种gridView的布局*/
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        /*添加一个Margin*/
        gridLayoutHelper.setMargin(20, 20, 20, 20);
        adapters.add(new BaseItemsAdapter(this, gridLayoutHelper, "#112233", 5));

        /*添加一个固定的布局，这个应该是指定布局的*/
        adapters.add(new BaseItemsAdapter(this, new FixLayoutHelper(200, 500), "#667788", 1));

        /*添加一个ScrollFixLayoutHelper布局*/
        adapters.add(new BaseItemsAdapter(this, new ScrollFixLayoutHelper(500, 800), "#00ff00", 1));

        /*添加一个浮动布局*/
        FloatLayoutHelper layoutHelper = new FloatLayoutHelper();
        layoutHelper.setAlignType(FixLayoutHelper.BOTTOM_RIGHT);
        layoutHelper.setDefaultLocation(100, 400);
        adapters.add(new BaseItemsAdapter(this, layoutHelper, "#000000", 1));

        /*添加一个吸顶或者吸低布局*/
        adapters.add(new BaseItemsAdapter(this, new StickyLayoutHelper(true), "#88ff88", 1));

         /*添加一种线性布局*/
        adapters.add(new BaseItemsAdapter(this, new LinearLayoutHelper(20), "#332211", 10));

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecycleView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        /*添加了所有的适配器之后添加到*/
        delegateAdapter.addAdapters(adapters);

        /*设置recycleView*/
        mRecycleView.setAdapter(delegateAdapter);
    }
}
