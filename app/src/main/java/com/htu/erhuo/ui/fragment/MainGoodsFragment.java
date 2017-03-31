package com.htu.erhuo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.entity.ItemQueryCondition;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.GoodsDetailActivity;
import com.htu.erhuo.ui.adapter.GoodsListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Description
 * Created by yzw on 2017/3/30.
 */

public class MainGoodsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    boolean isLogin;
    String account;
    String name;
    UserInfo mUserInfo;
    List<ItemInfo> itemInfos;
    int page;
    int count = -1;
    @BindView(R.id.sl_refresh)
    SwipeRefreshLayout slRefresh;
    private ItemQueryCondition itemQueryCondition;
    private GoodsListAdapter adapter;
    private String rule;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        ButterKnife.bind(this, view);

        isLogin = getArguments().getBoolean("isLogin");
        account = getArguments().getString("account");
        name = getArguments().getString("name");
        mUserInfo = EHApplication.getInstance().getUserInfo();

        initUIAndData();
        return view;
    }


    private void initUIAndData() {
        slRefresh.setOnRefreshListener(this);
        slRefresh.setColorSchemeResources(R.color.colorPrimary);
        page = 1;
        itemQueryCondition = new ItemQueryCondition.Builder().page(page).rule(rule).build();
        adapter = new GoodsListAdapter(R.layout.recycler_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this, recyclerView);
        adapter.setEnableLoadMore(true);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ItemInfo itemInfo = (ItemInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("goodsId", itemInfo.getId().toString());
                startActivity(intent);
            }
        });
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

    public void refreshAfterCreateGoods() {
        refreshData();
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
