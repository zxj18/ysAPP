package com.vodbyte.movie.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class Tools {

    private static Context context;

    public Tools(Context context) {
        Tools.context = context;
    }

    public static String getVerName(Context context) { // 获取版本名字
        try {
            String pkName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;

            return versionName;
        } catch (Exception e) {
        }
        return "";
    }

    public static int getVerCode(Context context) { // 获取版本号
        String pkName = context.getPackageName();
        try {
            int versionCode = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
