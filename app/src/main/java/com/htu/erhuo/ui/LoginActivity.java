package com.htu.erhuo.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.htu.erhuo.MainActivity;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.utiles.DeviceInfoUtil;
import com.htu.erhuo.utiles.DialogUtil;
import com.htu.erhuo.utiles.PreferenceUtils;
import com.htu.erhuo.utiles.SoftKeyBoardListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class LoginActivity extends BaseActivity {

    private static final int WEB_PAGE_URL = 0;
    private static final int WEB_PAGE_GET_CHECK_IMAGE = 1;
    private static final int WEB_PAGE_LOGIN = 2;

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.web_jw)
    WebView webJw;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    @BindView(R.id.iv_verification)
    ImageView ivVerification;
    @BindView(R.id.et_verification)
    EditText etVerification;
    @BindView(R.id.iv_forget_password)
    ImageView ivForgetPassword;
    @BindView(R.id.iv_input)
    ImageView ivInput;
    @BindView(R.id.btn_login_skip)
    Button btnLoginSkip;

    String jsGetCheckImage;
    String jsLogin;
    String jsGetName;
    int webViewPage;

    String account = "";
    String password = "";
    String verify = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        ButterKnife.bind(this);
        jsGetCheckImage = "javascript:" + EHApplication.JAVASCRIPT_GET_CHECK_IMAGE;
        jsLogin = "javascript:" + EHApplication.JAVASCRIPT_LOGIN;
        jsGetName = "javascript:" + EHApplication.JAVASCRIPT_GET_NAME;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initVerification();
        etAccount.postDelayed(new Runnable() {
            @Override
            public void run() {
                setKeyBoardListener();
            }
        }, 300);
    }

    private void setKeyBoardListener() {
        SoftKeyBoardListener.setListener(LoginActivity.this,
                new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
                    @Override
                    public void keyBoardShow(int height) {
                        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) ivInput.getLayoutParams();
                        int a[] = new int[2];
                        btnLogin.getLocationInWindow(a);
                        int beforeHeight = DeviceInfoUtil.getHeight(mContext) - (a[1] + DeviceInfoUtil.dip2px(48));
                        param.setMargins(0, DeviceInfoUtil.dip2px(67) + beforeHeight - height, 0, 0);
                        ivInput.setLayoutParams(param);
                        Log.d("LoginActivity", height + "");
                    }

                    @Override
                    public void keyBoardHide(int height) {
                        RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) ivInput.getLayoutParams();
                        param.setMargins(0, DeviceInfoUtil.dip2px(67), 0, 0);
                        ivInput.setLayoutParams(param);
                    }
                });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initVerification() {
        webJw.getSettings().setJavaScriptEnabled(true);
        webJw.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                switch (webViewPage) {
                    case WEB_PAGE_URL:
                        webJw.loadUrl(jsLogin);
                        webJw.loadUrl(jsGetCheckImage);
                        webJw.loadUrl("javascript:getCheckImg()");
                        break;
                    case WEB_PAGE_GET_CHECK_IMAGE:
                        break;
                    case WEB_PAGE_LOGIN:
                        webJw.loadUrl(jsGetName);
                        webJw.loadUrl("javascript:getName()");
                        break;
                }
            }
        });
        webJw.addJavascriptInterface(
                new Object() {
                    @JavascriptInterface
                    public void imgData(final String data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initVerifyImage(data);
                            }
                        });
                    }

                    @JavascriptInterface
                    public void name(final String name) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String dex;
                                if (name != null && name.length() > 0) {
                                    requestServerToLogin(etAccount.getText().toString(), getRealName(name));
                                } else {
                                    hideLoadingDialog();
                                    dex = "登陆失败";
                                    DialogUtil.showTips(LoginActivity.this,
                                            "登陆状态",
                                            dex,
                                            "确定",
                                            new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    PreferenceUtils.getInstance().setIsLogin(false);
                                                    etVerification.setText("");
                                                    initVerification();
                                                    showLoadingDialog();
                                                }
                                            });
                                }
                            }
                        });
                    }
                }
                , "android");
        webViewPage = WEB_PAGE_URL;
        webJw.loadUrl(EHApplication.HTU_JW_URL);
    }

    private void initVerifyImage(String data) {
        byte[] imgData = Base64.decode(data, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgData, 0, imgData.length);
        ivVerification.setImageBitmap(bitmap);
        ivVerification.setVisibility(View.VISIBLE);
        hideLoadingDialog();
    }

    @OnClick(R.id.btn_login)
    public void clickBtnLogin() {
        if (checkInput()) {
            showLoadingDialog("正在登录");
            account = etAccount.getText().toString();
            password = etPassword.getText().toString();
            verify = etVerification.getText().toString();
            webViewPage = WEB_PAGE_LOGIN;
            webJw.loadUrl("javascript:test('" + account + "','" + password + "','" + verify + "')");
        }
    }

    private boolean checkInput() {
        if (etAccount.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入学号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etPassword.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etVerification.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_login_skip)
    public void clickBtnLoginSkip() {
        DialogUtil.showTips(LoginActivity.this,
                "登陆状态",
                "游客登录成功",
                "确定",
                new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        startActivity(new Intent(mContext, MainActivity.class));
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    }
                });
    }


    @OnClick(R.id.iv_forget_password)
    public void clickIvForgetPassword() {
        DialogUtil.showTips(LoginActivity.this,
                "忘记密码",
                "此处为教务系统登录的账号密码，如果遗忘请登录jw.htu.cn/jwweb找回\n");
    }

    private String getRealName(String name) {
        return name.split("]")[1];
    }

    private void requestServerToLogin(String account, String name) {
        final UserInfo userInfo = new UserInfo();
        userInfo.setUserId(account);
        userInfo.setUserName(name);
        userInfo.setImei(PreferenceUtils.getInstance().getMacAddress());

        Subscriber<EntityResponse> subscriber = new Subscriber<EntityResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                hideLoadingDialog();
                PreferenceUtils.getInstance().setIsLogin(false);
                initVerification();
                Toast.makeText(LoginActivity.this, "请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(EntityResponse entityResponse) {
                hideLoadingDialog();
                if (entityResponse.getCode().equals("0")) {
                    if (!TextUtils.isEmpty(userInfo.getUserId())) {
                        PreferenceUtils.getInstance().setUserId(userInfo.getUserId());
                        PreferenceUtils.getInstance().setUserName(userInfo.getUserName());
                        PreferenceUtils.getInstance().setIsLogin(true);
                    }
                    startActivity(new Intent(mContext, MainActivity.class));
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "登录遇到问题", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Network.getInstance().login(userInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
