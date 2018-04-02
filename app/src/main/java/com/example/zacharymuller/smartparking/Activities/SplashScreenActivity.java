package com.example.zacharymuller.smartparking.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.example.zacharymuller.smartparking.APIClient.RequestTask;
import com.example.zacharymuller.smartparking.Entities.Garages;
import com.example.zacharymuller.smartparking.R;

public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        String[] keys = {"A", "B", "C", "D", "H", "I", "Libra", "Test"};
        Garages.initGarages(keys);

        new RequestTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "A", "B", "C", "D", "H", "I", "Libra", "Test");
    }
}
