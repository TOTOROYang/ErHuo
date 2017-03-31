package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.adapter.GoodsDetailPictureAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

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
    private GoodsDetailPictureAdapter adapter;
    private List<String> picList;
    ItemInfo itemInfo;

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
        adapter = new GoodsDetailPictureAdapter(this);
        adapter.setData(picList);
        gvGoodsDetailPhoto.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gvGoodsDetailPhoto.setOnItemClickListener(this);

        getGoodsDetail();
    }

    private void getGoodsDetail() {
        Subscriber<EntityResponse<ItemInfo>> subscriber = new Subscriber<EntityResponse<ItemInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(mContext, "商品详情获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse<ItemInfo> itemInfoEntityResponse) {
                if (itemInfoEntityResponse.getCode().equals("0")) {
                    itemInfo = itemInfoEntityResponse.getMsg();
                    showGoodsDetail();
                } else {
                    Toast.makeText(mContext, "商品详情获取失败", Toast.LENGTH_SHORT).show();

                }
            }
        };
        Network.getInstance().getGoodsDetail(goodsId).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private void showGoodsDetail() {
        tvGoodsDetailTitle.setText(itemInfo.getItemTitle());
        tvGoodsDetailDes.setText(itemInfo.getItemDesc());
        tvGoodsDetailPrice.setText(String.format(Locale.CHINESE, "￥%.2f", itemInfo.getPrice()));
        String[] photoListString = itemInfo.getPhotoList().split(",");
        picList.addAll(Arrays.asList(photoListString).subList(1, photoListString.length));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
