package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.R;

public class SetSuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsuccess);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void gotoprevious(View view) {
        startActivity(new Intent(this,SetSafeNumActivity.class));
    }

    @Override
    public void gotonext(View view) {
        Toast.makeText(SetSuccessActivity.this, "已经是最后一页，请点击设置成功继续", Toast.LENGTH_SHORT).show();
    }


}
