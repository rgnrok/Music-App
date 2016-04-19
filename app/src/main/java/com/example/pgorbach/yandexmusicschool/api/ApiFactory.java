package com.example.pgorbach.yandexmusicschool.api;


import android.content.Context;
import android.support.annotation.NonNull;

import com.example.pgorbach.yandexmusicschool.api.interceptors.CacheInterceptor;
import com.example.pgorbach.yandexmusicschool.api.interceptors.LogInterceptor;
import com.example.pgorbach.yandexmusicschool.api.services.ArtistService;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {

    private static final boolean DEBUG = true;
    private static final boolean USE_CACHE = true;

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;
    private static final int CACHE_SIZE = 2 * 1024 * 1024;

    private static OkHttpClient CLIENT;
    private static final String API_ENDPOINT = "http://download.cdn.yandex.net/mobilization-2016/";


    @NonNull
    public static ArtistService getArtistService(Context context) {
        return getRetrofit(context).create(ArtistService.class);
    }


    @NonNull
    private static Retrofit getRetrofit(Context context) {

        if (CLIENT == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

            if (DEBUG) {
                builder.addInterceptor(new LogInterceptor());
            }
            if (USE_CACHE && context != null) {
                File httpCacheDirectory = new File(context.getCacheDir(), "cache_file");

                Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);
                builder.cache(cache);
                builder.addNetworkInterceptor(new CacheInterceptor(context));
            }

            CLIENT = builder.build();
        }


        return new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(CLIENT)
                .build();
    }
}
