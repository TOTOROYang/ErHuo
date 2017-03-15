package com.htu.erhuo.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.ui.LoginActivity;
import com.htu.erhuo.ui.SetPersonalInfoActivity;
import com.htu.erhuo.utiles.DialogUtil;
import com.htu.erhuo.utiles.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description
 * Created by yzw on 2017/2/27.
 */

public class MeFragment extends Fragment {


    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_bg_avatar)
    RelativeLayout rlBgAvatar;
    @BindView(R.id.btn_my_goods)
    Button btnMyGoods;
    @BindView(R.id.btn_my_favorite)
    Button btnMyFavorite;
    @BindView(R.id.btn_about_erhuo)
    Button btnAboutErhuo;
    @BindView(R.id.btn_exit_or_login)
    Button btnExitOrLogin;

    boolean isLogin;
    String account;
    String name;
    UserInfo mUserInfo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        isLogin = getArguments().getBoolean("isLogin");
        account = getArguments().getString("account");
        name = getArguments().getString("name");
        mUserInfo = EHApplication.getInstance().getUserInfo();
        return view;
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }

    private void init() {
        if (mUserInfo != null) {
            tvName.setText(TextUtils.isEmpty(mUserInfo.getNickName()) ? name : mUserInfo.getNickName());
        }else{
            tvName.setText("游客");
        }
        if (isLogin) {
            btnExitOrLogin.setText("退出账号");
        } else {
            btnExitOrLogin.setText("登录");
        }
    }

    public MeFragment() {
        super();
    }

    @OnClick(R.id.iv_avatar)
    void clickIvAvatar() {
        if (isLogin) {
            Intent intent = new Intent(getContext(), SetPersonalInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("account", account);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "请先登录,才能进入个人设置", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_exit_or_login)
    void clickExitOrLogin() {
        if (isLogin) {
            DialogUtil.showTips(getActivity(),
                    "退出账号",
                    "确定退出吗?",
                    "确定",
                    "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PreferenceUtils.getInstance().setIsLogin(false);
                            PreferenceUtils.getInstance().setUserId("");
                            PreferenceUtils.getInstance().setUserName("");
                            EHApplication.getInstance().setUserInfo(null);
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            getActivity().finish();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            PreferenceUtils.getInstance().setUserId("");
            PreferenceUtils.getInstance().setUserName("");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        }
    }
}
