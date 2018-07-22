package com.nhatro.tab_details;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.Gson;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.Newpost;
import com.nhatro.R;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.adapter.SpinnerQuanHuyen_Adapter;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.PhongTros;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinhTP;
import com.nhatro.model.User;
import com.nhatro.sqlite.SQLite_QuanHuyen;
import com.nhatro.sqlite.SQLite_TinhTP;

import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabChiTiet extends Fragment {

    String idItem;
    TextView valueDatCoc, valueLoaiNhaO, valueDienTich, valueGiaThue, valueTienDien, valueTienNuoc, valueGioGiac,
            valueHoten, valueSDT, valueDiaChi, txtbinhluan, valueGioDongCua, donViTienDien, donViTienNuoc, valueSoNguoi,
            minSoNguoi, maxSoNguoi, valueGioiTinhs, textBCLoi;

    ImageView btnSMS, btnCall, btnFacebook, btnMessenger, btnEditTTCT, btnCancelEditTTCT, btnSaveEdit, btnEditTienIch, btnCancelEditTienNghi,
            btnSaveEditTienNghi, btnEditMoTaThem, btnCancelEditMota, btnSaveEditMota, btnEditDiaChi, btnSaveEditDiaChi, btnCancelEditDiaChi;

    ExpandableHeightGridView gridTienNghi, gridTienNghiEdt;
    ArrayList<Item_Grid_Facilities> lstTienNghi, listSuaTienNghi;
    Grid_Facilities_Adapter myAdapter, adapterListSuaTienNghi;
    LinearLayout layoutReport, layoutCocTruoc, layoutTienDien, layoutTienNuoc, layoutTexxtReP;
    ConstraintLayout layoutLienHeEdit, loadlayoutThongTinChiTiet, layoutThongTinChiTietEdt, layoutThongTinChiTiet, layoutLienHe, layoutTienIchEdt, layoutTienIch, layoutMoTaThemEdit, layoutMoTaThem;
    PhongTros phongTros;
    ScrollView scrollChiTiet;
    View v;
    CrystalRangeSeekbar seekSoNguoi;

    RadioButton checkPhongTro, checkNhaNguyenCan, checkTimOGhep, radTuDo, radGio, radCa2, radNu, radNam;
    EditText valueChieuDai, valueChieuRong, valueGia, valueDatCocEdt, valueTienDienEdt, valueTienNuocEdt, valueTieuDeEdt, txtbinhluanEdit,
            valueHotenEdit, valueSDTedit, valueSoNhaEdit, valuefacebookEdit;

    ProgressBar loadSave, loadSaveTienNghi, loadSaveEditMota, loadSaveDiaChi, progressreport;
    Spinner spinnerTinhTP, spinnerQuanHuyen;
    private int chonDonViTienDien = -1, tempChonDvTienDien = 0, ChonDonViTienNuoc = -1, tempChonDvTienNuoc;
    CharSequence[] arrdonViTienDien = new CharSequence[]{"VNĐ/Tháng", "VNĐ/kWh", "VNĐ/Người"};
    CharSequence[] arrdonViTienNuoc = new CharSequence[]{"VNĐ/Tháng", "VNĐ/Khối", "VNĐ/Người"};
    private int mHour, mMinute;

    android.support.v7.app.AlertDialog.Builder alertDialogBuilder;

    ArrayList<TinhTP> arrTinhTP;
    ArrayList<QuanHuyen> arrQuanHuyen;
    SQLite_TinhTP sqLite_tinhTP;
    SpinnerTinhTP spinnerTinhTPAdapter;
    SQLite_QuanHuyen sqLite_quanHuyen;
    SpinnerQuanHuyen_Adapter spinnerQuanHuyen_adapter;
    int indexSpnTinh, indexSpnQH;
    PhongTros tempSuaPT;
    private boolean suadl;
    User users;
    boolean opop; // cho biết là nút báo cáo hay nút hết phòng.
    boolean isreporting; // Cho biết đang report hay gì gì đó
    int set1;
    EditText valueEdtReport;
    AlertDialog alertReport;

    public TabChiTiet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*if (v == null) {*/
        v = inflater.inflate(R.layout.fragment_tab_chi_tiet, container, false);
        findView(v);
        getUserInfo();


        lstTienNghi = new ArrayList<>();
        listSuaTienNghi = new ArrayList<>();

        gridTienNghi = v.findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);

        gridTienNghiEdt.setExpanded(true);
        gridTienNghiEdt.setFocusable(false);

        Bundle bundle = this.getArguments();
        if (bundle != null && v != null) {
            idItem = bundle.getString("id");

            //lstTienNghi.clear();
            Log.d("LOADDD", "LOAD DLLLLLLLLLLL");

            lstTienNghi.add(new Item_Grid_Facilities("Wifi", R.drawable.icons_wi_fi, false));
            lstTienNghi.add(new Item_Grid_Facilities("Gác", R.drawable.icon_gac, false));
            lstTienNghi.add(new Item_Grid_Facilities("Toilet riêng", R.drawable.icon_toilet, false));
            lstTienNghi.add(new Item_Grid_Facilities("Phòng tắm riêng", R.drawable.icon_bathroom, false));
            lstTienNghi.add(new Item_Grid_Facilities("Giường", R.drawable.icon_giuong, false));
            lstTienNghi.add(new Item_Grid_Facilities("Tivi", R.drawable.icon_tv, false));
            lstTienNghi.add(new Item_Grid_Facilities("Tủ lạnh", R.drawable.icon_tulanh, false));
            lstTienNghi.add(new Item_Grid_Facilities("Bếp gas", R.drawable.icon_bepga, false));
            lstTienNghi.add(new Item_Grid_Facilities("Quạt", R.drawable.icon_quat, false));
            lstTienNghi.add(new Item_Grid_Facilities("Tủ đồ", R.drawable.icon_tu_quan_ao, false));
            lstTienNghi.add(new Item_Grid_Facilities("Máy lạnh", R.drawable.icon_may_lanh, false));
            lstTienNghi.add(new Item_Grid_Facilities("Đèn điện", R.drawable.icon_bongden, false));

            lstTienNghi.add(new Item_Grid_Facilities("Bảo vệ", R.drawable.icon_baove, false));
            lstTienNghi.add(new Item_Grid_Facilities("Camera", R.drawable.icon_camera, false));
            lstTienNghi.add(new Item_Grid_Facilities("Khu để xe riêng", R.drawable.icon_doxe, false));


            myAdapter = new Grid_Facilities_Adapter(getContext(), R.layout.grid_facilities_items, lstTienNghi);
            myAdapter.notifyDataSetChanged();

            //grid.setAdapter(adapter);
            gridTienNghi.setAdapter(myAdapter);

            layoutReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isreporting) {
                        if (textBCLoi.getText().equals("Báo hết phòng")) {
                            //Toast.makeText(getContext(), "Báo hết phòng", Toast.LENGTH_SHORT).show();
                            set1 = 0;
                            SetConPhong setConPhong = new SetConPhong();
                            setConPhong.execute(0);

                        } else {
                            if (textBCLoi.getText().equals("Báo cáo lỗi")) {
                                //Toast.makeText(getContext(), "REPORT", Toast.LENGTH_SHORT).show();
                                //Report report = new Report();
                                //report.execute();

                                alertReport.show();
                            } else {
                                if (textBCLoi.getText().equals("Báo còn phòng")) {
                                    set1 = 1;
                                    SetConPhong setConPhong = new SetConPhong();
                                    setConPhong.execute(1);
                                }
                            }
                        }
                    }
                    //Toast.makeText(getContext(), "REPORT", Toast.LENGTH_SHORT).show();

                }
            });

            phongTros = new PhongTros();
            LoadData loadData = new LoadData();
            loadData.execute(idItem);

            setEvent();
        }
        //}

        return v;
    }

    public void setHidden() {
        btnEditTTCT.setVisibility(View.GONE);
        btnEditDiaChi.setVisibility(View.GONE);
        btnEditTienIch.setVisibility(View.GONE);
        btnEditMoTaThem.setVisibility(View.GONE);

        progressreport.setVisibility(View.GONE);
        textBCLoi.setText("Báo cáo lỗi");
    }

    public void setEvent() {

        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Xác nhận cập nhật..!!!");
        alertDialogBuilder.setMessage("Xác nhận cập nhật thông tin mới ???");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                arg0.cancel();
                if (suadl) {
                    Toast.makeText(getContext(), "Đang xử lý dữ liệu.. Thử lại trong giây lát...", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkField()) {
                        tempSuaPT.setId(idItem);
                        tempSuaPT.setTieude(valueTieuDeEdt.getText().toString());
                        if (checkPhongTro.isChecked()) {
                            tempSuaPT.setLoaitin(1);
                        } else {
                            if (checkTimOGhep.isChecked()) {
                                tempSuaPT.setLoaitin(2);
                            } else {
                                if (checkNhaNguyenCan.isChecked()) {
                                    tempSuaPT.setLoaitin(3);
                                }
                            }
                        }

                        tempSuaPT.setChieudai(Float.parseFloat(valueChieuDai.getText().toString()));
                        tempSuaPT.setChieurong(Float.parseFloat(valueChieuRong.getText().toString()));
                        tempSuaPT.setGia(Integer.parseInt(valueGia.getText().toString()));
                        tempSuaPT.setTiencoc(Integer.parseInt(valueDatCocEdt.getText().toString()));
                        tempSuaPT.setGiadien(Integer.parseInt(valueTienDienEdt.getText().toString()));
                        tempSuaPT.setDonvidien(donViTienDien.getText().toString());
                        tempSuaPT.setGianuoc(Integer.parseInt(valueTienNuocEdt.getText().toString()));
                        tempSuaPT.setDonvinuoc(donViTienNuoc.getText().toString());
                        if (radTuDo.isChecked()) {
                            tempSuaPT.setGiogiac("-1");
                        } else {
                            tempSuaPT.setGiogiac(valueGioDongCua.getText().toString());
                        }

                        tempSuaPT.setSonguoimin(Integer.parseInt(minSoNguoi.getText().toString()));
                        tempSuaPT.setSonguoimax(Integer.parseInt(maxSoNguoi.getText().toString()));

                        if (radCa2.isChecked()) {
                            tempSuaPT.setDoituong(3);
                        } else {
                            if (radNam.isChecked()) {
                                tempSuaPT.setDoituong(1);
                            } else {
                                tempSuaPT.setDoituong(2);
                            }
                        }
                        CapNhatThongTinChiTiet capNhatThongTinChiTiet = new CapNhatThongTinChiTiet();
                        capNhatThongTinChiTiet.execute(tempSuaPT);
                    }

                }
            }
        });
        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });

        alertDialogBuilder.create();


        btnEditTTCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditValue();
                layoutThongTinChiTiet.setVisibility(View.GONE);
                layoutThongTinChiTietEdt.setVisibility(View.VISIBLE);
            }
        });


        btnCancelEditTTCT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suadl) {
                    showToast("Đang xử lý dữ liệu vui lòng chờ...");
                } else {
                    try {
                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {

                    }

                    layoutThongTinChiTietEdt.setVisibility(View.GONE);
                    layoutThongTinChiTiet.setVisibility(View.VISIBLE);
                }
            }
        });

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.show();
            }
        });
        radGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radTuDo.setChecked(false);
            }
        });
        radTuDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radGio.setChecked(false);
            }
        });


        layoutTienNuoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ChonDonViTienNuoc == -1) {
                    if (phongTros.getDonvinuoc().equals("VNĐ/Tháng")) {
                        tempChonDvTienNuoc = 0;
                    } else {
                        if (phongTros.getDonvinuoc().equals("VNĐ/Khối")) {
                            tempChonDvTienNuoc = 1;
                        } else {
                            tempChonDvTienNuoc = 2;
                        }
                    }
                    ChonDonViTienNuoc = tempChonDvTienNuoc;
                }

                AlertDialog.Builder dialogDonViNuoc = new AlertDialog.Builder(getContext());
                dialogDonViNuoc.setTitle("Đơn vị tiền nước")
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
                dialogDonViNuoc.create().show();
            }
        });

        layoutTienDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chonDonViTienDien == -1) {
                    if (phongTros.getDonvinuoc().equals("VNĐ/Tháng")) {
                        tempChonDvTienDien = 0;
                    } else {
                        if (phongTros.getDonvinuoc().equals("VNĐ/Khối")) {
                            tempChonDvTienDien = 1;
                        } else {
                            tempChonDvTienDien = 2;
                        }
                    }
                    chonDonViTienDien = tempChonDvTienDien;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
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
                timePickerDialog.show();
            }
        });

        seekSoNguoi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minSoNguoi.setText(String.valueOf(minValue));
                maxSoNguoi.setText(String.valueOf(maxValue));
                valueTieuDeEdt.clearFocus();
                valueChieuDai.clearFocus();
                valueChieuRong.clearFocus();
                valueGiaThue.clearFocus();
                valueDatCoc.clearFocus();
            }
        });

        btnEditTienIch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setuplistSuaTienNghi();
                layoutTienIch.setVisibility(View.GONE);
                layoutTienIchEdt.setVisibility(View.VISIBLE);
            }
        });

        btnCancelEditTienNghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suadl) {
                    showToast("Đang xử lý dữ liệu, vui lòng chờ...");
                } else {
                    layoutTienIch.setVisibility(View.VISIBLE);
                    layoutTienIchEdt.setVisibility(View.GONE);
                }
            }
        });

        btnSaveEditTienNghi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*btnSaveEditTienNghi.setVisibility(View.GONE);
                loadSaveTienNghi.setVisibility(View.VISIBLE);
                suadl = true;*/

                CapNhatTienNghi capNhatTienNghi = new CapNhatTienNghi();
                capNhatTienNghi.execute();
            }
        });

        gridTienNghiEdt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listSuaTienNghi.get(position).setSelected(!listSuaTienNghi.get(position).isSelected());
                adapterListSuaTienNghi.notifyDataSetChanged();
            }
        });

        btnEditMoTaThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtbinhluanEdit.setText(txtbinhluan.getText().toString());
                layoutMoTaThem.setVisibility(View.GONE);
                layoutMoTaThemEdit.setVisibility(View.VISIBLE);
            }
        });

        btnCancelEditMota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suadl) {
                    showToast("Đang xử lý, thử lại trong giây lát...");
                } else {
                    layoutMoTaThem.setVisibility(View.VISIBLE);
                    layoutMoTaThemEdit.setVisibility(View.GONE);
                }
            }
        });

        btnSaveEditMota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempSuaPT.setMotathem(txtbinhluanEdit.getText().toString());
                CapNhatMoTaThem capNhatMoTaThem = new CapNhatMoTaThem();
                capNhatMoTaThem.execute();
            }
        });

        btnEditDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataEditDiaChi();

            }
        });

        btnCancelEditDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suadl) {
                    showToast("Đang xử lý, vui lòng chờ trong giây lát...");
                } else {
                    layoutLienHeEdit.setVisibility(View.GONE);
                    layoutLienHe.setVisibility(View.VISIBLE);
                }
            }
        });
        spinnerTinhTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexSpnTinh = position;
                arrQuanHuyen.clear();
                arrQuanHuyen.addAll(sqLite_quanHuyen.getDSQH(arrTinhTP.get(position).getId()));
                spinnerQuanHuyen_adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                indexSpnQH = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSaveEditDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valueHotenEdit.getText().toString().equals("")) {
                    showToast("Nhập họ tên...");
                } else {
                    if (valueSDTedit.getText().toString().equals("")) {
                        showToast("Nhập số điện thoại...");
                    } else {
                        if (valueSoNhaEdit.getText().toString().equals("")) {
                            showToast("Nhập địa chỉ chi tiết (số nhà, đường, phường/xã...)");
                        } else {
                            CapNhatLienLac capNhatLienLac = new CapNhatLienLac();
                            capNhatLienLac.execute();
                        }
                    }
                }
            }
        });
    }

    public void getDataEditDiaChi() {
        valueHotenEdit.setText(phongTros.getHoten());
        valueSDTedit.setText(phongTros.getSdt());
        valuefacebookEdit.setText(phongTros.getFacebook());
        arrQuanHuyen = new ArrayList<>();
        sqLite_quanHuyen = new SQLite_QuanHuyen(getContext());
        if (arrTinhTP == null) {
            arrTinhTP = new ArrayList<>();
            sqLite_tinhTP = new SQLite_TinhTP(getContext());
            arrTinhTP = sqLite_tinhTP.getDSTP();

            for (int i = 0; i < arrTinhTP.size(); i++) {
                if (arrTinhTP.get(i).getId() == phongTros.getIdtp()) {
                    indexSpnTinh = i;
                    break;
                }
            }
            spinnerTinhTPAdapter = new SpinnerTinhTP(getContext(), arrTinhTP);
            spinnerTinhTP.setAdapter(spinnerTinhTPAdapter);
            spinnerTinhTP.setSelection(indexSpnTinh);

        }

        arrQuanHuyen = sqLite_quanHuyen.getDSQH(arrTinhTP.get(indexSpnTinh).getId());
        for (int i = 0; i < arrQuanHuyen.size(); i++) {
            if (arrQuanHuyen.get(i).getId() == phongTros.getIdqh()) {
                indexSpnQH = i;
                break;
            }
        }
        spinnerQuanHuyen_adapter = new SpinnerQuanHuyen_Adapter(getContext(), arrQuanHuyen);
        spinnerQuanHuyen.setAdapter(spinnerQuanHuyen_adapter);
        spinnerQuanHuyen.setSelection(indexSpnQH);
        valueSoNhaEdit.setText(phongTros.getDiachi());

        layoutLienHe.setVisibility(View.GONE);
        layoutLienHeEdit.setVisibility(View.VISIBLE);
    }

    public void setuplistSuaTienNghi() {
        listSuaTienNghi.clear();
        listSuaTienNghi.addAll(lstTienNghi);
        if (adapterListSuaTienNghi == null) {
            adapterListSuaTienNghi = new Grid_Facilities_Adapter(getContext(), R.layout.grid_facilities_items, listSuaTienNghi);
            gridTienNghiEdt.setAdapter(adapterListSuaTienNghi);
        } else {
            adapterListSuaTienNghi.notifyDataSetChanged();
        }
    }

    public void setEditValue() {

        valueTieuDeEdt.setText(phongTros.getTieude());
        if (phongTros.getLoaitin() == 1) {
            checkPhongTro.setChecked(true);
        } else {
            if (phongTros.getLoaitin() == 2) {
                checkTimOGhep.setChecked(true);
            } else {
                if (phongTros.getLoaitin() == 3) {
                    checkNhaNguyenCan.setChecked(true);
                }
            }
        }
        valueChieuDai.setText(String.valueOf(phongTros.getChieudai()));
        valueChieuRong.setText(String.valueOf(phongTros.getChieurong()));
        valueGia.setText(String.valueOf(phongTros.getGia()));
        valueDatCocEdt.setText(String.valueOf(phongTros.getTiencoc()));
        valueTienDienEdt.setText(String.valueOf(phongTros.getGiadien()));
        valueTienNuocEdt.setText(String.valueOf(phongTros.getGianuoc()));

        donViTienDien.setText(phongTros.getDonvidien());
        donViTienNuoc.setText(phongTros.getDonvinuoc());

        if (phongTros.getGiogiac().equals("-1")) {
            radTuDo.setChecked(true);
            radGio.setChecked(false);
        } else {
            //valueGioGiac.setText("Đóng cửa từ " + phongTros.getGiogiac());
            radTuDo.setChecked(false);
            radGio.setChecked(true);
            valueGioDongCua.setText(phongTros.getGiogiac());
        }

        minSoNguoi.setText(String.valueOf(phongTros.getSonguoimin()));
        maxSoNguoi.setText(String.valueOf(phongTros.getSonguoimax()));

        /*seekSoNguoi.setMinValue(phongTros.getSonguoimin());
        seekSoNguoi.setMaxValue(phongTros.getSonguoimax());*/
        /*seekSoNguoi.setMinStartValue(phongTros.getSonguoimin());
        seekSoNguoi.setMaxStartValue(phongTros.getSonguoimax());*/

        seekSoNguoi.setMinValue(1)
                .setMaxValue(10)
                .setMinStartValue(phongTros.getSonguoimin())
                .setMaxStartValue(phongTros.getSonguoimax())
                .apply();


        if (phongTros.getDoituong() == 1) {
            radNu.setChecked(false);
            radNam.setChecked(true);
            radCa2.setChecked(false);
        } else {
            if (phongTros.getDoituong() == 2) {
                radCa2.setChecked(false);
                radNam.setChecked(false);
                radNu.setChecked(true);
            } else {
                radCa2.setChecked(true);
                radNu.setChecked(false);
                radNam.setChecked(false);
            }
        }
    }

    public void findView(View v) {
        tempSuaPT = new PhongTros();
        valueLoaiNhaO = v.findViewById(R.id.valueLoaiNhaO);
        valueGioiTinhs = v.findViewById(R.id.valueGioiTinhs);
        layoutThongTinChiTietEdt = v.findViewById(R.id.layoutThongTinChiTietEdt);
        layoutThongTinChiTiet = v.findViewById(R.id.layoutThongTinChiTiet);
        btnEditTTCT = v.findViewById(R.id.btnEditTTCT);
        btnCancelEditTTCT = v.findViewById(R.id.btnCancelEditTTCT);
        btnSaveEdit = v.findViewById(R.id.btnSaveEdit);
        valueSoNguoi = v.findViewById(R.id.valueSoNguoi);
        valueDienTich = v.findViewById(R.id.valueDienTich);
        valueGiaThue = v.findViewById(R.id.valueGiaThue);
        valueDatCoc = v.findViewById(R.id.valueDatCoc);
        valueTienDien = v.findViewById(R.id.valueTienDien);
        valueTienNuoc = v.findViewById(R.id.valueTienNuoc);
        valueGioGiac = v.findViewById(R.id.valueGioGiac);
        valueHoten = v.findViewById(R.id.valueHoten);
        valueSDT = v.findViewById(R.id.valueSDT);
        valueDiaChi = v.findViewById(R.id.valueDiaChi);
        txtbinhluan = v.findViewById(R.id.txtbinhluan);
        scrollChiTiet = v.findViewById(R.id.scrollChiTiet);
        loadlayoutThongTinChiTiet = v.findViewById(R.id.loadlayoutThongTinChiTiet);
        btnMessenger = v.findViewById(R.id.iconMessenger);
        btnCall = v.findViewById(R.id.iconCall);
        btnFacebook = v.findViewById(R.id.iconfacebook);
        btnSMS = v.findViewById(R.id.iconSMS);
        layoutReport = v.findViewById(R.id.layoutReport);
        checkPhongTro = v.findViewById(R.id.checkPhongTro);
        checkNhaNguyenCan = v.findViewById(R.id.checkNhaNguyenCan);
        checkTimOGhep = v.findViewById(R.id.checkTimOGhep);
        valueChieuDai = v.findViewById(R.id.valueChieuDai);
        valueChieuRong = v.findViewById(R.id.valueChieuRong);
        valueGia = v.findViewById(R.id.valueGia);
        valueDatCocEdt = v.findViewById(R.id.valueDatCocEdt);
        valueTienDienEdt = v.findViewById(R.id.valueTienDienEdt);
        valueTienNuocEdt = v.findViewById(R.id.valueTienNuocEdt);
        radGio = v.findViewById(R.id.radGio);
        radTuDo = v.findViewById(R.id.radTuDo);
        valueGioDongCua = v.findViewById(R.id.valueGioDongCua);
        donViTienDien = v.findViewById(R.id.donViTienDien);
        donViTienNuoc = v.findViewById(R.id.donViTienNuoc);
        layoutTienDien = v.findViewById(R.id.layoutTienDien);
        layoutTienNuoc = v.findViewById(R.id.layoutTienNuoc);
        seekSoNguoi = v.findViewById(R.id.seekSoNguoi);
        minSoNguoi = v.findViewById(R.id.minSoNguoi);
        maxSoNguoi = v.findViewById(R.id.maxSoNguoi);
        suadl = false;
        loadSave = v.findViewById(R.id.loadSave);
        valueTieuDeEdt = v.findViewById(R.id.valueTieuDeEdt);
        radCa2 = v.findViewById(R.id.radCa2);
        radNam = v.findViewById(R.id.radNam);
        radNu = v.findViewById(R.id.radNu);
        gridTienNghiEdt = v.findViewById(R.id.gridTienNghiEdt);
        layoutTienIchEdt = v.findViewById(R.id.layoutTienIchEdt);
        layoutTienIch = v.findViewById(R.id.layoutTienIch);
        btnEditTienIch = v.findViewById(R.id.btnEditTienIch);
        btnSaveEditTienNghi = v.findViewById(R.id.btnSaveEditTienNghi);
        btnCancelEditTienNghi = v.findViewById(R.id.btnCancelEditTienNghi);
        loadSaveTienNghi = v.findViewById(R.id.loadSaveTienNghi);
        txtbinhluanEdit = v.findViewById(R.id.txtbinhluanEdit);
        btnEditMoTaThem = v.findViewById(R.id.btnEditMoTaThem);
        layoutMoTaThem = v.findViewById(R.id.layoutMoTaThem);
        layoutMoTaThemEdit = v.findViewById(R.id.layoutMoTaThemEdit);
        btnCancelEditMota = v.findViewById(R.id.btnCancelEditMota);
        loadSaveEditMota = v.findViewById(R.id.loadSaveEditMota);
        btnSaveEditMota = v.findViewById(R.id.btnSaveEditMota);
        btnEditDiaChi = v.findViewById(R.id.btnEditDiaChi);
        layoutLienHe = v.findViewById(R.id.layoutLienHe);
        valueHotenEdit = v.findViewById(R.id.valueHotenEdit);
        valueSDTedit = v.findViewById(R.id.valueSDTedit);
        layoutLienHeEdit = v.findViewById(R.id.layoutLienHeEdit);
        spinnerTinhTP = v.findViewById(R.id.spinnerTinhTP);
        spinnerQuanHuyen = v.findViewById(R.id.spinnerQuanHuyen);
        valueSoNhaEdit = v.findViewById(R.id.valueSoNhaEdit);
        valuefacebookEdit = v.findViewById(R.id.valuefacebookEdit);
        btnSaveEditDiaChi = v.findViewById(R.id.btnSaveEditDiaChi);
        loadSaveDiaChi = v.findViewById(R.id.loadSaveDiaChi);
        btnCancelEditDiaChi = v.findViewById(R.id.btnCancelEditDiaChi);
        textBCLoi = v.findViewById(R.id.textBCLoi);
        progressreport = v.findViewById(R.id.progressreport);
        layoutTexxtReP = v.findViewById(R.id.layoutTexxtReP);

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.modal_report_custom, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptsView);

        valueEdtReport = promptsView.findViewById(R.id.valueEdtReport);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", null) //Set to null. We override the onclick
                .setNegativeButton("CANCEL", null);

        /*alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String valuesss = valueEdtReport.getText().toString();
                                if (valuesss.length() > 10) {
                                    alertReport.hide();
                                    Report report = new Report();
                                    report.execute();
                                } else {
                                    //Toast.makeText(getContext(), "Nhập nội dung lớn hơn 10 ký tự !!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });*/


        alertReport = alertDialogBuilder.create();
        alertReport.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alertReport.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String valuesss = valueEdtReport.getText().toString();
                        if (valuesss.length() > 10) {
                            alertReport.hide();
                            Report report = new Report();
                            report.execute(valuesss);
                        } else {
                            Toast.makeText(getContext(), "Nhập nội dung lớn hơn 10 ký tự !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Button c = alertReport.getButton(AlertDialog.BUTTON_NEGATIVE);
                c.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertReport.dismiss();
                    }
                });
            }
        });
    }

    public boolean checkField() {
        if (valueTieuDeEdt.getText().toString().equals("")) {
            showToast("Nhập tiêu đề...");
            return false;
        } else {
            if (valueChieuDai.getText().toString().equals("") || valueChieuDai.getText().toString().equals("0")) {
                showToast("Chiều dài không hợp lệ...");
                return false;
            } else {
                if (valueChieuRong.getText().toString().equals("") || valueChieuRong.getText().toString().equals("0")) {
                    showToast("Chiều rộng không hợp lệ...");
                    return false;
                } else {
                    if (valueGia.getText().toString().equals("") || valueGia.getText().toString().equals("0")) {
                        showToast("Giá không hợp lệ...");
                        return false;
                    } else {
                        if (valueDatCocEdt.getText().toString().equals("")) {
                            showToast("Nhập số tiền đặt cọc phòng, nhập 0 nếu không cần đặt cọc !");
                            return false;
                        } else {
                            if (valueTienDienEdt.getText().toString().equals("")) {
                                showToast("Nhập tiền điện và đơn vị tính !");
                                return false;
                            } else {
                                if (valueTienNuocEdt.getText().toString().equals("")) {
                                    showToast("Nhập tiền nước và đơn vị tính !");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public void showToast(String string) {
        Toast.makeText(getContext(), string, Toast.LENGTH_SHORT).show();
    }

    public class LoadData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {

            try {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("MyUser", "");

                if (user.equals("") || user == null) {
                    DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                    phongTros = dal_phongTro.thongTinPhong(strings[0], -1);
                } else {
                    Gson gsonUser = new Gson();
                    User users1 = new User();
                    users1 = gsonUser.fromJson(user, User.class);

                    DAL_PhongTro dal_phongTro = new DAL_PhongTro();
                    phongTros = dal_phongTro.thongTinPhong(strings[0], Integer.parseInt(users1.getId()));
                }
            } catch (Exception e) {

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (phongTros != null) {

                try {
                    if (users == null || Integer.parseInt(users.getId()) != phongTros.getIduser()) {
                        setHidden();
                    } else {
                        if (phongTros.getConphong() == 1) {
                            textBCLoi.setText("Báo hết phòng");
                        } else {
                            textBCLoi.setText("Báo còn phòng");
                        }
                    }
                    if (phongTros.getLoaitin() == 1) {
                        valueLoaiNhaO.setText("Cho thuê phòng trọ");
                    } else {
                        if (phongTros.getLoaitin() == 2) {
                            valueLoaiNhaO.setText("Tìm bạn ở ghép");
                        } else {
                            if (phongTros.getLoaitin() == 3) {
                                valueLoaiNhaO.setText("Cho thuê nhà nguyên căn");
                            }
                        }
                    }
                    valueDienTich.setText(phongTros.getDientich() + "m2 " + "(" + phongTros.getChieudai() + "m x " + phongTros.getChieurong() + "m)");

                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String tmp = formatter.format(phongTros.getGia()) + " VNĐ/Tháng";

                    valueGiaThue.setText(tmp);

                    if (phongTros.getTiencoc() == 0) {
                        valueDatCoc.setText("Không cần");
                    } else {
                        tmp = formatter.format(phongTros.getTiencoc()) + " VNĐ/Tháng";
                        valueDatCoc.setText(tmp);
                    }
                    tmp = formatter.format(phongTros.getGiadien()) + " " + phongTros.getDonvidien();
                    valueTienDien.setText(tmp);

                    tmp = formatter.format(phongTros.getGianuoc()) + " " + phongTros.getDonvinuoc();
                    valueTienNuoc.setText(tmp);

                    if (phongTros.getGiogiac().equals("-1")) {
                        valueGioGiac.setText("Tự do");
                    } else {
                        valueGioGiac.setText("Đóng cửa từ " + phongTros.getGiogiac());
                    }
                    valueSoNguoi.setText("Từ " + phongTros.getSonguoimin() + " - " + phongTros.getSonguoimax() + " người");
                    valueHoten.setText(phongTros.getHoten());
                    valueSDT.setText(phongTros.getSdt());
                    valueDiaChi.setText(phongTros.getDiachi() + "," + phongTros.getTenqh() + "," + phongTros.getTentp());

                    txtbinhluan.setText(phongTros.getMotathem());

                    String[] array = phongTros.getTiennghi().split(",", -1);
                    for (int i = 0; i < array.length; i++) {
                        if (array[i].equals("wifi")) {
                            lstTienNghi.get(0).setSelected(true);
                        } else {
                            if (array[i].equals("gac")) {
                                lstTienNghi.get(1).setSelected(true);
                            } else {
                                if (array[i].equals("toilet")) {
                                    lstTienNghi.get(2).setSelected(true);
                                } else {
                                    if (array[i].equals("phongtam")) {
                                        lstTienNghi.get(3).setSelected(true);
                                    } else {
                                        if (array[i].equals("giuong")) {
                                            lstTienNghi.get(4).setSelected(true);
                                        } else {
                                            if (array[i].equals("tv")) {
                                                lstTienNghi.get(5).setSelected(true);
                                            } else {
                                                if (array[i].equals("tulanh")) {
                                                    lstTienNghi.get(6).setSelected(true);
                                                } else {
                                                    if (array[i].equals("bepga")) {
                                                        lstTienNghi.get(7).setSelected(true);
                                                    } else {
                                                        if (array[i].equals("quat")) {
                                                            lstTienNghi.get(8).setSelected(true);
                                                        } else {
                                                            if (array[i].equals("tudo")) {
                                                                lstTienNghi.get(9).setSelected(true);
                                                            } else {
                                                                if (array[i].equals("maylanh")) {
                                                                    lstTienNghi.get(10).setSelected(true);
                                                                } else {
                                                                    if (array[i].equals("den")) {
                                                                        lstTienNghi.get(11).setSelected(true);
                                                                    } else {
                                                                        if (array[i].equals("baove")) {
                                                                            lstTienNghi.get(12).setSelected(true);
                                                                        } else {
                                                                            if (array[i].equals("camera")) {
                                                                                lstTienNghi.get(13).setSelected(true);
                                                                            } else {
                                                                                if (array[i].equals("khudexe")) {
                                                                                    lstTienNghi.get(14).setSelected(true);
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
                    loadlayoutThongTinChiTiet.setVisibility(View.GONE);
                    scrollChiTiet.setVisibility(View.VISIBLE);
                    setClickCall_SMS();
                    if (!phongTros.getFacebook().equals("")) {
                        setClickFacebook(phongTros.getFacebook());
                    }
                } catch (Exception e) {

                }
            }

            //Log.d("BD","KẾT THÚC LOAD DỮ LIỆU");
        }
    }

    public void setClickFacebook(String id) {
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                   /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id));
                    startActivity(intent);*/
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + id)));
                } catch (Exception e) {
                    //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + id)));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(id)));
                }
            }
        });

        btnMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + id));
                    startActivity(i);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Oups!Can't open Facebook messenger right now. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setClickCall_SMS() {
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phongTros.getSdt());
                smsIntent.putExtra("sms_body", "");
                startActivity(smsIntent);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phongTros.getSdt()));
                startActivity(intent);
            }
        });
    }

    public class CapNhatThongTinChiTiet extends AsyncTask<PhongTros, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            suadl = true;
            btnSaveEdit.setVisibility(View.GONE);
            loadSave.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(PhongTros... phongTros) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.capNhatThongTinChiTiet(phongTros[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                showToast("Cập nhật thất bại, vui lòng thử lại...");
            } else {
                if (tempSuaPT.getLoaitin() == 1) {
                    valueLoaiNhaO.setText("Cho thuê phòng trọ");
                } else {
                    if (tempSuaPT.getLoaitin() == 2) {
                        valueLoaiNhaO.setText("Tìm bạn ở ghép");
                    } else {
                        valueLoaiNhaO.setText("Cho thuê nhà nguyên căn");
                    }
                }

                phongTros.setDientich(tempSuaPT.getDientich());
                phongTros.setChieudai(tempSuaPT.getChieudai());
                phongTros.setChieurong(tempSuaPT.getChieurong());

                valueDienTich.setText(tempSuaPT.getChieudai() * tempSuaPT.getChieurong() + " m2 ( " + tempSuaPT.getChieudai() + "m x " + tempSuaPT.getChieurong() + "m )");
                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String tmp = formatter.format(tempSuaPT.getGia()) + " VNĐ/Tháng";
                valueGiaThue.setText(tmp);
                phongTros.setGia(tempSuaPT.getGia());

                tmp = formatter.format(tempSuaPT.getTiencoc()) + "VNĐ/Phòng";
                valueDatCoc.setText(tmp);
                phongTros.setTiencoc(tempSuaPT.getTiencoc());

                tmp = formatter.format(tempSuaPT.getGiadien()) + " " + tempSuaPT.getDonvidien();
                valueTienDien.setText(tmp);
                phongTros.setGiadien(tempSuaPT.getGiadien());
                phongTros.setDonvidien(tempSuaPT.getDonvidien());


                tmp = formatter.format(tempSuaPT.getGianuoc()) + " " + tempSuaPT.getDonvinuoc();
                valueTienNuoc.setText(tmp);
                phongTros.setGianuoc(tempSuaPT.getGianuoc());
                phongTros.setDonvinuoc(tempSuaPT.getDonvinuoc());

                phongTros.setGiogiac(tempSuaPT.getGiogiac());
                if (tempSuaPT.getGiogiac().equals("-1")) {
                    valueGioGiac.setText("Tự do");
                } else {
                    valueGioGiac.setText("Đóng cửa từ " + tempSuaPT.getGiogiac());
                }

                phongTros.setSonguoimin(tempSuaPT.getSonguoimin());
                phongTros.setSonguoimax(tempSuaPT.getSonguoimax());
                if (tempSuaPT.getSonguoimin() == tempSuaPT.getSonguoimax()) {
                    valueSoNguoi.setText(tempSuaPT.getSonguoimin() + " người");
                } else {
                    valueSoNguoi.setText("Từ " + tempSuaPT.getSonguoimin() + " - " + tempSuaPT.getSonguoimax() + " người");
                }

                phongTros.setDoituong(tempSuaPT.getDoituong());
                if (tempSuaPT.getDoituong() == 3) {
                    valueGioiTinhs.setText("Cả nam và nữ");
                } else {
                    if (tempSuaPT.getDoituong() == 2) {
                        valueGioiTinhs.setText("Nữ");
                    } else {
                        valueGioiTinhs.setText("Nam");
                    }
                }
                layoutThongTinChiTiet.setVisibility(View.VISIBLE);
                layoutThongTinChiTietEdt.setVisibility(View.GONE);

            }
            suadl = false;
            btnSaveEdit.setVisibility(View.VISIBLE);
            loadSave.setVisibility(View.GONE);

        }
    }

    public class CapNhatTienNghi extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                showToast("Có lỗi xảy ra, thử lại sau...");
            } else {
                lstTienNghi.clear();
                lstTienNghi.addAll(listSuaTienNghi);
                myAdapter.notifyDataSetChanged();
                layoutTienIchEdt.setVisibility(View.GONE);
                layoutTienIch.setVisibility(View.VISIBLE);
            }
            suadl = false;
            btnSaveEditTienNghi.setVisibility(View.VISIBLE);
            loadSaveTienNghi.setVisibility(View.GONE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            suadl = true;
            btnSaveEditTienNghi.setVisibility(View.GONE);
            loadSaveTienNghi.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            String tiennghi = "";
            for (int i = 0; i < 15; i++) {
                if (i == 0) {
                    if (listSuaTienNghi.get(0).isSelected() == true) {
                        tiennghi = "wifi,";
                    }
                } else {
                    if (i == 1) {
                        if (listSuaTienNghi.get(1).isSelected() == true) {
                            tiennghi = tiennghi + "gac,";
                        }
                    } else {
                        if (i == 2) {
                            if (listSuaTienNghi.get(2).isSelected() == true) {
                                tiennghi = tiennghi + "toilet,";
                            }
                        } else {
                            if (i == 3) {
                                if (listSuaTienNghi.get(3).isSelected() == true) {
                                    tiennghi = tiennghi + "phongtam,";
                                }
                            } else {
                                if (i == 4) {
                                    if (listSuaTienNghi.get(4).isSelected() == true) {
                                        tiennghi = tiennghi + "giuong,";
                                    }
                                } else {
                                    if (i == 5) {
                                        if (listSuaTienNghi.get(5).isSelected() == true) {
                                            tiennghi = tiennghi + "tv,";
                                        }
                                    } else {
                                        if (i == 6) {
                                            if (listSuaTienNghi.get(6).isSelected() == true) {
                                                tiennghi = tiennghi + "tulanh,";
                                            }
                                        } else {
                                            if (i == 7) {
                                                if (listSuaTienNghi.get(7).isSelected() == true) {
                                                    tiennghi = tiennghi + "bepga,";
                                                }
                                            } else {
                                                if (i == 8) {
                                                    if (listSuaTienNghi.get(8).isSelected() == true) {
                                                        tiennghi = tiennghi + "quat,";
                                                    }
                                                } else {
                                                    if (i == 9) {
                                                        if (listSuaTienNghi.get(9).isSelected() == true) {
                                                            tiennghi = tiennghi + "tudo,";
                                                        }
                                                    } else {
                                                        if (i == 10) {
                                                            if (listSuaTienNghi.get(10).isSelected() == true) {
                                                                tiennghi = tiennghi + "maylanh,";
                                                            }
                                                        } else {
                                                            if (i == 11) {
                                                                if (listSuaTienNghi.get(11).isSelected() == true) {
                                                                    tiennghi = tiennghi + "den,";
                                                                }
                                                            } else {
                                                                if (i == 12) {
                                                                    if (listSuaTienNghi.get(12).isSelected() == true) {
                                                                        tiennghi = tiennghi + "baove,";
                                                                    }
                                                                } else {
                                                                    if (i == 13) {
                                                                        if (listSuaTienNghi.get(13).isSelected() == true) {
                                                                            tiennghi = tiennghi + "camera,";
                                                                        }
                                                                    } else {
                                                                        if (i == 14) {
                                                                            if (listSuaTienNghi.get(14).isSelected() == true) {
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

            DAL_PhongTro dal_phongTro = new DAL_PhongTro();

            return dal_phongTro.capNhatTienNghi(idItem, tiennghi);
        }
    }

    public class CapNhatMoTaThem extends AsyncTask<Void, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            btnSaveEditMota.setVisibility(View.GONE);
            loadSaveEditMota.setVisibility(View.VISIBLE);
            suadl = true;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.capNhatMoTaThem(idItem, txtbinhluanEdit.getText().toString());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                showToast("Thất bại, vui lòng thử lại sau...");
            } else {
                txtbinhluan.setText(txtbinhluanEdit.getText().toString());
                layoutMoTaThemEdit.setVisibility(View.GONE);
                layoutMoTaThem.setVisibility(View.VISIBLE);
                phongTros.setMotathem(tempSuaPT.getMotathem());
            }

            btnSaveEditMota.setVisibility(View.VISIBLE);
            loadSaveEditMota.setVisibility(View.GONE);
            suadl = false;
        }
    }

    public class CapNhatLienLac extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.capNhatLienLac(idItem, valueHotenEdit.getText().toString(), valueSDTedit.getText().toString(), valuefacebookEdit.getText().toString(),
                    arrTinhTP.get(indexSpnTinh).getId(), arrQuanHuyen.get(indexSpnQH).getId(), valueSoNhaEdit.getText().toString());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                showToast("Cập nhật thất bại, thử lại sau...");
            } else {
                phongTros.setHoten(valueHotenEdit.getText().toString());
                phongTros.setSdt(valueSDTedit.getText().toString());
                phongTros.setFacebook(valuefacebookEdit.getText().toString());
                phongTros.setIdtp(arrTinhTP.get(indexSpnTinh).getId());
                phongTros.setIdqh(arrQuanHuyen.get(indexSpnQH).getId());
                phongTros.setDiachi(valueSoNhaEdit.getText().toString());

                valueHoten.setText(phongTros.getHoten());
                valueSDT.setText(phongTros.getSdt());
                valueDiaChi.setText(phongTros.getDiachi() + ", " + arrQuanHuyen.get(indexSpnQH).getTen() + ", " + arrTinhTP.get(indexSpnTinh).getTen());
                setClickCall_SMS();
                layoutLienHeEdit.setVisibility(View.GONE);
                layoutLienHe.setVisibility(View.VISIBLE);
            }

            suadl = false;
            loadSaveDiaChi.setVisibility(View.GONE);
            btnSaveEditDiaChi.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            suadl = true;
            btnSaveEditDiaChi.setVisibility(View.GONE);
            loadSaveDiaChi.setVisibility(View.VISIBLE);
        }
    }

    public void getUserInfo() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("MyUser", "");

        if (user.equals("") || user == null) {

        } else {
            Gson gsonUser = new Gson();
            users = new User();
            users = gsonUser.fromJson(user, User.class);
        }

    }

    public class Report extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            int idusersss = -1;
            try {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("MyUser", "");

                if (user.equals("") || user == null) {
                    idusersss = -1;
                } else {
                    Gson gsonUser = new Gson();
                    User users1 = new User();
                    users1 = gsonUser.fromJson(user, User.class);
                    idusersss = Integer.parseInt(users1.getId());
                }
            } catch (Exception e) {

            }

            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.Report(phongTros.getId(), strings[0], idusersss);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            valueEdtReport.setText("");
            isreporting = true;
            layoutTexxtReP.setVisibility(View.GONE);
            progressreport.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 1) {
                Toast.makeText(getContext(), "Cảm ơn bạn đã góp ý cho chúng tôi, chúng tôi sẽ xem xét lại thông tin này!", Toast.LENGTH_SHORT).show();
            }
            isreporting = false;
            layoutTexxtReP.setVisibility(View.VISIBLE);
            progressreport.setVisibility(View.GONE);
        }
    }

    public class SetConPhong extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... integers) {

            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            return dal_phongTro.SetConPhong(phongTros.getId(), integers[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isreporting = true;
            layoutTexxtReP.setVisibility(View.GONE);
            progressreport.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 1) {
                if (set1 == 0) {
                    textBCLoi.setText("Báo còn phòng");
                } else {
                    textBCLoi.setText("Báo hết phòng");
                }
            } else {
                Toast.makeText(getContext(), "Có lỗi xảy ra.. Thử lại sau...", Toast.LENGTH_SHORT).show();
            }

            isreporting = false;
            layoutTexxtReP.setVisibility(View.VISIBLE);
            progressreport.setVisibility(View.GONE);
        }
    }
}
