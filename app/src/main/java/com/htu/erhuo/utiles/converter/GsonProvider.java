package com.htu.erhuo.utiles.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.htu.erhuo.entity.MovieEntity;

/**
 * Created by Administrator on 2016/12/29.
 * <p>
 * 联系方式：。。。
 */
public class GsonProvider {
    private GsonProvider() {}

    final static Gson gson = new GsonBuilder()
            .registerTypeAdapter(MovieEntity.class, new NoteContentDeserializer())
            .create();

    final static Gson pureGson = new Gson();
}
