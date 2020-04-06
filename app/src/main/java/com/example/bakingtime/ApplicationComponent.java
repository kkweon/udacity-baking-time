package com.example.bakingtime;

import android.content.Context;
import com.example.bakingtime.data.RecipeNetworkService;
import com.example.bakingtime.detail.DetailActivity;
import com.example.bakingtime.list.MainActivity;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;

@Component
@Singleton
public interface ApplicationComponent {

    RecipeNetworkService recipeNetworkService();

    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);

        ApplicationComponent build();
    }
}
