package com.app.hugh.androidphonemanager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Utils.HttpUtils;

public class SplashActivity extends ActionBarActivity {
    final  int GET_OK = 1;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getCurrentVersion();
        versionInfoFromServer();
    }

    public int getCurrentVersion() //获取当前app的版本号
    {
        int CurrentVersionCode = 0;
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            CurrentVersionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return CurrentVersionCode;
    }

    public void versionInfoFromServer()//从服务器获取最新版本的信息
    {
        //final String path="";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.3.69/Android_NetWork/newversion.json");
                    HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setRequestMethod("GET");
                    urlConn.setConnectTimeout(5000);
                    urlConn.setReadTimeout(5000);
                    urlConn.connect();
                    int responseCode = urlConn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = urlConn.getInputStream();
                        String JsonFromServer = HttpUtils.getText(is);
                        if (JsonFromServer != null) {
                            JSONObject jsonObject = new JSONObject(JsonFromServer);
                            String describe = jsonObject.getString("describe");
                            String version_name = jsonObject.getString("version_name");
                            String download_url = jsonObject.getString("download_url");
                            String[] info = new String[]{version_name, describe, download_url};

                            //下面进入疯狂Log模式
                            Log.e("描述", describe);
                            Log.e("版本名", version_name);
                            Log.e("下载地址", download_url);

                            Message message = MsgHandler.obtainMessage();
                            message.what = GET_OK;
                            message.obj = info;
                            MsgHandler.sendMessage(message);
                            is.close();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    Handler MsgHandler = new Handler()//处理versionInfoFromServer发来的message
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch (msg.what)
            {
                case GET_OK:
                    final String[] info = (String[]) msg.obj;
                    float newversioncode = Float.parseFloat(info[0]);
                    if (newversioncode > getCurrentVersion())
                    {
                        new AlertDialog.Builder(SplashActivity.this)
                                .setTitle("更新提示")
                                .setMessage(info[1])
                                .setCancelable(false)
                                .setPositiveButton("确认更新", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        //开始下载
                                        Toast.makeText(SplashActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
                                        AsyncHttpClient client = new AsyncHttpClient();
                                        client.get("http://192.168.3.69/Android_NetWork"+info[2],new NewResponseHandler());
                                    }
                                })
                                .setNegativeButton("下次再说", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //进入主页
                                        Toast.makeText(SplashActivity.this, "进入主页", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                    break;
            }
        }
    };

    /*实现msgHandler里面的下载NewResponseHandler*/

    class NewResponseHandler extends AsyncHttpResponseHandler
    {
        File file=null;
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {

            /*添加进度条*/
            pd = new ProgressDialog(SplashActivity.this);
            pd.setMessage("正在下载");
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
            pd.setCancelable(true);// 设置是否可以通过点击Back键取消
            pd.setCanceledOnTouchOutside(false);
            pd.setProgress(i);
            pd.show();

            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/anzhistore.apk");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.close();
                /*此处取消进度条*/
                pd.cancel();
                /*此处调用installApk()方法安装下载的新版的应用*/
                installApk();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable)
        {
            Toast.makeText(SplashActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
        }

        /*此方法用于安装下载好的新版本应用*/
        private void installApk()
        {
            if(file!=null)
            {
                Intent intent =new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            }
        }
    }

}
