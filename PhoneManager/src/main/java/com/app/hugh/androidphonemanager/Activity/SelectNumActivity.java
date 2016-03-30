package com.app.hugh.androidphonemanager.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.hugh.androidphonemanager.R;
import com.app.hugh.androidphonemanager.bean.Contact;

import java.util.ArrayList;
import java.util.List;

public class SelectNumActivity extends ActionBarActivity {

    private List<Contact> contactList;
    private String name;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectnum);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        /*获取Listview组件，设置适配器*/
        ListView lv_selectnum_showallnum = (ListView) findViewById(R.id.lv_selectnum_showallnum);
        lv_selectnum_showallnum.setAdapter(new ContactListAdapter());
        /*从数据库获取联系人数据*/
        contactList = getAllContact(this);

        /*设置ListView的点击事件*/
        lv_selectnum_showallnum.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Contact contact = contactList.get(position);
                num = contact.getNum();
                Intent intent = new Intent();
                intent.putExtra("num",num);
                Log.e("set",num);
                setResult(1000, intent);
                finish();
            }
        });
    }

    public static List<Contact> getAllContact(Context ctx){

        List<Contact> contactslist = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {

            int contact_id = cursor.getInt(0);
            if (contact_id==0) {
                continue;
            }
            Log.i("show one contact", contact_id + "");
            Cursor cursor2 =contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                    new String[]{"data1","mimetype"}, "raw_contact_id=?", new String[]{""+contact_id}, null);
            Contact onecontact = new Contact();
            while (cursor2.moveToNext()) {
                String data1 = cursor2.getString(0);
                String mimetype = cursor2.getString(1);

                if ("vnd.android.cursor.item/name".equals(mimetype)){
                    onecontact.setName(data1);
                }else if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                    onecontact.setNum(data1);
                }
            }
            Log.i("show one contact", onecontact.toString());
            contactslist.add(onecontact);
        }
        return contactslist;
    }

    class ContactListAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            int count;
            if (contactList!=null)
            {
                count=contactList.size();
            }
            else
            {
               count=0;
            }
            return count;
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

            View inflate = View.inflate(SelectNumActivity.this, R.layout.item4selectnum, null);
            TextView tv_item4list_name = (TextView) inflate.findViewById(R.id.tv_item4list_name);
            TextView tv_item4list_num = (TextView) inflate.findViewById(R.id.tv_item4list_num);

            Contact contact = contactList.get(position);
            name = contact.getName();
            num = contact.getNum();
            tv_item4list_name.setText(name);
            tv_item4list_num.setText(num);
            return inflate;
        }
    }
}
