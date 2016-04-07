package com.app.hugh.androidphonemanager.Application;

import android.app.ActivityManager;
import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.ProcessUtils;
import com.app.hugh.androidphonemanager.Widget.MyAppWidgetProvider;
import com.app.hugh.androidphonemanager.bean.ProcessInfo;

import java.util.List;

/**
 * Created by hs on 2016/3/25.
 */
public class DataShareApplication extends Application
{

    public static SharedPreferences sp;
    public static String path;
    private static SharedPreferences.Editor edit;
    private MyReveiver myReveiver;

    @Override
    public void onCreate()
    {
        /*注册接受widget更新的广播*/
        IntentFilter filter = new IntentFilter("com.app.hugh.widgetupdate");
        myReveiver = new MyReveiver();
        getApplicationContext().registerReceiver(myReveiver, filter);

        super.onCreate();
        /*初始化SharePreference*/
        sp = getSharedPreferences("Config", MODE_PRIVATE);
        edit = sp.edit();
        /*初始化路径*/
        path="http://192.168.3.69/Android_NetWork";
    }

    /*存取字符串参数的方法*/
    public static void savedata(String key,String value)
    {
        edit = sp.edit();
        edit.putString(key,value);
        edit.commit();
    }

    public static String getdata(String key)
    {
        String value = sp.getString(key, null);
        return value;
    }
    /*存取布尔型参数的方法*/
    public static void savebooleandata(String key,Boolean value)
    {
        edit = sp.edit();
        edit.putBoolean(key,value);
        edit.commit();
    }

    public static Boolean getbooleandata(String key)
    {
        Boolean value = sp.getBoolean(key, true);
        return value;
    }


    class MyReveiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //kill后台进程
            final ActivityManager ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            List<ProcessInfo> userprocessInfoList = ProcessUtils.getAllProcInfo(getApplicationContext());
            for (ProcessInfo pp : userprocessInfoList) {
                if (pp.getPackagename().equals("com.app.hugh.androidphonemanager"))
                    continue;
                ams.killBackgroundProcesses(pp.getPackagename());
            }
            //更新widget
            final AppWidgetManager instance = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context,MyAppWidgetProvider.class);
            RemoteViews remoteview =  new RemoteViews("com.app.hugh.androidphonemanager", R.layout.processmanager_appwidget);

            final long availableRam = ProcessUtils.getAvailableRam(context);
            remoteview.setTextViewText(R.id.tv_processwidget_memory, "可用内存:" +Formatter.formatFileSize(context,availableRam));

            final int runningProcessCount = ProcessUtils.getRunningProcessCount(context);
            remoteview.setTextViewText(R.id.tv_processwidget_count, "总进程数:" + runningProcessCount);
            instance.updateAppWidget(name, remoteview);
        }
    }
}
