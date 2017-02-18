package com.htu.erhuo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.htu.erhuo.R;

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
        gotoLoginActivity();
    }

    protected void gotoLoginActivity() {
        long endTime = System.currentTimeMillis();
        long stayTime = 2000;
        long delay = stayTime > endTime - mStartTime ? stayTime - (endTime - mStartTime) : 0;

        sHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(mContext, LoginActivity.class));
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        }, delay);
    }
}
