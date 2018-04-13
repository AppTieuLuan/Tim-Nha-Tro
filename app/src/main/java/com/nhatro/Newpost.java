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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Add_Image_Adapter;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.ImageFilePath;
import com.nhatro.adapter.SpinnerQuanHuyen_Adapter;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.Add_Images;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinhTP;
import com.nhatro.sqlite.SQLiteDataController;
import com.nhatro.sqlite.SQLite_QuanHuyen;
import com.nhatro.sqlite.SQLite_TinhTP;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.IOException;
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
    CharSequence[] arrdonViTienDien = new CharSequence[]{"VNĐ/kWh", "VNĐ/Tháng", "VNĐ/Người"};
    CharSequence[] arrdonViTienNuoc = new CharSequence[]{"VNĐ/Khối", "VNĐ/Tháng", "VNĐ/Người"};

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


    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstFacilities = new ArrayList<>();
    Grid_Facilities_Adapter myAdapter;

    ExpandableHeightGridView gridHA;
    Grid_Add_Image_Adapter grid_add_image_adapter;
    ArrayList<String> add_images;
    RoundedImageView idbtnAddImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Đăng tin mới");
        isposting = false;
        layoutButtonOK = findViewById(R.id.layoutButtonOK);
        layoutTransparent = findViewById(R.id.layoutTransparent);
        layoutTienDien = findViewById(R.id.layoutTienDien);
        layoutTienNuoc = findViewById(R.id.layoutTienNuoc);
        donViTienDien = findViewById(R.id.donViTienDien);
        donViTienNuoc = findViewById(R.id.donViTienNuoc);
        spinnerTinhTP = findViewById(R.id.spinnerTinhTP);
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
        valueGioDongCua = findViewById(R.id.valueGioDongCua);
        radTuDo = findViewById(R.id.radTuDo);
        radGio = findViewById(R.id.radGio);

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


        layoutButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isposting) {
                    ThreadXuLy threadXuLy = new ThreadXuLy();
                    threadXuLy.start();
                }

            }
        });

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

                Toast.makeText(getApplicationContext(), mHour + " : " + mMinute, Toast.LENGTH_SHORT).show();
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

                spinnerQuanHuyen_adapter = new SpinnerQuanHuyen_Adapter(getApplicationContext(), arrQuanHuyen);
                //spinnerQuanHuyen_adapter.notifyDataSetChanged();
                spinnerQuanHuyen.setAdapter(spinnerQuanHuyen_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
            int kq = 0;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = mHadler.obtainMessage(1, kq);
            mHadler.sendMessage(message);
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
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latt, lngg), 15));
                marker.setPosition(new LatLng(latt, lngg));

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

}
