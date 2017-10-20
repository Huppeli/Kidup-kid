package com.kidup.kidup;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by t3math00 on 5/11/2017.
 */

public class API {
    public static final String BASE_URL = "https://kidup-server.herokuapp.com/api/";

    static CustomInterceptor interceptor = new CustomInterceptor();

    static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

    static RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());

    static Retrofit retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(rxAdapter)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit get(){
        return retrofit;
    }
}
