package com.loktra.tvmaze.base;

import android.app.Application;

import com.loktra.tvmaze.repository.api.WebService;
import com.loktra.tvmaze.utils.GsonHelper;

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();

    public static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        initGlobalAppComponents();
    }

    private void initGlobalAppComponents() {

        WebService.init();
        GsonHelper.getInstance();
        //SharedPrefUtil.init(this);
        BaseApplication.application = this;
    }
}
