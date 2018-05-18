package com.nhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nhatro.adapter.Adapter_ChonQuan_TinTimPhong;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.Grid_Quan_Huyen_Adapter;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.QuanHuyen;
import com.nhatro.sqlite.SQLite_QuanHuyen;

import java.text.DecimalFormat;
import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.apptik.widget.MultiSlider;

public class Filter2 extends AppCompatActivity {


    /// GRID Tiện ích
    GridView gridFacilities;
    ArrayList<Item_Grid_Facilities> lstFacilities = new ArrayList<>();
    boolean[] chontiennghi = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    int valueGia;
    boolean tinnhatro;
    boolean tinoghep;
    boolean tinnhanguyencan;
    int giogiac;
    int namnu;
    RadioButton radNam, radNu, radCa2;

    // Danh sách các quận huyện đang chọn

    int idqh;
    int idtp;
    int indexqh = -1;
    String tenTP = "";
    ExpandableHeightGridView gridQuanHuyen;
    Adapter_ChonQuan_TinTimPhong grid_quan_huyen_adapter;
    ArrayList<QuanHuyen> listQuanHuyen;


    TextView btnOK, btnCancel, txtTPHT, txtGia;

    CheckBox checkNhaTro, checkNhaNguyenCan, checkTimOGhep, checkGioGiac;
    LinearLayout btnChonTinh;

    MultiSlider gia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Bộ lọc");

        //selectedFacitilies = bundle.getBooleanArray("arrFacilities");
        /// FIND VIEW

        findView();
        getDataBundle();
        setViewAfterGetData();
        setupgridTN();
        setEvent();
        ///////////

