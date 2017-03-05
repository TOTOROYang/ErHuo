package com.htu.erhuo.utiles;

import android.content.Context;

import com.htu.erhuo.application.EHApplication;

/**
 * Description
 * Created by yzw on 2017/3/13.
 */

public class PreferenceUtils extends BasePreference {
    private static PreferenceUtils preferenceUtils;
    /**
     * 需要增加key就在这里新建
     */
    private String USER_ID = "user_id";
    //用户名的key
    private String USER_NAME = "user_name";
    //是否首次启动的key
    private String FIRST_LAUNCH = "first_launch";
    //mac地址
    private String MAC_ADDRESS = "mac_address";

    private PreferenceUtils(Context context) {
        super(context);
    }

    /**
     * 这里我通过自定义的Application来获取Context对象，所以在获取preferenceUtils时不需要传入Context。
     *
     */
    public synchronized static PreferenceUtils getInstance() {
        if (null == preferenceUtils) {
            preferenceUtils = new PreferenceUtils(EHApplication.getInstance());
        }
        return preferenceUtils;
    }

    public void setFirstLaunch(boolean isFirst) {
        setBoolean(FIRST_LAUNCH, isFirst);
    }

    public boolean getFirstlaunch() {
        return getBoolean(FIRST_LAUNCH);
    }

    public void setUserId(String id) {
        setString(USER_ID, id);
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public void setUserName(String name) {
        setString(USER_NAME, name);
    }

    public String getUserName() {
        return getString(USER_NAME);
    }

    public void setMacAddress(String macAddress) {
        setString(MAC_ADDRESS, macAddress);
    }

    public String getMacAddress() {
        return getString(MAC_ADDRESS);
    }
}
