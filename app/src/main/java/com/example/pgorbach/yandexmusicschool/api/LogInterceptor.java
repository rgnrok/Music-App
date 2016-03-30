package com.example.pgorbach.yandexmusicschool.api;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


public class LogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Buffer buffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(buffer);
        }
        Logger.d("Request " + request.method() + " to " + request.url().toString() + "\n" + buffer.readUtf8());
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String msg = response.body().string();
        Logger.d(String.format("Response from %s in %.1fms%n\n%s",
                response.request().url().toString(), (t2 - t1) / 1e6d, msg));
        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), msg))
                .build();
    }
}
