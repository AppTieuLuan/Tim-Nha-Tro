package com.nhatro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.AdapterView;
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
import com.google.android.gms.maps.model.Marker;
import com.nhatro.adapter.AdapterRecyclerViewChonQuan;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.SpinnerQuanHuyen_Adapter;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinhTP;
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
    SpinnerTinhTP spinnerTinhTPAdapter;
    SQLite_QuanHuyen sqLite_quanHuyen;
    //int indexSpinnerTinhTp;

    TextView layoutButtonOK;

    RecyclerView recycleQH;
    ArrayList<QuanHuyen> quanHuyens;
    AdapterRecyclerViewChonQuan adapterRecyclerViewChonQuan;

    int bankinh;

    private MapView mapView;
    private GoogleMap map;
    private Circle currentCircle;


    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstFacilities = new ArrayList<>();
    Grid_Facilities_Adapter myAdapter;


    LinearLayout layoutTransparent;
    LoadToast lt, loadToast2;
    ThemTinTimPhong.mHadler mHadler;
    Toast toastError;

    EditText valueTieuDe;
    RadioButton checkPhongTro, checkNhaNguyenCan, checkTimOGhep;

    ArrayList<Integer> lstChonQH;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tin_tim_phong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thêm tin mới");
        lstChonQH = new ArrayList<>();
        seekGia = findViewById(R.id.seekGia);
        minGia = findViewById(R.id.minGia);
        maxGia = findViewById(R.id.maxGia);
        mapView = (MapView) findViewById(R.id.mapViTri);
        gridTienNghi = findViewById(R.id.gridTienNghi);
        valueTieuDe = findViewById(R.id.valueTieuDe);
        bankinh = 1;
        layoutTransparent = findViewById(R.id.layoutTransparent);
        seekSoNguoi = (CrystalRangeSeekbar) findViewById(R.id.seekSoNguoi);
        minSoNguoi = findViewById(R.id.minSoNguoi);
        maxSoNguoi = findViewById(R.id.maxSoNguoi);
        layoutButtonOK = findViewById(R.id.layoutButtonOK);
        sqLite_quanHuyen = new SQLite_QuanHuyen(ThemTinTimPhong.this);
        quanHuyens = new ArrayList<>();

        quanHuyens = sqLite_quanHuyen.getDSQH(1);

        arrTinhTP = new ArrayList<>();
        spinnerTinhTP = findViewById(R.id.spinnerTinhTP);
        sqLite_tinhTP = new SQLite_TinhTP(ThemTinTimPhong.this);

        arrTinhTP = sqLite_tinhTP.getDSTP();
        spinnerTinhTPAdapter = new SpinnerTinhTP(getApplicationContext(), arrTinhTP);
        spinnerTinhTP.setAdapter(spinnerTinhTPAdapter);


        // set listener
        seekSoNguoi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minSoNguoi.setText(String.valueOf(minValue));
                maxSoNguoi.setText(String.valueOf(maxValue));
            }
        });


        minGia.setText("0 VNĐ");
        maxGia.setText("10.000.000 VNĐ");


        seekGia.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

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


        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        setupGridTienNghi();
        setUpbuttonOK();
        setUpCheck();
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
        lt.setText("Sending Reply...");
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
                if ((int) msg.obj == 1) {
                    lt.success();
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


            // Xử lý thêm ở đây ở đây
            int kq = 1;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = mHadler.obtainMessage(1, kq);
            mHadler.sendMessage(message);
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
                    showToast("Chọn các quận/huyện muốn tìm !");
                    return false;
                } else {
                    if (currentCircle == null) {
                        showToast("Đánh dấu khu vực muốn tìm trên bản đồ !!");
                        return false;
                    } else {

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
}
