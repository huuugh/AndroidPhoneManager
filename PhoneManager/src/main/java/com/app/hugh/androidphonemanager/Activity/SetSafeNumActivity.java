package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;

public class SetSafeNumActivity extends BaseActivity {

    private Button bt_setsafenum_selectnum;
    private EditText et_setsafenum_inputnum;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setsafenum);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        et_setsafenum_inputnum = (EditText) findViewById(R.id.et_setsafenum_inputnum);
        bt_setsafenum_selectnum = (Button) findViewById(R.id.bt_setsafenum_selectnum);
        bt_setsafenum_selectnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*跳入选择号码页面*/
                startActivityForResult(new Intent(SetSafeNumActivity.this, SelectNumActivity.class), 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==1000)
        {
            num = data.getStringExtra("num");
            Log.e("get",num);
            et_setsafenum_inputnum.setText(num);
        }
    }

    /*页面的跳转*/
    public void gotonext(View view)
    {
        startActivity(new Intent(this, SetSuccessActivity.class));
        String inputnum = et_setsafenum_inputnum.getText().toString();
        DataShareApplication.savedata("safenum",inputnum);
    }
    public void gotoprevious(View view)
    {
        startActivity(new Intent(this,BindSIMActivity.class));
    }

}
