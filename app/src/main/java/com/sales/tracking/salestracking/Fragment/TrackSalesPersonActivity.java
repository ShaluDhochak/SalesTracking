package com.sales.tracking.salestracking.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sales.tracking.salestracking.Activity.NavigationDrawerActivity;
import com.sales.tracking.salestracking.Adapter.CustomInfoWindowGoogleMap;
import com.sales.tracking.salestracking.Bean.InfoWindowData;
import com.sales.tracking.salestracking.Bean.SalesPersonTrackerBean;
import com.sales.tracking.salestracking.R;
import com.sales.tracking.salestracking.Utility.ApiLink;
import com.sales.tracking.salestracking.Utility.GSONRequest;
import com.sales.tracking.salestracking.Utility.Utilities;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackSalesPersonActivity extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener{

    ImageView drawerIcon_iv;

    private GoogleMap mMap;

    private String userType, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_sales_activity);

        drawerIcon_iv = (ImageView) findViewById(R.id.drawerIcon_iv);
        drawerIcon_iv.setOnClickListener(this);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        userType = sharedPref.getString("user_type", "");
        userId = sharedPref.getString("user_id", "");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        if (userType.equals("Sales Manager")){
            getManager();
        }else if (userType.equals("Manager Head")){
            getManagerHead();
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(1);
    }

    public void getManager(){
        String Url = ApiLink.ROOT_URL + ApiLink.TRACK_SALES_PERSON;
        Map<String, String> map = new HashMap<>();
        map.put("get_live_location", "");
        map.put("manager_id", userId);
        map.put("user_type", "Sales Executive");

        GSONRequest<SalesPersonTrackerBean> locationTrackRequest = new GSONRequest<>(
                Request.Method.POST,
                Url,
                SalesPersonTrackerBean.class, map,
                new com.android.volley.Response.Listener<SalesPersonTrackerBean>() {
                    @Override
                    public void onResponse(SalesPersonTrackerBean response) {
                        try {

                            if (response.getSales_person_tracking().size() > 0) {
                                setMarker(response.getSales_person_tracking());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(TrackSalesPersonActivity.this, "Api response Problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error :", error.getMessage());
                    }
                });
        locationTrackRequest.setShouldCache(false);
        Utilities.getRequestQueue(this).add(locationTrackRequest);
    }

    private void getManagerHead(){
        String Url = ApiLink.ROOT_URL + ApiLink.TRACK_MANAGER_HEAD;
        Map<String, String> map = new HashMap<>();
        map.put("get_live_location", "");
        map.put("managerhead_id", userId);
        map.put("user_type", "");

        GSONRequest<SalesPersonTrackerBean> locationTrackRequest = new GSONRequest<>(
                Request.Method.POST,
                Url,
                SalesPersonTrackerBean.class, map,
                new com.android.volley.Response.Listener<SalesPersonTrackerBean>() {
                    @Override
                    public void onResponse(SalesPersonTrackerBean response) {
                        try {

                            if (response.getSales_person_tracking().size() > 0) {
                                setMarker(response.getSales_person_tracking());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(TrackSalesPersonActivity.this, "Api response Problem", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error :", error.getMessage());
                    }
                });
        locationTrackRequest.setShouldCache(false);
        Utilities.getRequestQueue(this).add(locationTrackRequest);
    }

    private void setMarker(List<SalesPersonTrackerBean.SalesTracker> salesTrackerList){


        for (SalesPersonTrackerBean.SalesTracker salesTracker : salesTrackerList){

            LatLng latLng = new LatLng(Double.parseDouble(salesTracker.getUser_lattitude()), Double.parseDouble(salesTracker.getUser_longitude()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng)
                    .title("")
                    .snippet("")
                    .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));
            InfoWindowData infoWindowData = new InfoWindowData();
            infoWindowData.setName(salesTracker.getUser_name());
            infoWindowData.setAddress(salesTracker.getUser_type());
            infoWindowData.setContact(salesTracker.getUser_mobile());

            final Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(infoWindowData);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7.0f));
        }

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.drawerIcon_iv :
                Intent backIntent  =new Intent(TrackSalesPersonActivity.this, NavigationDrawerActivity.class);
                backIntent.putExtra("drawer_Open", "open_track_sales" );
                startActivity(backIntent);
                break;
        }
    }
}
