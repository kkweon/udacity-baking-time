package com.example.bakingtime.data;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class ListJsonConverter {

    @TypeConverter
    public String fromIngredientList(List<Ingredient> xs) {
        if (xs == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.toJson(xs, type);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String xs) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>() {}.getType();

        return gson.fromJson(xs, type);
    }

    @TypeConverter
    public String fromRecipeStepList(List<RecipeStep> xs) {
        if (xs == null) {
            return null;
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<RecipeStep>>() {}.getType();
        return gson.toJson(xs, type);
    }

    @TypeConverter
    public List<RecipeStep> toRecipeStepList(String xs) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<RecipeStep>>() {}.getType();

        return gson.fromJson(xs, type);
    }
}
