package com.htu.erhuo.application;

import android.app.Application;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.htu.erhuo.entity.DaoMaster;
import com.htu.erhuo.entity.DaoSession;
import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ErhuoOssToken;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.Network;
import com.htu.erhuo.utiles.PreferenceUtils;

import org.greenrobot.greendao.database.Database;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description application
 * Created by yzw on 2017/1/24.
 */

public class EHApplication extends Application {
    public static final String HTU_JW_URL = "http://jw.htu.cn/jwweb/_data/index_LOGIN.aspx";
    public static final String JAVASCRIPT_GET_CHECK_IMAGE = "function getCheckImg() {var img = window.document.getElementById('imgCode');    var canvas = document.createElement(\"canvas\");    canvas.width = img.width;    canvas.height = img.height;    var ctx = canvas.getContext(\"2d\");    ctx.drawImage(img, 0, 0, img.width, img.height);    var dataURL = canvas.toDataURL(\"image/png\");    android.imgData(dataURL.replace(\"data:image/png;base64,\", \"\"));}";
    public static final String JAVASCRIPT_LOGIN = "function test(u,p,c){    window.document.getElementById('txt_asmcdefsddsd').value =u;window.document.getElementById('txt_pewerwedsdfsdff').value =p;window.document.getElementById('txt_pewerwedsdfsdff').onkeyup();    window.document.getElementById('txt_sdertfgsadscxcadsads').value =c;    window.document.getElementById('txt_sdertfgsadscxcadsads').onkeyup();    window.document.getElementsByClassName('but20')[0].click();}";
    public static final String JAVASCRIPT_GET_NAME = "function getName(){if(window.frames[3] == null){android.name('');}else{var name = window.frames[3].document.getElementById('TheFootMemo').innerText;android.name(name);}}";
    private static final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;
    private static EHApplication instance;
    private UserInfo userInfo;
    OSS oss;
    OSSFederationToken token;

    public EHApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "erhuo-db-encrypted" : "erhuo-db");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public void initOss() {
        OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                String requestKey;
                if (PreferenceUtils.getInstance().getIsLogin()) {
                    requestKey = PreferenceUtils.getInstance().getUserId();
                } else {
                    requestKey = PreferenceUtils.getInstance().getMacAddress();
                }
                getOssToken(requestKey);
                return token;
            }
        };
        oss = new OSSClient(this, endpoint, credentialProvider);
    }

    private void getOssToken(String requestKey) {
        Subscriber<EntityResponse<ErhuoOssToken>> subscriber = new Subscriber<EntityResponse<ErhuoOssToken>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                token = null;
            }

            @Override
            public void onNext(EntityResponse<ErhuoOssToken> ossFederationTokenEntityResponse) {
                if (ossFederationTokenEntityResponse.getCode().equals("0")) {
                    ErhuoOssToken erhuoOssToken = ossFederationTokenEntityResponse.getMsg();
                    token = new OSSFederationToken(erhuoOssToken.getAccessKeyId(), erhuoOssToken.getAccessKeySecret(),
                            erhuoOssToken.getSecurityToken(), erhuoOssToken.getExpiration());
                    Log.d("yzw", "1111");
                } else {
                    token = null;
                }
            }
        };
        Network.getInstance().getOssToken(requestKey).observeOn(Schedulers.immediate()).subscribe(subscriber);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static EHApplication getContext() {
        return instance;
    }

    public static EHApplication getInstance() {
        return instance;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public OSS getOss() {
        return oss;
    }
}
