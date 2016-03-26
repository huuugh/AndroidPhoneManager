package com.app.hugh.androidphonemanager.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.R;

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

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            switch (position)
            {
                case 0:
                    Toast.makeText(HomeActivity.this, "手机防盗", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(HomeActivity.this, "通信卫士", Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    Toast.makeText(HomeActivity.this, "软件管理", Toast.LENGTH_SHORT).show();

                    break;
                case 3:
                    Toast.makeText(HomeActivity.this, "进程管理", Toast.LENGTH_SHORT).show();

                    break;
                case 4:
                    Toast.makeText(HomeActivity.this, "流量统计", Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    Toast.makeText(HomeActivity.this, "手机杀毒", Toast.LENGTH_SHORT).show();

                    break;
                case 6:
                    Toast.makeText(HomeActivity.this, "缓存清理", Toast.LENGTH_SHORT).show();

                    break;
                case 7:
                    Toast.makeText(HomeActivity.this, "高级工具", Toast.LENGTH_SHORT).show();

                    break;
                case 8:
                    startActivity(new Intent(HomeActivity.this,SettingCenterActivity.class));
                    break;
            }
        }
    }
}
