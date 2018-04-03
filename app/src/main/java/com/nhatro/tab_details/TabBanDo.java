package com.nhatro.tab_details;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nhatro.R;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabBanDo extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    View v;
    TextView txtChiDuong;
    Marker marker;
    public TabBanDo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_tab_ban_do, container, false);
            mapView = (MapView) v.findViewById(R.id.mapTabBanDo);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);

            txtChiDuong = (TextView) v.findViewById(R.id.txtChiDuong);

            txtChiDuong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* String strUri = "http://maps.google.com/maps?q=loc:" + 10.845672 + "," + 106.779129 + " (Nhà trọ lê văn việt)";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);*/

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?daddr=10.845672,106.779129"));
                    startActivity(intent);


                }
            });


        }


        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        marker = map.addMarker(new MarkerOptions()
                .title("Nhà trọ mới cho thuê tại quận 9")
                .snippet("78/12 đường Làng Tăng Phú, phường Tăng Nhơn Phú A, quận 9, TPHCM")
                .position(new LatLng(10.85064713, 106.77209787)));


        marker.showInfoWindow();

        map.getUiSettings().setZoomGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String tieude = "hà trọ mới cho thuê tại quận 9";
                String geoUri = "http://maps.google.com/maps?q=loc:" + 10.85064713 + "," + 106.77209787 + " (" + tieude + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                getContext().startActivity(intent);
            }
        });

    }

}
