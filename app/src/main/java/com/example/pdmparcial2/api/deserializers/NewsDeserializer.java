package com.example.pdmparcial2.api.deserializers;

import com.example.pdmparcial2.model.New;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

public class NewsDeserializer implements JsonDeserializer<New> {

    @Override
    public New deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        New mNew = new New();
        JsonObject newJsonObject = json.getAsJsonObject();

        if (newJsonObject.get("_id") != null) {
            mNew.setId(newJsonObject.get("_id").getAsString());
        } else {
            mNew.setId("");
        }

        if (newJsonObject.get("title") != null) {
            mNew.setTitle(newJsonObject.get("title").getAsString());
        } else {
            mNew.setTitle("");
        }

        if (newJsonObject.get("body") != null) {
            mNew.setBody(newJsonObject.get("body").getAsString());
        } else {
            mNew.setBody("");
        }

        if (newJsonObject.get("coverImage") != null) {
            mNew.setCoverImage(newJsonObject.get("coverImage").getAsString());
        } else {
            mNew.setCoverImage("");
        }

        if (newJsonObject.get("created_date") != null) {
            mNew.setCreate_date(newJsonObject.get("created_date").getAsString());
        } else {
            mNew.setCreate_date("");
        }

        if (newJsonObject.get("description") != null) {
            mNew.setDescription(newJsonObject.get("description").getAsString());
        } else {
            mNew.setDescription("");
        }

        if (newJsonObject.get("game") != null) {
            mNew.setGame(newJsonObject.get("game").getAsString());
        } else {
            mNew.setGame("");
        }

        return mNew;
    }
}