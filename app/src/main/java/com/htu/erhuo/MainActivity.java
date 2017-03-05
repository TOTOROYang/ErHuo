package com.htu.erhuo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.BaseActivity;
import com.htu.erhuo.ui.LoginActivity;
import com.htu.erhuo.ui.adapter.MyViewPagerAdapter;
import com.htu.erhuo.ui.fragment.MeFragment;
import com.htu.erhuo.ui.fragment.MyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp_goods)
    ViewPager vpGoods;
    @BindView(R.id.rl_me)
    RelativeLayout rlMe;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;
    @BindView(R.id.fab_create)
    FloatingActionButton fabCreate;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    MeFragment meFragment;

    UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        test();
        initUiAndData();
    }

    /**
     * 初始化话界面和数据
     */
    private void initUiAndData() {
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyFragment(), "最新");
        adapter.addFragment(new MyFragment(), "价格");
        adapter.addFragment(new MyFragment(), "喜欢");
        meFragment = new MeFragment();
        vpGoods.setAdapter(adapter);
        tabLayout.setupWithViewPager(vpGoods);

    }

    @OnClick({R.id.fab_create
            , R.id.iv_main
            , R.id.iv_me
    })
    void clickFabCreate(View v) {
        switch (v.getId()) {
            case R.id.fab_create:
                Log.d("yzw", "create");
                Toast.makeText(this, "发布", Toast.LENGTH_SHORT).show();
//                getUserInfo("1308424017");
                break;
            case R.id.iv_main:
                Log.d("yzw", "main");
                showGoods();
                break;
            case R.id.iv_me:
                showMe();
//                test();
                break;
        }
    }

    private void test() {
        if (mUserInfo != null) {
            mUserInfo.setNickName("totoro");
            setUserInfo("1308424017", mUserInfo);
        }
    }

    private void getUserInfo(String account) {
        Subscriber<EntityResponse<UserInfo>> subscriber = new Subscriber<EntityResponse<UserInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse<UserInfo> entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    UserInfo userInfo = entityResponse.getMsg();
                    mUserInfo = userInfo;
                    Log.d("yzw", userInfo.toString());
                } else {
                    Toast.makeText(MainActivity.this, "请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().getUserInfo(account).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private void setUserInfo(String account, UserInfo userInfo) {
        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Log.d("yzw", "success");
                } else {
                    Toast.makeText(MainActivity.this, "请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().setUserInfo(account, userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private void showGoods() {
        toolBar.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        vpGoods.setVisibility(View.VISIBLE);
        rlMe.setVisibility(View.GONE);
        setSupportActionBar(toolBar);
    }

    private void showMe() {
        toolBar.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        vpGoods.setVisibility(View.GONE);
        rlMe.setVisibility(View.VISIBLE);
        if (!meFragment.isAdded())
            getSupportFragmentManager().beginTransaction().add(R.id.rl_me, meFragment).commit();
    }

}
