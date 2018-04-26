package com.nhatro;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.DAL.HoTros;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Add_Image_Adapter;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.ImageFilePath;
import com.nhatro.adapter.SpinnerQuanHuyen_Adapter;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.Add_Images;
import com.nhatro.model.HoTro;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.PhongTros;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinhTP;
import com.nhatro.sqlite.SQLiteDataController;
import com.nhatro.sqlite.SQLite_QuanHuyen;
import com.nhatro.sqlite.SQLite_TinhTP;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;


public class Newpost extends AppCompatActivity implements OnMapReadyCallback {

    LinearLayout layoutButtonOK, layoutTransparent, layoutTienDien, layoutTienNuoc;
    boolean isposting;
    LoadToast lt, loadToast2;
    mHadler mHadler;
    //mHandlerUpdateGrid mHandlerUpdateGrid;
    Toast toastError;

    RadioButton radTuDo, radGio;
    TextView valueGioDongCua, donViTienDien, donViTienNuoc;
    Calendar c;
    private int mHour, mMinute;


    private int chonDonViTienDien = 0, tempChonDvTienDien = 0, ChonDonViTienNuoc, tempChonDvTienNuoc;
    CharSequence[] arrdonViTienDien = new CharSequence[]{"VNĐ/Tháng", "VNĐ/kWh", "VNĐ/Người"};
    CharSequence[] arrdonViTienNuoc = new CharSequence[]{"VNĐ/Tháng", "VNĐ/Khối", "VNĐ/Người"};

    ArrayList<TinhTP> arrTinhTP;
    ArrayList<QuanHuyen> arrQuanHuyen;

    Spinner spinnerTinhTP, spinnerQuanHuyen;

    SQLite_TinhTP sqLite_tinhTP;
    SpinnerTinhTP spinnerTinhTPAdapter;
    SQLite_QuanHuyen sqLite_quanHuyen;
    SpinnerQuanHuyen_Adapter spinnerQuanHuyen_adapter;
    int indexSpinnerTinhTp, indexSpinnerQuanHuyen;

    private MapView mapView;
    private GoogleMap map;
    Marker marker;
    LatLng currentLatLng;


    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstFacilities = new ArrayList<>();
    Grid_Facilities_Adapter myAdapter;

    ExpandableHeightGridView gridHA;
    Grid_Add_Image_Adapter grid_add_image_adapter;
    ArrayList<String> add_images;
    ArrayList<String> imagesAdded;
    RoundedImageView idbtnAddImage;

    EditText valueTieuDe;
    CheckBox checkPhongTro, checkNhaNguyenCan, checkTimOGhep;
    EditText valueGia, valueDatCoc, valueChieuDai, valueChieuRong, valueTienDien, valueTienNuoc, valueSoNha;
    RadioButton radNam, radNu, radCa2;
    CrystalRangeSeekbar seekSoNguoi;
    EditText edtMoTaThem;
    TextView minSoNguoi, maxSoNguoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Đăng tin mới");
        isposting = false;

        init();

        mapView = (MapView) findViewById(R.id.mapViTri);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        createDB();
        arrTinhTP = new ArrayList<>();
        arrQuanHuyen = new ArrayList<>();
        sqLite_tinhTP = new SQLite_TinhTP(Newpost.this);
        sqLite_quanHuyen = new SQLite_QuanHuyen(Newpost.this);

        //spinnerTinhTPAdapter = new SpinnerTinhTP()

        c = Calendar.getInstance();


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


        seekbarSoNguoi();

        layoutTransparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        valueGioDongCua.setText(hourOfDay + ":" + minute);

