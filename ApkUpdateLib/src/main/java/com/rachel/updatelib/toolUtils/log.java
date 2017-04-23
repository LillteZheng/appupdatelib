package com.rachel.updatelib.toolUtils;

import android.util.Log;

/**
 * Created by zhengshaorui on 2017/4/15.
 */

public class log {
    private static final String TAG = "zsr";
    private static final boolean DEBUG = true;

    public static void d(String msg){
        if (DEBUG){
            Log.d(TAG, msg);
        }

    }
}
