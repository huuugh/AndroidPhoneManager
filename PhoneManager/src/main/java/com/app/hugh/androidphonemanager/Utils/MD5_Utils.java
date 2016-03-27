package com.app.hugh.androidphonemanager.Utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hs on 2016/3/26.
 */

/*首先得到一个信息摘要器 MessageDigest 然后设置MD5
		 我们获取要加密的数据 例如 password=“123456”
		 将这些数据转换成字节数据 password.getBytes()
		 通过摘要器获取字节数组的摘要
		 循环对每个字节数进行处理
		 这里主要是做2个处理 一个是 对每个字节数据进行 与运算 一般是与上一个16进制的数
		 例如 int number = p $ 0xff;
		 然后 将得到的number'进行转换为16进制的数
		 Integer.toHexString(number);
		 如果这个数转换后的长度为1 那么我们就补0
		 不满八个二进制那么我们就补全*/
public class MD5_Utils
{
    public static StringBuffer result;
    public static String pressure_tight(String password)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(password.getBytes());
            result=new StringBuffer();
            for(byte b:bytes)
            {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                Log.e("hexString",hexString);//注释
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
        return result.toString();
    }
}
