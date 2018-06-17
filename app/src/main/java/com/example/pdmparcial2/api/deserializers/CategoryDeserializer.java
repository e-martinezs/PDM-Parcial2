package com.example.pdmparcial2.api.deserializers;

import com.example.pdmparcial2.model.Category;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CategoryDeserializer implements JsonDeserializer<Category> {

    @Override
    public Category deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Category category = new Category();
        category.setName(json.getAsString());
        return category;
    }
}
