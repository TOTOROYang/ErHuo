package com.htu.erhuo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.htu.erhuo.MainActivity;
import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.UserContact;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class SetPersonalInfoActivity extends BaseActivity {

    private final static int REQUEST_SET_AVATAR = 0;
    private final static int REQUEST_SET_NAME = 1;
    private final static int REQUEST_SET_SEX = 2;
    private final static int REQUEST_SET_SIGN = 3;
    private final static int REQUEST_SET_PHONE = 4;
    private final static int REQUEST_SET_WECHAT = 5;
    private final static int REQUEST_SET_QQ = 6;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.activity_set_personal_info)
    LinearLayout activitySetPersonalInfo;
    @BindView(R.id.rl_set_avatar)
    RelativeLayout rlSetAvatar;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.rl_set_name)
    RelativeLayout rlSetName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_set_sex)
    RelativeLayout rlSetSex;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.rl_set_sign)
    RelativeLayout rlSetSign;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.rl_set_phone)
    RelativeLayout rlSetPhone;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.rl_set_wechat)
    RelativeLayout rlSetWechat;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.rl_set_qq)
    RelativeLayout rlSetQq;

    String account;
    UserInfo mUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_personal_info);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_personal_info);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSupportActionBar(toolBar);

        account = getIntent().getStringExtra("account");
        mUserInfo = (UserInfo) getIntent().getSerializableExtra("userInfo");
        init();
    }

    private void init() {
        if (!TextUtils.isEmpty(mUserInfo.getNickName())) {
            tvNickName.setText(mUserInfo.getNickName());
        }
        if (mUserInfo.getSex() != null) {
            tvSex.setText(mUserInfo.getSex() == 0 ? "女" : "男");
        }
        if (!TextUtils.isEmpty(mUserInfo.getSignature())) {
            tvSign.setText(mUserInfo.getSignature());
        }
        if (mUserInfo.getUserContact() != null) {
            UserContact userContact = mUserInfo.getUserContact();
            if (!TextUtils.isEmpty(userContact.getMobile())) {
                tvPhone.setText(userContact.getMobile());
            }
            if (!TextUtils.isEmpty(userContact.getWechat())) {
                tvWechat.setText(userContact.getWechat());
            }
            if (!TextUtils.isEmpty(userContact.getQq())) {
                tvQq.setText(userContact.getQq());
            }
        }
    }

    @OnClick({R.id.rl_set_avatar,
            R.id.rl_set_name,
            R.id.rl_set_sex,
            R.id.rl_set_sign,
            R.id.rl_set_phone,
            R.id.rl_set_wechat,
            R.id.rl_set_qq})
    void click(View v) {
        switch (v.getId()) {
            case R.id.rl_set_avatar:
                break;
            case R.id.rl_set_name:
                Intent intent = new Intent(this, SetInfoActivity.class);
                intent.putExtra("title", "设置昵称");
                startActivityForResult(intent, REQUEST_SET_NAME);
                break;
            case R.id.rl_set_sex:
                break;
            case R.id.rl_set_sign:
                break;
            case R.id.rl_set_phone:
                break;
            case R.id.rl_set_wechat:
                break;
            case R.id.rl_set_qq:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SET_NAME) {
            if (resultCode == 0) {
                String name = data.getStringExtra("result");
                mUserInfo.setNickName(name);
                setUserInfo(account, mUserInfo);
            }
        }
    }

    private void setUserInfo(String account, UserInfo userInfo) {
        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SetPersonalInfoActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Log.d("yzw", "success");
                    init();
                } else {
                    Toast.makeText(SetPersonalInfoActivity.this, "设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().setUserInfo(account, userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
