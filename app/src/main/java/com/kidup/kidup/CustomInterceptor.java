package com.kidup.kidup;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class CustomInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException{
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder().build();
        Request.Builder requestBuilder = original.newBuilder().url(url);
        Request req = requestBuilder.build();
        return  chain.proceed(req);
    }

}
