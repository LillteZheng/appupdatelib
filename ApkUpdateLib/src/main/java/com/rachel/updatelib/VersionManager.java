package com.rachel.updatelib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.rachel.updatelib.callback.VersionCallback;
import com.rachel.updatelib.enties.LocalInfo;
import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.toolUtils.log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by zhengshaorui on 2017/4/20.
 */

public class VersionManager {

    private Context mContext ;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private VersionManager(Context context){
        mContext = context;
    };
    private volatile static VersionManager mVersionManager;
    public static VersionManager getInstance(Context context){
        if (mVersionManager == null){
            synchronized (VersionManager.class){
                if (mVersionManager == null){
                    mVersionManager = new VersionManager(context);
                }
            }
        }
        return mVersionManager;
    }

    public void checkUpdate(String jsonurl, VersionCallback callback){

        getServerInfo(jsonurl,callback);
    }
    /**
     * 获取本地版本
     * @param context
     * @return
     */
    public LocalInfo getLocalInfo(Context context){
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            LocalInfo localInfo = new LocalInfo();
            localInfo.setVersioncode(packageInfo.versionCode);
            localInfo.setVersionname(packageInfo.versionName);
            return localInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void getServerInfo(final String url,final  VersionCallback callback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String jsondata = getJsonFromUrl(url);
                if (jsondata != null) {
                    try {
                        JSONObject Root = new JSONObject(jsondata);
                        final FileInfo apkInfo = new FileInfo();
                        apkInfo.setFilename("floatball" + ".apk");
                        apkInfo.setVersioncode(Root.getInt("versioncode"));
                        apkInfo.setVersionname(Root.getString("versionname"));
                        apkInfo.setVersionmsg(Root.getString("versionmsg"));
                        // apkInfo.setFileurl(Root.getString("apkurl"));
                        apkInfo.setFileurl("http://downloads.jianshu.io/apps/haruki/jianShu-release-2.1.3-JianShu.apk");
                        apkInfo.setFilesize(getFileSize(apkInfo.getFileurl()));


                        Log.d(TAG, "apk: " + apkInfo);
                        final LocalInfo localInfo = getLocalInfo(mContext);
                        if (localInfo.getVersioncode() < apkInfo.getVersioncode()) {
                            // Toast.makeText(mContext, "可以更新", Toast.LENGTH_SHORT).show();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.success(apkInfo,localInfo);
                                }
                            });

                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callback.lastest();
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "eroor: " + e.toString());
                    }
                }
            }
        }).start();

    }
    /**
     * 从服务器获取更新信息
     * @param url
     * @return
     */
    private String getJsonFromUrl(String url){
        StringBuffer sb = null;
        HttpURLConnection connection =null;
        try {
            URL httpurl = new URL(url);
            connection = (HttpURLConnection) httpurl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(20000);
            connection.setConnectTimeout(20000);
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(),"utf-8"));
            sb = new StringBuffer();
            String line = null;
            while( (line = br.readLine())!= null ){
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.d("getjson: "+e.toString());
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        if (sb == null){
            return null;
        }
        return sb.toString();
    }

    /**
     * 根据url或者文件大小
     * @param url
     * @return
     */
    public int getFileSize(String url){
        StringBuffer sb = null;
        HttpURLConnection connection =null;
        try {
            URL httpurl = new URL(url);
            connection = (HttpURLConnection) httpurl.openConnection();
            //加上这句是为了防止connection.getContentLength()获取不到
            connection.setRequestProperty(
                    "Accept",
                    "image/gif, image/jpeg, image/pjpeg, image/pjpeg, " +
                            "application/x-shockwave-flash, application/xaml+xml, " +
                            "application/vnd.ms-xpsdocument, application/x-ms-xbap, " +
                            "application/x-ms-application, application/vnd.ms-excel, " +
                            "application/vnd.ms-powerpoint, application/msword, */*");
            connection.setRequestProperty("Accept-Language", "zh-CN");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Accept-Encoding", "identity");
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            int filesize = connection.getContentLength();
            return filesize;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (connection != null){
                connection.disconnect();
            }
        }
        return 0;
    }



}
