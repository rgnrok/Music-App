package com.example.pgorbach.yandexmusicschool.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


public class Helper {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // Skip if no connection, or background data disabled
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }

        // Only update if WiFi or 3G is connected and not roaming
        int netType = info.getType();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            return info.isConnected();

        } else if (netType == ConnectivityManager.TYPE_MOBILE) {
            return info.isConnected();
        }
        return false;
    }
}
