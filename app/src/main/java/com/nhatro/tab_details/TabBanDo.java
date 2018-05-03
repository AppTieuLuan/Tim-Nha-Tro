package com.nhatro.tab_details;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.R;
import com.nhatro.model.Marker_TabBanDo;

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
    String iditem = "";
    boolean dangtaidl;
    LinearLayout layoutLoad;

    public TabBanDo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment'

        if (v == null) {
            v = inflater.inflate(R.layout.fragment_tab_ban_do, container, false);
            layoutLoad = v.findViewById(R.id.layoutLoad);
            dangtaidl = true;
            Bundle bundle = this.getArguments();

            iditem = bundle.getString("id");

            mapView = (MapView) v.findViewById(R.id.mapTabBanDo);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
            txtChiDuong = (TextView) v.findViewById(R.id.txtChiDuong);
        }
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
        LoadBanDo loadBanDo = new LoadBanDo();
        loadBanDo.execute(iditem);
    }

    public class LoadBanDo extends AsyncTask<String, Void, Marker_TabBanDo> {

        @Override
        protected Marker_TabBanDo doInBackground(String... strings) {
            Marker_TabBanDo temp = new Marker_TabBanDo();
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            temp = dal_phongTro.layViTriTrenBanDo(strings[0]);
            return temp;
        }

        @Override
        protected void onPostExecute(Marker_TabBanDo marker_tabBanDo) {
            super.onPostExecute(marker_tabBanDo);
            if (marker_tabBanDo != null) {
                layoutLoad.setVisibility(View.GONE);
                dangtaidl = false;
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(marker_tabBanDo.getLat(), marker_tabBanDo.getLng()), 15));

                marker = map.addMarker(new MarkerOptions()
                        .title(marker_tabBanDo.getTieude())
                        .snippet(marker_tabBanDo.getDiachi())
                        .position(new LatLng(marker_tabBanDo.getLat(), marker_tabBanDo.getLng())));


                marker.showInfoWindow();

                map.getUiSettings().setZoomGesturesEnabled(false);
                map.getUiSettings().setScrollGesturesEnabled(false);

                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if (!dangtaidl) {
                            String tieude = marker_tabBanDo.getTieude();
                            String geoUri = "http://maps.google.com/maps?q=loc:" + marker_tabBanDo.getLat() + "," + marker_tabBanDo.getLng() + " (" + tieude + ")";
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                            getContext().startActivity(intent);
                        }

                    }
                });

                txtChiDuong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!dangtaidl) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("http://maps.google.com/maps?daddr=" + marker_tabBanDo.getLat() + "," + marker_tabBanDo.getLng()));
                            startActivity(intent);

                        }
                    }
                });
            }

        }
    }

}
