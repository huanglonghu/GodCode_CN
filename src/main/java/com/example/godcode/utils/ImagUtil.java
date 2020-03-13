package com.example.godcode.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.base.GodCodeApplication;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImagUtil {

    public static String handleUrl(String path) {
        if (!TextUtils.isEmpty(path)) {
            if (!path.contains("http")) {
                if (path.substring(0, 1).equals("/")) {
                    path = Constant.baseUrl + path;
                } else {
                    path = Constant.baseUrl + "/" + path;
                }
            }
            return path;
        }
        return null;
    }


    public static Drawable circle(Bitmap bitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(GodCodeApplication.getInstance().getResources(), bitmap);
        roundedBitmapDrawable.setCornerRadius(100);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }


    public static Drawable conner(Bitmap bitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(GodCodeApplication.getInstance().getResources(), bitmap); //创建RoundedBitmapDrawable对象
        roundedBitmapDrawable.setCornerRadius(10); //设置圆角半径（根据实际需求）
        roundedBitmapDrawable.setAntiAlias(true); //设置反走样
        return roundedBitmapDrawable;
    }

    public static Bitmap getViewBitmap(View addViewContent) {
        addViewContent.setDrawingCacheEnabled(true);
        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());
        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        //获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        //计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        //取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        //得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }


    public static void saveBitmap(Bitmap bitmap, String bitName, Context context) {
        String fileName;
        File file;
        fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            // 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
            Toast.makeText(context, "二维码已经保存到手机相册中", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        // 发送广播，通知刷新图库的显示
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }


}
