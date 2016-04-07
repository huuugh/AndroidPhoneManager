package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Service.BlackListService;
import com.app.hugh.androidphonemanager.Service.GetLocation;

public class SafeinfoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safeinfo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tv_safeinfo_shownum = (TextView) findViewById(R.id.tv_safeinfo_shownum);
        ImageView iv_safeinfo_showstate = (ImageView) findViewById(R.id.iv_safeinfo_showstate);

        Intent intent = getIntent();
        if(intent!=null)
        {
            String safenum = intent.getStringExtra("safenum");
            tv_safeinfo_shownum.setText(safenum);
            iv_safeinfo_showstate.setImageResource(R.drawable.lock);
        }
        else
        {
            iv_safeinfo_showstate.setImageResource(R.drawable.unlock);
        }
    }

    public void reset(View view)
    {
        startActivity(new Intent(this,BindSIMActivity.class));
    }
}
