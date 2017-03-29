package com.htu.erhuo.utils;

import android.content.Context;
import android.text.TextUtils;

import com.htu.erhuo.application.EHApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Description
 * Created by yzw on 2017/3/19.
 */

public class FileUtils {
    public final static String ERHUO_BUCKET = "htuerhuo-img";
    public static String DATA_PATH;
    public static String IMG_SAVE_PATH;

    private Context mContext;
    private static FileUtils mInstance;

    public FileUtils(Context context) {
        mContext = context;
    }

    /**
     * 创建文件工具类示例
     *
     * @param context 上下文
     * @return FileUtils
     */
    public static synchronized FileUtils createInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FileUtils(context);
            mInstance.initPath();
        }
        return mInstance;
    }

    /**
     * 获取文件工具类实例
     *
     * @return FileUtils
     */
    public static synchronized FileUtils getInstance() {
        if (mInstance == null) {
            mInstance = new FileUtils(EHApplication.getInstance());
            mInstance.initPath();
        }
        return mInstance;
    }

    private void initPath() {
        DATA_PATH = mContext.getFilesDir().getParent() + "/";                  // DATA
        IMG_SAVE_PATH = DATA_PATH + "images/";                                 // 图片文件夹
        File erhuoFilePath = new File(DATA_PATH);
        erhuoFilePath.setExecutable(true, true);
        erhuoFilePath.setReadable(true, true);
        erhuoFilePath.setWritable(true, true);
        if (!erhuoFilePath.exists()) erhuoFilePath.mkdirs();
        // 图片
        File imgSavePath = new File(IMG_SAVE_PATH);
        if (!imgSavePath.exists()) imgSavePath.mkdirs();
    }

    public static String getUploadAvatarNameFromPath(String path) {
        return PreferenceUtils.getInstance().getUserId() + "_" +
                System.currentTimeMillis() +
                path.substring(path.lastIndexOf("."));
    }


    /**
     * 判断文件是否存在
     */
    public boolean isExists(String strPath) {
        if (TextUtils.isEmpty(strPath)) return false;

        final File strFile = new File(strPath);
        return strFile.exists();
    }

    public boolean deleteFile(String strPath) {
        if (TextUtils.isEmpty(strPath)) return false;

        final File strFile = new File(strPath);
        return strFile.exists() && strFile.delete();
    }

    /**
     * 拷贝一个文件到另一个目录
     */
    public boolean copyFile(String from, String to) {
        File fromFile, toFile;
        fromFile = new File(from);
        toFile = new File(to);
        FileInputStream fis;
        FileOutputStream fos;

        try {
            toFile.createNewFile();
            fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);
            int bytesRead;
            byte[] buf = new byte[4 * 1024];  // 4K buffer
            while ((bytesRead = fis.read(buf)) != -1) {
                fos.write(buf, 0, bytesRead);
            }
            fos.flush();
            fos.close();
            fis.close();
        } catch (IOException e) {
//            LogCatLog.e(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     */
    public static boolean removeFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            } else {
//                LogCatLog.d(TAG, "删除文件失败");
            }
        } catch (Exception e) {
//            LogCatLog.e(TAG, "删除文件失败" + e);
        }
        return false;
    }

}
