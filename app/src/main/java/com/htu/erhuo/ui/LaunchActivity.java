package com.htu.erhuo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.htu.erhuo.MainActivity;
import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.utiles.DeviceInfoUtil;
import com.htu.erhuo.utiles.PreferenceUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class LaunchActivity extends Activity {

    Handler sHandler;
    private Context mContext;
    private long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_launch);
        mStartTime = System.currentTimeMillis();
        mContext = this;
        sHandler = new Handler();
        Log.d("mac address", DeviceInfoUtil.getWifiMacAddress(this));
        System.out.println("mac address" + DeviceInfoUtil.getWifiMacAddress(this));
        init();
    }

    private void init() {
        String macAddress;
        if (TextUtils.isEmpty(PreferenceUtils.getInstance().getMacAddress())) {
            macAddress = DeviceInfoUtil.getWifiMacAddress(this);
            if (macAddress.equals("")) {
                macAddress = "erhuo";
            }
            PreferenceUtils.getInstance().setMacAddress(macAddress);
        } else {
            macAddress = PreferenceUtils.getInstance().getMacAddress();
        }
        PreferenceUtils.getInstance().setIsLogin(false);
        if (TextUtils.isEmpty(PreferenceUtils.getInstance().getUserId())) {
            gotoLoginActivity();
        } else {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(PreferenceUtils.getInstance().getUserId());
            userInfo.setUserName(PreferenceUtils.getInstance().getUserName());
            userInfo.setImei(macAddress);
            requestServerToLogin(userInfo);
        }
    }

    protected void gotoLoginActivity() {
        long endTime = System.currentTimeMillis();
        long stayTime = 2000;
        long delay = stayTime > endTime - mStartTime ? stayTime - (endTime - mStartTime) : 0;

        sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, delay);
    }

    private void requestServerToLogin(UserInfo userInfo) {
        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                PreferenceUtils.getInstance().setIsLogin(false);
                Toast.makeText(LaunchActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                gotoLoginActivity();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                if (entityResponse.getCode().equals("0")) {
                    Log.d("yzw", entityResponse.toString());
                    PreferenceUtils.getInstance().setIsLogin(true);
                    startActivity(new Intent(mContext, MainActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else {
                    PreferenceUtils.getInstance().setIsLogin(false);
                    Toast.makeText(LaunchActivity.this, "自动登录失败", Toast.LENGTH_SHORT).show();
                    gotoLoginActivity();
                }
            }
        };
        Network.getInstance().login(userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
