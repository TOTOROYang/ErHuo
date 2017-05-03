package com.htu.erhuo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.Feedback;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class ErHuoReplyActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.btn_reply)
    Button btnReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_huo_reply);
        ButterKnife.bind(this);
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle(R.string.str_about_reply);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_36dp);
        setSupportActionBar(toolBar);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.btn_reply)
    void clickBtnReply() {
        if (TextUtils.isEmpty(etReply.getText().toString())) {
            Toast.makeText(ErHuoReplyActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Feedback feedback = new Feedback();
        feedback.setFbContent(etReply.getText().toString());
        feedback.setFbType("9");
        if (PreferenceUtils.getInstance().getIsLogin()) {
            feedback.setUserId(PreferenceUtils.getInstance().getUserId());
        }

        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Toast.makeText(ErHuoReplyActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Toast.makeText(ErHuoReplyActivity.this, "感谢你的反馈", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ErHuoReplyActivity.this, "请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().feedBack(feedback).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
