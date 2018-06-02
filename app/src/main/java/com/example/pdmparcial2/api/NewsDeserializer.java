package com.example.pdmparcial2.api;

import com.example.pdmparcial2.model.New;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewsDeserializer implements JsonDeserializer<New>{

    @Override
    public New deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        New mNew = new New();

        JsonObject newJsonObject = json.getAsJsonObject();
        mNew.setId(newJsonObject.get("_id").getAsString());
        mNew.setTitle(newJsonObject.get("title").getAsString());
        mNew.setBody(newJsonObject.get("body").getAsString());

        if (newJsonObject.get("coverImage") != null){
            mNew.setCoverImage(newJsonObject.get("coverImage").getAsString());
        }else{
            mNew.setCoverImage("");
        }

        if (newJsonObject.get("create_date") != null){
            mNew.setCreate_date(newJsonObject.get("create_date").getAsString());
        }else{
            mNew.setCreate_date("");
        }

        if (newJsonObject.get("description") != null){
            mNew.setDescription(newJsonObject.get("description").getAsString());
        }else{
            mNew.setDescription("");
        }

        mNew.setGame(newJsonObject.get("game").getAsString());

        return mNew;
    }
}