package com.example.bakingtime.data;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

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
        return Single.create(
                emitter -> {
                    StringRequest stringRequest =
                            new StringRequest(
                                    Request.Method.GET,
                                    BAKING_BAKING_JSON,
                                    response -> {
                                        List<Recipe> recipes =
                                                mGson.fromJson(response, Recipe.RecipeListType);
                                        emitter.onSuccess(recipes);
                                    },
                                    error -> emitter.onError(error));

                    Log.d(TAG, "getRecipes() called");
                    mRequestQueue.add(stringRequest);
                });
    }
}
