package com.vodbyte.freetv.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class AppTools {

    private static Context context;

    public static void init(Context context) {
        AppTools.context = context;
    }

    public static String getVerName() { // 获取版本名字
        try {
            String pkName = context.getPackageName();
            return AppTools.context.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
        }
        return "";
    }

    public static int getVerCode() { // 获取版本号
        try {
            String pkName = context.getPackageName();
            return AppTools.context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
