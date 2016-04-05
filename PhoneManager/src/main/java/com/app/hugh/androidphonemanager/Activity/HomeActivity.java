package com.app.hugh.androidphonemanager.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.MD5_Utils;

public class HomeActivity extends ActionBarActivity {

    private GridView gv_home_func;
    private final int FUNC_COUNT=9;

    private int[] icon={R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                    R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                    R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings};
    private String[] funcname={"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理"
                                ,"高级工具","设置中心"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*隐藏ActionBar*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tv_home_marquee = (TextView) findViewById(R.id.tv_home_marquee);
        /*设置被选中状态，保证滚动*/
        tv_home_marquee.setSelected(true);
        gv_home_func = (GridView) findViewById(R.id.gv_home_func);
        gv_home_func.setAdapter(new FuncListAdapter());
        gv_home_func.setOnItemClickListener(new FuncListOnclickListener());
    }

    class FuncListAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return FUNC_COUNT;
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

            View inflate = View.inflate(HomeActivity.this, R.layout.item4gridview, null);
            ImageView iv_item_icon = (ImageView) inflate.findViewById(R.id.iv_item_icon);
            TextView tv_item_name = (TextView) inflate.findViewById(R.id.tv_item_name);
            iv_item_icon.setImageResource(icon[position]);
            tv_item_name.setText(funcname[position]);
            return inflate;
        }
    }

    class FuncListOnclickListener implements AdapterView.OnItemClickListener {

        private EditText et_dialog_inputpsw;
        private String psw_phoneBak;
        private Button bt_dialog_confirminput;
        private Button bt_dialog_cancelinput;
        private AlertDialog inputalertDialog;
        private EditText et_dialog_setpsw;
        private EditText et_dialog_cofirmsetpsw;
        private Button bt_dialog_confirmset;
        private Button bt_dialog_cancelset;
        private AlertDialog setalertDialog;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            switch (position)
            {
                case 0:
                    /*打开手机防盗*/
                    psw_phoneBak = DataShareApplication.getdata("psw_PhoneBak");
                    if (psw_phoneBak!=null)
                    {
                        inputpsw();
                    }
                    else
                    {
                        setpsw();
                    }
                    break;

                case 1:
                    Toast.makeText(HomeActivity.this, "通信卫士", Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    Toast.makeText(HomeActivity.this, "软件管理", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
                    break;
                case 3:
                    Toast.makeText(HomeActivity.this, "进程管理", Toast.LENGTH_SHORT).show();

                    break;
                case 4:
                    Toast.makeText(HomeActivity.this, "流量统计", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    Toast.makeText(HomeActivity.this, "手机杀毒", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, AntiVirusActivity.class));
                    break;
                case 6:
                    Toast.makeText(HomeActivity.this, "缓存清理", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this, CleanCacheActivity.class));
                    break;
                case 7:
                    Toast.makeText(HomeActivity.this, "高级工具", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HomeActivity.this,AdvanceToolsActivity.class));
                    break;
                case 8:
                    startActivity(new Intent(HomeActivity.this,SettingCenterActivity.class));
                    break;
            }
        }

        private void setpsw()
        {
            /*显示设置密码的对话框*/
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            View setinflate = View.inflate(HomeActivity.this, R.layout.setpswdialog, null);
            builder.setView(setinflate);
            setalertDialog = builder.create();
            setalertDialog.show();
            /*取出输入的内容*/
            et_dialog_setpsw = (EditText) setinflate.findViewById(R.id.et_dialog_setpsw);
            et_dialog_cofirmsetpsw = (EditText) setinflate.findViewById(R.id.et_dialog_cofirmsetpsw);
            bt_dialog_confirmset = (Button) setinflate.findViewById(R.id.bt_dialog_confirmset);
            bt_dialog_cancelset = (Button) setinflate.findViewById(R.id.bt_dialog_cancelset);
            /*设置确定键监听*/
            bt_dialog_confirmset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String setpsw = et_dialog_setpsw.getText().toString();
                    String setpswagain = et_dialog_cofirmsetpsw.getText().toString();
                    if(setpsw.isEmpty()||setpswagain.isEmpty())
                    {
                        Toast.makeText(HomeActivity.this, "输入的内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(setpsw.equals(setpswagain))
                        {
                            /*设置密码成功，保存密码(加密)，停顿两秒进入手机防盗页面*/
                            DataShareApplication.savedata("psw_PhoneBak", MD5_Utils.pressure_tight(setpsw));
                            setalertDialog.dismiss();
                            startActivity(new Intent(HomeActivity.this, PhoneBakActivity.class));
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            /*设置取消键监听*/
            bt_dialog_cancelset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setalertDialog.dismiss();
                }
            });
        }

        private void inputpsw()
        {
            /*显示输入密码的对话框*/
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            View inputinflate = View.inflate(HomeActivity.this, R.layout.inputpswdialog,null);
            builder.setView(inputinflate);
            inputalertDialog = builder.create();
            inputalertDialog.show();
            /*取出输入的内容*/
            et_dialog_inputpsw = (EditText) inputinflate.findViewById(R.id.et_dialog_inputpsw);
            bt_dialog_confirminput = (Button) inputinflate.findViewById(R.id.bt_dialog_confirminput);
            bt_dialog_cancelinput = (Button) inputinflate.findViewById(R.id.bt_dialog_cancelinput);
            /*设置确认键监听*/
            bt_dialog_confirminput.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.e("bt_dialog_confirminput", "确认");
                    String inputpsw = et_dialog_inputpsw.getText().toString();
                    String pressure_tight = MD5_Utils.pressure_tight(inputpsw);
                    if (!pressure_tight.isEmpty())
                    {
                        if (pressure_tight.equals(psw_phoneBak))
                        {
                            inputalertDialog.dismiss();
                            startActivity(new Intent(HomeActivity.this,PhoneBakActivity.class));
                        }
                        else
                        {
                            Toast.makeText(HomeActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        Toast.makeText(HomeActivity.this, "输入密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            /*设置取消键监听*/
            bt_dialog_cancelinput.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Log.e("bt_dialog_cancelinput","取消");
                    inputalertDialog.dismiss();
                }
            });
        }
    }
}
