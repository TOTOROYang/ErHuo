package com.htu.erhuo.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htu.erhuo.MainActivity;
import com.htu.erhuo.R;
import com.htu.erhuo.ui.LoginActivity;
import com.htu.erhuo.ui.SetPersonalInfoActivity;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public MeFragment() {
        super();
    }

    @OnClick(R.id.iv_avatar)
    void clickIvAvatar() {
        startActivity(new Intent(getContext(), SetPersonalInfoActivity.class));
    }

    @OnClick(R.id.btn_exit_or_login)
    void clickExitOrLogin() {
        PreferenceUtils.getInstance().setUserId("");
        PreferenceUtils.getInstance().setUserName("");
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
