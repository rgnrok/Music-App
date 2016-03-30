package com.example.pgorbach.yandexmusicschool.api;


import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactory {
    private static final boolean DEBUG = true;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;

    private static OkHttpClient CLIENT;
    private static final String API_ENDPOINT = "https://academy.yandex.ru/events/mobdev/msk-2016";



    @NonNull
    public static ArtistService getArtistService() {
        return getRetrofit().create(ArtistService.class);
    }


    @NonNull
    private static Retrofit getRetrofit() {

        if (CLIENT == null) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);

            if (DEBUG) {
                builder.addInterceptor(new LogInterceptor());
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
