package com.htu.erhuo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.htu.erhuo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetInfoActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.et_text)
    EditText etText;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.activity_set_info)
    LinearLayout activitySetInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(getIntent().getStringExtra("title"));
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolBar);
        setResult(1);
    }

    @OnClick(R.id.btn_confirm)
    void click() {
        if (etText.getText().toString().length() == 0) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("result", etText.getText().toString());
            setResult(0, intent);
            finish();
        }
    }
}
