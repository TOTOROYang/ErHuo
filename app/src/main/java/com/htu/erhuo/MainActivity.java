package com.htu.erhuo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.gson.Gson;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.BaseActivity;
import com.htu.erhuo.ui.adapter.MyViewPagerAdapter;
import com.htu.erhuo.ui.fragment.MyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        test();
        initUiAndData();
    }

    /** *
     初始化话界面和数据
     */
    private void initUiAndData() {
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
        //初始化ViewPager的 Aapter 代码会在后面贴
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        //为Adapter添加Aapter和标题
        adapter.addFragment(new MyFragment(),"最新");
        adapter.addFragment(new MyFragment(),"价格");
        adapter.addFragment(new MyFragment(),"喜欢");
        //为ViewPager绑定Adapter
        viewPage.setAdapter(adapter);
        //为TabLayout添加标签，注意这里我们传入了标签名称，但demo运行时显示的标签名称并不是我们添加的，那么为什么呢？卖个官子...
        tabLayout.addTab(tabLayout.newTab().setText("one_"));
        tabLayout.addTab(tabLayout.newTab().setText("two_"));
        tabLayout.addTab(tabLayout.newTab().setText("three_"));
        tabLayout.addTab(tabLayout.newTab().setText("three_"));
        //给tabLayout设置ViewPage，如果设置关联了ViewPage，那么ViewPagAdapter中getPageTitle返回的就是Tab上标题(上面疑问的回答)
        //为ViewPager 和TableLayout进行绑定，从而实现滑动标签切换Fragment的目的
        tabLayout.setupWithViewPager(viewPage);
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
