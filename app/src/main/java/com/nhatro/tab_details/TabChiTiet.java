package com.nhatro.tab_details;


import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.R;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.adapter.MyCustomPagerAdapter;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.PhongTros;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabChiTiet extends Fragment {

    String idItem;
    TextView valueDatCoc, valueLoaiNhaO, valueDienTich, valueGiaThue, valueTienDien, valueTienNuoc, valueGioGiac,
            valueHoten, valueSDT, valueDiaChi, txtbinhluan;

    ImageView btnSMS, btnCall, btnFacebook, btnMessenger;

    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstTienNghi;
    Grid_Facilities_Adapter myAdapter;
    LinearLayout layoutReport;
    ConstraintLayout loadlayoutThongTinChiTiet;
    PhongTros phongTros;
    ScrollView scrollChiTiet;
    View v;

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

        lstTienNghi = new ArrayList<>();
        gridTienNghi = v.findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);


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
                    Toast.makeText(getContext(), "REPORT", Toast.LENGTH_SHORT).show();
                }
            });

            phongTros = new PhongTros();
            LoadData loadData = new LoadData();
            loadData.execute(idItem);


        }
        //}

        return v;
    }

    public void findView(View v) {
        valueLoaiNhaO = v.findViewById(R.id.valueLoaiNhaO);
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
    }

    public class LoadData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... strings) {
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            phongTros = dal_phongTro.thongTinPhong(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (phongTros != null) {

                if (phongTros.getLoaitin() == 1) {
                    valueLoaiNhaO.setText("Cho thuên phòng trọ");
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
                valueGiaThue.setText(phongTros.getGia() + "vnđ/tháng");
                if (phongTros.getTiencoc() == 0) {
                    valueDatCoc.setText("Không cần");
                } else {
                    valueDatCoc.setText(phongTros.getTiencoc() + " " + phongTros.getDonvicoc());
                }
                valueTienDien.setText(phongTros.getGiadien() + " " + phongTros.getDonvidien());
                valueTienNuoc.setText(phongTros.getGianuoc() + " " + phongTros.getDonvinuoc());
                if (phongTros.getGiogiac().equals("-1")) {
                    valueGioGiac.setText("Tự do");
                } else {
                    valueGioGiac.setText("Đóng cửa từ " + phongTros.getGiogiac());
                }

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
            }
        }
    }

    public void setClickFacebook(String id) {
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + id));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + id)));
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
}
