package com.htu.erhuo.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.adapter.GoodsCreatePictureAdapter;
import com.htu.erhuo.utils.DialogUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class GoodsCreateActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.activity_goods_create)
    LinearLayout activityGoodsCreate;
    @BindView(R.id.tv_goods_description)
    EditText tvGoodsDescription;
    @BindView(R.id.gv_goods_picture)
    GridView gvGoodsPicture;
    @BindView(R.id.btn_goods_create)
    Button btnGoodsCreate;

    private String account;
    private GoodsCreatePictureAdapter goodsCreatePictureAdapter;
    private List<String> picList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_create);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_goods_create);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();

            }
        });
        account = getIntent().getStringExtra("account");

        picList = new ArrayList<>();
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        picList.add("add");
        goodsCreatePictureAdapter = new GoodsCreatePictureAdapter(this);
        goodsCreatePictureAdapter.setData(picList);
        gvGoodsPicture.setAdapter(goodsCreatePictureAdapter);
        goodsCreatePictureAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void showExitDialog() {
        DialogUtil.showTips(mContext,
                "返回",
                "确定放弃发布吗?",
                "确定",
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((BaseActivity) mContext).finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.btn_goods_create)
    void clickGoodsCreate() {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.setItemTitle(tvGoodsDescription.getText().toString());
        itemInfo.setCreator(account);
        BigDecimal bigDecimal = new BigDecimal("6.66");
        itemInfo.setPrice(bigDecimal);
        itemInfo.setSortId("101");
        itemInfo.setPhotoList("123,456,789");

        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(GoodsCreateActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Toast.makeText(GoodsCreateActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(GoodsCreateActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().createGoods(itemInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
