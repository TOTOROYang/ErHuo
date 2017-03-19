package com.htu.erhuo.utils;

import android.content.Context;

import java.io.File;

/**
 * Description
 * Created by yzw on 2017/3/19.
 */

public class FileUtils {
    public final static String ERHUO_BUCKET = "htuerhuo-img";
    private static String DATA_PATH;
    private static String IMG_SAVE_PATH;

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


}
