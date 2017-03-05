package com.htu.erhuo.utiles;

/**
 * Description
 * Created by yzw on 2017/2/5.
 */

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.htu.erhuo.application.EHApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

/**
 * 设备信息实用类
 */
public class DeviceInfoUtil {

    /**
     * 获取手机SDK版本号
     */
    public static int getSystemSDKVersion() {
        int sys_version = -1;
        try {
            sys_version = Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return sys_version;
    }

    public static int getVersionCode() {
        try {
            PackageManager pm = EHApplication.getInstance().getPackageManager();
            PackageInfo pacInfo = pm.getPackageInfo(EHApplication.getInstance().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            int versionCode = pacInfo.versionCode;

            String[] permissions = pm.getPackageInfo(EHApplication.getInstance().getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
            for (int i = 0; permissions != null && i < permissions.length; i++) {
                System.out.println("permission=" + permissions[i]);
            }
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getUnderlineVersionCode() {
        String versionCode = "";
        try {
            PackageManager pm = EHApplication.getInstance().getPackageManager();
            PackageInfo pacInfo = pm.getPackageInfo(EHApplication.getInstance().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionCode = pacInfo.versionName + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String[] split = versionCode.split("\\.");
        String code = "";
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) {
                code += split[i];
            } else {
                code += split[i] + "_";
            }
        }
        return code;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            // int versionCode = pinfo.versionCode;
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 安装app
     */
    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 程序是否在前台运行
     * 是否锁屏
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the  device
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) return false;

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        if (isScreenOn) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断程序的运行在前台还是后台
     *
     * @return 0在后台运行  大于0在前台运行  2表示当前主界面是MainFragmentActivity
     */
    public static int isBackground(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        String bingMapMainActivityClassName = "MainActivity";
        String bingMapMainActivityClassName2 = "ChatMainActivity";
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            ComponentName topComponent = tasksInfo.get(0).topActivity;
            if (packageName.equals(topComponent.getPackageName())) {
                // 当前的APP在前台运行
                if (topComponent.getClassName().equals(bingMapMainActivityClassName)
                        || topComponent.getClassName().equals(bingMapMainActivityClassName2)) {
                    // 当前正在运行的是不是期望的Activity
                    return 2;
                }
                return 1;
            } else {
                // 当前的APP在后台运行
                return 0;
            }
        }
        return 0;
    }

    /**
     * 获取Application meta-data数据
     */
    public static String getAppMetaData(Context context, String node) {
        String from = "";
        try {
            if (context == null) {
                context = EHApplication.getInstance();
            }
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            from = appInfo.metaData.getString(node);// 001取不到要通过下面方式来取
//			if (from == null) {
//				int channelInt = appInfo.metaData.getInt("app_channel");
//				from = String.format("%03d", channelInt);
//			}

        } catch (Exception e) {
            e.printStackTrace();
        }

        return from;
    }

    /**
     * 获取Activity meta-data数据
     */
    public static String getActivityMetaData(Activity context, String node) {
        String value = "";
        try {
            ActivityInfo appInfo = context.getPackageManager().getActivityInfo(
                    context.getComponentName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(node);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取屏幕密度比
     */
    public static float getDensity() {
        DisplayMetrics dm = EHApplication.getInstance().getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = EHApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = EHApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕宽度 px值
     */
    public static int getWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕宽度 px值
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度dp值
     */
    public static int getWidthDip(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int Dpi = dm.densityDpi;
        return (160 * width) / Dpi;
    }

    /**
     * 获取屏幕高度dp值
     */
    public static int getHeightDip(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int height = dm.heightPixels;
        int Dpi = dm.densityDpi;
        return 2 * (160 * height) / Dpi;
    }

    /**
     * 退出程序方法
     *
     * @param context
     */
    public static void exitApp(Context context) {
        // 清空所有活动Activity
        //EHApplication.getInstance().exit();
        // 跳转到桌面
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetAvailable(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context
                    .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (manager == null) return false;

            NetworkInfo networkinfo = manager.getActiveNetworkInfo();
            if (networkinfo == null || !networkinfo.isAvailable()
                    || !networkinfo.isConnectedOrConnecting()) {
                return false;
            } else {// /如果有网络连接，再判断一下是否可以正常上网，
                return true;
//				if (openUrl()) {// //正常
//				} else {
//					Toast.makeText(context, "网络异常,请检查网络设置", Toast.LENGTH_LONG).show();
//					/*if(context instanceof LoadingActivity){
//						((LoadingActivity)context).finish();
//					}*/
//					return false;
//				}
            }
        } catch (Exception e) {
            //LogCatLog.e("DeviceInfoUtil", "isNetAvailable error !", e);
            return false;
        }
    }

    public static String getWifiMacAddress(Context context) {
        try {
            /*WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String m_szWLANMAC="";
			if(wm!=null){
				m_szWLANMAC= wm.getConnectionInfo().getMacAddress();
				PersonSharePreference.setAndroidMac(m_szWLANMAC);//保存wifi信息
			}*/

            String interfaceName = "wlan0";
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                    continue;
                }

                byte[] mac = intf.getHardwareAddress();
                if (mac == null) {
                    return "";
                }

                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) {
                    buf.append(String.format("%02X:", aMac));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception e) {
            //LogCatLog.e("DeviceInfoUtil", "getWifiMacAddress error!", e);
        }
        return "";
    }

    /**
     * 当android id 为空时 用来代替 它  同时保存cpu wifi bt 信息
     *
     * @return 设备id
     */
    public static String initHhAndroidID(Context context) {
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);

            String m_szImei = TelephonyMgr.getDeviceId();
            String m_szDevIDShort = "35"
                    + Build.BOARD.length() % 10 + Build.BRAND.length() % 10
                    + Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10
                    + Build.DISPLAY.length() % 10 + Build.HOST.length() % 10
                    + Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10
                    + Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10
                    + Build.TAGS.length() % 10 + Build.TYPE.length() % 10
                    + Build.USER.length() % 10;
            String m_szAndroidID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

//            PersonSharePreference.setAndroidCpu(Build.CPU_ABI);//保存cpu信息

            String m_szWLANMAC = getWifiMacAddress(context);//wifi信息

            BluetoothAdapter m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            String m_szBTMAC = "";
            if (m_BluetoothAdapter != null) {
                m_szBTMAC = m_BluetoothAdapter.getAddress();
//                PersonSharePreference.setAndroidBtMac(m_szBTMAC); //保存蓝牙信息
            }

            String m_szLongID = m_szImei + m_szDevIDShort + m_szAndroidID + m_szWLANMAC + m_szBTMAC;

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
            byte p_md5Data[] = m.digest();

            String m_szUniqueID = "";
            for (byte aP_md5Data : p_md5Data) {
                int b = (0xFF & aP_md5Data);
                if (b <= 0xF)
                    m_szUniqueID += "0";

                m_szUniqueID += Integer.toHexString(b);
            }
            m_szUniqueID = m_szUniqueID.toUpperCase();

            if (TextUtils.isEmpty(m_szAndroidID)) {
                return m_szUniqueID;
            } else {
                return m_szAndroidID; //当android id 为空时 用来代替 它
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return DeviceInfo.createInstance(context).getTimeStamp();
        return null;
    }

    /**
     * 动态设置listView高度
     */
    public static void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 获取listview的高度
     */
    public static int getListViewHeight(ListView pull) {
        int totalHeight = 0;
        ListAdapter adapter = pull.getAdapter();
        if (adapter == null)
            return totalHeight;
        try {
            for (int i = 0; i < adapter.getCount(); i++) { //listAdapter.getCount()返回数据项的数目
                View listItem = adapter.getView(i, null, pull);
                listItem.measure(0, 0); //计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//        ViewGroup.LayoutParams params = pull.getLayoutParams();
//        params.height = totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));
//        pull.setLayoutParams(params);

        return totalHeight + (pull.getDividerHeight() * (pull.getCount() - 1));
    }

    public static int checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        //AppOpsManager appOpsManager;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(),
                        context.getPackageName());
            } catch (NoSuchMethodException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

}

