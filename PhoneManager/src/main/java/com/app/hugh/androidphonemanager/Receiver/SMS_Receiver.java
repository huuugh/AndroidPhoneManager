package com.app.hugh.androidphonemanager.Receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;

/**
 * Created by hs on 2016/3/29.
 */
/*处理安全号码发来的指令*/
public class SMS_Receiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            /*获取短信的发送者和内容*/
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String body = smsMessage.getMessageBody();
            String sender = smsMessage.getOriginatingAddress();

            Log.i("sms", "onReceive " + body);

            if (body.equals("*#alarm#*"))
            {
                playalarm(context);
            }
            else if (body.equals("*#location#*"))
            {
                getlocation(context);
            }
            else if (body.equals("*#wipedata#*"))
            {
                wipedata(context);
            }
            else if (body.equals("*#lockscreen#*"))
            {
                lockscreen(context);
            }
        }
    }

    private void lockscreen(Context ctx)
    {
        /*实现锁屏，此功能需要获取设备权限*/
        DevicePolicyManager DPM = (DevicePolicyManager) ctx.getSystemService(ctx.DEVICE_POLICY_SERVICE);
        DPM.lockNow();
        /*锁屏后重新设置密码*/
       // DPM.resetPassword("123", 0);
    }

    private void wipedata(Context ctx)
    {
        /*实现删除数据，此功能需要获取设备权限*/
        DevicePolicyManager DPM = (DevicePolicyManager) ctx.getSystemService(ctx.DEVICE_POLICY_SERVICE);
        DPM.wipeData(0);
    }

    private void getlocation(Context ctx)
    {
        String longitude = null;
        String latitude = null;

        longitude = DataShareApplication.getdata("longitude");
        latitude = DataShareApplication.getdata("latitude");
        /*每次循环获取当前的毫秒数*/

        Log.e("经纬度",longitude+"__"+latitude);
        String safenum = DataShareApplication.getdata("safenum");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(safenum,null,longitude+"__"+latitude,null,null);
    }

    private void playalarm(Context ctx)
    {
        MediaPlayer mediaPlayer =   MediaPlayer.create(ctx, R.raw.alarm);
        //让硬件开始播放音频
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }
}
