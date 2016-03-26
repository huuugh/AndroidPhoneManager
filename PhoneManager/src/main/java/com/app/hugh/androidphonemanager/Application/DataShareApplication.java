package com.app.hugh.androidphonemanager.Application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by hs on 2016/3/25.
 */
public class DataShareApplication extends Application
{

    public static SharedPreferences sp;

    @Override
    public void onCreate()
    {
        super.onCreate();
        sp = getSharedPreferences("Config", MODE_PRIVATE);
    }
}
