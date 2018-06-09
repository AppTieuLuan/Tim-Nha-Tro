package com.nhatro;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.adapter.CardAdapter;
import com.nhatro.adapter.CardMapViewAdapter;
import com.nhatro.adapter.HorizontalSnapRecyclerView;
import com.nhatro.adapter.Horizontal_Recycle_Map_Adapter;
import com.nhatro.adapter.Horizontal_Recycle_Map_Adapter$ItemHolder_ViewBinding;
import com.nhatro.adapter.ShadowTransformer;
import com.nhatro.model.ItemOnMapView;
import com.nhatro.model.LoadMap;
import com.nhatro.model.LocDL;
import com.nhatro.model.PhongTro;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;
import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private MapView mapView;
    private GoogleMap map;
    private LatLng currentSelected;
    private ArrayList<PhongTro> item;

    private Marker selectedMarker;
    private int indexSelected;
    private int banKinh;
    private ArrayList<Marker> lstMarker;

    HorizontalSnapRecyclerView recyclerView;
    Horizontal_Recycle_Map_Adapter horizontal_recycle_map_adapter;

    ArrayList<PhongTro> tem = new ArrayList<>();
    //private CardMapViewAdapter mCardAdapter;
    //private ShadowTransformer mCardShadowTransformer;
    //private ViewPager mViewPager;


    //    private CardFragmentPagerAdapter mFragmentCardAdapter;
