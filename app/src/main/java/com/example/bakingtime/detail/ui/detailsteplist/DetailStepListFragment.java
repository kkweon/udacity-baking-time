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

import com.example.bakingtime.SimpleIdlingResource;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeStep;
import com.example.bakingtime.databinding.FragmentDetailStepListBinding;
import com.example.bakingtime.detail.DetailViewModel;

import lombok.Setter;

public class DetailStepListFragment extends Fragment
        implements Observer<Recipe>, DetailStepListAdapter.OnCardViewClickListener {

    private static final String KEY_RECIPE_ID = "KEY_RECIPE_ID";
    private static final String TAG = DetailStepListFragment.class.getSimpleName();

    @Setter private long mRecipeId;
    private DetailViewModel mViewModel;
    private FragmentDetailStepListBinding mDetailStepListFragmentBinding;
    private long INVALID_RECIPE_ID = -1;
    private DetailStepListAdapter mAdapter = new DetailStepListAdapter();
    private SimpleIdlingResource mIdlingResource;

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
        mViewModel.geRecipe(mRecipeId).observe(getViewLifecycleOwner(), this);

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
        // if it's a phone, start a new activity
        Intent intent = new Intent(getContext(), DetailStepItemActivity.class);
        intent.putExtra(DetailStepItemActivity.EXTRA_RECIPE_STEP, recipeStep);
        startActivity(intent);
    }
}
