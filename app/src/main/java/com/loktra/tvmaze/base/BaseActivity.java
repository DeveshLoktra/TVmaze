package com.loktra.tvmaze.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d(getLocalClassName(), "OnCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getLocalClassName(), "OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getLocalClassName(), "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getLocalClassName(), "OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getLocalClassName(), "OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getLocalClassName(), "OnDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(getLocalClassName(), "OnRestart");
    }

    public ProgressBar getProgressBar() {
        return null;
    }

    public void showProgressbar() {
        if (getProgressBar() != null) {
            getProgressBar().setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "Unable To Show Progressbar. Make getprogressBar not null");
        }
    }

    public void hideProgressbar() {
        if (getProgressBar() != null) {
            getProgressBar().setVisibility(View.GONE);
        } else {
            Log.d(TAG, "Unable To Hide Progressbar. Make getprogressBar not null");
        }
    }
}
