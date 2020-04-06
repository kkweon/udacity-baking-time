package com.example.bakingtime.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.bakingtime.BakingTimeApplication;
import com.example.bakingtime.data.RecipeDatabaseService;
import com.example.bakingtime.databinding.DetailActivityBinding;
import com.example.bakingtime.detail.ui.detailsteplist.DetailStepListFragment;
import javax.inject.Inject;
import lombok.SneakyThrows;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    private static final int INVALID_RECIPE_ID = -1;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private DetailActivityBinding mDetailActivityBinding;

    @Inject RecipeDatabaseService recipeDatabaseService;
    private DetailViewModel mDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BakingTimeApplication) getApplicationContext()).mApplicationComponent.inject(this);

        mDetailActivityBinding = DetailActivityBinding.inflate(getLayoutInflater());
        setContentView(mDetailActivityBinding.getRoot());

        mDetailViewModel =
                new ViewModelProvider(
                                this,
                                new ViewModelProvider.Factory() {
                                    @SneakyThrows
                                    @NonNull
                                    @Override
                                    public <T extends ViewModel> T create(
                                            @NonNull Class<T> modelClass) {
                                        return modelClass
                                                .getDeclaredConstructor(RecipeDatabaseService.class)
                                                .newInstance(recipeDatabaseService);
                                    }
                                })
                        .get(DetailViewModel.class);

        Intent intent = getIntent();
        long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);

        if (recipeId == INVALID_RECIPE_ID) {
            finish();
            return;
        }

        if (savedInstanceState == null) {

            Log.d(TAG, "onCreate: creating a new fragment");
            DetailStepListFragment fragment = new DetailStepListFragment();
            fragment.setRecipeId(recipeId);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(mDetailActivityBinding.container.getId(), fragment)
                    .commitNow();
        }
    }
}
