package com.app.hugh.androidphonemanager.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Dao.VirusScanDao;
import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.AppMD5Utils;
import com.app.hugh.androidphonemanager.Utils.StorageAndAppUtils;
import com.app.hugh.androidphonemanager.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusActivity extends ActionBarActivity {

    private ImageView iv_antivirus_scaning;
    private ProgressBar pb_antivirus_scan;
    private TextView tv_antivirus_currentstate;
    private ListView lv_antivirus_list;
    private List resultlist;
    private RotateAnimation animation;
    private ScanVirusAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_antivirus);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        resultlist=new ArrayList<scanresult>();

        iv_antivirus_scaning = (ImageView) findViewById(R.id.iv_antivirus_scaning);
        pb_antivirus_scan = (ProgressBar) findViewById(R.id.pb_antivirus_scan);
        tv_antivirus_currentstate = (TextView) findViewById(R.id.tv_antivirus_currentstate);
        lv_antivirus_list = (ListView) findViewById(R.id.lv_antivirus_list);
        /*添加动画*/
        startanimation(iv_antivirus_scaning);
        /*listView添加适配器*/
        adapter = new ScanVirusAdapter();
        lv_antivirus_list.setAdapter(adapter);

        new AsyncTask<Void, Integer, Void>()
        {
            private String currentpackagename;
            private boolean isvirus;
            private List<AppInfo> infolist;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                infolist = StorageAndAppUtils.getpackageinfo(AntiVirusActivity.this);
                pb_antivirus_scan.setMax(infolist.size());
            }

            @Override
            protected Void doInBackground(Void... params) {
                int count = 0;
                while(count<infolist.size())
                {
                    try
                    {
                        currentpackagename = StorageAndAppUtils.getpackageinfo(AntiVirusActivity.this).get(count).getPackagename();
                        /*子线程更新UI*/
                        runOnUiThread(new Runnable() {
                            public void run() {
                                tv_antivirus_currentstate.setText(currentpackagename);
                            }
                        });
                        ApplicationInfo appInfo = getPackageManager().getApplicationInfo(currentpackagename,0);
                        String dir = appInfo.sourceDir;
                        String appMD5 = AppMD5Utils.apptoMD5(dir);
                        Log.e("AppMD5",appMD5);
                        isvirus = VirusScanDao.isVirus(appMD5, AntiVirusActivity.this);
                    }
                    catch (PackageManager.NameNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    scanresult sr = new scanresult(currentpackagename, isvirus);
                    /*每次将新new的scanresult放在第一个用来实现ListView从上面开始刷新*/
                    resultlist.add(0,sr);
                    publishProgress(++count);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                tv_antivirus_currentstate.setText("扫描完毕");
                animation.cancel();
                super.onPostExecute(aVoid);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                pb_antivirus_scan.setProgress(values[0]);
                adapter.notifyDataSetChanged();
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    private void startanimation(ImageView iv_antivirus_scaning)
    {
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(4000);
        animation.setRepeatCount(Animation.INFINITE);
        iv_antivirus_scaning.setAnimation(animation);
        animation.start();
    }

    class scanresult
    {
        String packagename;
        boolean isVirus;

        public scanresult(String packagename, boolean isVirus) {
            this.packagename = packagename;
            this.isVirus = isVirus;
        }

        public String getPackagename() {
            return packagename;
        }

        public boolean isVirus() {
            return isVirus;
        }
    }

    private class ScanVirusAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return resultlist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(AntiVirusActivity.this);
            scanresult sc = (scanresult)resultlist.get(position);
            tv.setText(sc.getPackagename());
            if(sc.isVirus())
            {
                tv.setTextColor(Color.RED);
            }
            return tv;
        }
    }
}
