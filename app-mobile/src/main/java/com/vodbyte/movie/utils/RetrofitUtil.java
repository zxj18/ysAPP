package com.vodbyte.movie.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.vodbyte.movie.BuildConfig;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    public static Retrofit bind(String BASE_URL) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();

        if (BuildConfig.isDebug) {
//            Log.d("URL: ",BASE_URL);
        }

        return retrofit;
    }

    public static Retrofit bind(String BASE_URL, Gson gson) {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient())
                .build();

        return retrofit;
    }

    private static OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(15, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS);//设置写入超时时间
        return builder.build();
    }
}
