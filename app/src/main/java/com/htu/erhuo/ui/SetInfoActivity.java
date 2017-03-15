package com.htu.erhuo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.utiles.DeviceInfoUtil;

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

    int type;
    UserInfo mUserInfo;

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
        type = getIntent().getIntExtra("type", -1);
        mUserInfo = EHApplication.getInstance().getUserInfo();
        init();
    }

    private void init() {
        if (type == -1) return;
        switch (type) {
            case SetPersonalInfoActivity.REQUEST_SET_NAME:
                etText.setInputType(InputType.TYPE_CLASS_TEXT);
                etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                etText.setMaxLines(1);
                etText.setText(mUserInfo.getNickName() == null ? "" : mUserInfo.getNickName());
                break;
            case SetPersonalInfoActivity.REQUEST_SET_SIGN:
                etText.setInputType(InputType.TYPE_CLASS_TEXT);
                etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
                etText.setMaxLines(5);
                etText.setHeight(DeviceInfoUtil.dip2px(150));
                etText.setText(mUserInfo.getSignature() == null ? "" : mUserInfo.getSignature());
                break;
            case SetPersonalInfoActivity.REQUEST_SET_PHONE:
                etText.setInputType(InputType.TYPE_CLASS_NUMBER);
                etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                etText.setMaxLines(1);
                etText.setText((mUserInfo.getUserContact() == null || mUserInfo.getUserContact().getMobile() == null) ?
                        "" : mUserInfo.getUserContact().getMobile());
                break;
            case SetPersonalInfoActivity.REQUEST_SET_WECHAT:
                etText.setInputType(InputType.TYPE_CLASS_TEXT);
                etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                etText.setMaxLines(1);
                etText.setText((mUserInfo.getUserContact() == null || mUserInfo.getUserContact().getWechat() == null) ?
                        "" : mUserInfo.getUserContact().getWechat());
                break;
            case SetPersonalInfoActivity.REQUEST_SET_QQ:
                etText.setInputType(InputType.TYPE_CLASS_NUMBER);
                etText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                etText.setMaxLines(1);
                etText.setText((mUserInfo.getUserContact() == null || mUserInfo.getUserContact().getQq() == null) ?
                        "" : mUserInfo.getUserContact().getQq());
                break;
        }
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
