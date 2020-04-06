package com.example.bakingtime.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeDatabaseService {

    private final RecipeDatabase mDatabase;
    private Context mContext;

    @Inject
    public RecipeDatabaseService(Context context) {
        mContext = context;
        mDatabase = RecipeDatabase.getDatabase(context);
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mDatabase.recipeDao().getRecipes();
    }

    public void addRecipes(List<Recipe> recipes) {
        RecipeDatabase.databaseWriteExecutor.execute(
                () -> mDatabase.recipeDao().addRecipes(recipes));
    }

    public void addRecipe(Recipe recipe) {
        RecipeDatabase.databaseWriteExecutor.execute(() -> mDatabase.recipeDao().addRecipe(recipe));
    }

    public LiveData<Recipe> getRecipeById(int recipeId) {
        return mDatabase.recipeDao().getRecipeById(recipeId);
    }
}
