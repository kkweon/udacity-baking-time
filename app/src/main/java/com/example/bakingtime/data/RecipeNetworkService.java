package com.example.bakingtime.data;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeNetworkService {
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
                new SingleOnSubscribe<List<Recipe>>() {
                    @Override
                    public void subscribe(@NonNull final SingleEmitter<List<Recipe>> emitter)
                            throws Throwable {

                        StringRequest stringRequest =
                                new StringRequest(
                                        Request.Method.GET,
                                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Do something with the response
                                                List<Recipe> recipes =
                                                        mGson.fromJson(
                                                                response, Recipe.RecipeListType);
                                                emitter.onSuccess(recipes);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            /**
                                             * Callback method that an error has been occurred with
                                             * the provided error code and optional user-readable
                                             * message.
                                             *
                                             * @param error
                                             */
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                emitter.onError(error);
                                            }
                                        });

                        mRequestQueue.add(stringRequest);
                    }
                });
    }
}
