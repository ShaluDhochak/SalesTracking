package com.sales.tracking.salestracking.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.sales.tracking.salestracking.Bean.LocationTrackerBean;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LocationTracker {

    private Context context;
    private String userType, userId, latitude, longitude;

    public LocationTracker(Context context, String latitude, String longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        userType = sharedPref.getString("user_type", "");
        userId = sharedPref.getString("user_id", "");

    }

    public void trackSalesPersonAPI() {

        if (Connectivity.isConnected(context)) {
            Log.i("Location API Call", "Location API Call");

            String Url = ApiLink.ROOT_URL + ApiLink.TRACK_SALES_PERSON;
            Map<String, String> map = new HashMap<>();
            map.put("update_live_location", "");
            map.put("user_id", userId);
            map.put("user_type", userType);
            map.put("user_lattitude", latitude);
            map.put("user_longitude", longitude);

            GSONRequest<LocationTrackerBean> locationTrackRequest = new GSONRequest<LocationTrackerBean>(
                    Request.Method.POST,
                    Url,
                    LocationTrackerBean.class, map,
                    new com.android.volley.Response.Listener<LocationTrackerBean>() {
                        @Override
                        public void onResponse(LocationTrackerBean response) {
                            try {
                                if (response.getSuccess() == 1) {
                                    // Location Updated
                                }
                            } catch (Exception e) {
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public void trackSalesPersonAttendance(String attendenceType) {

        if (Connectivity.isConnected(context)) {

            String Url = ApiLink.ROOT_URL + ApiLink.ATTENDENCE;
            Map<String, String> map = new HashMap<>();
            map.put("atten_uid", userId);

            if (attendenceType.equals("IN")) {
                map.put("atten_in_add", getCompleteAddressString(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                map.put("atten_in_latitude", latitude);
                map.put("atten_in_longitude", longitude);
            } else {
                map.put("atten_out_add", "1");
                map.put("filter[atten_out_latitude]", latitude);
                map.put("filter[atten_out_longitude]", longitude);
            }

            GSONRequest<LocationTrackerBean> locationTrackRequest = new GSONRequest<LocationTrackerBean>(
                    Request.Method.POST,
                    Url,
                    LocationTrackerBean.class, map,
                    new com.android.volley.Response.Listener<LocationTrackerBean>() {
                        @Override
                        public void onResponse(LocationTrackerBean response) {
                            try {
                                if (response.getSuccess() == 1) {
                                    Utilities.showToast(context, response.getMessage());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Api response Problem", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Api response Problem", Toast.LENGTH_SHORT).show();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };


            locationTrackRequest.setShouldCache(false);
            Utilities.getRequestQueue(context).add(locationTrackRequest);
        }

    }
}
