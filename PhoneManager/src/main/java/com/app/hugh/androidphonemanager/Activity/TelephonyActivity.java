package com.app.hugh.androidphonemanager.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Dao.BlackListDao;
import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.bean.BlackListItem;

import org.w3c.dom.Text;

import java.util.List;

public class TelephonyActivity extends ActionBarActivity {

    private View inflateview;
    private EditText et_addnumdialog_iuputnum;
    private RadioGroup rg_addnumdialog_settype;
    private Button bt_addblacknum_confirm;
    private Button bt_addblacknum_cancle;
    private BlackListDao bld;
    private List<BlackListItem> initblacklist;
    private BlacklistAdapter blacklistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telephony);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /*初始化*/
        bld=new BlackListDao(this);
        /*获取已经加入黑名单的号码*/
        initblacklist = bld.Initblacklist();
        Button bt_telephony_addnum = (Button) findViewById(R.id.bt_telephony_addnum);
        ListView lv_telephony_blacknumlist = (ListView) findViewById(R.id.lv_telephony_blacknumlist);
        /*ListView添加适配器*/
        blacklistAdapter = new BlacklistAdapter();
        lv_telephony_blacknumlist.setAdapter(blacklistAdapter);
        /*ListView添加长按监听*/
        lv_telephony_blacknumlist.setOnItemLongClickListener(new BlackListLongClickListener());

        /*添加号码的Button添加监听*/
        bt_telephony_addnum.setOnClickListener(new AddblacknumListener());
    }

    /*长按listView弹出popup*/
    private class BlackListLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {

        private PopupWindow popupWindow;

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            View blacklistpopup = View.inflate(TelephonyActivity.this, R.layout.blacklist_popup, null);
            TextView tv_blacklistpopup_delete = (TextView) blacklistpopup.findViewById(R.id.tv_blacklistpopup_delete);
            TextView tv_blacklistpopup_update = (TextView) blacklistpopup.findViewById(R.id.tv_blacklistpopup_update);

            final String currentnumber = initblacklist.get(position).getNumber();
            final int currenttype = initblacklist.get(position).getType();

            tv_blacklistpopup_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bld.deleteblacknum(currentnumber);
                    initblacklist.remove(currentnumber);
                    blacklistAdapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            });

            tv_blacklistpopup_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TelephonyActivity.this);
                    builder.setSingleChoiceItems(new String[]{"拦截电话", "拦截短信", "拦截全部"}, currenttype - 1, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.e("which",which+"");
                            switch (which)
                            {
                                case 0:
                                    initblacklist.get(position).setType(which + 1);
                                    bld.updateblacknum(currentnumber,which+1);
                                    break;
                                case 1:
                                    initblacklist.get(position).setType(which+1);
                                    bld.updateblacknum(currentnumber,which+1);
                                    break;
                                case 2:
                                    initblacklist.get(position).setType(which+1);
                                    bld.updateblacknum(currentnumber,which+1);
                                    break;
                            }
                            blacklistAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                    popupWindow.dismiss();
                }
            });
            if (popupWindow!=null)
            {
                popupWindow.dismiss();
            }
            popupWindow = new PopupWindow(blacklistpopup, ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
            /*获取当前item的位置*/
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            Log.e("location", location[0] + "_" + location[1]);
            popupWindow.showAsDropDown(view, location[0] + 350, 0);
            return true;
        }
    }

    private class BlacklistAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return initblacklist.size();
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
            View blacklistitem = View.inflate(TelephonyActivity.this,R.layout.blacklistitem,null);
            TextView tv_blacklistitem_num = (TextView) blacklistitem.findViewById(R.id.tv_blacklistitem_num);
            TextView tv_blacklistitem_type = (TextView) blacklistitem.findViewById(R.id.tv_blacklistitem_type);

            String number = initblacklist.get(position).getNumber();
            int type = initblacklist.get(position).getType();
            String refusemode = null;
            if (type==1)
            {
                refusemode="拦截电话";
            }
            else if(type==2)
            {
                refusemode="拦截短信";
            }
            else if(type==3)
            {
                refusemode="拦截电话和短信";
            }
            tv_blacklistitem_num.setText(number);
            tv_blacklistitem_type.setText(refusemode);
            return blacklistitem;
        }
    }

    private class AddblacknumListener implements View.OnClickListener {

        private AlertDialog dialog;

        @Override
        public void onClick(View v) {
            /*下面这一段放在oncreate中会报错，因为每次点击使用的都是同一个view*/
            inflateview = View.inflate(TelephonyActivity.this, R.layout.dialog_addblacknum, null);
            /*找到布局文件里的控件*/
            et_addnumdialog_iuputnum = (EditText)inflateview.findViewById(R.id.et_addnumdialog_iuputnum);
            rg_addnumdialog_settype = (RadioGroup)inflateview.findViewById(R.id.rg_addnumdialog_settype);

            AlertDialog.Builder builder = new AlertDialog.Builder(TelephonyActivity.this);
            builder.setView(inflateview);
            builder.setCancelable(false);
            builder.setPositiveButton("确认添加", new DialogInterface.OnClickListener() {

                private String inputmum;
                private int type;

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputmum = et_addnumdialog_iuputnum.getText().toString();
                    int crbi = rg_addnumdialog_settype.getCheckedRadioButtonId();
                    if(crbi==R.id.rb_addnum_call)
                    {
                        type = 1;
                    }
                    else if(crbi==R.id.rb_addnum_sms)
                    {
                        type = 2;
                    }
                    else if(crbi==R.id.rb_addnum_all)
                    {
                        type = 3;
                    }
                    bld.addblacknum(inputmum,type);
                    BlackListItem listItem = new BlackListItem(inputmum, type);
                    initblacklist.add(0,listItem);
                    blacklistAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();
        }
    }
}
