package com.sales.tracking.salestracking.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sales.tracking.salestracking.Adapter.CustomInfoWindowGoogleMap;
import com.sales.tracking.salestracking.Bean.InfoWindowData;
import com.sales.tracking.salestracking.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackSalesPersonActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    //private MarkerOptions options = new MarkerOptions();
    //private ArrayList<LatLng> latLngs = new ArrayList<>();
    List<Marker> markersList = new ArrayList<>();
    List<String> latposition= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_sales_person);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

       /* LatLng marker1 = new LatLng(19.0604489, 72.8369127);
        LatLng marker2= new LatLng(18.5233964, 73.9317373);

        // Markers All day long
        markersList.add(mMap.addMarker(new MarkerOptions().position(marker1).title("Pune").snippet("Android").snippet("android2")));
        markersList.add(mMap.addMarker(new MarkerOptions().position(marker2)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker1,7.0f));
        */

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(1);

        LatLng snowqualmie = new LatLng(47.5287132, -121.8253906);
        LatLng mumbai = new LatLng(19.0604489, 72.8369127);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(snowqualmie)
                .title("Snowqualmie Falls")
                .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(mumbai)
                .title("Mumbai here")
                .snippet("located in india")
                .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_CYAN));

        InfoWindowData info = new InfoWindowData();
        info.setImage("snowqualmie");
        info.setHotel("Hotel : excellent hotels available");
        info.setFood("Food : all types of restaurants available");
        info.setTransport("Reach the site by bus, car and train.");

        InfoWindowData info1 = new InfoWindowData();
        info1.setImage("shalu");
        info1.setHotel("Hotel : shalu dhochak");
        info1.setFood("Food : all type");
        info1.setTransport("reach there");

        CustomInfoWindowGoogleMap customInfoWindow1 = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow1);

        Marker m1 = mMap.addMarker(markerOptions1);
        m1.setTag(info1);
        m1.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mumbai, 7.0f));

        CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);

        Marker m = mMap.addMarker(markerOptions);
        m.setTag(info);
        m.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(snowqualmie, 7.0f));

        /*
        final JSONArray array = new JSONArray(jsonStrOportunidades);
for (int i = 0; i < array.length(); i++)
{
    JSONObject jsonObject = array.getJSONObject(i);
    final String Designacao = jsonObject.getString("Designacao");
    String Coord_LAT = jsonObject.getString("Coord_LAT");
    String Coord_LONG = jsonObject.getString("Coord_LONG");
    final String Morada = jsonObject.getString("Morada");

    final HashMap<String, String> oportunidades = new HashMap<>();

    oportunidades.put("Designacao", Designacao);
    oportunidades.put("Coord_LAT", Coord_LAT);
    oportunidades.put("Coord_LONG", Coord_LONG);
    oportunidades.put("Morada", Morada);

    double lat1 = Double.parseDouble(Coord_LAT);
    double lng1 = Double.parseDouble(Coord_LONG);

    mMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1)));

    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            new AlertDialog.Builder(MapsActivity.this)
                    .setTitle(Designacao)
                    .setMessage("Endereço: " + Morada + "\n" + "Telefone: " )
                    .setPositiveButton("Ir", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new GetDirecoes().execute();//Enviar as coordenadas
                            mBottomSheetBehavior.setPeekHeight(250);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
            return false;
        }
    });
    listaOportunidades.add(oportunidades);
}
         */
    }

}