package com.landmarkremark.utils;

import android.app.Application;
import android.content.Context;

public class LandmarkRemark extends Application {

    private static LandmarkRemark instance;

    public LandmarkRemark() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    public static synchronized LandmarkRemark getInstance() {
        return instance;
    }


}
