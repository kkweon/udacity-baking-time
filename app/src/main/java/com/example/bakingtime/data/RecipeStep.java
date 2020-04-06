package com.example.bakingtime.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeStep {
    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("videoURL")
    @Expose
    private String videoURL;

    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;
}
