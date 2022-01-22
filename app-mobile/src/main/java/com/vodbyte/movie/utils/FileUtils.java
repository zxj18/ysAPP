package com.vodbyte.movie.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class FileUtils {

    /**
     * 判断本地文件是否存在
     * @param context
     * @param url
     * @return
     */
    public static boolean fileExists(Context context, String url) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getFileNameByUrl(url));
        return file.exists();
    }

    public static File getFile(Context context, String url) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), getFileNameByUrl(url));
    }

    /**
     * 保存图片
     * @param context
     * @param file
     */
    public static void saveImage(Context context, File file) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),file.getName(),null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                mediaScanIntent.setData(contentUri);
                context.sendBroadcast(mediaScanIntent);
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
        }catch (Exception e) {
            Log.d("saveImage",e.getMessage());
        }
    }

    /**
     * 通过URL获取文件名
     *
     * @param url
     * @return
     */
    public static  String getFileNameByUrl(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = filename.substring(0, filename.indexOf("?") == -1 ? filename.length() : filename.indexOf("?"));
        return filename;
    }

}
