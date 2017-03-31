package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.htu.erhuo.R;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.ui.adapter.GoodsCreatePictureAdapter;
import com.htu.erhuo.ui.adapter.GoodsDetailPictureAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.tv_goods_detail_title)
    TextView tvGoodsDetailTitle;
    @BindView(R.id.tv_goods_detail_price)
    TextView tvGoodsDetailPrice;
    @BindView(R.id.tv_goods_detail_des)
    TextView tvGoodsDetailDes;
    @BindView(R.id.gv_goods_detail_photo)
    GridView gvGoodsDetailPhoto;

    String goodsId;
    private String account;
    private GoodsDetailPictureAdapter adapter;
    private List<String> picList;
    ItemInfo itemInfo = new ItemInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_goods_detail);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        goodsId = getIntent().getStringExtra("goodsId");
        Log.d("GoodsDetailActivity", goodsId);
        initViewAndData();
    }

    private void initViewAndData() {
        tvGoodsDetailTitle.setFocusable(true);
        tvGoodsDetailTitle.setFocusableInTouchMode(true);
        tvGoodsDetailTitle.requestFocus();

        picList = new ArrayList<>();
        picList.add("1308424017_1490792612878.webp");
        picList.add("1308424017_1490792612878.webp");
        picList.add("1308424017_1490792612878.webp");

        adapter = new GoodsDetailPictureAdapter(this);
        adapter.setData(picList);
        gvGoodsDetailPhoto.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gvGoodsDetailPhoto.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
