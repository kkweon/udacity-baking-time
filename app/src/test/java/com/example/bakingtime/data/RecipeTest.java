package com.example.bakingtime.data;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class RecipeTest {
    @Test
    public void parseRecipeFromJsonTest() throws IOException {
        Gson gson = new Gson();
        List<Recipe> recipes =
                gson.fromJson(
                        new BufferedReader(
                                new FileReader(
                                        getClass()
                                                .getClassLoader()
                                                .getResource("mock_recipes.json")
                                                .getFile())),
                        Recipe.RecipeListType);

        assertEquals(1, recipes.size());

        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(
                Ingredient.builder()
                        .quantity(2)
                        .measure(IngredientMeasure.CUP)
                        .ingredient("Graham Cracker crumbs")
                        .build());

        List<RecipeStep> steps = new ArrayList<>();
        steps.add(
                RecipeStep.builder()
                        .id(0)
                        .shortDescription("Recipe Introduction")
                        .description("Recipe Introduction")
                        .videoURL(
                                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4")
                        .thumbnailURL("")
                        .build());

        Recipe expected =
                Recipe.builder()
                        .id(1)
                        .name("Nutella Pie")
                        .ingredients(ingredients)
                        .steps(steps)
                        .servings(8)
                        .image("")
                        .build();

        assertEquals(expected, recipes.get(0));
    }
}
