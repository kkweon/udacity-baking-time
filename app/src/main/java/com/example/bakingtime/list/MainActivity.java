package com.example.bakingtime.list;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bakingtime.BakingTimeApplication;
import com.example.bakingtime.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BakingTimeApplication) getApplicationContext()).mApplicationComponent.inject(this);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityMainBinding.getRoot());

        mActivityMainBinding.textViewHelloWorld.setText("Set from onCreate");
    }
}
