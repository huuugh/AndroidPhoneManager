package com.app.hugh.androidphonemanager.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;

/**
 * Created by hs on 2016/3/29.
 */
public class StartUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("StartUpReceiver","收到广播");

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = manager.getSimSerialNumber();
        String safenum = DataShareApplication.getdata("safenum");
        if (safenum!=null)
        {
            if (!safenum.equals(simSerialNumber))
            {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safenum,null,"你的手机被换卡了",null,null);
            }
        }
    }
}
