package Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hs on 2016/3/24.
 */
public class HttpUtils
{
    public static String getText(InputStream is)
    {
        String result="";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len;
        try
        {
            while((len=is.read(bytes))!=-1)
            {
                baos.write(bytes,0,len);
            }
            baos.close();
            result = baos.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
