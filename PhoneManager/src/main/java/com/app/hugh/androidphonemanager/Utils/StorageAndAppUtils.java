package com.app.hugh.androidphonemanager.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.app.hugh.androidphonemanager.bean.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hs on 2016/3/31.
 */
public class StorageAndAppUtils
{
    /*获取可用的SD卡空间*/
    public static long getavailableSD()
    {
        long availableBlocks;
        long blockSize;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(externalStorageDirectory.getAbsolutePath());
        if(Build.VERSION.SDK_INT>=18)
        {
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        }
        else
        {
            availableBlocks = statFs.getAvailableBlocks();
            blockSize = statFs.getBlockSize();
        }
        return availableBlocks*blockSize;
    }
    /*获取可用的ROM空间*/
    public static long getavailableROM()
    {
        long availableBlocks;
        long blockSize;
        File dataDirectory = Environment.getDataDirectory();
        StatFs statFs = new StatFs(dataDirectory.getAbsolutePath());
        if(Build.VERSION.SDK_INT>=18)
        {
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        }
        else
        {
            availableBlocks = statFs.getAvailableBlocks();
            blockSize = statFs.getBlockSize();
        }
        return availableBlocks*blockSize;
    }


    public static List<AppInfo> getpackageinfo(Context ctx)
    {
        ArrayList<AppInfo> AppInfoList = new ArrayList<>();

        PackageManager packageManager = ctx.getPackageManager();
        List<ApplicationInfo> installedPackagesinfo = packageManager.getInstalledApplications(0);
        for (ApplicationInfo mAI:installedPackagesinfo)
        {
            boolean isSystem;
            boolean isSDCARD;

            String lable = mAI.loadLabel(packageManager).toString();
            Drawable icon = mAI.loadIcon(packageManager);

            if( (mAI.flags &   mAI.FLAG_SYSTEM )!=0 ) //=   1 | 4;
            {
                //系统应用
                isSystem=true;
            }else
            {
                //用户自己安装的应用
                isSystem=false;
            }

            if( (mAI.flags &   mAI.FLAG_EXTERNAL_STORAGE )!=0 ) //=   1 | 4;
            {
                //SDCARD
                isSDCARD=true;
            }else
            {
                //非SDcard安装
                isSDCARD=false;
            }

            AppInfo appInfo = new AppInfo(lable,icon,isSDCARD,isSystem);

            AppInfoList.add(appInfo);
        }
        return  AppInfoList;
    }
}
