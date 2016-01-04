package com.zibilal;

import android.app.Application;

import com.zibilal.data.file.FileHelper;

/**
 * Created by Bilal on 1/4/2016.
 */
public class GreenDaoCheckApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FileHelper.initInstance(getApplicationContext());
    }
}
