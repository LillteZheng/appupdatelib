package com.rachel.updatelib.toolUtils;

import android.os.Environment;

import com.rachel.updatelib.UpdateLibAppLication;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/21.
 */

public class ToolUtils {

    //int
    public static String getFloatSize(int filesize){
        String data;
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (filesize > 1024*1024) {
            data = decimalFormat.format((float)filesize / 1024 /1024) + "M";

        } else {
            data = decimalFormat.format((float)filesize / 1024) + "kb";
        }
        return data;

    }
    // long
    public static String getFloatSize(long filesize){
        String data;
        DecimalFormat decimalFormat = new DecimalFormat(".00");
        if (filesize > 1024*1024) {
            data = decimalFormat.format((float)filesize / 1024 /1024) + "M";

        } else {
            data = decimalFormat.format((float)filesize / 1024) + "kb";
        }
        return data;

    }

    /**
     * 获取文件的路径
     * @return
     */
    public static String getFilePath(){
        String dir = null;
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED){
            dir = Environment
                    .getExternalStorageDirectory().getAbsolutePath() + "/mydownload/";
        }else {

            dir = UpdateLibAppLication.getContext().getCacheDir().getPath()+"/mydownload/";

        }
        return dir;
    }

}