//    private ShadowTransformer mFragmentCardShadowTransformer;


    private TextView txtBanKinh;
    private Circle currentCircle;
    private Marker tempMarker; // Marker xuất hiện khi di chuyển map.
    private IndicatorSeekBar seekBarBanKinh;
    LocDL locDL;
    private boolean isGestured; // Xác định có phải là người di chuyển map để di chuyển circle
    com.rey.material.widget.ProgressView loadingbar;

    int tes = 1;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*if (view == null) {*/
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        locDL = new LocDL();
        Bundle bundle = getArguments();
        locDL.setBankinh(1);
        locDL.setGiamin(bundle.getInt("giamin"));
        locDL.setGiamax(bundle.getInt("giamax"));
        locDL.setDientichmin(bundle.getInt("dientichmin"));
        locDL.setDientichmax(bundle.getInt("dientichmax"));
        locDL.setSonguoio(bundle.getInt("songuoio"));
        locDL.setLoaitin(bundle.getString("loaitin"));
        locDL.setTiennghi(bundle.getString("tiennghi"));
        locDL.setDoituong(bundle.getInt("doituong"));
        locDL.setGiogiac(bundle.getInt("giogiac"));
        locDL.setDanhsach(0);

        findViewId(view);
        lstMarker = new ArrayList<>();
        item = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        horizontal_recycle_map_adapter = new Horizontal_Recycle_Map_Adapter(getContext(), item);
        recyclerView.setAdapter(horizontal_recycle_map_adapter);
        recyclerView.setSnap(true);

        banKinh = 1;
        isGestured = false;


        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        seekBarBanKinh.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {

            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {

                txtBanKinh.setText(String.valueOf(Math.round(seekBarBanKinh.getProgressFloat())) + " KM");
                banKinh = Integer.parseInt(String.valueOf(Math.round(seekBarBanKinh.getProgressFloat())));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        currentCircle.getCenter(), getZoomLevel(currentCircle)));

                loadData(locDL);
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerVieww, int newState) {
                super.onScrollStateChanged(recyclerVieww, newState);

                //Toast.makeText(getContext(), String.valueOf(newState), Toast.LENGTH_SHORT).show();
                if (newState == 0 && item.size() > 0) {
                    //Toast.makeText(getContext(), String.valueOf(recyclerView.getmCurrentPosition(0)), Toast.LENGTH_LONG).show();

                    Marker tmp = lstMarker.get(indexSelected);
                    IconGenerator icon = new IconGenerator(getContext());
                    icon.setStyle(IconGenerator.STYLE_DEFAULT);
                    tmp.setIcon(BitmapDescriptorFactory.fromBitmap(icon.makeIcon(makeCharSequence(String.valueOf(item.get(indexSelected).getGia()) + " vnđ"))));
                    indexSelected = recyclerView.getmCurrentPosition(0);
                    selectedMarker = lstMarker.get(indexSelected);

                    Marker tmp2 = lstMarker.get(indexSelected);
                    IconGenerator icon2 = new IconGenerator(getContext());
                    icon2.setStyle(IconGenerator.STYLE_BLUE);
                    tmp2.setIcon(BitmapDescriptorFactory.fromBitmap(icon2.makeIcon(makeCharSequence(String.valueOf(item.get(indexSelected).getGia()) + " vnđ"))));
                    CameraPosition temp = new CameraPosition.Builder()
                            .target(new LatLng(item.get(indexSelected).getLat(), item.get(indexSelected).getLng()))      // Sets the center of the map to location user
                            .zoom(14)                   // Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(temp));

                }
            }
        });
        return view;
    }

    private void setUpViewPager() {

    }

    public void getDataaaaa(int test) {
        if (test == 1) {
            ArrayList<PhongTro> a = new ArrayList<>();
            PhongTro s = new PhongTro();
            s.setLng(106.7720977217);
            s.setLat(10.850647124617);
            s.setLoaitintuc(3);
            s.setGioitinh("1");
            s.setHinhanh("");
            s.setChieudai(1);
            s.setChieurong(2);
            s.setId("1");
            s.setTieude("234234");
            s.setGia(234234);
            s.setDiachi("35345345");

            a.add(s);

            PhongTro s1 = new PhongTro();
            s1.setLng(106.767866);
            s1.setLat(10.845266);
            s1.setLoaitintuc(3);
            s1.setGioitinh("1");
            s1.setHinhanh("");
            s1.setChieudai(1);
            s1.setChieurong(2);
            s1.setId("1");
            s1.setTieude("234234");
            s1.setGia(234234);
            s1.setDiachi("35345345");

            a.add(s1);

            tem.addAll(a);

            // item.clear();

            item.addAll(a);
            horizontal_recycle_map_adapter.notifyDataSetChanged();
            //recyclerView.setVisibility(View.GONE);
            /*item.addAll(tem);
            horizontal_recycle_map_adapter.notifyDataSetChanged();*/
           /* horizontal_recycle_map_adapter = new Horizontal_Recycle_Map_Adapter(item);
            recyclerView.setAdapter(horizontal_recycle_map_adapter);*/
            this.tes = test + 1;
        } else {
            if (tes == 2) {
                ArrayList<PhongTro> a = new ArrayList<>();
                PhongTro s = new PhongTro();
                s.setLng(106.8720977217);
                s.setLat(10.850647124617);
                s.setLoaitintuc(3);
                s.setGioitinh("1");
                s.setHinhanh("");
                s.setChieudai(1);
                s.setChieurong(2);
                s.setId("1");
                s.setTieude("MOI LOADDD ");
                s.setGia(234234);
                s.setDiachi("vo van kiet");

                a.add(s);
                item.clear();
                item.addAll(a);
                horizontal_recycle_map_adapter.notifyDataSetChanged();

                //tes = tes + 1;
            } else {
                PhongTro s1 = new PhongTro();
                s1.setLng(106.767866);
                s1.setLat(10.545266);
                s1.setLoaitintuc(3);
                s1.setGioitinh("1");
                s1.setHinhanh("");
                s1.setChieudai(1);
                s1.setChieurong(2);
                s1.setId("1");
                s1.setTieude("234234");
                s1.setGia(234234);
                s1.setDiachi("35345345");


                /*ArrayList<PhongTro> temp = new ArrayList<>();
                temp = item;
                temp.add(s1);*/
                item.clear();
                horizontal_recycle_map_adapter.notifyDataSetChanged();

                /*item.addAll(temp);
                horizontal_recycle_map_adapter.notifyDataSetChanged();*/
            }

        }
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

    public void filterData(LocDL locDL) {
       /* Toast.makeText(getContext(),"Đang Lọc DL",Toast.LENGTH_SHORT).show();*/
        this.locDL = locDL;

        DAL_PhongTro dal_phongTro = new DAL_PhongTro();
        //String tsss = dal_phongTro.danhSachPhong(locDL);

        //Log.d("KẾT QUẢ : ", tsss);
    }

    private void findViewId(View view) {
        txtBanKinh = (TextView) view.findViewById(R.id.txtBanKinh);
        seekBarBanKinh = (IndicatorSeekBar) view.findViewById(R.id.bubbleSeekBar);
        mapView = (MapView) view.findViewById(R.id.mymap);
        //mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        loadingbar = view.findViewById(R.id.loadingbar);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void drawCircle(LatLng point, int banKinh) {

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(banKinh * 1000);
        circleOptions.strokeColor(Color.parseColor("#66b5ed"));
        // Fill color of the circle
        circleOptions.fillColor(0x5366b5ed);

        // Border width of the circle
        circleOptions.strokeWidth(1);
        // Adding the circle to the GoogleMap
        currentCircle = map.addCircle(circleOptions);
    }


    public void dataMoveCamera() {
        Toast.makeText(getContext(), "Đang Lọc DL cmr", Toast.LENGTH_SHORT).show();

    }

    public void update(ArrayList<PhongTro> a) {
        this.item = a;
        horizontal_recycle_map_adapter.notifyDataSetChanged();
    }

    public void loadData(LocDL loc) {
        Toast.makeText(getContext(), "Đang Lọc DL", Toast.LENGTH_SHORT).show();

        loc.setLat(map.getCameraPosition().target.latitude);
        loc.setLng(map.getCameraPosition().target.longitude);
        loc.setBankinh(banKinh);

        this.locDL = loc;
        /*ThreadFilter threadFilter = new ThreadFilter();
        threadFilter.run();*/


        GetDataAsync getDataAsync = new GetDataAsync();
        getDataAsync.execute(locDL);

        //getDataaaaa(tes);


        //drawCircle(new LatLng(map.getCameraPosition().target.latitude,map.getCameraPosition().target.longitude),Integer.parseInt(String.valueOf(seekBarBanKinh.getProgressFloat())));


    }

    public LatLng getLatLong() {
        return tempMarker.getPosition();
    }

    public int banKinh() {
        return banKinh;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        if (tempMarker == null) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tmpmarker);
            LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                    .icon(icon);

            tempMarker = map.addMarker(markerOptions);
            tempMarker.setVisible(false);
        }
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if (isGestured) {
                    if (tempMarker != null) {
                        //tempMarker.remove();
                        tempMarker.setVisible(true);
                        tempMarker.setPosition(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
                    } else {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tmpmarker);
                        LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                                .icon(icon);
                        tempMarker = map.addMarker(markerOptions);
                    }
                }
            }
        });

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    isGestured = true;
                    //Toast.makeText(getContext(), "USER TÁC ĐỘNG LÊN MAP.", Toast.LENGTH_SHORT).show();
                } else if (i == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
                    isGestured = false;
                    //Toast.makeText(getContext(), "The user tapped something on the map.",Toast.LENGTH_SHORT).show();
                } else if (i == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
                    //Toast.makeText(getContext(), "APP TỰ TÁC ĐỘNG LÊN MAP", Toast.LENGTH_SHORT).show();
                    isGestured = false;
                }
            }
        });



        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (isGestured) {
                    if (currentCircle != null) {
                        //currentCircle.setCenter(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
                        loadData(locDL);
                    } else {
                        // Instantiating CircleOptions to draw a circle around the marker
                        CircleOptions circleOptions = new CircleOptions();
                        // Specifying the center of the circle
                        circleOptions.center(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude));
                        // Radius of the circle
                        circleOptions.radius(1000);
                        // Border color of the circle
                        circleOptions.strokeColor(Color.BLACK);
                        // Fill color of the circle
                        circleOptions.fillColor(0x30ff0000);
                        // Border width of the circle
                        circleOptions.strokeWidth(2);
                        // Adding the circle to the GoogleMap
                        currentCircle = map.addCircle(circleOptions);
                    }
                    //currentCircle.remove();

                    if (tempMarker != null)
                        tempMarker.setVisible(false);
                } else {

                }
            }
        });


        getLocation();

        for (int i = 0; i < this.item.size(); i++) {
            LatLng temp = new LatLng(item.get(i).getLat(), item.get(i).getLng());

            IconGenerator iconFactory = new IconGenerator(getContext());
            if (i == 0) {
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                //scurrentSelected = new LatLng(item.get(i).getLat(),item.get(i).getLng());
            } else {
                iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
            }


            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(makeCharSequence(String.valueOf(item.get(i).getGia()) + " vnđ")))).
                    position(temp).
                    anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());


            //map.addMarker(markerOptions);

            Marker m = map.addMarker(markerOptions);
            lstMarker.add(m);
            if (i == 0) {
                selectedMarker = lstMarker.get(0);
                indexSelected = 0;
            }
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                isGestured = false;
                if (marker.equals(selectedMarker)) {
                    return false;
                } else {
                    Marker tmp = lstMarker.get(indexSelected);
                    IconGenerator icon = new IconGenerator(getContext());
                    icon.setStyle(IconGenerator.STYLE_DEFAULT);
                    tmp.setIcon(BitmapDescriptorFactory.fromBitmap(icon.makeIcon(makeCharSequence(String.valueOf(item.get(indexSelected).getGia()) + " vnđ"))));


                    for (int i = 0; i < lstMarker.size(); i++) {
                        if (lstMarker.get(i).equals(marker)) {
                            indexSelected = i;
                            selectedMarker = marker;

                            Marker tmp2 = lstMarker.get(indexSelected);
                            IconGenerator icon2 = new IconGenerator(getContext());
                            icon2.setStyle(IconGenerator.STYLE_BLUE);
                            tmp2.setIcon(BitmapDescriptorFactory.fromBitmap(icon2.makeIcon(makeCharSequence(String.valueOf(item.get(indexSelected).getGia()) + " vnđ"))));
                            break;
                        }
                    }
                    recyclerView.smoothScrollToPosition(indexSelected);
                    //mViewPager.setCurrentItem(indexSelected);
                }
                return false;
            }
        });
        /*CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                .zoom(14)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.85064713, 106.77209787), 14));


        drawCircle(new LatLng(10.85064713, 106.77209787), banKinh);

        loadData(locDL);
    }

    public void renderMarker(int position) {
        for (int i = 0; i < this.item.size(); i++) {

            LatLng temp = new LatLng(item.get(i).getLat(), item.get(i).getLng());

            IconGenerator iconFactory = new IconGenerator(getContext());
            if (i == position) {
                iconFactory.setStyle(IconGenerator.STYLE_BLUE);
            } else {
                iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
            }

            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(makeCharSequence(String.valueOf(item.get(i).getGia()) + " vnđ")))).
                    position(temp).
                    anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            map.addMarker(markerOptions);
        }
    }

    private void getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (lastLocation != null) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                    .zoom(14)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onClick(View view) {
//        mViewPager.setAdapter(mCardAdapter);
//        mViewPager.setPageTransformer(false, mCardShadowTransformer);
    }

    private CharSequence makeCharSequence(String str) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new RelativeSizeSpan(0.6f), 0, str.length(), 0);
        ssb.setSpan(new StyleSpan(BOLD), 0, str.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public class GetDataAsync extends AsyncTask<LocDL, String, ArrayList<PhongTro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingbar.setVisibility(View.VISIBLE);
            // Tạo lại marker khi di chuyển map
            item.clear();
            map.clear();
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.tmpmarker);
            LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
            MarkerOptions markerOptionsss = new MarkerOptions().position(latLng)
                    .icon(icon);

            tempMarker = map.addMarker(markerOptionsss);
            tempMarker.setVisible(false);
            ////

            drawCircle(new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude), seekBarBanKinh.getProgress());
        }

        @Override
        protected ArrayList<PhongTro> doInBackground(LocDL... locDLS) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();

            ArrayList<PhongTro> a = new ArrayList<>();
            a = dal_phongTro.danhSachPhong(locDLS[0]);

            return a;
        }

        @Override
        protected void onPostExecute(ArrayList<PhongTro> a) {
            super.onPostExecute(a);
            loadingbar.setVisibility(View.INVISIBLE);
            item = a;
            if (a.size() > 0) {
                indexSelected = 0;
                recyclerView.setVisibility(View.VISIBLE);
                horizontal_recycle_map_adapter = new Horizontal_Recycle_Map_Adapter(getContext(), a);
                recyclerView.setAdapter(horizontal_recycle_map_adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
            }

            //update(a);
            //loadMap.getCardMapViewAdapter().notifyDataSetChanged();
            //mCardAdapter = new CardMapViewAdapter(item);
            //mViewPager.setAdapter(mCardAdapter);
            //mCardAdapter.notifyDataSetChanged();
            //mViewPager.requestLayout();
            /*mCardAdapter = new CardMapViewAdapter(item);
            mCardAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mCardAdapter);*/

            //refreshViewPager(loadMap.getPhongTros());
            //mCardAdapter.removeAll();

            //getDataaaaa(item);
            lstMarker.clear();

            for (int i = 0; i < item.size(); i++) {
                LatLng temp = new LatLng(item.get(i).getLat(), item.get(i).getLng());

                IconGenerator iconFactory = new IconGenerator(getContext());
                if (i == 0) {
                    iconFactory.setStyle(IconGenerator.STYLE_BLUE);
                    //scurrentSelected = new LatLng(item.get(i).getLat(),item.get(i).getLng());
                } else {
                    iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
                }

                MarkerOptions markerOptions = new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(makeCharSequence(String.valueOf(item.get(i).getGia()) + " vnđ")))).
                        position(temp).
                        anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

                //map.addMarker(markerOptions);

                Marker m = map.addMarker(markerOptions);
                lstMarker.add(m);
                if (i == 0) {
                    selectedMarker = lstMarker.get(0);
                    indexSelected = 0;
                }


            }

            if (item.size() > 0) {
                indexSelected = 0;
                selectedMarker = lstMarker.get(0);
            }
        }
    }


}