                        mHour = hourOfDay;
                        mMinute = minute;
                    }
                }, mHour, mMinute, false);

        valueGioDongCua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
               /* mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);*/
                // Launch Time Picker Dialog
                timePickerDialog.show();

                // Toast.makeText(getApplicationContext(), mHour + " : " + mMinute, Toast.LENGTH_SHORT).show();
            }
        });
        radTuDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radGio.setChecked(false);
            }
        });

        radGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radTuDo.setChecked(false);
            }
        });


        layoutTienDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Newpost.this);
                builder.setTitle("Đơn vị tiền điện")
                        .setSingleChoiceItems(arrdonViTienDien, chonDonViTienDien, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tempChonDvTienDien = which;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chonDonViTienDien = tempChonDvTienDien;
                                donViTienDien.setText(arrdonViTienDien[chonDonViTienDien].toString());
                            }
                        });
                builder.create().show();

            }
        });

        layoutTienNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Newpost.this);
                builder.setTitle("Đơn vị tiền nước")
                        .setSingleChoiceItems(arrdonViTienNuoc, ChonDonViTienNuoc, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tempChonDvTienNuoc = which;
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ChonDonViTienNuoc = tempChonDvTienNuoc;
                                donViTienNuoc.setText(arrdonViTienNuoc[ChonDonViTienNuoc].toString());
                            }
                        });
                builder.create().show();
            }
        });

        arrTinhTP = sqLite_tinhTP.getDSTP();
        spinnerTinhTPAdapter = new SpinnerTinhTP(getApplicationContext(), arrTinhTP);
        spinnerTinhTPAdapter.notifyDataSetChanged();

        spinnerTinhTP.setAdapter(spinnerTinhTPAdapter);

        spinnerQuanHuyen_adapter = new SpinnerQuanHuyen_Adapter(getApplicationContext(), arrQuanHuyen);
        spinnerQuanHuyen_adapter.notifyDataSetChanged();
        spinnerQuanHuyen.setAdapter(spinnerQuanHuyen_adapter);
        spinnerTinhTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), String.valueOf(arrTinhTP.get(position).getId()) + " - " + arrTinhTP.get(position).getTen(),Toast.LENGTH_SHORT).show();
                arrQuanHuyen = sqLite_quanHuyen.getDSQH(arrTinhTP.get(position).getId());
                //spinnerQuanHuyen_adapter.notifyDataSetChanged();
                indexSpinnerTinhTp = position;
                spinnerQuanHuyen_adapter = new SpinnerQuanHuyen_Adapter(getApplicationContext(), arrQuanHuyen);
                //spinnerQuanHuyen_adapter.notifyDataSetChanged();
                spinnerQuanHuyen.setAdapter(spinnerQuanHuyen_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexSpinnerQuanHuyen = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initGridTienNghi();

        idbtnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Add ảnh",Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10002);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10002);


            }
        });
        grid_add_image_adapter = new Grid_Add_Image_Adapter(getApplicationContext(), R.layout.item_grid_chon_hinh_anh, add_images);
        gridHA.setAdapter(grid_add_image_adapter);
        grid_add_image_adapter.notifyDataSetChanged();

        setUpButtonOK();

        //add_images.add("");

    }


    @Override
    public boolean onSupportNavigateUp() {
        if (!isposting) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        map.getUiSettings().setZoomGesturesEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Intent intent = new Intent(Newpost.this, LayViTri.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lat", map.getCameraPosition().target.latitude);
                bundle.putDouble("lng", map.getCameraPosition().target.longitude);
                intent.putExtra("data", bundle);
                //startActivity(intent);
                startActivityForResult(intent, 111);
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
                isposting = true;
            } else {
                isposting = false;
                layoutTransparent.setVisibility(View.GONE);
                if ((boolean) msg.obj) {
                    lt.success();
                    try {
                        Thread.sleep(2000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
            // Xử lý thêm ở đây ở đây
            boolean kq = false;
            boolean kqHa = true; // Kết quả up hình ảnh
            imagesAdded.clear();

            for (int i = 0; i < add_images.size(); i++) {
                String rs = upImage(add_images.get(i));
                if (rs.equals("-1")) {
                    kqHa = false;
                    break;
                } else {
                    rs = rs.substring(1);
                    imagesAdded.add(rs);
                }
            }
            if (kqHa == true) {
                DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                PhongTros a = new PhongTros();
                a.setTieude(valueTieuDe.getText().toString());
                a.setGia(Integer.parseInt(valueGia.getText().toString()));
                a.setDiachi(valueSoNha.getText().toString());
                a.setChieudai(Float.parseFloat(valueChieuDai.getText().toString()));
                a.setChieurong(Float.parseFloat(valueChieuRong.getText().toString()));
                a.setDientich(a.getChieudai() * a.getChieurong());
                if (checkPhongTro.isChecked()) {
                    a.setLoaitin(1);
                } else {
                    if (checkTimOGhep.isChecked()) {
                        a.setLoaitin(2);
                    } else {
                        if (checkNhaNguyenCan.isChecked()) {
                            a.setLoaitin(3);
                        }
                    }
                }

                a.setSonguoimin(Integer.parseInt(minSoNguoi.getText().toString()));
                a.setSonguoimax(Integer.parseInt(maxSoNguoi.getText().toString()));
                a.setTiennghi("1111111111111111");

                if (radNam.isChecked()) {
                    a.setDoituong(1);
                } else {
                    if (radNu.isChecked()) {
                        a.setDoituong(0);
                    } else {
                        a.setDoituong(3);
                    }
                }

                a.setLat(currentLatLng.latitude);
                a.setLng(currentLatLng.longitude);

                a.setIduser(1);
                a.setMotathem(edtMoTaThem.getText().toString());
                a.setGiadien(Integer.parseInt(valueTienDien.getText().toString()));
                a.setDonvidien(donViTienDien.getText().toString());

                a.setGianuoc(Integer.parseInt(valueTienNuoc.getText().toString()));
                a.setDonvinuoc(donViTienNuoc.getText().toString());
                a.setTiencoc(Integer.parseInt(valueDatCoc.getText().toString()));
                a.setDonvicoc("VNĐ/phòng");
                if (radTuDo.isChecked()) {
                    a.setGiogiac("-1");
                } else {
                    a.setGiogiac(mHour + " : " + mMinute);
                }
                a.setIdtp(arrTinhTP.get(indexSpinnerTinhTp).getId());
                a.setIdqh(arrQuanHuyen.get(indexSpinnerQuanHuyen).getId());
                boolean kqss = false;
                kqss = dal_phongTro.themTinPhong(a, imagesAdded);
                if (kqss == false) {
                    // Xử lý xóa ảnh đã up lên...
                } else {

                }
                Message message = mHadler.obtainMessage(1, kqss);
                mHadler.sendMessage(message);
            } else {
                // Xóa ảnh đã up lên..
                // Xử lý up lỗi

                Message message = mHadler.obtainMessage(1, false);
                mHadler.sendMessage(message);
            }
        }
    }


    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 111) {

            Bundle bundles = data.getBundleExtra("data");
            //tenTP = bundle.getString("tenTP");
            double latt = bundles.getDouble("lat");
            double lngg = bundles.getDouble("lng");

            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(10.85064713, 106.77209787))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

            if (marker == null) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), 15));

                marker = map.addMarker(new MarkerOptions()
                        .position(new LatLng(latt, lngg)));

                currentLatLng = new LatLng(latt, lngg);
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), 15));
                marker.setPosition(new LatLng(latt, lngg));
                currentLatLng = new LatLng(latt, lngg);

            }

        } else {

            if (requestCode == 10002) {
                try {
                    if (resultCode == RESULT_OK && null != data) {

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        ArrayList<String> imagesEncodedList = new ArrayList<String>();

                        if (data.getData() != null) {

                            Uri mImageUri = data.getData();


                            String kq = ImageFilePath.getPath(Newpost.this, data.getData());

                            add_images.add(kq);
                            grid_add_image_adapter.notifyDataSetChanged();

                            /*ThreadUpdateGrid threadUpdateGrid = new ThreadUpdateGrid();
                            threadUpdateGrid.run();*/

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {

                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri = item.getUri();
                                    mArrayUri.add(uri);
                                    String kq = ImageFilePath.getPath(Newpost.this, uri);
                                    add_images.add(kq);
                                }
                                grid_add_image_adapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(this, "Bạn chưa chọn ảnh nào", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_LONG).show();
                }
            }


        }
    }

    public void showToast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

    public void setUpButtonOK() {

        layoutButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kiemTraDuLieu()) {
                    if (!isposting) {
                        ThreadXuLy threadXuLy = new ThreadXuLy();
                        threadXuLy.start();
                    }
                }


            }
        });
    }

    public boolean kiemTraDuLieu() {
        if (valueTieuDe.getText().toString().equals("")) {
            showToast("Nhập tiêu đề tin tức");
            return false;
        } else {
            if (!checkNhaNguyenCan.isChecked() && !checkPhongTro.isChecked() && !checkTimOGhep.isChecked()) {
                showToast("Chọn loại tin tức !");
                return false;
            } else {
                if (valueGia.getText().toString().equals("")) {
                    showToast("Nhập giá thuê phòng 1 tháng !");
                    return false;
                } else {
                    if (valueDatCoc.getText().toString().equals("")) {
                        showToast("Nhập số tiền đặt cọc trước, nhập 0 nếu không cần đặt cọc !");
                        return false;
                    } else {
                        if (valueChieuDai.getText().toString().equals("")) {
                            showToast("Nhập chiều dài phòng !");
                            return false;
                        } else {
                            if (valueChieuRong.getText().toString().equals("")) {
                                showToast("Nhập chiều rộng phòng !");
                                return false;
                            } else {
                                if (valueTienDien.getText().toString().equals("")) {
                                    showToast("Nhập tiền điện và đơn vị tính !");
                                    return false;
                                } else {
                                    if (valueTienNuoc.getText().toString().equals("")) {
                                        showToast("Nhập tiền nước và đơn vị tính");
                                        return false;
                                    } else {
                                        if (!radTuDo.isChecked() && !radGio.isChecked()) {
                                            showToast("Chọn giờ giấc !");
                                            return false;
                                        } else {
                                            if (!radNam.isChecked() && !radNu.isChecked() && !radCa2.isChecked()) {
                                                showToast("Chọn đối tượng: Nam - Nữ hay dành cho cả 2 !");
                                                return false;
                                            } else {
                                                if (valueSoNha.getText().toString().equals("")) {
                                                    showToast("Nhập địa chỉ chi tiết: Số nhà, đường, phường/xã/thị trấn... !");
                                                    return false;
                                                } else {
                                                    if (marker == null) {
                                                        showToast("Chọn vị trí phòng trên bản đồ !");
                                                        return false;
                                                    } else {
                                                        if (add_images.size() < 3) {
                                                            showToast("Chọn ít nhất 3 hình ảnh cho phòng !");
                                                            return false;
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
        return true;
    }

    public void seekbarSoNguoi() {
        seekSoNguoi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minSoNguoi.setText(String.valueOf(minValue));
                maxSoNguoi.setText(String.valueOf(maxValue));
            }
        });
    }


    public void init() {
        layoutButtonOK = findViewById(R.id.layoutButtonOK);
        edtMoTaThem = findViewById(R.id.edtMoTaThem);
        layoutTransparent = findViewById(R.id.layoutTransparent);
        layoutTienDien = findViewById(R.id.layoutTienDien);
        layoutTienNuoc = findViewById(R.id.layoutTienNuoc);
        donViTienDien = findViewById(R.id.donViTienDien);
        donViTienNuoc = findViewById(R.id.donViTienNuoc);
        spinnerTinhTP = findViewById(R.id.spinnerTinhTP);
        valueTieuDe = findViewById(R.id.valueTieuDe);
        checkPhongTro = findViewById(R.id.checkPhongTro);
        checkNhaNguyenCan = findViewById(R.id.checkNhaNguyenCan);
        checkTimOGhep = findViewById(R.id.checkTimOGhep);
        valueGia = findViewById(R.id.valueGia);
        valueDatCoc = findViewById(R.id.valueDatCoc);
        valueChieuDai = findViewById(R.id.valueChieuDai);
        valueChieuRong = findViewById(R.id.valueChieuRong);
        valueTienDien = findViewById(R.id.valueTienDien);
        valueTienNuoc = findViewById(R.id.valueTienNuoc);
        radNam = findViewById(R.id.radNam);
        radNu = findViewById(R.id.radNu);
        radCa2 = findViewById(R.id.radCa2);
        valueSoNha = findViewById(R.id.valueSoNha);
        seekSoNguoi = findViewById(R.id.seekSoNguoi);
        minSoNguoi = findViewById(R.id.minSoNguoi);
        maxSoNguoi = findViewById(R.id.maxSoNguoi);

        indexSpinnerTinhTp = 0;
        spinnerQuanHuyen = findViewById(R.id.spinnerQuanHuyen);
        indexSpinnerQuanHuyen = 0;

        idbtnAddImage = findViewById(R.id.idbtnAddImage);

        gridHA = findViewById(R.id.gridHA);
        gridHA.setExpanded(true);
        gridHA.setFocusable(false);
        add_images = new ArrayList<>();


        gridTienNghi = findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);

        valueGioDongCua = findViewById(R.id.valueGioDongCua);
        radTuDo = findViewById(R.id.radTuDo);
        radGio = findViewById(R.id.radGio);
        imagesAdded = new ArrayList<>();

    }

    public void initGridTienNghi() {
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

    public String upImage(String filePath) {
        String rs = "-1";
        try {
            String sourceFileUri = filePath;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {

                try {
                    String upLoadServerUri = "https://nhatroservice.000webhostapp.com/images/upload_image.php";
                    //String upLoadServerUri = "http://192.168.1.9:8080/firebase/images/upimg.php";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    int serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    if (serverResponseCode == 200) {
                        //return serverResponseMessage;
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        fileInputStream.close();
                        dos.flush();
                        dos.close();
                        return response.toString();
                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }

}
