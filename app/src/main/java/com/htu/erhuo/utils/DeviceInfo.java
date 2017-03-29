package com.htu.erhuo.utils;

/**
 * Description
 * Created by yzw on 2017/3/29.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.htu.erhuo.application.EHApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 设备管理工具
 */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";

    private static DeviceInfo mInstance;

    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    /**
     * 屏幕Dencity
     */
    private int mDencity;

    /**
     * 手机厂商
     */
    private String mMobileBrand;

    private String mMobileModel;

    private String mSystemVersion;

    private String mDid;

    // 未知
    public static final String UNKNOWN = "unknown";

    private static final String NULL = "null";

    /**
     * 任意个0的字符串的正则表达式
     */
    private static final String ANY_ZERO_STR = "[0]+";

    /**
     * 定义硬件标志的非法长度，目前暂定为5
     */
    private static final int HARDWARD_INVALID_LEN = 5;

    private final static int IMEI_LEN = 15;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");

    private DeviceInfo(Context context) {
        mContext = context;
    }

    /**
     * 获取设备信息实例
     *
     * @return 性能记录器
     */
    public static synchronized DeviceInfo getInstance() {
        if (mInstance == null) {
            mInstance = new DeviceInfo(EHApplication.getInstance());
            mInstance.init();
        }
        return mInstance;
    }

    /**
     * 创建设备信息示例
     *
     * @param context 上下文
     * @return 性能记录器
     */
    public static synchronized DeviceInfo createInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DeviceInfo(context);
            mInstance.init();
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    private void init() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mDencity = displayMetrics.densityDpi;
        mMobileBrand = Build.BRAND;
        mMobileModel = Build.MODEL;
        mSystemVersion = Build.VERSION.RELEASE;
    }

    /**
     * 获取DeviceID
     */
    public String getDeviceId() {
        if (TextUtils.isEmpty(mDid) || "-1".equals(mDid)) {
            SharedPreferences sp = mContext.getSharedPreferences("hhapp_base_cache",//SharedPreference路径
                    Context.MODE_PRIVATE);
            mDid = sp.getString("DeviceId", "-1");
        }
        return mDid;
    }

    public void setmDid(String mDid) {
        this.mDid = mDid;
    }

    public int getmScreenWidth() {
        return mScreenWidth;
    }

    public int getmScreenHeight() {
        return mScreenHeight;
    }

    public String getmMobileBrand() {
        return mMobileBrand;
    }

    public String getmMobileModel() {
        return mMobileModel;
    }

    public String getmSystemVersion() {
        return mSystemVersion;
    }

    /**
     * 获取设备屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        return mScreenWidth;
    }

    /**
     * 获取设备屏幕高度
     *
     * @return 屏幕高度
     */
    public int getScreenHeight() {
        return mScreenHeight;
    }

    /**
     * 获取设备屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getDencity() {
        return mDencity;
    }

    /**
     * 获取UserAgent
     *
     * @return UserAgent
     */
    public String getUserAgent() {
        return Build.MANUFACTURER + Build.MODEL;
    }

    /**
     * 获取外接SD卡上当前应用根目录下面的目录，如果不存在则创建
     *
     * @param dir 外接SD卡上当前应用目录下的的目录名
     */
    public String getExternalStoragePath(String dir) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdCardPath = "hoho";//应用sd卡文件路径
            String path = Environment.getExternalStorageDirectory().getPath()
                    + File.separatorChar + sdCardPath;
            File file = new File(path);
            if (!file.exists() && !file.mkdir()) {
//                LogCatLog.e(TAG, "fail to create " + dir + " dir:" + path);
                return path;
            } else if (!file.isDirectory()) {
//                LogCatLog.e(TAG, dir + " dir exist,but not directory:" + path);
                return null;
            }

            path = path + File.separatorChar + dir;
            file = new File(path);
            if (!file.exists() && !file.mkdir()) {
//                LogCatLog.e(TAG, "fail to create " + dir + " dir:" + path);
                return path;
            } else if (!file.isDirectory()) {
//                LogCatLog.e(TAG, dir + " dir exist,but not directory:" + path);
                return null;
            } else {
                return path;
            }
        }
        return null;
    }


    /**
     * 判断是否为空字符串
     *
     * @param s
     * @return
     */
    private boolean isBlank(String s) {
        return s == null || s.trim().length() == 0;
    }


    /**
     * 判断clientId是否合法
     *
     * @param clientID
     * @return
     */
    private boolean isValidClientID(String clientID) {
        if (isBlank(clientID))
            return false;
        return clientID
                .matches("[[a-z][A-Z][0-9]]{15}\\|[[a-z][A-Z][0-9]]{15}");
    }

    /**
     * 将非16进制字符替换成'0'
     *
     * @param imei
     * @return
     */
    private String replaceNonHexChar(String imei) {
        if (isBlank(imei))
            return imei;
        byte[] byteClientId = imei.getBytes();
        for (int i = 0; i < byteClientId.length; i++) {
            if (!isDigitOrAlphaBelta(byteClientId[i])) // 如果不是十六进制字符，则用0替换。
                byteClientId[i] = '0';
        }
        return new String(byteClientId);
    }

    /**
     * 判断c是否是十六进制字符
     *
     * @param c
     * @return
     */
    private boolean isDigitOrAlphaBelta(byte c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z');
    }


    public String getTimeStamp() {
        String timeStamp = this.format.format(System.currentTimeMillis());
        return timeStamp;
    }

    /**
     * xuxi
     *
     * @return
     */
    public String getKey() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String strKey = format.format(date);
        return strKey;
    }

//    /**
//     * 获取经度
//     *
//     * @return
//     */
//    public double getLatitudeDoube() {
//        LBSLocation location = LBSLocationManagerProxy.getInstance().getLastKnownLocation(mContext);
//        if (location != null) {
//            return location.getLatitude();
//        }
//        return 0.0d;
//    }
//
//    /**
//     * 获取纬度
//     *
//     * @return
//     */
//    public double getLongitudeDoube() {
//        LBSLocation location = LBSLocationManagerProxy.getInstance().getLastKnownLocation(mContext);
//        if (location != null) {
//            return location.getLongitude();
//        }
//        return 0.0d;
//    }
//
//    public LBSLocation getLBSLocation(){
//        return LBSLocationManagerProxy.getInstance().getLastKnownLocation(mContext);
//    }

//    /**
//     * 判断手机是否是飞行模式
//     *
//     * @return
//     */
//    public static boolean getAirplaneMode() {
//        int isAirplaneMode = Settings.System.getInt(HoHoApplication.getInstance().getContentResolver(),
//                Settings.Global.AIRPLANE_MODE_ON, 0);
//        return (isAirplaneMode == 1) ? true : false;
//    }

}

