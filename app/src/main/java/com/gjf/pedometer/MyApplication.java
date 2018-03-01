package com.gjf.pedometer;

import android.app.Application;
import android.content.Context;

/**
 * Created by guojunfu on 18/2/11.
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        GlobalConfig.setAppContext(this);
    }
}
