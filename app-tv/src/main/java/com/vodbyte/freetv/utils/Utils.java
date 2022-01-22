package com.vodbyte.freetv.utils;


import android.content.Context;

public class Utils {
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static String formatTime(int time) {
        String result = "";
        int h = time / 3600;
        int m = (time % 3600) / 60;
        int s = time % 60;
        if (h > 0) {
            result = h + ":";
        }
        if (m < 10) {
            result = result + "0" + m + ":";
        } else {
            result = result + m + ":";
        }
        if (s < 10) {
            result = result + "0" + s;
        } else {
            result = result + s;
        }
        return result;
    }
}
