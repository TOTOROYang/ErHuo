package com.htu.erhuo.utils;

/**
 * Description
 * Created by yzw on 2017/3/29.
 */

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.google.webp.libwebp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/***
 * 加载图片
 */
@SuppressLint("NewApi")
class LoadWebpImage {

    private static String TAG = LoadWebpImage.class.getSimpleName();

    private Bitmap bitmap;

    LoadWebpImage() {

    }

    /**
     * 普通图片转webp bitmap
     */
    Bitmap imageFileTowebBitmap(String fromImagePath) {
        try {
            File file = new File(fromImagePath);
            FileInputStream stream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return webpToBitmap(imageFileTowebBitmap(bitmap));
        } catch (Exception e) {
//            LogCatLog.d(TAG, "图片转换失败 失败信息：" + e.getMessage());
        }
        return null;
    }

    /**
     * 普通图片转webp字节数组
     */
    public byte[] imageFileToByteArray(String fromImagePath) {
        try {
            File file = new File(fromImagePath);
            FileInputStream stream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            return imageFileTowebBitmap(bitmap);
        } catch (Exception e) {
//            LogCatLog.d(TAG, "图片转换失败 失败信息：" + e.getMessage());
        }
        return null;
    }

    /**
     * 普通图片转webp byte[]
     */
    byte[] imageFileTowebBitmap(Bitmap bitmap) {
        try {
            int bytes = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bitmap.copyPixelsToBuffer(buffer);
            byte[] pixels = buffer.array();
            int stride = bytes / bitmap.getHeight();
//            LogCatLog.d(TAG, "宽高" + bitmap.getWidth() + bitmap.getHeight() + "质量" + stride);
            byte[] encoded = libwebp.WebPEncodeRGBA(pixels, bitmap.getWidth(), bitmap.getHeight(), stride, 80);
            return encoded;
        } catch (Exception e) {
//            LogCatLog.d(TAG, "普通图片转webp byte[]" + e.getMessage());
        }
        return null;
    }

    /**
     * 普通图片 转 webp格式图片
     */
    boolean imageFileTowebpFile(String fromImagePath, String toImagePath) {
        try {
            File file = new File(fromImagePath);
            FileInputStream stream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);
            writeFileFromByteArray(toImagePath, imageFileTowebBitmap(bitmap));
            return true;
        } catch (Exception e) {
//            LogCatLog.d(TAG, "图片转换");
        }
        return false;
    }

    /**
     * 普通图片［jpg、png、jpeg］ 转 webp
     */
    @SuppressLint("NewApi")
    public byte[] bitmapToWebp(String filePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        int bytes = bitmap.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        bitmap.copyPixelsToBuffer(buffer);
        byte[] pixels = buffer.array();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int stride = width;
        int quality = 100;
        byte[] rgb = new byte[3];

        for (int y = 0; y < height * 4; y++) {
            for (int x = 0; x < width; x += 4) {
                for (int i = 0; i < 3; i++) {
                    rgb[i] = pixels[x + y * width + i];
                }
                for (int i = 0; i < 3; i++) {
                    pixels[x + y * width + 2 - i] = rgb[i];
                }
            }
        }
        byte[] encoded = libwebp.WebPEncodeBGRA(pixels, width, height, stride, quality);
        return encoded;
    }

    /**
     * webp 转换 bitmap
     */
    Bitmap webpToBitmap(byte[] encoded) {
        try {
            int[] width = new int[]{0};
            int[] height = new int[]{0};
            byte[] decoded = libwebp.WebPDecodeARGB(encoded, encoded.length, width, height);
            int[] pixels = new int[decoded.length / 4];
            ByteBuffer.wrap(decoded).asIntBuffer().get(pixels);
            return Bitmap.createBitmap(pixels, width[0], height[0], Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
//            LogCatLog.d(TAG, "webp 转换 bitmap：" + e.getMessage());
        }
        return null;
    }

    /**
     * 写入文件
     */
    boolean writeFileFromByteArray(String filePath, byte[] data) {
        File file = new File(filePath);
        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(data);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取文件字节数
     *
     * @param filePath 文件路径
     */
    public byte[] loadFileAsByteArray(String filePath) {
        File file = new File(filePath);
        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream inputStream;
            inputStream = new FileInputStream(file);
            inputStream.read(data);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 获取文件的字节数
     *
     * @param filePath 文件路径
     * @return 字节数组
     * @throws Exception
     */
    public byte[] getBytes(String filePath) throws Exception {
        if (filePath == null) throw new NullPointerException("filePath 不能等于null");
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 设置Bitmap
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
//        LogCatLog.d(TAG, "设置bitmap  size:" + bitmap.getByteCount() / 1024);
        this.bitmap = bitmap;
    }

    /**
     * 加载bitmap 到 设置的imageView
     *
     * @param imageView
     */
    public void into(ImageView imageView) {
        imageView.setImageBitmap(bitmap);
    }

    static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;

        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

}
