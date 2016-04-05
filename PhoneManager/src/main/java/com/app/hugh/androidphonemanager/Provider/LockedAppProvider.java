package com.app.hugh.androidphonemanager.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.app.hugh.androidphonemanager.Db.AppDbHelper;

/**
 * Created by hs on 2016/4/3.
 */
public class LockedAppProvider extends ContentProvider {

    private SQLiteDatabase rdb;

    @Override
    public boolean onCreate()
    {
        AppDbHelper appDbHelper = new AppDbHelper(getContext(), "lockedapp.db", null, 1);
        rdb = appDbHelper.getReadableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        rdb.insert("lockedapp", null, values);
        /*用于监控数据库的的变化*/
        getContext().getContentResolver().notifyChange(uri.parse("com.hugh.lockedapp"),null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        rdb.delete("lockedapp",selection,selectionArgs);
        getContext().getContentResolver().notifyChange(uri.parse("com.hugh.lockedapp"),null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
