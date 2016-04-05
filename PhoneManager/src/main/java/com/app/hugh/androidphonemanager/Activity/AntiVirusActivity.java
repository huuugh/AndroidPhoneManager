package com.app.hugh.androidphonemanager.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.R;

import java.util.List;

public class AntiVirusActivity extends ActionBarActivity {

    private ImageView iv_antivirus_scaning;
    private ProgressBar pb_antivirus_scan;
    private TextView tv_antivirus_currentstate;
    private ListView lv_antivirus_list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);

        iv_antivirus_scaning = (ImageView) findViewById(R.id.iv_antivirus_scaning);
        pb_antivirus_scan = (ProgressBar) findViewById(R.id.pb_antivirus_scan);
        tv_antivirus_currentstate = (TextView) findViewById(R.id.tv_antivirus_currentstate);
        lv_antivirus_list = (ListView) findViewById(R.id.lv_antivirus_list);
        /*添加动画*/
        startanimation(iv_antivirus_scaning);

        List<PackageInfo> installedPackages = getPackageManager().getInstalledPackages(0);
        ApplicationInfo applicationInfo = installedPackages.get(1).applicationInfo;
        applicationInfo
    }

    private void startanimation(ImageView iv_antivirus_scaning)
    {
        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(4000);
        animation.setRepeatCount(Animation.INFINITE);
        iv_antivirus_scaning.setAnimation(animation);
        animation.start();
    }
}
