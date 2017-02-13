package com.htu.erhuo.utiles.converter;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.htu.erhuo.entity.MovieEntity;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/12/29.
 * <p>
 * 联系方式：。。。
 */
public class NoteContentDeserializer implements JsonDeserializer<MovieEntity> {

    @Override
    public MovieEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Gson gson = GsonProvider.gson;
        JsonObject jsonObject = (JsonObject) json;
//        final JsonElement image_path = jsonObject.get("image_path");
//        final JsonElement content = jsonObject.get("content");
        MovieEntity noteContent = gson.fromJson(json, MovieEntity.class);
//        if (image_path == null && content != null) {
//            noteContent = gson.fromJson(json, SimpleTextNote.class);
//        } else if (image_path != null && content == null) {
//            noteContent = gson.fromJson(json, SimpleImageNote.class);
//        }else if(image_path == null && content != null){
//            noteContent = gson.fromJson(json, Note.class);
//        }else{
//            noteContent = gson.fromJson(json,Note.class);
//        }
        return noteContent;
    }


    private String stringOrEmpty(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? "" : jsonElement.getAsString();
    }
}