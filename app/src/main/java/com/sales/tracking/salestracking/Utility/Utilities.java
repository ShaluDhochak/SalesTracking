package com.sales.tracking.salestracking.Utility;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class Utilities {
    public static RequestQueue getRequestQueue(Context context) {
        return VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static void serverError(Context context) {
        Toast.makeText(context, "Server Error.Try again later.", Toast.LENGTH_SHORT).show();
    }

    public static void internetConnectionError(Context context) {
        Toast.makeText(context, "Check Internet Connectivity.", Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

