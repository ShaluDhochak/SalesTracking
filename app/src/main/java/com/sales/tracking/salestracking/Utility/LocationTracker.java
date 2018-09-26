package com.sales.tracking.salestracking.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.LocationTrackerBean;


import java.util.HashMap;
import java.util.Map;

public class LocationTracker {

    private Context context;
    private String userType, userId, latitude, longitude;

    public LocationTracker(Context context, String latitude, String longitude){
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        userType = sharedPref.getString("user_type", "");
        userId = sharedPref.getString("user_id", "");

    }

    public void trackSalesPersonAPI(){

            if (Connectivity.isConnected(context)) {
                Log.i("Location API Call","Location API Call");

                String Url = ApiLink.ROOT_URL + ApiLink.TRACK_SALES_PERSON;
                Map<String, String> map = new HashMap<>();
                map.put("update_live_location", "");
                map.put("user_id", userId);
                map.put("user_type", userType);
                map.put("user_latitude",latitude);
                map.put("user_longitude",longitude);

                GSONRequest<LocationTrackerBean> locationTrackRequest = new GSONRequest<LocationTrackerBean>(
                        Request.Method.POST,
                        Url,
                        LocationTrackerBean.class, map,
                        new com.android.volley.Response.Listener<LocationTrackerBean>() {
                            @Override
                            public void onResponse(LocationTrackerBean response) {
                                try{
                                    if (response.getSuccess() == 1){
                                        // Location Updated
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(context, "Api response Problem", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                locationTrackRequest.setShouldCache(false);
                Utilities.getRequestQueue(context).add(locationTrackRequest);
            }
    }
}
