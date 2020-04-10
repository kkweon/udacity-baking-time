package com.example.bakingtime.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;

/**
 * Fetch recipes from network.
 *
 * <p>When the app launches, it initially downloads data and inserts to the local database. After
 * that, it works offline.
 */
@Singleton
public class RecipeNetworkService {
    private static final String BAKING_BAKING_JSON =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String TAG = RecipeNetworkService.class.getSimpleName();
    private Context mContext;
    private RequestQueue mRequestQueue;
    private Gson mGson;

    @Inject
    public RecipeNetworkService(Context context) {
        this.mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);

        mRequestQueue.start();

        mGson = new Gson();
    }

    public Single<List<Recipe>> getRecipes() {
        return Single.create(this::fetchRecipeJsonFromUdacity)
                .map(recipes -> patchRecipeWithPrevStepAndNextStep((List<Recipe>) recipes));
    }

    private void fetchRecipeJsonFromUdacity(@NonNull SingleEmitter<Object> emitter) {
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        BAKING_BAKING_JSON,
                        response -> onResponse(emitter, response),
                        emitter::onError);

        Log.d(TAG, "getRecipes() called");
        mRequestQueue.add(stringRequest);
    }

    private void onResponse(@NonNull SingleEmitter<Object> emitter, String response) {
        List<Recipe> recipes = mGson.fromJson(response, Recipe.RecipeListType);
        emitter.onSuccess(recipes);
    }

    @NotNull
    private List<Recipe> patchRecipeWithPrevStepAndNextStep(List<Recipe> recipes) {
        return recipes.stream().map(this::patchRecipe).collect(Collectors.toList());
    }

    @NotNull
    private Recipe patchRecipe(Recipe r) {
        List<RecipeStep> steps = r.getSteps();
        r.setSteps(
                IntStream.range(0, steps.size())
                        .mapToObj(i -> patchRecipeStep(steps, i))
                        .collect(Collectors.toList()));

        return r;
    }

    @NotNull
    private RecipeStep patchRecipeStep(List<RecipeStep> steps, int i) {
        RecipeStep recipeStep = steps.get(i);
        recipeStep.setPrev(true);
        recipeStep.setNext(true);

        if (i == 0) {
            recipeStep.setPrev(false);
        }

        if (i == steps.size() - 1) {
            recipeStep.setNext(false);
        }

        return recipeStep;
    }
}
