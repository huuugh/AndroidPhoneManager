package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.app.hugh.androidphonemanager.R;

public class SetSafeNumActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsafenum);
    }

    public void tonext(View view)
    {
        startActivity(new Intent(this, SetSuccessActivity.class));
    }

    public void toprevious(View view)
    {
        startActivity(new Intent(this,BindSIMActivity.class));
    }
}
