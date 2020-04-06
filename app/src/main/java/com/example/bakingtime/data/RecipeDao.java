package com.example.bakingtime.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * from Recipe ORDER BY name ASC")
    LiveData<List<Recipe>> getRecipes();

    @Query("DELETE FROM Recipe")
    void deleteAllRecipes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addRecipe(Recipe recipe);

    @Query("SELECT * from Recipe where id = :recipeId")
    LiveData<Recipe> getRecipeById(int recipeId);
}
