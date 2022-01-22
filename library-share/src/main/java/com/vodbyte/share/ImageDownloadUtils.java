package com.vodbyte.share;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 下载分享图片文件

 */
public class ImageDownloadUtils {

    private static String TAG = "图片下载服务";
    public static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final String CACHE_FILENAME_PREFIX = "share_";
    private ExecutorService LIMITED_TASK_EXECUTOR = (ExecutorService) Executors.newFixedThreadPool(1);
    private ExecutorService DEFAULT_TASK_EXECUTOR = LIMITED_TASK_EXECUTOR;
    private static Object lock = new Object();
    // 下载状态监听，提供回调
    private DownloadImageStateListener listener;
    private String downloadPath;
    // 下载链接集合
    private List<String> listURL;
    private List<String> filePaths = new ArrayList<>();
    // 下载个数
    private int size = 0;

    // 下载完成回调接口
    public interface DownloadImageStateListener {
        public void onFinish(List<String> filePaths);
        public void onFailed();
    }

    public ImageDownloadUtils(Context context, List<String> listURL, ImageDownloadUtils.DownloadImageStateListener listener) {
        this.downloadPath = context.getExternalCacheDir() + File.separator + "images";
        this.listURL = listURL;
        this.listener = listener;
    }

    /**
     * 开始下载
     */
    public void startDownload(boolean isClearDir) {

        // 首先检测downloadPath是否存在
        File downloadDirectory = new File(downloadPath);
        try {
            if (!downloadDirectory.exists()) {  //判断下载目录是否存在  如果不存在则创建
                if (!downloadDirectory.mkdirs()) {
                    Log.i("startDownload ", "文件夹创建失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isClearDir) {
            FileUtils.deleteFilesInDir(downloadPath);
        }
        if (filePaths.size() > 1) {
            filePaths.clear();
        }

        for (final String url : listURL) {
            //捕获线程池拒绝执行异常
            try {
                // 线程放入线程池
                DEFAULT_TASK_EXECUTOR.execute(new Runnable() {
                    @Override
                    public void run() {downloadImage(url);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailed();
            }

        }

    }

    /**
     * 下载图片
     *
     * @param loadUrl
     * @return
     */
    private File downloadImage(String loadUrl) {

        if (!loadUrl.contains("http")) {

            File cacheFile = new File(loadUrl);
            if (cacheFile.exists()) {
                if (!filePaths.contains(loadUrl)) {
                    filePaths.add(loadUrl);
                }
                statDownloadNum();
            }else {
                listener.onFailed();
            }

            return cacheFile;
        }

        // 自定义图片命名
        String filePath = createFilePath(new File(downloadPath)) + EncryptUtils.encryptMD5ToString(TimeUtils.getNowMills() + "").toLowerCase() + ".jpg";

        if (!filePaths.contains(filePath)) {
            filePaths.add(filePath);

            File cacheFile = new File(filePath);

            HttpURLConnection urlConnection = null;
            BufferedOutputStream out = null;
            try {
                final URL url = new URL(loadUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                final InputStream in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
                out = new BufferedOutputStream(new FileOutputStream(cacheFile), IO_BUFFER_SIZE);
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                // 每下载成功一个，统计一下图片个数

                statDownloadNum();
                return cacheFile;
            } catch (final IOException e) {
                // 有一个下载失败，则表示批量下载没有成功

                e.printStackTrace();

                Log.e(TAG, "下载 " + loadUrl + " 错误");
                listener.onFailed();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "下载图片错误 - " + e);
                    }
                }
            }

        }

        return null;

    }

    /**
     * 创建文件路径
     *
     * @param cacheDir 缓存路径
     * @return 文件名
     */
    public static String createFilePath(File cacheDir) {
        return cacheDir.getAbsolutePath() + File.separator + CACHE_FILENAME_PREFIX;
    }

    /**
     * 统计下载个数
     */
    private void statDownloadNum() {
        synchronized (lock) {
            size++;
            if (size == listURL.size()) {
                // 释放资源
                DEFAULT_TASK_EXECUTOR.shutdownNow();
                // 如果下载成功的个数与列表中 url个数一致，说明下载成功

                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFinish(filePaths); // 下载成功回调
                    }
                });
            }
        }
    }


}
