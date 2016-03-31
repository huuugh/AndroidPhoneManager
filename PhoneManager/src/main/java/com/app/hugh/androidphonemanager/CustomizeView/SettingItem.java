package com.app.hugh.androidphonemanager.CustomizeView;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.Application.DataShareApplication;
import com.app.hugh.androidphonemanager.R;

/**
 * Created by hs on 2016/3/25.
 */
public class SettingItem extends RelativeLayout {

    private TextView tv_setting_autoupate;
    private CheckBox cb_setting_switch;
    private String itemname;
    private String itemState_off;
    private String itemState_on;
    private SharedPreferences.Editor edit;
    private String key;
    private TextView tv_setting_state;


    public SettingItem(Context context)
    {
        super(context);
        Innit(null);
    }

    public SettingItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Innit(attrs);
    }

    public void Innit(AttributeSet attrs)
    {
        edit = DataShareApplication.sp.edit();
        /*填充布局文件*/
        View inflate = View.inflate(getContext(), R.layout.settingitem, null);
        /*找到布局文件里的组件*/
        tv_setting_autoupate = (TextView) inflate.findViewById(R.id.tv_setting_autoupate);
        cb_setting_switch = (CheckBox) inflate.findViewById(R.id.cb_setting_switch);
        tv_setting_state = (TextView)inflate.findViewById(R.id.tv_setting_state);
        /*获取attrs里的信息*/
        if(attrs!=null)
        {
            itemname = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "ItemName");
            itemState_off = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "ItemState_OFF");
            itemState_on = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "ItemState_ON");
            key = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "Key");

            /*初始化显示*/
            tv_setting_autoupate.setText(itemname);
            if(DataShareApplication.sp.getBoolean(key,true))
            {
                tv_setting_state.setText(itemState_on);
                cb_setting_switch.setChecked(true);
            }
            else
            {
                tv_setting_state.setText(itemState_off);
                cb_setting_switch.setChecked(false);
            }
        }

        setOnClickListener(new SettingItemOnClickListener());
        addView(inflate);
    }

    class SettingItemOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v)
        {
            boolean checked = cb_setting_switch.isChecked();
            if (checked)
            {
                tv_setting_state.setText(itemState_off);
                cb_setting_switch.setChecked(false);
                edit.putBoolean(key, false);
                edit.commit();
            }
            else
            {
                tv_setting_state.setText(itemState_on);
                cb_setting_switch.setChecked(true);
                edit.putBoolean(key, true);
                edit.commit();
            }
        }
    }
}
