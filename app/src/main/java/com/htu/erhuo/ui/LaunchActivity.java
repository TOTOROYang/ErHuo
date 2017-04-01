package com.htu.erhuo.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.htu.erhuo.MainActivity;
import com.htu.erhuo.R;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.ui.callback.BaseActivityPermissionsDispatcher;
import com.htu.erhuo.ui.callback.PermissionHandler;
import com.htu.erhuo.utils.DeviceInfoUtil;
import com.htu.erhuo.utils.PreferenceUtils;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

@RuntimePermissions
public class LaunchActivity extends BaseActivity {

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
        requestPermission();
    }

    private void requestPermission() {
        requestBasePermission(new PermissionHandler() {
            @Override
            public void onGranted() {
                init();
            }

            @Override
            public void onDenied() {
                super.onDenied();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.str_permission));
                builder.setMessage(mContext.getResources().getString(R.string.str_permission_tip));
                builder.setPositiveButton(mContext.getResources().getString(R.string.confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quit();
                            }
                        });
                builder.show();
            }
        });
    }

    private void init() {
        String macAddress;
        if (TextUtils.isEmpty(PreferenceUtils.getInstance().getMacAddress())) {
            macAddress = DeviceInfoUtil.getWifiMacAddress(this);
            if (macAddress.equals("")) {
                macAddress = "erhuoerhuo";
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

    private void quit() {
        finish();
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

    private PermissionHandler mHandler;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BaseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    //-----------------------------------------------------------

    /**
     * 请求相机权限
     *
     * @param permissionHandler
     */
    public void requestCameraPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseActivityPermissionsDispatcher.handleCameraPermissionWithCheck(this);
    }


    @NeedsPermission(Manifest.permission.CAMERA)
    public void handleCameraPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void rationaleCamera(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void deniedCameraPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void OnCameraNeverAskAgain() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    //-----------------------------------------------------------

    /**
     * 请求通讯录权限
     *
     * @param permissionHandler
     */
    public void requestContactsPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseActivityPermissionsDispatcher.handleContactsPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_CONTACTS)
    public void handleContactsPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.READ_CONTACTS)
    public void rationaleContacts(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.READ_CONTACTS)
    public void deniedContactsPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.READ_CONTACTS)
    public void OnContactsNeverAskAgain() {
        if (mHandler != null)
            mHandler.onDenied();
    }
    //-----------------------------------------------------------

    /**
     * 请求sd卡权限
     *
     * @param permissionHandler
     */
    public void requestBasePermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseActivityPermissionsDispatcher.handleBasePermissionWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    public void handleBasePermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    public void rationaleBasePermission(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    public void deniedBasePermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE})
    public void onBaseNeverAskAgain() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    //-----------------------------------------------------------

    /**
     * 请求麦克风权限
     *
     * @param permissionHandler
     */
    public void requestRecordPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseActivityPermissionsDispatcher.handleRecordPermissionWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void handleRecordPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    public void rationaleRecordPermission(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    public void deniedRecordPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain(Manifest.permission.RECORD_AUDIO)
    public void onRecordNeverAskAgain() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    //-----------------------------------------------------------

    /**
     * 请求定位权限
     *
     * @param permissionHandler
     */
    public void requestLocationPermission(PermissionHandler permissionHandler) {
        this.mHandler = permissionHandler;
        BaseActivityPermissionsDispatcher.handleLocationPermissionWithCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    public void handleLocationPermission() {
        if (mHandler != null)
            mHandler.onGranted();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    public void rationaleLocationPermission(final PermissionRequest request) {
        request.proceed();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    public void deniedLocationPermission() {
        if (mHandler != null)
            mHandler.onDenied();
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    public void onLocationNeverAskAgain() {
        if (mHandler != null)
            mHandler.onDenied();
    }

}
