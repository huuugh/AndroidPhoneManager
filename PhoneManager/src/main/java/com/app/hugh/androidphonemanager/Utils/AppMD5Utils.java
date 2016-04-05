package com.app.hugh.androidphonemanager.Utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hs on 2016/4/4.
 */
public class AppMD5Utils
{
    public static StringBuffer result;
    public static String apptoMD5(String app_path)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(new File(app_path));
            int len=-1;
            byte[] bytes = new byte[1024];
            while((len=is.read(bytes))!=-1)
            {
                digest.update(bytes,0,len);
            }
            byte[] bytes1 = digest.digest();
            result=new StringBuffer();
            for(byte b:bytes1)
            {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                Log.e("hexString", hexString);//注释
                if(hexString.length()==1)
                {
                    result.append(0);
                }
                else
                {
                    result.append(hexString);
                }
            }
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result.toString();
    }
}
