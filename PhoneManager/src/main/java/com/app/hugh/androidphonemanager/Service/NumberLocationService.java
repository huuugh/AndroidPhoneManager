package com.app.hugh.androidphonemanager.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.Dao.NumLoacationDao;
import com.app.hugh.androidphonemanager.R;

public class NumberLocationService extends Service {

    private View v;
    private WindowManager mWM=null;

    public NumberLocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("NumberLocationService","启动");
        TelephonyManager mTM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mTM.listen(new CustomizePhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
    }

    private class CustomizePhoneStateListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state)
            {
                case TelephonyManager.CALL_STATE_IDLE:
                    hidelocation();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    String location = NumLoacationDao.getNumberLocation(incomingNumber, NumberLocationService.this);
                    showlocation(location);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
            }
        }
    }

    private void hidelocation()
    {
        if (mWM!=null)
        {
            mWM.removeViewImmediate(v);
        }
    }

    /*自定义显示号码归属地*/
    private void showlocation(String location)
    {
        int locx = 200;
        int locy = 350;

        LayoutInflater inflate = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        /*填充布局文件*/
        v = inflate.inflate(R.layout.shownumberlocation, null);
        /*设置布局文件的背景*/
        v.setBackgroundResource(R.drawable.call_locate_gray);
        /*设置显示内容*/
        TextView tv_shownumlocation_View = (TextView) v.findViewById(R.id.tv_shownumlocation_View);
        tv_shownumlocation_View.setText(location);
        /*WindowManager管理一切显示的内容*/
        mWM = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        /*设置参数*/
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity= Gravity.LEFT|Gravity.TOP;

        /*从设置文件读取显示位置*/
        String toastx = DataShareApplication.getdata("toastx");
        String toasty = DataShareApplication.getdata("toasty");
        if (toastx!=null&&toasty!=null)
        {
            locx = Integer.parseInt(toastx);
            locy = Integer.parseInt(toasty);
        }
        /*设置窗口显示的位置*/
        params.x=locx;
        params.y=locy;

//      params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWM.addView(v, params);
    }
}
