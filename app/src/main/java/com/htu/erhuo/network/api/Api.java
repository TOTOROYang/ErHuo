package com.htu.erhuo.network.api;

import android.content.ClipData;

import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.ErhuoOssToken;
import com.htu.erhuo.entity.ItemInfo;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.entity.UserInfo;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description
 * Created by yzw on 2017/2/13.
 */

public interface Api {

    @GET("top250")
    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

    @POST("user/login")
    Observable<EntityResponse> login(@Body UserInfo userInfo);

    @GET("user/{account}")
    @Headers("token:need")
    Observable<EntityResponse<UserInfo>> getUserInfo(@Path("account") String account);

    @POST("user/{account}")
    @Headers("token:need")
    Observable<EntityResponse> setUserInfo(@Path("account") String account, @Body UserInfo userInfo);

    @GET("sts/img/{key}")
    @Headers("token:need")
    Observable<EntityResponse<ErhuoOssToken>> getOssToken(@Path("key") String key);

    @POST("item")
    @Headers("token:need")
    Observable<EntityResponse> createGoods(@Body ItemInfo itemInfo);

    @GET("item/list")
    @Headers("token:need")
    Observable<EntityResponse<List<ItemInfo>>> getGoodsList(@Query("sid") String sid,
                                                            @Query("t") String title,
                                                            @Query("c") String creator,
                                                            @Query("s") String status,
                                                            @Query("pl") BigDecimal priceLow,
                                                            @Query("ph") BigDecimal priceHigh,
                                                            @Query("r") String rule,
                                                            @Query("p") int page,
                                                            @Query("offset") int office,
                                                            @Query("limit") int limit);
}
