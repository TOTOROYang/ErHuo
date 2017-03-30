package com.htu.erhuo.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropSquareTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Description
 * Created by yzw on 2017/3/19.
 */

public class ImageUtils {

    public static void showImage(final Activity activity, final ImageView imageView, final String imageName) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final String url = EHApplication.getInstance().getOss().presignPublicObjectURL(FileUtils.ERHUO_BUCKET, imageName);
//                    Log.d("yzw", "imageUrl " + url);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(activity)
//                                    .load(url)
//                                    .into(imageView);
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.d("yzw", "图片展示失败 " + imageName);
//                }
//            }
//        }).start();
        final String url = EHApplication.getInstance().getOss().presignPublicObjectURL(FileUtils.ERHUO_BUCKET, imageName);
        Log.d("yzw", "imageUrl " + url);
        Glide.with(activity)
                .load(url)
                .into(imageView);
    }

    public static void showGaussImage(final Activity activity, final ImageView imageView, final String imageName) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final String url = EHApplication.getInstance().getOss().presignConstrainedObjectURL(FileUtils.ERHUO_BUCKET, imageName, 30 * 60);
//                    Log.d("yzw", "imageUrl " + url);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(activity)
//                                    .load(url)
//                                    .bitmapTransform(new BlurTransformation(EHApplication.getInstance(), Glide.get(EHApplication.getInstance()).getBitmapPool(), 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//                                    .into(imageView);
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.d("yzw", "图片展示失败 " + imageName);
//                }
//            }
//        }).start();
        final String url = EHApplication.getInstance().getOss().presignPublicObjectURL(FileUtils.ERHUO_BUCKET, imageName);
        Log.d("yzw", "imageUrl " + url);
        Glide.with(activity)
                .load(url)
                .bitmapTransform(new BlurTransformation(EHApplication.getInstance(), Glide.get(EHApplication.getInstance()).getBitmapPool(), 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }

    public static void showImageRes(Activity activity, ImageView imageView, int resId) {
        Glide.with(activity)
                .load(resId)
                .into(imageView);
    }

    public static void showGaussImageRes(Activity activity, ImageView imageView, int resId) {
        Glide.with(activity)
                .load(resId)
                .bitmapTransform(new BlurTransformation(activity, Glide.get(activity).getBitmapPool(), 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }

    public static void showNormalAndGaussImage(final Activity activity, final ImageView ivAvatar, final ImageView ivBgAvatar, final String imageName) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    final String url = EHApplication.getInstance().getOss().presignPublicObjectURL(FileUtils.ERHUO_BUCKET, imageName);
//                    Log.d("yzw", "imageUrl " + url);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Glide.with(activity)
//                                    .load(url)
//                                    .bitmapTransform(new ColorFilterTransformation(activity, 0x7f000000), new BlurTransformation(activity, 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//                                    .into(ivBgAvatar);
//
//                            Glide.with(activity)
//                                    .load(url)
//                                    .bitmapTransform(new CropCircleTransformation(activity))
//                                    .into(ivAvatar);
//                        }
//                    });
//                } catch (Exception e) {
//                    Log.d("yzw", "图片展示失败 " + imageName);
//                }
//            }
//        }).start();
        final String url = EHApplication.getInstance().getOss().presignPublicObjectURL(FileUtils.ERHUO_BUCKET, imageName);
        Glide.with(activity)
                .load(url)
                .bitmapTransform(new ColorFilterTransformation(activity, 0x7f000000), new BlurTransformation(activity, 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(ivBgAvatar);

        Glide.with(activity)
                .load(url)
                .bitmapTransform(new CropCircleTransformation(activity))
                .into(ivAvatar);
    }

    /**
     * 计算BitmapFactory的inSample
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromSDCard(String path, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        int width = options.outWidth;
        int height = options.outHeight;

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // 使用获取到的inSampleSize值再次解析图片
        int targetDensity = DeviceInfo.getInstance().getDencity();
        double xSScale = ((double) options.outWidth) / ((double) reqWidth);
        double ySScale = ((double) options.outHeight) / ((double) reqHeight);
        double startScale = xSScale > ySScale ? xSScale : ySScale;
        if (width < reqWidth || height < reqHeight) {
            reqWidth = width;
            reqHeight = height;
        } else {
            options.inScaled = true;
            options.inDensity = (int) (targetDensity * startScale / options.inSampleSize);
            options.inTargetDensity = targetDensity;
        }
        options.inJustDecodeBounds = false;
        //		if(width<reqWidth||height<reqHeight){
//			reqWidth=width;
//			reqHeight=height;
//		}else{
//			bmp=resizeImage(bmp, reqWidth, reqHeight);
//		}
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 将位图存到指定空间
     */
    public static void savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File photoFile = new File(path, photoName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
