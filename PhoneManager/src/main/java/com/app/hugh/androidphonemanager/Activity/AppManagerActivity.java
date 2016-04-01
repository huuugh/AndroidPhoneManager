package com.app.hugh.androidphonemanager.Activity;

import android.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.Utils.StorageAndAppUtils;
import com.app.hugh.androidphonemanager.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends ActionBarActivity implements View.OnClickListener {

    private ListView lv_appmanager_showapps;
    private TextView tv_appmanager_internalstorage;
    private TextView tv_appmanager_externalstorage;
    private List Appinfolist;
    private List SystemApplist;
    private List UserApplist;
    private TextView tv_appmanage_apptype;
    private AppInfo CurrentClickApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);

        SystemApplist=new ArrayList();
        UserApplist=new ArrayList();

        lv_appmanager_showapps = (ListView) findViewById(R.id.lv_appmanager_showapps);
        tv_appmanager_internalstorage = (TextView) findViewById(R.id.tv_appmanager_internalstorage);
        tv_appmanager_externalstorage = (TextView) findViewById(R.id.tv_appmanager_externalstorage);
        tv_appmanage_apptype = (TextView) findViewById(R.id.tv_appmanage_apptype);

        long availableSD = StorageAndAppUtils.getavailableSD();
        long availableROM = StorageAndAppUtils.getavailableROM();
        tv_appmanager_externalstorage.setText("可用的SD卡空间：\n"+bytetoMega(availableSD));
        tv_appmanager_internalstorage.setText("可用的ROM空间：\n"+bytetoMega(availableROM));
        /*应用的集合*/
        Appinfolist = StorageAndAppUtils.getpackageinfo(this);
        /*把系统应用和用户应用分开*/
        for (Object ai:Appinfolist)
        {
            if(((AppInfo)ai).isSystemApp())
            {
                SystemApplist.add(ai);
            }
            else
            {
                UserApplist.add(ai);
            }
        }
        lv_appmanager_showapps.setAdapter(new AppListAdapter());
        /*给ListView设置监听*/
        lv_appmanager_showapps.setOnItemClickListener(new AppListItemOnclickListener());
        lv_appmanager_showapps.setOnScrollListener(new AppListScrollListener());
    }

    private class AppListScrollListener implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem<=SystemApplist.size()+1)
            {
                tv_appmanage_apptype.setText("系统应用" + SystemApplist.size() + "个");
            }
            else
            {
                tv_appmanage_apptype.setText("用户应用" + UserApplist.size() + "个");
            }
        }
    }

    @Override
    public void onClick(View v)
    {
      switch (v.getId())
      {
          case R.id.ll_popup_start:
              Toast.makeText(AppManagerActivity.this, "startup()", Toast.LENGTH_SHORT).show();
              startup();
              break;
          case R.id.ll_popup_share:
              Toast.makeText(AppManagerActivity.this, "share()", Toast.LENGTH_SHORT).show();

              //share();
              break;
          case R.id.ll_popup_uninstall:
              Toast.makeText(AppManagerActivity.this, "uninstall()", Toast.LENGTH_SHORT).show();

              //uninstall();
              break;
      }
    }

    private void startup() {
    }

    private class AppListItemOnclickListener implements android.widget.AdapterView.OnItemClickListener
    {

        private PopupWindow popupwindow;

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            View PopupView = View.inflate(AppManagerActivity.this, R.layout.popup_layout, null);
            /*找出popup上的控件*/
            LinearLayout ll_popup_start = (LinearLayout) PopupView.findViewById(R.id.ll_popup_start);
            LinearLayout ll_popup_share = (LinearLayout) PopupView.findViewById(R.id.ll_popup_share);
            LinearLayout ll_popup_uninstall = (LinearLayout) PopupView.findViewById(R.id.ll_popup_uninstall);
            /*确定当前点击的是那个应用*/
            if(position>0&&position<SystemApplist.size()+1)
            {
                CurrentClickApp = (AppInfo) SystemApplist.get(position-1);
                String label = CurrentClickApp.getLabel();
                Toast.makeText(AppManagerActivity.this, "CurrentClickApp"+position+label, Toast.LENGTH_SHORT).show();
            }
            else if(position>SystemApplist.size()+2)
            {
                CurrentClickApp = (AppInfo) UserApplist.get(position-2-SystemApplist.size());
                String label = CurrentClickApp.getLabel();
                Toast.makeText(AppManagerActivity.this, "CurrentClickApp"+position+label, Toast.LENGTH_SHORT).show();
            }

            /*添加监听*/
            ll_popup_start.setOnClickListener(AppManagerActivity.this);
            ll_popup_share.setOnClickListener(AppManagerActivity.this);
            ll_popup_uninstall.setOnClickListener(AppManagerActivity.this);
            /*如果没有窗口存在就新建一个*/
            if(popupwindow==null)
            {
                popupwindow = new PopupWindow(PopupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            }
            /*如果有窗口存在就让它消失*/
            else
            {
                popupwindow.dismiss();
            }
            int[] location=new int[2];
            view.getLocationOnScreen(location);
            popupwindow.showAtLocation(view, Gravity.TOP | Gravity.LEFT, location[0] + 80, location[1]-22);
        }
    }

    private class AppListAdapter extends BaseAdapter {

        private View applistitem;
        private Holder holder;
        private ImageView iv_item4applist_icon;
        private TextView tv_applistitem_lable;
        private TextView tv_applistitem_location;
        private AppInfo appinfo;

        @Override
        public int getCount()
        {
            return Appinfolist.size();
        }
        @Override
        public Object getItem(int position)
        {
            return null;
        }
        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            /*appinfo= (AppInfo) Appinfolist.get(position);*/
            if(position==0)
            {
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("系统应用" + SystemApplist.size() + "个");
/*                tv.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
                tv.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);*/
                tv.setTextColor(Color.RED);
                tv.setBackgroundColor(Color.GRAY);
                return tv;
            }
            else if(position>0&&position<SystemApplist.size()+1)
            {
                appinfo = (AppInfo) SystemApplist.get(position-1);
            }
            else if(position==SystemApplist.size()+2)
            {
                TextView tv = new TextView(AppManagerActivity.this);
                tv.setText("用户应用" + UserApplist.size() + "个");
             /*   tv.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
                tv.setHeight(ActionBar.LayoutParams.WRAP_CONTENT);*/
                tv.setTextColor(Color.RED);
                tv.setBackgroundColor(Color.GRAY);
                return tv;
            }
            else if(position>SystemApplist.size()+2)
            {
                appinfo = (AppInfo) UserApplist.get(position-2-SystemApplist.size());
            }

            if(convertView!=null&&convertView instanceof LinearLayout)
            {
                applistitem=convertView;
                holder = (Holder) applistitem.getTag();
            }
            else
            {
                applistitem = View.inflate(AppManagerActivity.this, R.layout.item4applist, null);
                iv_item4applist_icon = (ImageView) applistitem.findViewById(R.id.iv_item4applist_icon);
                tv_applistitem_lable = (TextView) applistitem.findViewById(R.id.tv_applistitem_lable);
                tv_applistitem_location = (TextView) applistitem.findViewById(R.id.tv_applistitem_location);
                holder = new Holder(iv_item4applist_icon, tv_applistitem_lable, tv_applistitem_location);
                applistitem.setTag(holder);
            }

            if(appinfo.isInSDcard())
            {
                holder.tv_location.setText("安装在SD卡");
            }
            else
            {
                holder.tv_location.setText("安装在ROM");
            }

            holder.iv_icon.setImageDrawable(appinfo.getIcon());
            holder.tv_lable.setText(appinfo.getLabel());
            return applistitem;
        }
        /*用于优化ListView*/
        private class Holder
        {
            ImageView iv_icon;
            TextView tv_lable;
            TextView tv_location;

            public Holder(ImageView iv, TextView tv_lable, TextView tv_location) {
                this.iv_icon = iv;
                this.tv_lable = tv_lable;
                this.tv_location = tv_location;
            }
        }
    }
        /*将得到的可用空间的大小单位转化为M*/
        public String bytetoMega(long bytesize)
        {
            String fileSize = Formatter.formatFileSize(this, bytesize);
            return fileSize;
        }

}
