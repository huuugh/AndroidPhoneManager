package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;

import com.app.hugh.androidphonemanager.R;

public class AdvanceToolsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancetools);
    }

    public void numlocation(View view)
    {
        startActivity(new Intent(this,NumberLocationActivity.class));
    }
}
