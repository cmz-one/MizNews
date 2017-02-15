package com.cmz_one.miznews.http;

import com.cmz_one.miznews.bean.NewsItemBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by cmz_o on 2016/12/14.
 */

public interface RetrofitService {

    @GET("index")
    Call<NewsItemBean> getNews(@Query("key")String key, @Query("type")String type);
}
