package com.htu.erhuo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.utils.DialogUtil;
import com.htu.erhuo.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsCreateActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.activity_goods_create)
    LinearLayout activityGoodsCreate;
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_create);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_goods_create);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        // TODO: 2017/3/29  返回放弃发布弹出提示框
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        account = getIntent().getStringExtra("account");

    }
}
