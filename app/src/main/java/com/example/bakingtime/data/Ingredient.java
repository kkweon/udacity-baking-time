package com.example.bakingtime.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
    @SerializedName("quantity")
    @Expose
    private float quantity;

    @SerializedName("measure")
    @Expose
    private IngredientMeasure measure;

    @SerializedName("ingredient")
    @Expose
    private String ingredient;
}
