package com.example.pdmparcial2.api;

import com.example.pdmparcial2.model.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDeserializer implements JsonDeserializer<User>{

    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user = new User();
        JsonObject jsonObject = json.getAsJsonObject();

        user.setId(jsonObject.get("_id").getAsString());

        List<String> news = new ArrayList<>();
        JsonArray array = jsonObject.getAsJsonArray("favoriteNews");
        System.out.println("FAVORITES "+array.toString());
        for (int i=0; i<array.size(); i++){
            news.add(array.get(i).getAsString());
        }
        user.setFavoriteNews(news);

        return user;
    }
}
