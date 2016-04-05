package com.app.hugh.androidphonemanager.Dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.app.hugh.androidphonemanager.Db.AppDbHelper;

/**
 * Created by hs on 2016/4/3.
 */
public class LockedAppDao
{

    private static ContentResolver contentResolver;
    Context ctx;
    private static SQLiteDatabase db;

    public LockedAppDao(Context ctx)
    {
        AppDbHelper dbHelper = new AppDbHelper(ctx, "lockedapp.db", null, 1);
        db = dbHelper.getReadableDatabase();
        contentResolver = ctx.getContentResolver();
        this.ctx=ctx;
    }
    public static void insert(String packagename)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("packagename",packagename);
        contentResolver.insert(Uri.parse("content://com.hugh.lockedapp"),contentValues);
    }
    public static void delete(String packagename)
    {
        String[] strs = {packagename};
        contentResolver.delete(Uri.parse("content://com.hugh.lockedapp"), "packagename=?", strs);
    }

    public static boolean isLocked(String packagename)
    {
        boolean locked;
        String[] strs = {packagename};
        Cursor cursor = db.rawQuery("select id from lockedapp where packagename=?", strs);
        if(cursor.moveToNext())
        {
            locked=true;
        }
        else
        {
            locked=false;
        }
        return locked;
    }
}
