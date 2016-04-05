package com.app.hugh.androidphonemanager.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.StorageAndAppUtils;
import com.app.hugh.androidphonemanager.bean.AppCacheInfo;
import com.app.hugh.androidphonemanager.bean.AppInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CleanCacheActivity extends ActionBarActivity {

    private TextView tv_cleancache_scanstate;
    private ProgressBar pb_cleancache_scan;
    private ListView lv_cleancache_cachelist;
    private PackageManager mPm;
    private ArrayList<AppCacheInfo> appcachelist;
    private MyAdpater adpater;
    private Button bt_cleancache_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleancache);

        tv_cleancache_scanstate = (TextView) findViewById(R.id.tv_cleancache_scanstate);
        pb_cleancache_scan = (ProgressBar) findViewById(R.id.pb_cleancache_scan);
        lv_cleancache_cachelist = (ListView) findViewById(R.id.lv_cleancache_cachelist);
        bt_cleancache_clean = (Button) findViewById(R.id.bt_cleancache_clean);

        new AsyncTask<Void,Integer,Float>(){


            private List<AppInfo> allAppInfo;
            int count =0;
            @Override
            protected void onPreExecute() {



                allAppInfo = StorageAndAppUtils.getpackageinfo(CleanCacheActivity.this);


                pb_cleancache_scan.setMax(allAppInfo.size());

                mPm = CleanCacheActivity.this.getPackageManager();

                appcachelist = new ArrayList<AppCacheInfo>();

                super.onPreExecute();
            }

            @Override
            protected Float doInBackground(Void... params) {

                //子线程内做耗时操作

                while(count<allAppInfo.size()){


                    //拿到每个应用的缓存信息：
                    //反射
                    //mPm.getPackageSizeInfo(allAppInfo.get(count).getPackagename(), mStatsObserver);

                    try {
                        final Class<?> pmClass = CleanCacheActivity.this.getClassLoader().loadClass("android.content.pm.PackageManager");

                        final Method getPackageSizeInfo = pmClass.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

                        getPackageSizeInfo.invoke(mPm,allAppInfo.get(count).getPackagename(),mStatsObserver);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    publishProgress(++count);

                }

                return null;
            }

            @Override
            protected void onPostExecute(Float aFloat) {


                if (appcachelist.size()==0){
                    tv_cleancache_scanstate.setText("扫描完成！没有需要要清理的缓存...");
                    //没有缓存可清理的case
                }else{
                    tv_cleancache_scanstate.setText("扫描完成！");
                    adpater = new MyAdpater();
                    lv_cleancache_cachelist.setAdapter(adpater);
                    bt_cleancache_clean.setVisibility(View.VISIBLE);
                    super.onPostExecute(aFloat);
                }

            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                pb_cleancache_scan.setProgress(values[0]);
                super.onProgressUpdate(values);
            }
        }.execute();


    }


    class MyAdpater extends BaseAdapter {


        @Override
        public int getCount() {
            return appcachelist.size();
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

            final View inflate = View.inflate(CleanCacheActivity.this, R.layout.item_cachelist,null);

            final ImageView iv_cachelist_icon = (ImageView) inflate.findViewById(R.id.iv_cachelist_icon);
            final TextView  tv_cachelist_appname = (TextView) inflate.findViewById(R.id.tv_cachelist_appname);
            final TextView  tv_cachelist_appcache = (TextView) inflate.findViewById(R.id.tv_cachelist_appcache);

            final AppCacheInfo cacheInfo = appcachelist.get(position);

            iv_cachelist_icon.setImageDrawable(cacheInfo.getIcon());
            tv_cachelist_appname.setText(cacheInfo.getName());
            tv_cachelist_appcache.setText(Formatter.formatFileSize(CleanCacheActivity.this, cacheInfo.getSize())  );

            return inflate;
        }
    }

    final IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {
        public void onGetStatsCompleted(PackageStats stats, boolean succeeded) {

            final long cacheSize = stats.cacheSize; //字节为单位

            final String packageName = stats.packageName;

            if (cacheSize>12288){

                try {
                    final ApplicationInfo applicationInfo = mPm.getApplicationInfo(packageName, 0);

                    final CharSequence name = applicationInfo.loadLabel(mPm);
                    final Drawable icon = applicationInfo.loadIcon(mPm);

                    AppCacheInfo cacheInfo  = new AppCacheInfo(cacheSize,icon,name.toString());

                    appcachelist.add(cacheInfo);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_cleancache_scanstate.setText(packageName+":"+cacheSize);
                }
            });

        }
    };


    public void cleanAllCache(View v){
        //清除所有缓存

        // 想系统申请空间，申请data storage
        // public abstract void freeStorageAndNotify(long freeStorageSize,IPackageDataObserver observer);
        // 系统把所有缓存清空之后，会call到 callback
        Class<?> pmClass = null;
        try {
            pmClass = CleanCacheActivity.class.getClassLoader()
                    .loadClass("android.content.pm.PackageManager");

            Method declaredMethod = pmClass.getDeclaredMethod(
                    "freeStorageAndNotify", Long.TYPE,
                    IPackageDataObserver.class);

            declaredMethod.invoke(mPm, Long.MAX_VALUE, new MyIPackageDataObserver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private class MyIPackageDataObserver extends IPackageDataObserver.Stub {
        // 在子线程中回调
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded)
                throws RemoteException {
            // 清理缓存完成
            appcachelist.clear();
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adpater.notifyDataSetChanged();
                }
            });
        }

    }
}

