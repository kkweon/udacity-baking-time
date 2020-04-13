package com.example.bakingtime;

import android.app.Application;

public class BakingTimeApplication extends Application {
    public static final String SHARED_PREFERENCES_SELECTED_RECIPE =
            "SHARED_PREFERENCES_SELECTED_RECIPE";
    public static final String SHARED_PREFERENCES_SELECTED_RECIPE_NAME =
            "SHARED_PREFERENCES_SELECTED_RECIPE_NAME";
    public static final String SHARED_PREFERENCES_SELECTED_INGREDIENTS =
            "SHARED_PREFERENCES_SELECTED_INGREDIENTS";
    public ApplicationComponent mApplicationComponent =
            DaggerApplicationComponent.builder().context(this).build();
}
