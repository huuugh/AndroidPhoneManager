package com.app.hugh.androidphonemanager.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.ProcessUtils;

/**
 * Created by hs on 2016/4/2.
 */
//广播接收者
public class MyAppWidgetProvider extends AppWidgetProvider{

    private static final String TAG ="MyAppWidgetProvider" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"onReceive");
        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.i(TAG,"onDeleted");
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Log.i(TAG,"onDisabled");
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        Log.i(TAG, "onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i(TAG,"onUpdate");
        //跨进程的更新
        //告诉系统要去更新哪个widget
        ComponentName name = new ComponentName(context,MyAppWidgetProvider.class);

        RemoteViews   remoteview =  new RemoteViews("com.app.hugh.androidphonemanager", R.layout.processmanager_appwidget);
        final long availableRam = ProcessUtils.getAvailableRam(context);
        remoteview.setTextViewText(R.id.tv_processwidget_memory, "可用内存:" + Formatter.formatFileSize(context,availableRam));

        final int runningProcessCount = ProcessUtils.getRunningProcessCount(context);
        remoteview.setTextViewText(R.id.tv_processwidget_count, "总进程数:" + runningProcessCount);

        Intent intent = new Intent("com.app.hugh.widgetupdate");
        PendingIntent pdintent = PendingIntent.getBroadcast(context,100,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteview.setOnClickPendingIntent(R.id.btn_widget_clear,pdintent);
        appWidgetManager.updateAppWidget(name,remoteview);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
