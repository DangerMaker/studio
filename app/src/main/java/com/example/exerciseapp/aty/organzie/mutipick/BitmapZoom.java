package com.example.exerciseapp.aty.organzie.mutipick;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hh on 2016/7/22.
 */
public class BitmapZoom {
    public static final String SDPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/imagess/";

    public static String compress(Activity activity, String srcPath, int maxSize) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            System.out.println(baos.toByteArray().length);
            while (baos.toByteArray().length > maxSize * 1024) {
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                quality -= 20;
                System.out.println(baos.toByteArray().length);
            }
            try {
                File dir = new File(SDPATH + "");
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                }
                String path = SDPATH + srcPath.substring(srcPath.lastIndexOf("/"));
                File file = new File(path);
                baos.writeTo(new FileOutputStream(file));
                return path;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    baos.flush();
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     *   * 读取照片exif信息中的旋转角度
     *   * @param path 照片路径
     *   * @return角度
     *   
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static String toturn(String src,String name,int a){
        Bitmap img = BitmapCache.revitionImageSize(src);
        Matrix matrix = new Matrix();
        matrix.postRotate(+a); /*翻转90度*/
        int width = img.getWidth();
        int height =img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return FileUtils.saveBitmap(img,name);
    }

}
