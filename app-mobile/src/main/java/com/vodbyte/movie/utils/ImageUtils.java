package com.vodbyte.movie.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vodbyte.movie.R;
import com.vodbyte.movie.app.App;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ImageUtils {

    public static void setSongImage(String uri, ImageView imageView) {
        if (uri != null) {
            Glide.with(App.getAppContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        } else {
            Glide.with(App.getAppContext())
                    .load(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    }

    /**
     * 设置高斯模糊的背景图片
     *
     * @param uri
     */
    public static void setBackground(final String uri, final ImageView imageView) {
        if (uri != null) {
            Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {
                Glide.with(App.getAppContext())
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(uri).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Bitmap blurBitmap = blur(resource, 5);
                        subscriber.onNext(blurBitmap);
                    }
                });

            })
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> imageView.setImageBitmap(bitmap), throwable -> imageView.setImageResource(R.mipmap.ic_launcher));
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
        }
    }



    /**
     * 设置首页转盘封面
     * @param uri 图片地址
     * @param imageView 图片控件
     */
//    public static void setHomeSongCoverView(final String uri, final HomeAlbumCoverView imageView) {
//        if (uri != null) {
//
//            Observable.create((Observable.OnSubscribe<Bitmap>) subscriber -> {
//                Glide.with(App.getAppContext())
//                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).load(uri).into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        int unit = Math.min(imageView.getWidth(), imageView.getHeight()) / 8;
//                        Bitmap bitmap = ImageUtils.resizeImage(resource,unit * 6, unit * 6);
//                        subscriber.onNext(ImageUtils.createCircleImage(bitmap));
//                    }
//                });
//
//            }).subscribeOn(Schedulers.computation())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(bitmap ->  imageView.setCoverBitmap(bitmap));
//        }
//    }

    /**
     * renderScript模糊图片
     * <p>API大于17</p>
     *
     * @param context 上下文
     * @param src     源图片
     * @param radius  模糊度(1...25)
     * @return 模糊后的图片
     */
    public static Bitmap renderScriptBlur(Context context, Bitmap src, float radius) {

        RenderScript rs = null;
        try {
            Bitmap outBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
            rs = RenderScript.create(context);

            ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

            Allocation input = Allocation.createFromBitmap(rs, src);
            Allocation output = Allocation.createFromBitmap(rs, outBitmap);

            if (radius > 25) {
                radius = 25.0f;
            } else if (radius <= 0) {
                radius = 1.0f;
            }
            blurScript.setRadius(radius);
            blurScript.setInput(input);
            blurScript.forEach(output);
            output.copyTo(outBitmap);
            return outBitmap;
        } finally {
            if (rs != null) {
                rs.destroy();
            }
            src.recycle();
        }
    }


    /**
     * 高斯模糊算法
     *
     * @param sentBitmap
     * @param radius
     * @return
     */
    public static Bitmap blur(Bitmap sentBitmap, int radius) {

        // Stack Blur v1.0 from
        // http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
        //
        // Java Author: Mario Klingemann <mario at quasimondo.com>
        // http://incubator.quasimondo.com
        // created Feburary 29, 2004
        // Android port : Yahel Bouaziz <yahel at kayenko.com>
        // http://www.kayenko.com
        // ported april 5th, 2012

        // This is a compromise between Gaussian Blur and Box blur
        // It creates much better looking blurs than Box Blur, but is
        // 7x faster than my Gaussian Blur implementation.
        //
        // I called it Stack Blur because this describes best how this
        // filter works internally: it creates a kind of moving stack
        // of colors whilst scanning through the image. Thereby it
        // just has to add one new block of color to the right side
        // of the stack and remove the leftmost color. The remaining
        // colors on the topmost layer of the stack are either added on
        // or reduced by one, depending on if they are on the right or
        // on the left side of the stack.
        //
        // If you are using this algorithm in your code please add
        // the following line:
        //
        // Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    /**
     * 缩小图片，配合模糊算法
     *
     * @param path
     * @param radio 缩放比例
     * @return
     */
    public static Bitmap getScaledBitmap(String path, int radio) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = radio;
        return BitmapFactory.decodeFile(path, options);
    }


    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //不加载图片到内存中，只获取图片信息
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcHeight = options.outHeight;
        float srcWidth = options.outWidth;

        int inSamplesize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcWidth > srcHeight) {
                inSamplesize = Math.round(srcHeight / destHeight);
            } else {
                inSamplesize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSamplesize;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 粗略估值，保证不会浪费太多资源
     *
     * @param path
     * @param activity
     * @return
     */
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }


    public static Bitmap getScaledBitmap(String path, ImageView container) {

        return getScaledBitmap(path, container.getWidth(), container.getHeight());
    }

    /**
     * 将图片放大或缩小到指定尺寸
     */
    public static Bitmap resizeImage(Bitmap source, int dstWidth, int dstHeight) {
        if (source == null) {
            return null;
        }

        if (source.getWidth() == dstWidth && source.getHeight() == dstHeight) {
            return source;
        }

        return Bitmap.createScaledBitmap(source, dstWidth, dstHeight, true);
    }

    /**
     * 将图片剪裁为圆形
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }

        int length = Math.min(source.getWidth(), source.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(source.getWidth() / 2, source.getHeight() / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

}
