package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.entity.ItemQueryCondition;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.adapter.GoodsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MyGoodsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.sl_refresh)
    SwipeRefreshLayout slRefresh;

    boolean isLogin;
    String account;
    String name;
    UserInfo mUserInfo;
    List<ItemInfo> itemInfos;
    int page;
    int count = -1;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    private ItemQueryCondition itemQueryCondition;
    private GoodsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_my_goods);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isLogin = getIntent().getBooleanExtra("isLogin", false);
        account = getIntent().getStringExtra("account");
        name = getIntent().getStringExtra("name");
        mUserInfo = EHApplication.getInstance().getUserInfo();

        initUIAndData();
    }

    private void initUIAndData() {
        slRefresh.setOnRefreshListener(this);
        slRefresh.setColorSchemeResources(R.color.colorPrimary);
        page = 1;
        itemQueryCondition = new ItemQueryCondition.Builder().page(page).creator(account).build();
        adapter = new GoodsListAdapter(R.layout.recycler_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setEnableLoadMore(true);
        getItemList();
    }

    private void getItemList() {
        Subscriber<EntityResponse<List<ItemInfo>>> subscriber = new Subscriber<EntityResponse<List<ItemInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                itemInfos = new ArrayList<>();
                adapter.setNewData(itemInfos);
            }

            @Override
            public void onNext(EntityResponse<List<ItemInfo>> listEntityResponse) {
                if (listEntityResponse.getCode().equals("0"))
                    if (listEntityResponse.getMsg() != null) {
                        if (itemInfos == null) itemInfos = new ArrayList<>();
                        itemInfos.addAll(listEntityResponse.getMsg());
                        count = listEntityResponse.getMsg().size();
                    } else {
                        count = 0;
                    }
                else {
                    itemInfos = new ArrayList<>();
                    count = 0;
                }
                adapter.setNewData(itemInfos);
                adapter.notifyDataSetChanged();
                slRefresh.setRefreshing(false);
                adapter.loadMoreComplete();
            }
        };
        Network.getInstance().getGoodsList(itemQueryCondition).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        page = 1;
        itemQueryCondition.page = page;
        itemInfos = null;
        getItemList();
    }

    @Override
    public void onLoadMoreRequested() {
        loadMoreData();
    }

    private void loadMoreData() {
        if (count == -1 || count == 5) {
            itemQueryCondition.page = ++page;
            getItemList();
        } else {
            adapter.loadMoreEnd();
        }
    }
}
