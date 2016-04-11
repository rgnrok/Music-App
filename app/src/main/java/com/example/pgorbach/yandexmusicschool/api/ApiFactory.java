package com.example.pgorbach.yandexmusicschool.api;


import android.content.Context;
import android.support.annotation.NonNull;

import com.example.pgorbach.yandexmusicschool.helpers.Helper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final boolean DEBUG = true;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;

    private static OkHttpClient CLIENT;
    private static final String API_ENDPOINT = "http://download.cdn.yandex.net/mobilization-2016/";


    @NonNull
    public static ArtistService getArtistService(Context context) {
        return getRetrofit(context).create(ArtistService.class);
    }

    public static OkHttpClient createCachedClient(final Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "cache_file");

        Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(cache);
        builder.interceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String cacheHeaderValue = Helper.isNetworkAvailable(context)
                                ? "public, max-age=2419200"
                                : "public, only-if-cached, max-stale=2419200";
                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        );
        builder.networkInterceptors().add(
                new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        String cacheHeaderValue = Helper.isNetworkAvailable(context)
                                ? "public, max-age=2419200"
                                : "public, only-if-cached, max-stale=2419200";
                        Request request = originalRequest.newBuilder().build();
                        Response response = chain.proceed(request);
                        return response.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", cacheHeaderValue)
                                .build();
                    }
                }
        );
        return builder.build();
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
            builder.addInterceptor(new CacheInterceptor(context));
            //setup cache
            File httpCacheDirectory = new File(context.getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);

            //add cache to the client
            builder.cache(cache);
            CLIENT = builder.build();

        }

        Gson GSON = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        return new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create(GSON))

                .client(createCachedClient(context))
                .build();
    }
}
