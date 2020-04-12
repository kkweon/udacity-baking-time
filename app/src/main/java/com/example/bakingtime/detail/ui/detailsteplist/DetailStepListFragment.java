package com.example.bakingtime.detail.ui.detailsteplist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingtime.R;
import com.example.bakingtime.SimpleIdlingResource;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeStep;
import com.example.bakingtime.databinding.FragmentDetailStepListBinding;
import com.example.bakingtime.detail.DetailViewModel;

import lombok.Setter;

import static android.app.Activity.RESULT_OK;

public class DetailStepListFragment extends Fragment
        implements Observer<Recipe>, DetailStepListAdapter.OnCardViewClickListener {

    private static final String KEY_RECIPE_ID = "KEY_RECIPE_ID";
    private static final String TAG = DetailStepListFragment.class.getSimpleName();
    private static final int REQUEST_CODE_START_DETAIL_STEP_ITEM_ACTIVITY = 0b11101101;

    @Setter private long mRecipeId;
    private DetailViewModel mViewModel;
    private FragmentDetailStepListBinding mDetailStepListFragmentBinding;
    private long INVALID_RECIPE_ID = -1;
    private DetailStepListAdapter mAdapter = new DetailStepListAdapter();
    private SimpleIdlingResource mIdlingResource;

    @Nullable private Recipe mRecipe;

    public DetailStepListFragment() {}

    public DetailStepListFragment(long recipeId, SimpleIdlingResource simpleIdlingResource) {
        mRecipeId = recipeId;
        mIdlingResource = simpleIdlingResource;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mDetailStepListFragmentBinding =
                FragmentDetailStepListBinding.inflate(inflater, container, false);
        return mDetailStepListFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(
                TAG,
                String.format(
                        "onActivityCreated() called with: savedInstanceState = [%s] recipeId = [%s]",
                        savedInstanceState, mRecipeId));
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getLong(KEY_RECIPE_ID, INVALID_RECIPE_ID);
            if (mRecipeId == INVALID_RECIPE_ID) {
                Log.wtf(TAG, "recipeId is not valid");
            }
        }

        mViewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        mViewModel.getRecipeById(mRecipeId).observe(getViewLifecycleOwner(), this);
        mViewModel
                .getSelectedRecipeStepId()
                .observe(
                        getViewLifecycleOwner(),
                        recipeStepId -> {
                            mAdapter.setMActiveRecipeStepId(recipeStepId);
                            mAdapter.notifyDataSetChanged();
                        });

        RecyclerView container = mDetailStepListFragmentBinding.recyclerViewRecipeStepsContainer;
        container.setAdapter(mAdapter);
        container.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter.setOnCardViewClickListener(this);
    }

    /**
     * Called when the data is changed.
     *
     * @param recipe The new data
     */
    @Override
    public void onChanged(Recipe recipe) {
        mRecipe = recipe;
        mAdapter.setMRecipe(recipe);
        mDetailStepListFragmentBinding.recyclerViewRecipeStepsContainer.setHasFixedSize(true);
        mAdapter.notifyDataSetChanged();

        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(KEY_RECIPE_ID, mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(RecipeStep recipeStep) {
        if (isTablet()) {
            if (mRecipe != null) {
                mViewModel.setSelectedRecipeStepId(mRecipe.getSteps().indexOf(recipeStep));
            }
            return;
        }
        // if it's a phone, start a new activity
        startDetailStepItemActivityForResult(recipeStep);
    }

    private boolean isTablet() {
        View view = requireActivity().findViewById(R.id.activity_detail_step_list_container);
        return view.getTag().equals(getString(R.string.device_tablet));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE_START_DETAIL_STEP_ITEM_ACTIVITY
                && resultCode == RESULT_OK) {
            ExtraNavButtonClickEvent buttonClickEvent =
                    (ExtraNavButtonClickEvent)
                            data.getSerializableExtra(
                                    DetailStepItemActivity.EXTRA_NAV_BUTTON_CLICKED);
            RecipeStep recipeStep =
                    data.getParcelableExtra(DetailStepItemActivity.EXTRA_RECIPE_STEP);

            if (mRecipe == null) {
                return;
            }

            int i = mRecipe.getSteps().indexOf(recipeStep);
            switch (buttonClickEvent) {
                case PREV_BUTTON_CLICKED:
                    if (0 < i) {
                        RecipeStep prevRecipeStep = mRecipe.getSteps().get(i - 1);
                        startDetailStepItemActivityForResult(prevRecipeStep);
                        return;
                    }
                    break;
                case NEXT_BUTTON_CLICKED:
                    if (i + 1 < mRecipe.getSteps().size()) {
                        RecipeStep nextRecipeStep = mRecipe.getSteps().get(i + 1);
                        startDetailStepItemActivityForResult(nextRecipeStep);
                        return;
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startDetailStepItemActivityForResult(RecipeStep recipeStep) {
        Intent intent = new Intent(getContext(), DetailStepItemActivity.class);
        intent.putExtra(DetailStepItemActivity.EXTRA_RECIPE_STEP, recipeStep);
        startActivityForResult(intent, REQUEST_CODE_START_DETAIL_STEP_ITEM_ACTIVITY);
    }
}
