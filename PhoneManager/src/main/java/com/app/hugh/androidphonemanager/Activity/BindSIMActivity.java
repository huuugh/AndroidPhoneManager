package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.CustomizeView.SettingItem;
import com.app.hugh.androidphonemanager.R;

public class BindSIMActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindsim);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SettingItem si_bindsim_bindswitch = (SettingItem) findViewById(R.id.si_bindsim_bindswitch);
        final CheckBox cb_setting_switch = (CheckBox) si_bindsim_bindswitch.findViewById(R.id.cb_setting_switch);

        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        final String simSerialNumber = manager.getSimSerialNumber();

        if(cb_setting_switch.isChecked())
        {
            DataShareApplication.savedata("imsi",simSerialNumber);
        }

        cb_setting_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb_setting_switch.isChecked())
                {
                    TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    final String simSerialNumber = manager.getSimSerialNumber();
                    DataShareApplication.savedata("imsi",simSerialNumber);
                    Log.e("imsi",simSerialNumber);
                }
                else
                {
                    DataShareApplication.savedata("imsi","");
                }
            }
        });
    }
    /*跳入上一个页面*/
    public void gotoprevious(View view)
    {
        startActivity(new Intent(this, PhoneBakActivity.class));
    }
    /*跳入下一个页面*/
    public void gotonext(View view)
    {
        String imsi = DataShareApplication.getdata("imsi");
        if (!imsi.isEmpty())
        {
            startActivity(new Intent(this, SetSafeNumActivity.class));
        }
        else
        {
            Toast.makeText(BindSIMActivity.this, "未绑定SIM卡无法使用下面的功能", Toast.LENGTH_SHORT).show();
        }
    }
}
