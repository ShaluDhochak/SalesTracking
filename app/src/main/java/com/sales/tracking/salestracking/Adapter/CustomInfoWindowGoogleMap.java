package com.sales.tracking.salestracking.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.sales.tracking.salestracking.Bean.InfoWindowData;
import com.sales.tracking.salestracking.R;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marher_info_content, null);

        TextView name_tv = view.findViewById(R.id.name);
        TextView addressDetails_tv = view.findViewById(R.id.addressDetails);
        TextView contactDetails_tv = view.findViewById(R.id.contactDetails);
        //  name_tv.setText(marker.getTitle());
        // addressDetails_tv.setText(marker.getSnippet());

        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        name_tv.setText(infoWindowData.getName());
        contactDetails_tv.setText(infoWindowData.getContact());
        addressDetails_tv.setText(infoWindowData.getAddress());

        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}