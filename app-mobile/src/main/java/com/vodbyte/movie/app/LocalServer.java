package com.vodbyte.movie.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 注意这里读取文件是在主线程的。
 */
public class LocalServer {
    private Context context;
    private String api;
    private volatile boolean isFirstLoad = true;
    public static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public LocalServer(Context context, String api) {
        this.context = context;
        this.api = api;
    }

    public String get(int code, Handler handler, int handlerId) {
        switch (code) {
            case 1:
                //首次加载和刷新
                return readFile(api + "/p1.json", handler, handlerId);
        }
        return null;
    }


    private String readFile(final String path, final Handler handler, final int handlerId) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    is = context.getAssets().open(path);
                    String rs = inputStream2String(is);
                    if (!isFirstLoad) {
                        Thread.sleep(1500);//模拟网络延迟
                    } else {
                        isFirstLoad = false;
                    }
                    Message message = Message.obtain();
                    message.what = handlerId;
                    message.obj = rs;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        return "";
    }

    public  String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];

        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        in.close();
        return out.toString();
    }
}
