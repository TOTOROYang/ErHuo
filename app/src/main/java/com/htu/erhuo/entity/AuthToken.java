package com.htu.erhuo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Description
 * Created by yzw on 2017/3/5.
 */

public class AuthToken {
    @SerializedName("token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
