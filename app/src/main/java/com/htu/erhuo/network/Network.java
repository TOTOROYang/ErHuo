package com.htu.erhuo.network;

import com.htu.erhuo.entity.EntityResponse;
import com.htu.erhuo.entity.MovieEntity;
import com.htu.erhuo.entity.UserInfo;
import com.htu.erhuo.network.api.Api;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Description
 * Created by yzw on 2017/2/13.
 */

public class Network {
    //    public static final String BASE_URL = "http://api.douban.com/v2/movie/";
    private static final String BASE_URL = "http://192.168.2.198:8080/erhuo/";
    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private Api api;

    private Network() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthInterceptor());
//        try {
//            httpClientBuilder.sslSocketFactory(getSSLSocketFactory()).hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ComplexGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        api = retrofit.create(Api.class);
    }

    public static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext
                .getSocketFactory();
    }

    //在访问Network时创建单例
    private static class SingletonHolder {
        private static final Network INSTANCE = new Network();
    }

    //获取单例
    public static Network getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * demo
     * 用于获取豆瓣电影Top250的数据
     *
     * @param start 起始位置
     * @param count 获取长度
     */
    public Observable<MovieEntity> getTopMovie(int start, int count) {
        return api.getTopMovie(start, count)
                .subscribeOn(Schedulers.io());
    }

    public Observable<EntityResponse> login(UserInfo userinfo) {
        return api.login(userinfo)
                .subscribeOn(Schedulers.io());

    }

    public Observable<EntityResponse<UserInfo>> getUserInfo(String account) {
        return api.getUserInfo(account)
                .subscribeOn(Schedulers.io());

    }

    public Observable<EntityResponse> setUserInfo(String account, UserInfo userInfo) {
        return api.setUserInfo(account, userInfo)
                .subscribeOn(Schedulers.io());

    }
}
