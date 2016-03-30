package com.app.hugh.androidphonemanager.Receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by hs on 2016/3/29.
 */

/*用于获取设备权限*/
public class DeviceAdminReceiver4Manager extends DeviceAdminReceiver
{
    @Override
    public void onEnabled(Context context, Intent intent) {
        //super.onEnabled(context, intent);
        Toast.makeText(context, "onEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        //super.onDisabled(context, intent);
        Toast.makeText(context, "onDisabled", Toast.LENGTH_SHORT).show();
    }
}
