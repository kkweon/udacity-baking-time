package com.example.bakingtime.detail.ui.detailsteplist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.bakingtime.data.RecipeStep;
import com.example.bakingtime.databinding.ActivityDetailStepItemBinding;

import java.util.Objects;

public class DetailStepItemActivity extends AppCompatActivity {
    public static final String EXTRA_RECIPE_STEP = "EXTRA_RECIPE_STEP";
    private static final String TAG = DetailStepItemActivity.class.getSimpleName();
    private ActivityDetailStepItemBinding activityDetailStepItemBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        RecipeStep recipeStep = intent.getParcelableExtra(EXTRA_RECIPE_STEP);

        if (recipeStep == null) {
            Log.d(TAG, "onCreate: found recipeStep = null");
            finish();
        }

        activityDetailStepItemBinding = ActivityDetailStepItemBinding.inflate(getLayoutInflater());
        setContentView(activityDetailStepItemBinding.getRoot());

        if (savedInstanceState == null) {

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            Fragment fragment = DetailStepItemFragment.create(recipeStep);
            supportFragmentManager
                    .beginTransaction()
                    .add(
                            activityDetailStepItemBinding.activityDetailStepItemContainer.getId(),
                            fragment)
                    .commit();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(recipeStep.getShortDescription());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
