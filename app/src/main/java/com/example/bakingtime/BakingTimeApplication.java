package com.example.bakingtime;

import android.app.Application;

public class BakingTimeApplication extends Application {
    public ApplicationComponent mApplicationComponent = DaggerApplicationComponent.create();
}
