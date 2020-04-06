package com.example.bakingtime.list;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bakingtime.BakingTimeApplication;
import com.example.bakingtime.data.Recipe;
import com.example.bakingtime.data.RecipeNetworkService;
import com.example.bakingtime.databinding.ActivityMainBinding;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mActivityMainBinding;

    @Inject RecipeNetworkService mRecipeNetworkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BakingTimeApplication) getApplicationContext()).mApplicationComponent.inject(this);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        mActivityMainBinding.textViewHelloWorld.setText("Set from onCreate");

        this.mRecipeNetworkService
                .getRecipes()
                .subscribe(
                        new SingleObserver<List<Recipe>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {}

                            @Override
                            public void onSuccess(@NonNull List<Recipe> recipes) {
                                Log.d(TAG, "onSuccess() called with: recipes = [" + recipes + "]");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, "onError() called with: e = [" + e + "]");
                            }
                        });
    }
}
