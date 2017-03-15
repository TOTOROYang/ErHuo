package com.htu.erhuo.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description
 * Created by yzw on 2017/3/5.
 */

class AuthInterceptor implements Interceptor {

    private String token = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("token", token)
                .header("Content-Type", "application/json;charset=UTF-8")
                .method(original.method(), original.body());

        Request request = requestBuilder.build();

        Response response = chain.proceed(request);
        if (request.url().toString().contains("login")) {
            token = response.header("token");
            if (token == null) token = "";
        }
        Log.d("yzw token", token + "");
        return response;
    }
}
