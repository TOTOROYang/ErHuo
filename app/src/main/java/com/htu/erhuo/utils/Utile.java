package com.htu.erhuo.utils;

import android.app.Activity;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Description
 * Created by yzw on 2017/3/19.
 */

public class Utile {
    public static String getRealPathFromURI(Activity activity, Uri contentUri) { //传入图片uri地址
        String[] pro = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(activity, contentUri, pro, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
