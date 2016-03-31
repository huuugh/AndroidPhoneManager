package com.app.hugh.androidphonemanager.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Dao.NumLoacationDao;
import com.app.hugh.androidphonemanager.R;

public class NumberLocationActivity extends ActionBarActivity {

    private EditText et_numlocation_input;
    private TextView tv_numlocation_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberlocation);

        et_numlocation_input = (EditText) findViewById(R.id.et_numlocation_input);
        tv_numlocation_output = (TextView) findViewById(R.id.tv_numlocation_output);

        et_numlocation_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void query(View view)
    {
        String inputnumber = et_numlocation_input.getText().toString();
        String location = NumLoacationDao.getNumberLocation(inputnumber, this);
        tv_numlocation_output.setText(location);
    }
}
