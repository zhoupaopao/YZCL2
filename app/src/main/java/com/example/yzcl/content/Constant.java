package com.example.yzcl.content;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Lenovo on 2017/11/28.
 */

public class Constant {
    //用于存放sharedpreferences的键值对的键
    public static String Token="Token";
    public static String username="username";
    public static String userid="userid";
    public static String password="password";
    public static String Url_head="";
    //判断网络是否可用
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
