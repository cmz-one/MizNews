package com.cmz_one.miznews.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cmz_o on 2016/12/14.
 */

public class RetrofitFactory {

    public static final String BASE_URL = "http://v.juhe.cn/toutiao/";
    private Retrofit retrofit;
    private static RetrofitFactory instance;

    private RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static RetrofitFactory getInstance() {
        if (instance == null) {
            synchronized (RetrofitFactory.class) {
                if (instance == null) {
                    instance = new RetrofitFactory();
                }
            }
        }
        return instance;
    }

    public RetrofitService getService() {
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }
}
