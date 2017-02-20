package com.htu.erhuo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.BaseActivity;
import com.htu.erhuo.ui.adapter.MyViewPagerAdapter;
import com.htu.erhuo.ui.fragment.MyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.activity_main)
    CoordinatorLayout activityMain;
    @BindView(R.id.fab_create)
    FloatingActionButton fabCreate;
    @BindView(R.id.iv_main)
    ImageView ivMain;
    @BindView(R.id.iv_me)
    ImageView ivMe;


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
        viewPage.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPage);

    }

    @OnClick({R.id.fab_create
            , R.id.iv_main
            , R.id.iv_me
    })
    void clickFabCreate(View v) {
        switch (v.getId()) {
            case R.id.fab_create:
                Log.d("yzw", "create");
                break;
            case R.id.iv_main:
                Log.d("yzw", "main");
                break;
            case R.id.iv_me:
                Log.d("yzw", "me");
                break;
        }
    }


    private void test() {

        Subscriber<MovieEntity> subscriber = new Subscriber<MovieEntity>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(MovieEntity movieEntity) {
                Gson gson = new Gson();
                Log.d("yzw", gson.toJson(movieEntity));
            }
        };
        Network.getInstance().getTopMovie(1, 20).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
