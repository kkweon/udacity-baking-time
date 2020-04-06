package com.example.bakingtime.data;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class Recipe {
    @Ignore public static Type RecipeListType = new TypeToken<List<Recipe>>() {}.getType();

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    @Embedded
    private List<Ingredient> ingredients;

    @SerializedName("steps")
    @Expose
    @Embedded
    private List<RecipeStep> steps;

    @SerializedName("servings")
    @Expose
    private long servings;

    @SerializedName("image")
    @Expose
    private String image;
}
