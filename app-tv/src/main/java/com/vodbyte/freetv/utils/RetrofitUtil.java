package com.vodbyte.freetv.utils;

import com.google.gson.Gson;
import com.vodbyte.freetv.BuildConfig;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
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


    private static OkHttpClient getClient()  {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .connectTimeout(45, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(45, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(45, TimeUnit.SECONDS);//设置写入超时时间

        return builder.build();
    }
}
