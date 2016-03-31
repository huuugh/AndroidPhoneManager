package com.app.hugh.androidphonemanager.Activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;

public class SetNumLocationShowActivity extends ActionBarActivity {

    private LinearLayout ll_settoastlocation;
    private int windowWidth;
    private int windowHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_setnumlocationshow);

            ll_settoastlocation = (LinearLayout) findViewById(R.id.ll_settoastlocation);

            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_settoastlocation.getLayoutParams();

            layoutParams.gravity= Gravity.LEFT|Gravity.TOP;

            layoutParams.leftMargin=200;
            layoutParams.topMargin=300;

            //获取屏幕宽度

            final WindowManager mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
            final Display defaultDisplay = mWM.getDefaultDisplay();

            DisplayMetrics metrics= new DisplayMetrics();
            defaultDisplay.getMetrics(metrics);

            windowWidth =metrics.widthPixels;
            windowHeight =metrics.heightPixels;


            ll_settoastlocation.setLayoutParams(layoutParams);

            ll_settoastlocation.setOnTouchListener(new View.OnTouchListener() {

                float startx;
                float starty;
                float endx;
                float endy;

                int finalx;
                int finaly;

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    Log.i("TAG", event.getAction() + "");

                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:

                            startx = event.getRawX();
                            starty = event.getRawY();

                            break;
                        case MotionEvent.ACTION_MOVE:

                            endx = event.getRawX();
                            endy = event.getRawY();

                            float dx = endx - startx;
                            float dy = endy - starty;

                            float oldx=  ll_settoastlocation.getX();
                            float oldy=  ll_settoastlocation.getY();

                            float oldrigth = oldx+ll_settoastlocation.getWidth();
                            float oldbottom = oldy+ll_settoastlocation.getHeight();

                            if (oldx+dx<0||oldrigth+dx> windowWidth ||oldbottom+dy> windowHeight ||oldy+dy<50){
                                break;
                            }
                            ll_settoastlocation.layout((int) (oldx + dx), (int) (oldy + dy), (int) (oldrigth + dx), (int) (oldbottom + dy));

                            finalx=(int) (oldx + dx);
                            finaly=(int) (oldy + dy);

                            //以当前move事件的终点作为下一次移动的起点
                            startx=endx;
                            starty=endy;
                            break;

                        case MotionEvent.ACTION_UP:
                            DataShareApplication.savedata("toastx", finalx + "");
                            DataShareApplication.savedata("toasty", finaly + "");
                            break;
                    }


                    return false;
                }
            });

            //要增加一个 双击让它恢复初始位置。连续点击两次，中间间隔不超过500ms
            ll_settoastlocation.setOnClickListener(new View.OnClickListener() {

                boolean firstcilck=true;
                long firstclicktime;
                @Override
                public void onClick(View v) {
                    Log.i("TAG","onClick");


                    if (firstcilck){

                        firstclicktime=System.currentTimeMillis();
                        firstcilck=false;
                    }else {

                        long secondclick =System.currentTimeMillis();

                        if (secondclick-firstclicktime<500){
                            //产生双击事件
                            Toast.makeText(SetNumLocationShowActivity.this, "双击", Toast.LENGTH_SHORT).show();
                            ll_settoastlocation.layout(200, 300, 200 + ll_settoastlocation.getWidth(), 300 + ll_settoastlocation.getHeight()  );
                            DataShareApplication.savedata("toastx", 200+"");
                            DataShareApplication.savedata("toasty", 300+"");
                            firstcilck=true;
                        }else
                        {
                            //重置
                            firstcilck=true;
                        }
                    }
                }
            });
        }
    }
}
