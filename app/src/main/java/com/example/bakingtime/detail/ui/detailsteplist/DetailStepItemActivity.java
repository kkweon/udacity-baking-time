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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DetailStepItemActivity extends AppCompatActivity implements DetailStepItemFragment.OnNavClickListener {
    public static final String EXTRA_RECIPE_STEP = "EXTRA_RECIPE_STEP";
    public static final String EXTRA_NAV_BUTTON_CLICKED = "EXTRA_NAV_BUTTON_CLICKED";
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
            return;
        }

        activityDetailStepItemBinding = ActivityDetailStepItemBinding.inflate(getLayoutInflater());
        setContentView(activityDetailStepItemBinding.getRoot());

        if (savedInstanceState == null) {

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            Fragment fragment = DetailStepItemFragment.create(recipeStep, this);
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

    @Override
    public void onClickPrev(RecipeStep recipeStep) {
        Intent intent = getNextStateIntent(recipeStep, ExtraNavButtonClickEvent.PREV_BUTTON_CLICKED);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClickNext(RecipeStep recipeStep) {
        Intent intent = getNextStateIntent(recipeStep, ExtraNavButtonClickEvent.NEXT_BUTTON_CLICKED);
        setResult(RESULT_OK, intent);
        finish();
    }

    @NotNull
    private Intent getNextStateIntent(RecipeStep recipeStep, ExtraNavButtonClickEvent nextButtonClicked) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_NAV_BUTTON_CLICKED, nextButtonClicked);
        intent.putExtra(EXTRA_RECIPE_STEP, recipeStep);
        return intent;
    }
}
