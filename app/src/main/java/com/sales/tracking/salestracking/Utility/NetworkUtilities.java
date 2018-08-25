package com.sales.tracking.salestracking.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtilities {
    public static final int MY_SOCKET_TIMEOUT_MS = 1 * 60 * 1000; // m1.30min

    /**
     * Check Internet connection.
     */
    public static boolean isInternet(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}