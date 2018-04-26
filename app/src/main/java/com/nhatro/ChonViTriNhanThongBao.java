package com.nhatro;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

public class ChonViTriNhanThongBao extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;
    Marker marker;
    PolygonOptions rectOptions;
    Polygon polygon;
    TextView txtVeLai;

    TextView txtChonMarker;
    boolean vesss = true;

    LatLng currentLatLng;
    private Circle currentCircle;
    int bankinh;
    private IndicatorSeekBar seekBarBanKinh;
    TextView txtBanKinh;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar_activity_tim_vi_tri, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_vi_tri_nhan_thong_bao);

        getSupportActionBar().setTitle("Chọn khu vực nhận thông báo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        seekBarBanKinh = (IndicatorSeekBar) findViewById(R.id.bubbleSeekBar);
        txtBanKinh = findViewById(R.id.txtBanKinh);

        mapView = findViewById(R.id.mymap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        txtVeLai = findViewById(R.id.txtVeLai);

        txtVeLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rectOptions = null;
                polygon.remove();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        currentLatLng = new LatLng(bundle.getDouble("lat"), bundle.getDouble("lng"));
        bankinh = bundle.getInt("bankinh");
        txtChonMarker = findViewById(R.id.txtChonMarker);
        txtChonMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vesss = false;
            }
        });

        //seekBarBanKinh.setProgress(Float.parseFloat(1));

        seekBarBanKinh.setProgress(bankinh);
        txtBanKinh.setText("Bán kính " + String.valueOf(bankinh) + " KM");
        seekBarBanKinh.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

                txtBanKinh.setText("Bán kính " + String.valueOf(Math.round(seekBarBanKinh.getProgressFloat())) + " KM");
                bankinh = Integer.parseInt(String.valueOf(Math.round(seekBarBanKinh.getProgressFloat())));
                if (currentCircle != null)
                    currentCircle.remove();
                drawCircle(currentLatLng, bankinh);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        currentCircle.getCenter(), getZoomLevel(currentCircle)));
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
    }


    private void drawCircle(LatLng point, int banKinh) {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(banKinh * 1000);
        circleOptions.strokeColor(Color.parseColor("#66b5ed"));
        // Fill color
        circleOptions.fillColor(0x5366b5ed);
        // Border width
        circleOptions.strokeWidth(1);
        currentCircle = map.addCircle(circleOptions);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

        /*marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.tmpmarker)));*/


        /// VẼ BẰNG CÁCH CHỌN
        /*map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_SHORT).show();
                if(vesss) {
                    Draw_Map(latLng);
                } else {
                    ArrayList<LatLng> tmp = new ArrayList<>(polygon.getPoints());
                    Toast.makeText(getApplicationContext(),String.valueOf(isPointInPolygon(latLng,tmp)),Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (marker != null) {
                    //tempMarker.remove();
                    marker.setVisible(true);
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

        drawCircle(currentLatLng, bankinh);



        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (currentCircle != null) {
                    currentLatLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                    currentCircle.setCenter(currentLatLng);
                } else {

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
                    circleOptions.radius(bankinh * 1000);
                    circleOptions.strokeColor(Color.BLACK);
                    circleOptions.fillColor(0x30ff0000);
                    circleOptions.strokeWidth(2);
                    currentCircle = map.addCircle(circleOptions);
                    currentCircle.remove();
                    drawCircle(currentLatLng, bankinh);
                }
                //currentCircle.remove();
                if (marker != null)
                    marker.setVisible(false);
            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                currentCircle.getCenter(), getZoomLevel(currentCircle)));

    }

    public void Draw_Map(LatLng latLng) {
        if (rectOptions == null) {
            rectOptions = new PolygonOptions();
        }
        if (polygon != null) {
            polygon.remove();
        }
        rectOptions.add(latLng);
        rectOptions.strokeColor(Color.parseColor("#66000000"));
        rectOptions.strokeWidth(5);
        rectOptions.fillColor(Color.parseColor("#5366b5ed"));
        polygon = map.addPolygon(rectOptions);

    }

    private boolean isPointInPolygon(LatLng tap, ArrayList<LatLng> vertices) {
        int intersectCount = 0;
        for (int j = 0; j < vertices.size() - 1; j++) {
            if (rayCastIntersect(tap, vertices.get(j), vertices.get(j + 1))) {
                intersectCount++;
            }
        }

        return ((intersectCount % 2) == 1); // odd = inside, even = outside;
    }

    private boolean rayCastIntersect(LatLng tap, LatLng vertA, LatLng vertB) {

        double aY = vertA.latitude;
        double bY = vertB.latitude;
        double aX = vertA.longitude;
        double bX = vertB.longitude;
        double pY = tap.latitude;
        double pX = tap.longitude;

        if ((aY > pY && bY > pY) || (aY < pY && bY < pY)
                || (aX < pX && bX < pX)) {
            return false; // a and b can't both be above or below pt.y, and a or
            // b must be east of pt.x
        }

        double m = (aY - bY) / (aX - bX); // Rise over run
        double bee = (-aX) * m + aY; // y = mx + b
        double x = (pY - bee) / m; // algebra is neat!

        return x > pX;
    }

    public void ve(LatLng latlng) {
        if (rectOptions == null) {
            rectOptions = new PolygonOptions().add(latlng);
            polygon = map.addPolygon(rectOptions);
        } else {
            rectOptions.add(latlng);

            //polygon = map.addPolygon(rectOptions);
            polygon.remove();
            polygon = map.addPolygon(rectOptions);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xong:
                Intent intents = getIntent();
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", map.getCameraPosition().target.latitude);
                bundle.putDouble("lng", map.getCameraPosition().target.longitude);
                bundle.putInt("bankinh", bankinh);
                intents.putExtra("data", bundle);
                setResult(1, intents); // phương thức này sẽ trả kết quả cho Activity trước
                finish(); // Đóng Activity hiện tại

                //Toast.makeText(getApplicationContext(), "Xong", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public int getZoomLevel(Circle circle) {
        int zoomLevel = 11;
        if (circle != null) {
            double radius = circle.getRadius() + circle.getRadius() / 2;
            double scale = radius / 500;
            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
        }
        return zoomLevel;
    }
}
