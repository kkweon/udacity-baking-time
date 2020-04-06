package com.example.bakingtime.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDatabaseService;
import java.util.List;
import javax.inject.Inject;

public class RecipesViewModel extends ViewModel {

    private RecipeDatabaseService recipeDatabaseService;
    private LiveData<List<Recipe>> mRecipes = null;

    @Inject
    public RecipesViewModel(RecipeDatabaseService recipeDatabaseService) {
        this.recipeDatabaseService = recipeDatabaseService;
    }

    public LiveData<List<Recipe>> getRecipes() {
        if (mRecipes == null) {
            mRecipes = recipeDatabaseService.getRecipes();
        }

        return mRecipes;
    }
}
