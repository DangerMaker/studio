package com.example.exerciseapp.aty.organzie.mutipick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by hh on 2016/8/8.
 */
public class Photo {
    public static void zoomPhoto(Activity context, String url, int PHOTO_REQUEST_CUT) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = Uri.fromFile(new File(url));
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        System.out.println("22================");
        context.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

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

    public static String toturn(String url, int j) {
        Bitmap img = BitmapFactory.decodeFile(url);
        Matrix matrix = new Matrix();
        matrix.postRotate(+j); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        String imgurl = FileUtils.saveBitmap(img, url.substring(url.lastIndexOf("/")));
        return imgurl;
    }

    public static String getPath(Activity activity,Intent data) {
        if (data == null) {
            return "";
        } else {
            if (data.getData() == null) {
                return FileUtils.saveBitmap((Bitmap) data.getParcelableExtra("data"), System.currentTimeMillis() + ".jpg");
            } else {
                Uri uri = data.getData();
                return FileUtils.saveBitmap(getBitmapFromUri(activity,uri),System.currentTimeMillis() + ".jpg");
            }
        }
    }


    private static Bitmap getBitmapFromUri(Activity activity,Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
