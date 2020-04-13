package com.example.bakingtime.list;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.test.espresso.IdlingResource;
import com.example.bakingtime.BakingTimeApplication;
import com.example.bakingtime.IngredientsWidget;
import com.example.bakingtime.SimpleIdlingResource;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeDatabaseService;
import com.example.bakingtime.databinding.ActivityMainBinding;
import com.example.bakingtime.detail.DetailActivity;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements Observer<List<Recipe>>, OnCardClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @Inject RecipeDatabaseService mRecipeDatabaseService;
    private ActivityMainBinding mActivityMainBinding;
    private RecipesViewModel mRecipesViewModel;
    private RecipeAdapter mAdapter;

    @Nullable private SimpleIdlingResource mIdlingResource;

    /**
     * Called when the data is changed.
     *
     * @param recipes The new data
     */
    @Override
    public void onChanged(List<Recipe> recipes) {
        mAdapter.setMRecipes(recipes);
        mAdapter.notifyDataSetChanged();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    /**
     * Go to the detail view activity. Called when the card view item is clicked.
     *
     * @param recipe
     */
    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_RECIPE_ID, recipe.getId());

        saveToSharedPreferences(recipe);

        maybeUpdateWidgets();
        startActivity(intent);
    }

    private void maybeUpdateWidgets() {
        Intent intent = new Intent(this, IngredientsWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        int[] ids =
                AppWidgetManager.getInstance(getApplication())
                        .getAppWidgetIds(
                                new ComponentName(getApplication(), IngredientsWidget.class));
        if (ids.length == 0) {
            return;
        }
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }

    private void saveToSharedPreferences(Recipe recipe) {
        SharedPreferences sharedPreferences =
                getSharedPreferences(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_RECIPE, MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_RECIPE_NAME,
                        recipe.getName())
                .putString(
                        BakingTimeApplication.SHARED_PREFERENCES_SELECTED_INGREDIENTS,
                        recipe.getIngredients()
                                .stream()
                                .map(
                                        ing ->
                                                String.format(
                                                        Locale.getDefault(),
                                                        "%d %s %s",
                                                        (int) ing.getQuantity(),
                                                        ing.getMeasure(),
                                                        ing.getIngredient()))
                                .collect(Collectors.joining("\n")))
                .apply();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BakingTimeApplication) getApplicationContext()).mApplicationComponent.inject(this);

        // initialize view model
        initializeFields();
        setContentView(mActivityMainBinding.getRoot());

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mRecipesViewModel.getRecipes().observe(this, this);
        mActivityMainBinding.recyclerViewRecipeContainer.setLayoutManager(
                new LinearLayoutManager(this));
        mActivityMainBinding.recyclerViewRecipeContainer.setAdapter(mAdapter);
    }

    private void initializeFields() {
        mRecipesViewModel = getRecipesViewModelProvider().get(RecipesViewModel.class);
        mAdapter = new RecipeAdapter(null, this, this);

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
