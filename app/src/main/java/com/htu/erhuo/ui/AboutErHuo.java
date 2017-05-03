package com.htu.erhuo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.htu.erhuo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutErHuo extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.btn_version)
    Button btnVersion;
    @BindView(R.id.btn_team)
    Button btnTeam;
    @BindView(R.id.btn_reply)
    Button btnReply;
    @BindView(R.id.btn_pay)
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_er_huo);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_about_erhuo);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @OnClick(R.id.btn_version)
    void clickVersion() {
        startActivity(new Intent(this, ErHuoVersionActivity.class));
    }

    @OnClick(R.id.btn_team)
    void clickTeam() {
        startActivity(new Intent(this, ErHuoTeamActivity.class));
    }

    @OnClick(R.id.btn_reply)
    void clickReply() {
        startActivity(new Intent(this, ErHuoReplyActivity.class));
    }

    @OnClick(R.id.btn_pay)
    void clickPay(){
        startActivity(new Intent(this, ErHuoPayActivity.class));
    }
}
