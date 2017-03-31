package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.htu.erhuo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ErHuoTeamActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo_team);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_about_team);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
