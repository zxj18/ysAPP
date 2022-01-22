package com.vodbyte.movie.utils;


import java.util.Locale;

public class TimeUtil {

    public static String formatSecond(long secondCount) {
        long second = secondCount % 60;
        long minute = (secondCount - second) / 60;
        return String.format(Locale.CHINA, "%02d:%02d", minute, second);
    }

    public static StringBuilder timeParse(long duration) {
        StringBuilder time = new StringBuilder("");
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time.append("0");
        }
        time.append(minute).append(":");

        if (second < 10) {
            time.append("0");
        }
        time.append(second);
        return time;
    }


}
