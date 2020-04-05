package com.example.bakingtime.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Data;

@Data
public class Recipe {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;

    @SerializedName("steps")
    @Expose
    private List<RecipeStep> steps = null;

    @SerializedName("servings")
    @Expose
    private long servings;

    @SerializedName("image")
    @Expose
    private String image;
}
