package com.example.bakingtime.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDatabaseService;

public class DetailViewModel extends ViewModel {

    private RecipeDatabaseService recipeDatabaseService;

    public DetailViewModel(RecipeDatabaseService recipeDatabaseService) {
        this.recipeDatabaseService = recipeDatabaseService;
    }

    public LiveData<Recipe> geRecipe(long recipeId) {
        return recipeDatabaseService.getRecipeById(recipeId);
    }
}
