package com.nhatro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.nhatro.DAL.DAL_TinTimPhong;
import com.nhatro.adapter.AdapterRecyclerViewChonQuan;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.SpinnerQuanHuyen_Adapter;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinTimPhong;
import com.nhatro.model.TinhTP;
import com.nhatro.model.User;
import com.nhatro.sqlite.SQLite_QuanHuyen;
import com.nhatro.sqlite.SQLite_TinhTP;

import net.steamcrafted.loadtoast.LoadToast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThemTinTimPhong extends AppCompatActivity implements OnMapReadyCallback {


    CrystalRangeSeekbar seekSoNguoi, seekGia;

    TextView minSoNguoi, maxSoNguoi, minGia, maxGia;

    ArrayList<TinhTP> arrTinhTP;
    Spinner spinnerTinhTP;

    SQLite_TinhTP sqLite_tinhTP;
    int idtp;
    SpinnerTinhTP spinnerTinhTPAdapter;
    SQLite_QuanHuyen sqLite_quanHuyen;
    //int indexSpinnerTinhTp;

    TextView layoutButtonOK;

    RecyclerView recycleQH;
    ArrayList<QuanHuyen> quanHuyens;
    AdapterRecyclerViewChonQuan adapterRecyclerViewChonQuan;

    int bankinh;
    double lat, lng;
    private MapView mapView;
    private GoogleMap map;
    private Circle currentCircle;

    CheckBox checkNhanTB, checkTD;
    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstFacilities = new ArrayList<>();
    Grid_Facilities_Adapter myAdapter;
    LinearLayout layoutTransparent;
    LoadToast lt, loadToast2;
    ThemTinTimPhong.mHadler mHadler;
    Toast toastError;

    EditText valueTieuDe, valueKV, edtNhapBl, valueHT, valueSDT, valueFB;
    RadioButton checkPhongTro, checkNhaNguyenCan, checkTimOGhep, radCa2, radNam, radNu;


    ArrayList<Integer> lstChonQH;
    AlertDialog.Builder alertDialogBuilder;

    String idqh = "";
    int indexSpnTinh;
    TinTimPhong thongTinTimPhong;
    int key;
    String iditem = "";

    User usDN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tin_tim_phong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm tin mới");
        idtp = 0;
        usDN = new User();

        lstChonQH = new ArrayList<>();
        radCa2 = findViewById(R.id.radCa2);
        radCa2.setChecked(true);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        seekGia = findViewById(R.id.seekGia);
        minGia = findViewById(R.id.minGia);
        maxGia = findViewById(R.id.maxGia);
        mapView = (MapView) findViewById(R.id.mapViTri);
        gridTienNghi = findViewById(R.id.gridTienNghi);
        edtNhapBl = findViewById(R.id.edtNhapBl);
        valueKV = findViewById(R.id.valueKV);
        valueTieuDe = findViewById(R.id.valueTieuDe);
        bankinh = 1;
        layoutTransparent = findViewById(R.id.layoutTransparent);
        seekSoNguoi = (CrystalRangeSeekbar) findViewById(R.id.seekSoNguoi);
        minSoNguoi = findViewById(R.id.minSoNguoi);
        maxSoNguoi = findViewById(R.id.maxSoNguoi);
        layoutButtonOK = findViewById(R.id.layoutButtonOK);
        checkNhanTB = findViewById(R.id.checkNhanTB);
        checkTD = findViewById(R.id.checkTD);
        valueFB = findViewById(R.id.valueFB);
        valueHT = findViewById(R.id.valueHT);
        valueSDT = findViewById(R.id.valueSDT);
        getUser();
        sqLite_quanHuyen = new SQLite_QuanHuyen(ThemTinTimPhong.this);
        quanHuyens = new ArrayList<>();

        quanHuyens = sqLite_quanHuyen.getDSQH(1);

        arrTinhTP = new ArrayList<>();
        spinnerTinhTP = findViewById(R.id.spinnerTinhTP);
        sqLite_tinhTP = new SQLite_TinhTP(ThemTinTimPhong.this);

        arrTinhTP = sqLite_tinhTP.getDSTP();
        spinnerTinhTPAdapter = new SpinnerTinhTP(getApplicationContext(), arrTinhTP);
        spinnerTinhTP.setAdapter(spinnerTinhTPAdapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        key = bundle.getInt("key");

        // set listener
        seekSoNguoi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                valueTieuDe.clearFocus();
                minSoNguoi.setText(String.valueOf(minValue));
                maxSoNguoi.setText(String.valueOf(maxValue));
            }
        });


        minGia.setText("0 VNĐ");
        maxGia.setText("10.000.000 VNĐ");
        seekGia.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                valueTieuDe.clearFocus();
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String tmp = formatter.format(minValue) + " VNĐ";
                minGia.setText(tmp);
                tmp = formatter.format(maxValue) + " VNĐ";
                maxGia.setText(tmp);
            }
        });


        spinnerTinhTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueTieuDe.clearFocus();
                idtp = arrTinhTP.get(position).getId();

                quanHuyens = sqLite_quanHuyen.getDSQH(arrTinhTP.get(position).getId());
                adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
                recycleQH.setAdapter(adapterRecyclerViewChonQuan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recycleQH = (RecyclerView) findViewById(R.id.recycleQH);
        recycleQH.setLayoutManager(layoutManager);

        adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
        recycleQH.setAdapter(adapterRecyclerViewChonQuan);

        /*recycleQH.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clearFocus();
                return false;
            }
        });*/

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        setupGridTienNghi();
        setUpbuttonOK();
        setUpCheck();

        valueSDT.setText(usDN.getSodt());
        valueHT.setText(usDN.getHoten());
        valueFB.setText(usDN.getFacebook());

        if (key == 2) {
            iditem = bundle.getString("iditem");
            thongTinTimPhong = new TinTimPhong();
            thongTinTimPhong = (TinTimPhong) bundle.getSerializable("tintimphong");
            getDataEdit();
        }
    }

    public void getDataEdit() {
        valueTieuDe.setText(thongTinTimPhong.getTieude());
        if (thongTinTimPhong.getLoaitin() == 1) {
            checkPhongTro.setChecked(true);
        } else {
            if (thongTinTimPhong.getLoaitin() == 2) {
                checkTimOGhep.setChecked(true);
            } else {
                checkNhaNguyenCan.setChecked(true);
            }
        }
        minSoNguoi.setText(String.valueOf(thongTinTimPhong.getSonguoimin()));
        maxSoNguoi.setText(String.valueOf(thongTinTimPhong.getSonguoimax()));
        seekSoNguoi.setMinValue(1)
                .setMaxValue(10)
                .setMinStartValue(thongTinTimPhong.getSonguoimin())
                .setMaxStartValue(thongTinTimPhong.getSonguoimax())
                .apply();

        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String tmp = formatter.format(thongTinTimPhong.getGiamin()) + " VNĐ";
        minGia.setText(tmp);

        tmp = formatter.format(thongTinTimPhong.getGiamax()) + " VNĐ";
        maxGia.setText(tmp);
        seekGia.setMinValue(0)
                .setMaxValue(10000000)
                .setMinStartValue(thongTinTimPhong.getGiamin())
                .setMaxStartValue(thongTinTimPhong.getGiamax())
                .apply();

        if (thongTinTimPhong.getGioitinh() == 1) {
            radNam.setChecked(true);
            radCa2.setChecked(false);
            radNu.setChecked(false);
        } else {
            if (thongTinTimPhong.getGioitinh() == 2) {
                radNam.setChecked(false);
                radCa2.setChecked(false);
                radNu.setChecked(true);
            } else {
                radNam.setChecked(false);
                radCa2.setChecked(true);
                radNu.setChecked(false);
            }
        }

        if (thongTinTimPhong.getGiogiac() == 0) {
            checkTD.setChecked(false);
        } else {
            checkTD.setChecked(true);
        }
        for (int i = 0; i < arrTinhTP.size(); i++) {
            if (arrTinhTP.get(i).getId() == thongTinTimPhong.getIdtp()) {
                indexSpnTinh = i;
                break;
            }
        }
        spinnerTinhTP.setSelection(indexSpnTinh);
        //spinnerTinhTP.setSelection(40);

        quanHuyens.clear();
        quanHuyens = sqLite_quanHuyen.getDSQH(thongTinTimPhong.getIdtp());

        adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
        recycleQH.setAdapter(adapterRecyclerViewChonQuan);

        String idqh = thongTinTimPhong.getIdqh();
        idqh = idqh.replaceAll("\"", "");

        String arr[] = idqh.split(",");

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < quanHuyens.size(); j++) {
                if (Integer.parseInt(arr[i]) == quanHuyens.get(j).getId()) {
                    quanHuyens.get(j).setSelect(true);
                    adapterRecyclerViewChonQuan.updateItem(j);
                    //quanHuyens.get(j).setSelect(true);
                    //adapterRecyclerViewChonQuan.notifyItemChanged(j);
                    //adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
                    //recycleQH.setAdapter(adapterRecyclerViewChonQuan);
                    break;
                }
            }
        }
        /*adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
        recycleQH.setAdapter(adapterRecyclerViewChonQuan);*/

        valueKV.setText(thongTinTimPhong.getKhuvuc());
        setUpGridTienNghiEdit();
        edtNhapBl.setText(thongTinTimPhong.getMotathem());


        valueSDT.setText(thongTinTimPhong.getSdt());
        valueHT.setText(thongTinTimPhong.getHoten());
        valueFB.setText(thongTinTimPhong.getFacebook());
    }

    public void setUpGridTienNghiEdit() {
        String[] array = thongTinTimPhong.getTiennghi().split(",", -1);
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals("wifi")) {
                lstFacilities.get(0).setSelected(true);
            } else {
                if (array[i].equals("gac")) {
                    lstFacilities.get(1).setSelected(true);
                } else {
                    if (array[i].equals("toilet")) {
                        lstFacilities.get(2).setSelected(true);
                    } else {
                        if (array[i].equals("phongtam")) {
                            lstFacilities.get(3).setSelected(true);
                        } else {
                            if (array[i].equals("giuong")) {
                                lstFacilities.get(4).setSelected(true);
                            } else {
                                if (array[i].equals("tv")) {
                                    lstFacilities.get(5).setSelected(true);
                                } else {
                                    if (array[i].equals("tulanh")) {
                                        lstFacilities.get(6).setSelected(true);
                                    } else {
                                        if (array[i].equals("bepga")) {
                                            lstFacilities.get(7).setSelected(true);
                                        } else {
                                            if (array[i].equals("quat")) {
                                                lstFacilities.get(8).setSelected(true);
                                            } else {
                                                if (array[i].equals("tudo")) {
                                                    lstFacilities.get(9).setSelected(true);
                                                } else {
                                                    if (array[i].equals("maylanh")) {
                                                        lstFacilities.get(10).setSelected(true);
                                                    } else {
                                                        if (array[i].equals("den")) {
                                                            lstFacilities.get(11).setSelected(true);
                                                        } else {
                                                            if (array[i].equals("baove")) {
                                                                lstFacilities.get(12).setSelected(true);
                                                            } else {
                                                                if (array[i].equals("camera")) {
                                                                    lstFacilities.get(13).setSelected(true);
                                                                } else {
                                                                    if (array[i].equals("khudexe")) {
                                                                        lstFacilities.get(14).setSelected(true);
                                                                    } else {

                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //map.getUiSettings().setZoomGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(ThemTinTimPhong.this, ChonViTriNhanThongBao.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", map.getCameraPosition().target.latitude);
                bundle.putDouble("lng", map.getCameraPosition().target.longitude);
                bundle.putInt("bankinh", bankinh);
                intent.putExtra("data", bundle);
                //startActivity(intent);
                startActivityForResult(intent, 1110);
            }
        });

        if (key == 2) {
            if (currentCircle != null) {
                currentCircle.setRadius(thongTinTimPhong.getBankinh() * 1000);
                currentCircle.setCenter(new LatLng(thongTinTimPhong.getLat(), thongTinTimPhong.getLng()));
            } else {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(thongTinTimPhong.getLat(), thongTinTimPhong.getLng()));
                circleOptions.radius(thongTinTimPhong.getBankinh() * 1000);
                circleOptions.strokeColor(Color.parseColor("#66b5ed"));
                circleOptions.fillColor(0x5366b5ed);
                circleOptions.strokeWidth(1);
                currentCircle = map.addCircle(circleOptions);
            }

            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), getZoomLevel(currentCircle)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    currentCircle.getCenter(), getZoomLevel(currentCircle)));

            this.lat = thongTinTimPhong.getLat();
            this.lng = thongTinTimPhong.getLng();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 1110) {
            Bundle bundles = data.getBundleExtra("data");
            //tenTP = bundle.getString("tenTP");
            double latt = bundles.getDouble("lat");
            double lngg = bundles.getDouble("lng");
            bankinh = bundles.getInt("bankinh");
            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

            if (currentCircle != null) {
                currentCircle.setRadius(bankinh * 1000);
                currentCircle.setCenter(new LatLng(latt, lngg));
            } else {
                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(new LatLng(latt, lngg));
                circleOptions.radius(bankinh * 1000);
                circleOptions.strokeColor(Color.parseColor("#66b5ed"));
                circleOptions.fillColor(0x5366b5ed);
                circleOptions.strokeWidth(1);
                currentCircle = map.addCircle(circleOptions);
            }

            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), getZoomLevel(currentCircle)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    currentCircle.getCenter(), getZoomLevel(currentCircle)));

            this.lat = latt;
            this.lng = lngg;
        }
    }

    public void setupGridTienNghi() {
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);
        lstFacilities.add(new Item_Grid_Facilities("Wifi", R.drawable.icons_wi_fi, false));
        lstFacilities.add(new Item_Grid_Facilities("Gác", R.drawable.icon_gac, false));
        lstFacilities.add(new Item_Grid_Facilities("Toilet riêng", R.drawable.icon_toilet, false));
        lstFacilities.add(new Item_Grid_Facilities("Phòng tắm riêng", R.drawable.icon_bathroom, false));
        lstFacilities.add(new Item_Grid_Facilities("Giường", R.drawable.icon_giuong, false));
        lstFacilities.add(new Item_Grid_Facilities("Tivi", R.drawable.icon_tv, false));
        lstFacilities.add(new Item_Grid_Facilities("Tủ lạnh", R.drawable.icon_tulanh, false));
        lstFacilities.add(new Item_Grid_Facilities("Bếp gas", R.drawable.icon_bepga, false));
        lstFacilities.add(new Item_Grid_Facilities("Quạt", R.drawable.icon_quat, false));
        lstFacilities.add(new Item_Grid_Facilities("Tủ đồ", R.drawable.icon_tu_quan_ao, false));
        lstFacilities.add(new Item_Grid_Facilities("Máy lạnh", R.drawable.icon_may_lanh, false));
        lstFacilities.add(new Item_Grid_Facilities("Đèn điện", R.drawable.icon_bongden, false));

        lstFacilities.add(new Item_Grid_Facilities("Bảo vệ", R.drawable.icon_baove, false));
        lstFacilities.add(new Item_Grid_Facilities("Camera", R.drawable.icon_camera, false));
        lstFacilities.add(new Item_Grid_Facilities("Khu để xe riêng", R.drawable.icon_doxe, false));


        myAdapter = new Grid_Facilities_Adapter(this, R.layout.grid_facilities_items, lstFacilities);
        myAdapter.notifyDataSetChanged();

        //grid.setAdapter(adapter);
        gridTienNghi.setAdapter(myAdapter);
        gridTienNghi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean tmp = lstFacilities.get(i).isSelected();
                lstFacilities.get(i).setSelected(!tmp);
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setUpbuttonOK() {
        toastError = new Toast(getApplicationContext());
        toastError.setDuration(Toast.LENGTH_SHORT);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toas_error, null);
        toastError.setView(view);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int height = displayMetrics.heightPixels;

        mHadler = new mHadler();
        //mHandlerUpdateGrid = new mHandlerUpdateGrid();

        lt = new LoadToast(this);
        lt.setText("Đang xử lý...");
        lt.setTranslationY(height / 2 - 100);


        loadToast2 = new LoadToast(this);
        loadToast2.setText("Đang xử lý...");
        loadToast2.setTranslationY(height / 2 - 100);

        layoutTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Xác nhận thêm");
        alertDialogBuilder.setMessage("Các thông tin bạn nhập đã chính xác chưa ?");
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ThreadXuLy threadXuLy = new ThreadXuLy();
                threadXuLy.start();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        layoutButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTraHopLe()) {
                    alertDialog.show();
                }
            }
        });
    }

    public class mHadler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                lt.show();
                layoutTransparent.setVisibility(View.VISIBLE);
                //isposting = true;
            } else {
                //isposting = false;
                layoutTransparent.setVisibility(View.GONE);
                if ((boolean) msg.obj) {
                    lt.success();

                    // Sửa dữ liệu thành công
                    if (key == 2) {
                        Intent intents = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", thongTinTimPhong);
                        intents.putExtra("data", bundle);
                        setResult(75, intents);
                        finish();
                    } else {

                    }
                } else {
                    //lt.error();
                    lt.hide();
                    toastError.show();
                }
            }
        }

    }

    public class ThreadXuLy extends Thread {
        @Override
        public void run() {
            mHadler.sendEmptyMessage(0);
            TinTimPhong tinTimPhong = new TinTimPhong();
            tinTimPhong.setTieude(valueTieuDe.getText().toString());
            if (checkPhongTro.isChecked()) {
                tinTimPhong.setLoaitin(1);
            } else {
                if (checkTimOGhep.isChecked()) {
                    tinTimPhong.setLoaitin(2);
                } else {
                    if (checkNhaNguyenCan.isChecked()) {
                        tinTimPhong.setLoaitin(3);
                    }
                }
            }
            tinTimPhong.setSonguoimin(Integer.parseInt(minSoNguoi.getText().toString()));
            tinTimPhong.setSonguoimax(Integer.parseInt(maxSoNguoi.getText().toString()));
            tinTimPhong.setGiamin(seekGia.getSelectedMinValue().intValue());
            tinTimPhong.setGiamax(seekGia.getSelectedMaxValue().intValue());
            tinTimPhong.setIdtp(idtp);
            idqh = "";
            String tenqh = "";
            for (int i = 0; i < quanHuyens.size(); i++) {
                if (quanHuyens.get(i).isSelect()) {
                    idqh = idqh + '"' + quanHuyens.get(i).getId() + '"' + ",";
                    tenqh = tenqh + quanHuyens.get(i).getTen() + ", ";
                }
            }
            if (idqh.length() > 1) {
                idqh = idqh.substring(0, idqh.length() - 1);
                tenqh = tenqh.substring(0, tenqh.length() - 2);
            }
            tinTimPhong.setQh(tenqh);
            tinTimPhong.setIdqh(idqh);
            tinTimPhong.setKhuvuc(valueKV.getText().toString());

            tinTimPhong.setLat(lat);
            tinTimPhong.setLng(lng);
            tinTimPhong.setBankinh(bankinh);
            tinTimPhong.setId(iditem);
            String tiennghi = "";
            for (int i = 0; i < lstFacilities.size(); i++) {
                if (i == 0) {
                    if (lstFacilities.get(i).isSelected() == true) {
                        tiennghi = "wifi,";
                    }
                } else {
                    if (i == 1) {
                        if (lstFacilities.get(i).isSelected() == true) {
                            tiennghi = tiennghi + "gac,";
                        }
                    } else {
                        if (i == 2) {
                            if (lstFacilities.get(i).isSelected() == true) {
                                tiennghi = tiennghi + "toilet,";
                            }
                        } else {
                            if (i == 3) {
                                if (lstFacilities.get(i).isSelected() == true) {
                                    tiennghi = tiennghi + "phongtam,";
                                }
                            } else {
                                if (i == 4) {
                                    if (lstFacilities.get(i).isSelected() == true) {
                                        tiennghi = tiennghi + "giuong,";
                                    }
                                } else {
                                    if (i == 5) {
                                        if (lstFacilities.get(i).isSelected() == true) {
                                            tiennghi = tiennghi + "tv,";
                                        }
                                    } else {
                                        if (i == 6) {
                                            if (lstFacilities.get(i).isSelected() == true) {
                                                tiennghi = tiennghi + "tulanh,";
                                            }
                                        } else {
                                            if (i == 7) {
                                                if (lstFacilities.get(i).isSelected() == true) {
                                                    tiennghi = tiennghi + "bepga,";
                                                }
                                            } else {
                                                if (i == 8) {
                                                    if (lstFacilities.get(i).isSelected() == true) {
                                                        tiennghi = tiennghi + "quat,";
                                                    }
                                                } else {
                                                    if (i == 9) {
                                                        if (lstFacilities.get(i).isSelected() == true) {
                                                            tiennghi = tiennghi + "tudo,";
                                                        }
                                                    } else {
                                                        if (i == 10) {
                                                            if (lstFacilities.get(i).isSelected() == true) {
                                                                tiennghi = tiennghi + "maylanh,";
                                                            }
                                                        } else {
                                                            if (i == 11) {
                                                                if (lstFacilities.get(i).isSelected() == true) {
                                                                    tiennghi = tiennghi + "den,";
                                                                }
                                                            } else {
                                                                if (i == 12) {
                                                                    if (lstFacilities.get(i).isSelected() == true) {
                                                                        tiennghi = tiennghi + "baove,";
                                                                    }
                                                                } else {
                                                                    if (i == 13) {
                                                                        if (lstFacilities.get(i).isSelected() == true) {
                                                                            tiennghi = tiennghi + "camera,";
                                                                        }
                                                                    } else {
                                                                        if (i == 14) {
                                                                            if (lstFacilities.get(i).isSelected() == true) {
                                                                                tiennghi = tiennghi + "khudexe,";
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            tinTimPhong.setIduser(Integer.parseInt(usDN.getId()));

            tinTimPhong.setTentp(arrTinhTP.get(indexSpnTinh).getTen());
            if (tiennghi.length() > 0) {
                tiennghi = tiennghi.substring(0, tiennghi.length() - 1);
            }

            tinTimPhong.setTiennghi(tiennghi);
            tinTimPhong.setMotathem(edtNhapBl.getText().toString());

            if (radCa2.isChecked()) {
                tinTimPhong.setGioitinh(3);
            } else {
                if (radNam.isChecked()) {
                    tinTimPhong.setGioitinh(1);
                } else {
                    tinTimPhong.setGioitinh(2);
                }
            }
            if (checkNhanTB.isChecked()) {
                tinTimPhong.setNhanthongbao(1);
            } else {
                tinTimPhong.setNhanthongbao(0);
            }

            if (!checkTD.isChecked()) {
                tinTimPhong.setGiogiac(0);
            } else {
                tinTimPhong.setGiogiac(1);
            }
            tinTimPhong.setHoten(valueHT.getText().toString());
            tinTimPhong.setSdt(valueSDT.getText().toString());
            tinTimPhong.setFacebook(valueFB.getText().toString());
            //boolean kq = false;
            // Xử lý thêm ở đây ở đây
            if (key != 2) {
                boolean kq;
                String token = "";
                DAL_TinTimPhong dal_tinTimPhong = new DAL_TinTimPhong();
                kq = dal_tinTimPhong.themTinMoi(tinTimPhong, token);
                Message message = mHadler.obtainMessage(1, kq);
                mHadler.sendMessage(message);
            } else {
                //Toast.makeText(getApplicationContext(), "Cập nhật..", Toast.LENGTH_SHORT).show();
                DAL_TinTimPhong dal_tinTimPhong = new DAL_TinTimPhong();
                boolean kq;
                kq = dal_tinTimPhong.capNhatTinTimPhong(tinTimPhong, thongTinTimPhong);
                if (kq) {
                    thongTinTimPhong = tinTimPhong;
                }
                Message message = mHadler.obtainMessage(1, kq);
                mHadler.sendMessage(message);

            }


        }
    }

    public boolean kiemTraHopLe() {
        if (valueTieuDe.getText().toString().equals("")) {
            showToast("Nhập tiêu đề tin tức");
            return false;
        } else {
            if (!checkPhongTro.isChecked() && !checkNhaNguyenCan.isChecked() && !checkTimOGhep.isChecked()) {
                showToast("Chọn loại tin tức");
                return false;
            } else {
                boolean tmp = false;
                for (int i = 0; i < quanHuyens.size(); i++) {
                    if (quanHuyens.get(i).isSelect()) {
                        tmp = true;
                        break;
                    }
                }
                if (!tmp) {
                    showToast("Chọn các quận/huyện muốn tìm kiếm !");
                    return false;
                } else {
                    if (currentCircle == null) {
                        showToast("Đánh dấu khu vực muốn tìm trên bản đồ !!");
                        return false;
                    } else {
                        if (valueHT.getText().toString().equals("")) {
                            showToast("Nhập họ tên người liên lạc");
                            return false;
                        } else {
                            if (valueSDT.getText().toString().equals("")) {
                                showToast("Nhập số điện thoại liên lạc");
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void showToast(String str) {
        Toast.makeText(ThemTinTimPhong.this, str, Toast.LENGTH_SHORT).show();
    }

    public void setUpCheck() {
        checkPhongTro = findViewById(R.id.checkPhongTro);
        checkNhaNguyenCan = findViewById(R.id.checkNhaNguyenCan);
        checkTimOGhep = findViewById(R.id.checkTimOGhep);
        checkPhongTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhongTro.setSelected(true);
                checkNhaNguyenCan.setSelected(false);
                checkTimOGhep.setSelected(false);
            }
        });
    }

    private void getUser() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            String user11 = sharedPreferences.getString("MyUser", "");
            if (user11.equals("") || user11 == null) {

            } else {
                Gson gsonUser = new Gson();

                usDN = gsonUser.fromJson(user11, User.class);
            }
        } catch (Exception e) {
        }
    }

    private void clearFocus() {
        valueTieuDe.clearFocus();
        valueKV.clearFocus();
        valueHT.clearFocus();
        valueSDT.clearFocus();
        valueFB.clearFocus();
        edtNhapBl.clearFocus();
    }
}
