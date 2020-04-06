package com.example.bakingtime.list;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.bakingtime.BakingTimeApplication;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDatabaseService;
import com.example.bakingtime.databinding.ActivityMainBinding;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements Observer<List<Recipe>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject RecipeDatabaseService mRecipeDatabaseService;
    private ActivityMainBinding mActivityMainBinding;
    private RecipesViewModel mRecipesViewModel;
    private RecipeAdapter mAdapter;

    /**
     * Called when the data is changed.
     *
     * @param recipes The new data
     */
    @Override
    public void onChanged(List<Recipe> recipes) {
        mAdapter.setMRecipes(recipes);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BakingTimeApplication) getApplicationContext()).mApplicationComponent.inject(this);

        // initialize view model
        initializeFields();
        setContentView(mActivityMainBinding.getRoot());

        mRecipesViewModel.getRecipes().observe(this, this);
        mActivityMainBinding.recyclerViewRecipeContainer.setLayoutManager(
                new LinearLayoutManager(this));
        mActivityMainBinding.recyclerViewRecipeContainer.setAdapter(mAdapter);
    }

    private void initializeFields() {
        mRecipesViewModel = getRecipesViewModelProvider().get(RecipesViewModel.class);
        mAdapter = new RecipeAdapter(null, this);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    private ViewModelProvider getRecipesViewModelProvider() {
        return new ViewModelProvider(
                this,
                new ViewModelProvider.Factory() {
                    @Override
                    public <T extends ViewModel> T create(
                            @androidx.annotation.NonNull Class<T> modelClass) {
                        try {
                            return modelClass
                                    .getDeclaredConstructor(RecipeDatabaseService.class)
                                    .newInstance(mRecipeDatabaseService);
                        } catch (IllegalAccessException
                                | InstantiationException
                                | InvocationTargetException
                                | NoSuchMethodException e) {
                            e.printStackTrace();
                            throw new IllegalArgumentException(
                                    String.format(
                                            "Unable to create a RecipesViewModel. See error message => %s",
                                            e.getMessage()));
                        }
                    }
                });
    }
}
