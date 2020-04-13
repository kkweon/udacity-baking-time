package com.example.bakingtime.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDatabaseService;

public class DetailViewModel extends ViewModel {

    private RecipeDatabaseService recipeDatabaseService;

    // Selected Recipe Step
    private MutableLiveData<Integer> mSelectedRecipeStepId = new MutableLiveData<>(0);

    public DetailViewModel(RecipeDatabaseService recipeDatabaseService) {
        this.recipeDatabaseService = recipeDatabaseService;
    }

    public LiveData<Recipe> getRecipeById(long recipeId) {
        return recipeDatabaseService.getRecipeById(recipeId);
    }

    public LiveData<Integer> getSelectedRecipeStepId() {
        return mSelectedRecipeStepId;
    }

    public void setSelectedRecipeStepId(int i) {
        mSelectedRecipeStepId.setValue(i);
    }
}
