package com.htu.erhuo.network.api;

import com.htu.erhuo.entity.MovieEntity;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description
 * Created by yzw on 2017/2/13.
 */

public interface Api {

    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

}
