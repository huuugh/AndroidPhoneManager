package com.app.hugh.androidphonemanager.Dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by hs on 2016/4/4.
 */
public class VirusScanDao
{
    public static boolean isVirus(String appMD5,Context ctx)
    {
        boolean flag=false;
        SQLiteDatabase database = SQLiteDatabase.openDatabase("data/data/" + ctx.getPackageName() + "/antivirus.db", null,0);
        String[] strs={appMD5};
        Cursor cursor = database.rawQuery("select * from datable where md5=?", strs);
        if(cursor.moveToNext())
        {
            flag=true;
        }
        return flag;
    }
}
