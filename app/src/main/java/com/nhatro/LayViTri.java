package com.nhatro;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LayViTri extends AppCompatActivity implements OnMapReadyCallback {


    private MapView mapView;
    private GoogleMap map;
    Marker marker;

    LatLng currentLatLng;
    EditText edtDiaChi;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_activity_tim_vi_tri, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_vi_tri);
        getSupportActionBar().setTitle("Lấy vị trí");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       /* Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        ActionBar actionBar = getSupportActionBar();;
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        edtDiaChi = findViewById(R.id.edtDiaChi);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        currentLatLng = new LatLng(bundle.getDouble("lat"),bundle.getDouble("lng"));

        mapView = findViewById(R.id.mymap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentLatLng)      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

        //marker.setPosition(new LatLng(latt, lngg));

        marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.tmpmarker)));

        //marker.showInfoWindow();



        //marker = map.addMarker(markerOptionsss);
        //marker.showInfoWindow();

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (marker != null) {
                    //tempMarker.remove();
                    marker.setPosition(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
                } else {
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tmpmarker);
                    LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                    MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                            .icon(icon);
                    marker = map.addMarker(markerOptions);
                }
            }
        });

        /*map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude, 1);
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();

                    String ad = address;

                    //Toast.makeText(getApplicationContext(),ad,Toast.LENGTH_SHORT).show();
                    edtDiaChi.setText(addresses.get(0).getSubAdminArea());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });*/

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.xong:
                Intent intents = getIntent();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",map.getCameraPosition().target.latitude);
                bundle.putDouble("lng",map.getCameraPosition().target.longitude);

                intents.putExtra("data", bundle);
                setResult(1, intents); // phương thức này sẽ trả kết quả cho Activity trước
                finish(); // Đóng Activity hiện tại
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
