package com.app.hugh.androidphonemanager.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Service.NumberLocationService;

import java.util.List;

public class SettingCenterActivity extends ActionBarActivity {

    private RelativeLayout item_setting_numlocation;
    private CheckBox cb_setting_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        item_setting_numlocation = (RelativeLayout) findViewById(R.id.item_setting_numlocation);
        cb_setting_switch = (CheckBox) item_setting_numlocation.findViewById(R.id.cb_setting_switch);
        item_setting_numlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(cb_setting_switch.isChecked())
                {
                    startService(new Intent(SettingCenterActivity.this, NumberLocationService.class));
                    DataShareApplication.savebooleandata("ShowNumLocation", false);
                    cb_setting_switch.setChecked(false);
                    Toast.makeText(SettingCenterActivity.this, "(\"ShowNumLocation\", false)", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    stopService(new Intent(SettingCenterActivity.this, NumberLocationService.class));
                    DataShareApplication.savebooleandata("ShowNumLocation", true);
                    cb_setting_switch.setChecked(true);
                    Toast.makeText(SettingCenterActivity.this, "(\"ShowNumLocation\", true)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*根据服务是否在运行来显示CheckBox的状态*/
    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager mAM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = mAM.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo ars:runningServices)
        {
            String className = ars.service.getClassName();
            if(className.equals("com.app.hugh.androidphonemanager.Service.NumberLocationService"))
            {
                cb_setting_switch.setChecked(true);
            }
            else
            {
                cb_setting_switch.setChecked(true);
            }
        }
    }

    public void settoastlocation(View view)
    {
        startActivity(new Intent(this,SetNumLocationShowActivity.class));
    }
}
