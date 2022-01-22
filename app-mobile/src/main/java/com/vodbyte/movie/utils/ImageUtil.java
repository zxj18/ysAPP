package com.vodbyte.movie.utils;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import com.vodbyte.movie.R;
import com.vodbyte.movie.config.Constant;

public class ImageUtil {

    /**
     * 改变图片的亮度
     * @param imageView
     * @param brightness [-255,0]时为暗
     */
    public static void changeBrightness(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    }
}
