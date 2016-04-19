package com.example.pgorbach.yandexmusicschool.api.interceptors;

import android.content.Context;

import com.example.pgorbach.yandexmusicschool.helpers.Helper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//Set cache headers, load data from cache if app is offline
public class CacheInterceptor implements Interceptor {

    Context mContext;

    public CacheInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        String cacheHeaderValue = Helper.isNetworkAvailable(mContext)
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
