package com.app.hugh.androidphonemanager.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.R;

public abstract class BaseActivity extends ActionBarActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        gestureDetector = new GestureDetector(this,new CustomizeGestureDetector());
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class CustomizeGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x1 = e1.getX();
            float x2 = e2.getX();

            WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = mWM.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            float xdpi = metrics.xdpi;
            Log.e("xdpi",xdpi+"");
           // float ydpi = metrics.ydpi;

            if (x2-x1>xdpi/3)
            {
                gotoprevious(null);
                Toast.makeText(BaseActivity.this, "右划", Toast.LENGTH_SHORT).show();
            }
            else if(x1-x2>xdpi/3)
            {
                gotonext(null);
                Toast.makeText(BaseActivity.this, "左划", Toast.LENGTH_SHORT).show();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public abstract void gotoprevious(View view);

    public abstract void gotonext(View view);

}
