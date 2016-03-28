package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.app.hugh.androidphonemanager.R;

public class BindSIMActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindsim);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Object systemService = getSystemService(TELEPHONY_SERVICE);

    }
    /*跳入上一个页面*/
    public void gotoprevious(View view)
    {
        startActivity(new Intent(this, PhoneBakActivity.class));
    }
    /*跳入下一个页面*/
    public void gotonext(View view)
    {
        startActivity(new Intent(this,SetSafeNumActivity.class));
    }
}
