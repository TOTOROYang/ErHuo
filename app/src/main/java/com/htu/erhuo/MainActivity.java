package com.htu.erhuo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.BaseActivity;
import com.htu.erhuo.ui.GoodsCreateActivity;
import com.htu.erhuo.ui.adapter.MyViewPagerAdapter;
import com.htu.erhuo.ui.fragment.MainGoodsFragment;
import com.htu.erhuo.ui.fragment.MeFragment;
import com.htu.erhuo.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

import static com.htu.erhuo.entity.enums.ItemRankEnum.PRICE_ASC;
import static com.htu.erhuo.entity.enums.ItemRankEnum.STAR_DESC;

public class MainActivity extends BaseActivity {

    private final static int REQUEST_CREATE_GOODS = 0;

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
    MainGoodsFragment mainGoodsFragment;
    MainGoodsFragment mainGoodsFragment2;
    MainGoodsFragment mainGoodsFragment3;

    boolean isLogin;
    String account;
    String name;
    UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUiAndData();
    }

    /**
     * 初始化话界面和数据
     */
    private void initUiAndData() {
        toolBar.setTitle("");
        toolBar.setLogo(R.drawable.toolbar_logo);
        setSupportActionBar(toolBar);

        EHApplication.getInstance().initOss();
        if (PreferenceUtils.getInstance().getIsLogin()) {
            isLogin = true;
            account = PreferenceUtils.getInstance().getUserId();
            name = PreferenceUtils.getInstance().getUserName();
            getUserInfo(account);
        }

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mainGoodsFragment = new MainGoodsFragment();
        mainGoodsFragment2 = new MainGoodsFragment();
        mainGoodsFragment3 = new MainGoodsFragment();
        meFragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogin", isLogin);
        bundle.putString("account", account);
        bundle.putString("name", name);
        mainGoodsFragment.setArguments(bundle);
        mainGoodsFragment2.setArguments(bundle);
        mainGoodsFragment3.setArguments(bundle);
        mainGoodsFragment.setRule(null);
        mainGoodsFragment2.setRule(PRICE_ASC.getCode());
        mainGoodsFragment3.setRule(STAR_DESC.getCode());
        meFragment.setArguments(bundle);

        adapter.addFragment(mainGoodsFragment, "最新");
        adapter.addFragment(mainGoodsFragment2, "价格");
        adapter.addFragment(mainGoodsFragment3, "喜欢");
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
                Intent intent = new Intent(this, GoodsCreateActivity.class);
                intent.putExtra("account", account);
                startActivityForResult(intent, REQUEST_CREATE_GOODS);
                break;
            case R.id.iv_main:
                ivMain.setImageResource(R.drawable.icon_main);
                ivMe.setImageResource(R.drawable.icon_me_unpress);
                showGoods();
                break;
            case R.id.iv_me:
                showMe();
                ivMain.setImageResource(R.drawable.icon_main_unpress);
                ivMe.setImageResource(R.drawable.icon_me);
                break;
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
                    EHApplication.getInstance().setUserInfo(mUserInfo);
                    Log.d("yzw", userInfo.toString());
                } else {
                    Toast.makeText(MainActivity.this, "请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().getUserInfo(account).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_GOODS) {
            if (resultCode == 0) {
                mainGoodsFragment.refreshAfterCreateGoods();
            }
        }
    }
}
