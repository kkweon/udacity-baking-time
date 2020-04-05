package com.example.bakingtime;

import com.example.bakingtime.list.MainActivity;
import dagger.Component;

@Component
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
}
