package com.example.pdmparcial2.api;

import com.example.pdmparcial2.model.User;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User>{

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user = new User();
        JsonObject jsonObject = json.getAsJsonObject();

        user.setId(jsonObject.get("_id").getAsString());

        return user;
    }
}
