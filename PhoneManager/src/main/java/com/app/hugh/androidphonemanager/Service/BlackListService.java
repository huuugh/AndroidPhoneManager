package com.app.hugh.androidphonemanager.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.app.hugh.androidphonemanager.Dao.BlackListDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hs on 2016/4/6.
 */
public class BlackListService extends Service {

    private BlackListDao blackListDao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public BlackListService() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*初始化Dao*/
        blackListDao = new BlackListDao(BlackListService.this);
        /*设置监听电话状态*/
        TelephonyManager mTM = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        mTM.listen(new IterceptPhoneStateListener(),IterceptPhoneStateListener.LISTEN_CALL_STATE);
        /*监听短信广播*/
        final BlackNumberSmsReceiver BlackNumberSmsReceiver = new BlackNumberSmsReceiver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        /*将接受者的优先级设置到最大*/
        filter.setPriority(Integer.MAX_VALUE); //1000 -1000
        registerReceiver(BlackNumberSmsReceiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    private class IterceptPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state)
            {
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    int numbertype = blackListDao.getnumbertype(incomingNumber);
                    if (numbertype==1||numbertype==3)
                    {
                        Toast.makeText(BlackListService.this, "拦截电话", Toast.LENGTH_SHORT).show();
                        intereptcall();
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }
    }

    private void intereptcall()
    {
        try
        {
            final Class<?> aClass = getClassLoader().loadClass("android.os.ServiceManager");
            final Method getService = aClass.getMethod("getService", String.class);
            final IBinder invoke = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
            final ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
            iTelephony.endCall();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private class BlackNumberSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String sender = smsMessage.getOriginatingAddress();
                
                final int mode = blackListDao.getnumbertype(sender);
                if (mode==1||mode==3)
                {
                    /*拦截有序广播*/
                    abortBroadcast();
                    Toast.makeText(BlackListService.this, "拦截短信", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
