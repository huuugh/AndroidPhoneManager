package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;

public class PhoneBakActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebak);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /*判断是否已经设置了手机防盗*/
        String safenum = DataShareApplication.getdata("safenum");
        if(safenum!=null)
        {
            Intent intent = new Intent(this, SafeinfoActivity.class);
            intent.putExtra("safenum",safenum);
            startActivity(intent);
        }
    }

    @Override
    public void gotoprevious(View view) {
        Toast.makeText(PhoneBakActivity.this, "已经是最前一页", Toast.LENGTH_SHORT).show();
    }

    public void gotonext(View view)
    {
        startActivity(new Intent(this,BindSIMActivity.class));
    }
}
