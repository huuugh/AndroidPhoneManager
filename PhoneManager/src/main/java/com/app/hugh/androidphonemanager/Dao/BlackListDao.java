package com.app.hugh.androidphonemanager.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.hugh.androidphonemanager.Db.BlacklistDbHelper;
import com.app.hugh.androidphonemanager.bean.BlackListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/4/5.
 */
public class BlackListDao
{

    private final BlacklistDbHelper dbHelper;
    private static SQLiteDatabase db;
    private static int update;

    public BlackListDao(Context ctx)
    {
        dbHelper = new BlacklistDbHelper(ctx,"blacklist.db",null,1);
        db = dbHelper.getReadableDatabase();
    }

    public static long addblacknum(String num,int type)
    {
        ContentValues values = new ContentValues();
        values.put("number",num);
        values.put("type",type);
        return  db.insert("blacklist", null,values);
    }

    public static int deleteblacknum(String num)
    {
        return db.delete("blacklist", "number=?", new String[]{num});
    }

    public static int updateblacknum(String num,int type)
    {
        Log.e("type",type+"");
        if(type==1||type==2||type==3)
        {
        ContentValues values = new ContentValues();
        values.put("type", type);
            update = db.update("blacklist", values, "number=?", new String[]{num});
        }
        return update;
    }

    public static int getnumbertype(String num)
    {
        int type = -1;
        Cursor cursor = db.rawQuery("select type from blacklist where number=?", new String[]{num});
        while (cursor.moveToNext())
        {
            type = cursor.getInt(0);
            return type;
        }
        return type;
    }

    public static List<BlackListItem> Initblacklist()
    {
        ArrayList<BlackListItem> bli_list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from blacklist", null);
        while (cursor.moveToNext())
        {
            String num = cursor.getString(0);
            int type = cursor.getInt(1);
            BlackListItem bli = new BlackListItem(num, type);
            bli_list.add(bli);
        }
        return bli_list;
    }
}
