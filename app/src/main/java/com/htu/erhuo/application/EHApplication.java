package com.htu.erhuo.application;

import android.app.Application;

import com.htu.erhuo.entity.DaoMaster;
import com.htu.erhuo.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Description application
 * Created by yzw on 2017/1/24.
 */

public class EHApplication extends Application {
    public static final String HTU_JW_URL = "http://jw.htu.cn/jwweb/_data/index_LOGIN.aspx";
    public static final String JAVASCRIPT_GET_CHECK_IMAGE = "function getCheckImg() {var img = window.document.getElementById('imgCode');    var canvas = document.createElement(\"canvas\");    canvas.width = img.width;    canvas.height = img.height;    var ctx = canvas.getContext(\"2d\");    ctx.drawImage(img, 0, 0, img.width, img.height);    var dataURL = canvas.toDataURL(\"image/png\");    android.imgData(dataURL.replace(\"data:image/png;base64,\", \"\"));}";
    public static final String JAVASCRIPT_LOGIN = "function test(u,p,c){    window.document.getElementById('txt_asmcdefsddsd').value =u;window.document.getElementById('txt_pewerwedsdfsdff').value =p;window.document.getElementById('txt_pewerwedsdfsdff').onkeyup();    window.document.getElementById('txt_sdertfgsadscxcadsads').value =c;    window.document.getElementById('txt_sdertfgsadscxcadsads').onkeyup();    window.document.getElementsByClassName('but20')[0].click();}";
    public static final String JAVASCRIPT_GET_NAME = "function getName(){if(window.frames[3] == null){android.name('');}else{var name = window.frames[3].document.getElementById('TheFootMemo').innerText;android.name(name);}}";
    public static final boolean ENCRYPTED = false;
    private DaoSession daoSession;
    private static EHApplication instance;

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

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static EHApplication getContext() {
        return instance;
    }
    public static EHApplication getInstance() {
        return instance;
    }
}
