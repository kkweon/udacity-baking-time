package com.example.bakingtime.detail.ui.detailsteplist;

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
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.databinding.DetailStepListFragmentBinding;
import com.example.bakingtime.detail.DetailViewModel;
import java.util.Objects;
import lombok.Setter;

public class DetailStepListFragment extends Fragment implements Observer<Recipe> {

    private static final String KEY_RECIPE_ID = "KEY_RECIPE_ID";
    private static final String TAG = DetailStepListFragment.class.getSimpleName();

    @Setter private long recipeId;
    private DetailViewModel mViewModel;
    private DetailStepListFragmentBinding mDetailStepListFragmentBinding;
    private long INVALID_RECIPE_ID = -1;

    public DetailStepListFragment() {}

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mDetailStepListFragmentBinding =
                DetailStepListFragmentBinding.inflate(inflater, container, false);
        return mDetailStepListFragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(
                TAG,
                String.format(
                        "onActivityCreated() called with: savedInstanceState = [%s] recipeId = [%s]",
                        savedInstanceState, recipeId));
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong(KEY_RECIPE_ID, INVALID_RECIPE_ID);
            if (recipeId == INVALID_RECIPE_ID) {
                Log.wtf(TAG, "recipeId is not valid");
            }
        }

        mViewModel =
                new ViewModelProvider(Objects.requireNonNull(getActivity()))
                        .get(DetailViewModel.class);
        mViewModel.geRecipe(recipeId).observe(getViewLifecycleOwner(), this);
    }

    /**
     * Called when the data is changed.
     *
     * @param recipe The new data
     */
    @Override
    public void onChanged(Recipe recipe) {
        if (recipe != null) {
            mDetailStepListFragmentBinding.message.setText(recipe.getName());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(KEY_RECIPE_ID, recipeId);
        super.onSaveInstanceState(outState);
    }
}
