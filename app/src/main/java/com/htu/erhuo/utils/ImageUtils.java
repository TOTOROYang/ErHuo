package com.htu.erhuo.utils;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.htu.erhuo.R;
import com.htu.erhuo.application.EHApplication;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = EHApplication.getInstance().getOss().presignConstrainedObjectURL(FileUtils.ERHUO_BUCKET, imageName, 30 * 60);
                    Log.d("yzw", "imageUrl " + url);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(activity)
                                    .load(url)
                                    .into(imageView);
                        }
                    });
                } catch (Exception e) {
                    Log.d("yzw", "图片展示失败 " + imageName);
                }
            }
        }).start();

    }

    public static void showGaussImage(final Activity activity, final ImageView imageView, final String imageName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = EHApplication.getInstance().getOss().presignConstrainedObjectURL(FileUtils.ERHUO_BUCKET, imageName, 30 * 60);
                    Log.d("yzw", "imageUrl " + url);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(activity)
                                    .load(url)
                                    .crossFade(1000)
                                    .bitmapTransform(new BlurTransformation(EHApplication.getInstance(), Glide.get(EHApplication.getInstance()).getBitmapPool(), 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                                    .into(imageView);
                        }
                    });
                } catch (Exception e) {
                    Log.d("yzw", "图片展示失败 " + imageName);
                }
            }
        }).start();
    }

    public static void showGaussImageRes(Activity activity, ImageView imageView, int resId) {
        Glide.with(activity)
                .load(resId)
                .bitmapTransform(new BlurTransformation(activity, Glide.get(activity).getBitmapPool(), 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }

    public static void showNormalAndGaussImage(final Activity activity, final ImageView ivAvatar, final ImageView ivBgAvatar, final String imageName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String url = EHApplication.getInstance().getOss().presignConstrainedObjectURL(FileUtils.ERHUO_BUCKET, imageName, 30 * 60);
                    Log.d("yzw", "imageUrl " + url);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(activity)
                                    .load(url)
                                    .bitmapTransform(new ColorFilterTransformation(activity, 0x7f000000), new BlurTransformation(activity, 10)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                                    .into(ivBgAvatar);

                            Glide.with(activity)
                                    .load(url)
                                    .bitmapTransform(new CropCircleTransformation(activity))
                                    .into(ivAvatar);
                        }
                    });
                } catch (Exception e) {
                    Log.d("yzw", "图片展示失败 " + imageName);
                }
            }
        }).start();
    }
}