        ////

    }

    public void getDataBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        idtp = bundle.getInt("idtp");
        tenTP = bundle.getString("tentp");
        idqh = bundle.getInt("idqh");
        valueGia = bundle.getInt("gia");
        tinnhatro = bundle.getBoolean("tinnhatro");
        tinnhanguyencan = bundle.getBoolean("tinnhanguyencan");
        tinoghep = bundle.getBoolean("tinoghep");
        giogiac = bundle.getInt("giogiac");
        namnu = bundle.getInt("namnu");
        chontiennghi = bundle.getBooleanArray("chontiennghi");
    }

    public void setViewAfterGetData() {
        listQuanHuyen = getDsQH(idtp);
        if (idqh != -1) {
            for (int i = 0; i < listQuanHuyen.size(); i++) {
                if (listQuanHuyen.get(i).getId() == idqh) {
                    listQuanHuyen.get(i).setSelect(true);
                    indexqh = i;
                    break;
                }
                //listQuanHuyen.g
            }
        }

        txtTPHT.setText(tenTP);
        grid_quan_huyen_adapter = new Adapter_ChonQuan_TinTimPhong(getApplicationContext(), R.layout.item_recycle_view_chon_quan, listQuanHuyen);
        gridQuanHuyen.setAdapter(grid_quan_huyen_adapter);

        /// SLIDER GIÁ
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        String tmp = formatter.format(valueGia) + " VNĐ";
        txtGia.setText(tmp);
        gia.getThumb(0).setValue(valueGia);
        ////


        // CHECK BOX LOẠI TIN
        if (tinnhatro) {
            checkNhaTro.setChecked(true);
        }
        if (tinoghep) {
            checkTimOGhep.setChecked(true);
        }
        if (tinnhanguyencan) {
            checkNhaNguyenCan.setChecked(true);
        }
        //

        //CHECK GIỜ GIẤC
        if (giogiac == 1) {
            checkGioGiac.setChecked(true);
        }

        //
        if (namnu == 1) {
            radNam.setChecked(true);
        } else {
            if (namnu == 2) {
                radNu.setChecked(true);
            } else {
                if (namnu == 3) {
                    radCa2.setChecked(true);
                }
            }
        }


    }

    public void findView() {
        checkGioGiac = findViewById(R.id.checkGioGiac);
        checkNhaNguyenCan = (CheckBox) findViewById(R.id.checkNhaNguyenCan);
        checkNhaTro = (CheckBox) findViewById(R.id.checkPhongTro);
        checkTimOGhep = (CheckBox) findViewById(R.id.checkTimOGhep);
        btnChonTinh = (LinearLayout) findViewById(R.id.lblTinh);
        txtTPHT = (TextView) findViewById(R.id.txtTPHT);
        radNam = (RadioButton) findViewById(R.id.radNam);
        radNu = (RadioButton) findViewById(R.id.radNu);
        radCa2 = (RadioButton) findViewById(R.id.radCa2);


        listQuanHuyen = new ArrayList<>();
        gridQuanHuyen = (ExpandableHeightGridView) findViewById(R.id.gridQuan);
        gridQuanHuyen.setExpanded(true);
        gridQuanHuyen.setFocusable(false);


        gia = findViewById(R.id.sliderArea);
        txtGia = findViewById(R.id.minValue);
        btnOK = (TextView) findViewById(R.id.btnTimKiem);
        btnCancel = (TextView) findViewById(R.id.btnHuy);
    }

    public void setEvent() {

        btnChonTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Chọn Tỉnh",Toast.LENGTH_SHORT).show();

                Intent inten1 = new Intent(getApplicationContext(), ChonTinh.class);
                Bundle bundle = new Bundle();

                bundle.putInt("tinhTP", idtp);
                inten1.putExtra("data", bundle);
                startActivityForResult(inten1, 101);

                //Toast.makeText(getApplicationContext(),String.valueOf(tinhTP),Toast.LENGTH_SHORT).show();
            }
        });


        gia.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                valueGia = value;
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String tmp = formatter.format(value) + " VNĐ";
                txtGia.setText(tmp);
            }
        });

        gridQuanHuyen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (indexqh == i) {
                    idqh = -1;
                    indexqh = -1;
                    listQuanHuyen.get(i).setSelect(false);
                } else {

                    if (indexqh != -1) {
                        listQuanHuyen.get(indexqh).setSelect(false);
                    }
                    indexqh = i;
                    idqh = listQuanHuyen.get(i).getId();
                    listQuanHuyen.get(i).setSelect(true);
                }

                grid_quan_huyen_adapter.notifyDataSetChanged();

                //selectedFacitilies[i] = !tmp;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = getIntent();
                Bundle bundle = new Bundle();

                bundle.putString("tiennghi", tienNghi());
                bundle.putInt("idtp", idtp);
                bundle.putString("tentp", tenTP);
                if (radCa2.isChecked()) {
                    bundle.putInt("namnu", 3);
                } else {
                    if (radNam.isChecked()) {
                        bundle.putInt("namnu", 1);
                    } else {
                        bundle.putInt("namnu", 2);
                    }
                }

                bundle.putInt("gia", valueGia);
                bundle.putInt("idqh", idqh);
                bundle.putBooleanArray("chontiennghi", chontiennghi);
                if (checkGioGiac.isChecked()) {
                    bundle.putInt("giogiac", 1);
                } else {
                    bundle.putInt("giogiac", 0);
                }

                if (checkNhaNguyenCan.isChecked()) {
                    bundle.putBoolean("tinnhanguyencan", true);
                } else {
                    bundle.putBoolean("tinnhanguyencan", false);
                }
                if (checkNhaTro.isChecked()) {
                    bundle.putBoolean("tinnhatro", true);
                } else {
                    bundle.putBoolean("tinnhatro", false);
                }

                if (checkTimOGhep.isChecked()) {
                    bundle.putBoolean("tinoghep", true);
                } else {
                    bundle.putBoolean("tinoghep", false);
                }

                intent.putExtra("data", bundle);


                setResult(1112, intent);
                finish();
            }
        });
    }

    public String tienNghi() {
        String tiennghi = "";
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                if (chontiennghi[0] == true) {
                    tiennghi = "wifi,";
                }
            } else {
                if (i == 1) {
                    if (chontiennghi[1] == true) {
                        tiennghi = tiennghi + "gac,";
                    }
                } else {
                    if (i == 2) {
                        if (chontiennghi[2] == true) {
                            tiennghi = tiennghi + "toilet,";
                        }
                    } else {
                        if (i == 3) {
                            if (chontiennghi[i] == true) {
                                tiennghi = tiennghi + "phongtam,";
                            }
                        } else {
                            if (i == 4) {
                                if (chontiennghi[i] == true) {
                                    tiennghi = tiennghi + "giuong,";
                                }
                            } else {
                                if (i == 5) {
                                    if (chontiennghi[i] == true) {
                                        tiennghi = tiennghi + "tv,";
                                    }
                                } else {
                                    if (i == 6) {
                                        if (chontiennghi[i] == true) {
                                            tiennghi = tiennghi + "tulanh,";
                                        }
                                    } else {
                                        if (i == 7) {
                                            if (chontiennghi[i] == true) {
                                                tiennghi = tiennghi + "bepga,";
                                            }
                                        } else {
                                            if (i == 8) {
                                                if (chontiennghi[i] == true) {
                                                    tiennghi = tiennghi + "quat,";
                                                }
                                            } else {
                                                if (i == 9) {
                                                    if (chontiennghi[i] == true) {
                                                        tiennghi = tiennghi + "tudo,";
                                                    }
                                                } else {
                                                    if (i == 10) {
                                                        if (chontiennghi[i] == true) {
                                                            tiennghi = tiennghi + "maylanh,";
                                                        }
                                                    } else {
                                                        if (i == 11) {
                                                            if (chontiennghi[i] == true) {
                                                                tiennghi = tiennghi + "den,";
                                                            }
                                                        } else {
                                                            if (i == 12) {
                                                                if (chontiennghi[i] == true) {
                                                                    tiennghi = tiennghi + "baove,";
                                                                }
                                                            } else {
                                                                if (i == 13) {
                                                                    if (chontiennghi[i] == true) {
                                                                        tiennghi = tiennghi + "camera,";
                                                                    }
                                                                } else {
                                                                    if (i == 14) {
                                                                        if (chontiennghi[i] == true) {
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

        if (tiennghi.length() > 0) {
            tiennghi = tiennghi.substring(0, tiennghi.length() - 1);
        }

        return tiennghi;
    }

    public ArrayList<QuanHuyen> getDsQH(int id) {
        ArrayList<QuanHuyen> data = new ArrayList<>();
        SQLite_QuanHuyen sqLite_quanHuyen1 = new SQLite_QuanHuyen(getApplicationContext());
        data = sqLite_quanHuyen1.getDSQH(id);
        return data;
    }

    public void setupgridTN() {

        lstFacilities = new ArrayList<>();
        gridFacilities = (GridView) findViewById(R.id.gridFacilities);
        ExpandableHeightGridView gridFacilities = (ExpandableHeightGridView) findViewById(R.id.gridFacilities);

        gridFacilities.setExpanded(true);
        gridFacilities.setFocusable(false);

        lstFacilities.add(new Item_Grid_Facilities("Wifi", R.drawable.icons_wi_fi, chontiennghi[0]));
        lstFacilities.add(new Item_Grid_Facilities("Gác", R.drawable.icon_gac, chontiennghi[1]));
        lstFacilities.add(new Item_Grid_Facilities("Toilet riêng", R.drawable.icon_toilet, chontiennghi[2]));
        lstFacilities.add(new Item_Grid_Facilities("Phòng tắm riêng", R.drawable.icon_bathroom, chontiennghi[3]));
        lstFacilities.add(new Item_Grid_Facilities("Giường", R.drawable.icon_giuong, chontiennghi[4]));
        lstFacilities.add(new Item_Grid_Facilities("Tivi", R.drawable.icon_tv, chontiennghi[5]));
        lstFacilities.add(new Item_Grid_Facilities("Tủ lạnh", R.drawable.icon_tulanh, chontiennghi[6]));
        lstFacilities.add(new Item_Grid_Facilities("Bếp gas", R.drawable.icon_bepga, chontiennghi[7]));
        lstFacilities.add(new Item_Grid_Facilities("Quạt", R.drawable.icon_quat, chontiennghi[8]));
        lstFacilities.add(new Item_Grid_Facilities("Tủ đồ", R.drawable.icon_tu_quan_ao, chontiennghi[9]));
        lstFacilities.add(new Item_Grid_Facilities("Máy lạnh", R.drawable.icon_may_lanh, chontiennghi[10]));
        lstFacilities.add(new Item_Grid_Facilities("Đèn điện", R.drawable.icon_bongden, chontiennghi[11]));

        lstFacilities.add(new Item_Grid_Facilities("Bảo vệ", R.drawable.icon_baove, chontiennghi[12]));
        lstFacilities.add(new Item_Grid_Facilities("Camera", R.drawable.icon_camera, chontiennghi[13]));
        lstFacilities.add(new Item_Grid_Facilities("Khu để xe riêng", R.drawable.icon_doxe, chontiennghi[14]));

        final Grid_Facilities_Adapter myAdapter = new Grid_Facilities_Adapter(this, R.layout.grid_facilities_items, lstFacilities);

        gridFacilities.setAdapter(myAdapter);

        gridFacilities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean tmp = lstFacilities.get(i).isSelected();
                lstFacilities.get(i).setSelected(!tmp);
                myAdapter.notifyDataSetChanged();
                chontiennghi[i] = !tmp;
                //HomeFragment.selectedFacilities[i] = !HomeFragment.selectedFacilities[i];

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 101) {
            Bundle bundle = data.getBundleExtra("datas");
            idtp = bundle.getInt("tinhTP");
            tenTP = bundle.getString("tenTP");
            txtTPHT.setText(bundle.getString("tenTP"));
            /*listQuanHuyen.clear();
            grid_quan_huyen_adapter.notifyDataSetChanged();*/

            listQuanHuyen = getDsQH(idtp);
            idqh = 0;
            indexqh = 0;
            grid_quan_huyen_adapter = new Adapter_ChonQuan_TinTimPhong(getApplicationContext(), R.layout.item_recycle_view_chon_quan, listQuanHuyen);
            grid_quan_huyen_adapter.notifyDataSetChanged();
            gridQuanHuyen.setAdapter(grid_quan_huyen_adapter);

        }
    }
}
