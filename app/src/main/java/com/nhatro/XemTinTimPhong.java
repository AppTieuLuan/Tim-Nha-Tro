package com.nhatro;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.DAL.BinhLuans;
import com.nhatro.DAL.DAL_TinTimPhong;
import com.nhatro.DAL.HinhAnhs;
import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.model.BinhLuan;
import com.nhatro.model.Item_Grid_Facilities;
import com.nhatro.model.ThongBao;
import com.nhatro.model.TinTimPhong;
import com.nhatro.model.User;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class XemTinTimPhong extends AppCompatActivity {


    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstTienNghi;
    Grid_Facilities_Adapter myAdapter;

    ArrayList<BinhLuan> data;
    Adapter_List_View_Binh_Luan adapter_list_view_binh_luan;


    String idItem;
    int trang;
    CircleImageView profile_image;
    TextView txtTen, txtSoDT, txtTieuDe, valueLoaiNhaO, valueSoNguoi, valueGiaMonMuon,
            valueQH, valueKhuVuc, valueGioGiac, txtbinhluan, valueGioiTinh, txtxemthem;
    ImageView btnCall, btnGuiSMS, btnFaceBook, btnMessenger, btnEdit;
    EditText edtNhapBl, edtSuaBL;
    ConstraintLayout layoutND;
    LinearLayout layoutLoad;
    ImageView btnSend, btnSendSuaBL;
    ExpandableHeightGridView lstBinhLuan;
    boolean loadnew;
    ProgressBar progressloadbl, progressloadthembl, progresSuabl;
    boolean isloading; // Đang load dl mới hay ko
    boolean isloadings; // Đang load dl hay ko
    boolean isnext; // Còn dữ liệu hay ko
    boolean isXoaBL; // có đang xóa bl hay ko
    int iduser;
    int indexxoa; // index chọn bình luận để xóa
    BottomSheetDialog bottomSheetDialog;
    View sheetView;

    LinearLayout layoutSua, layoutXoa, layoutXoaBL;
    AlertDialog.Builder alertDialogBuilder;

    BottomSheetDialog bottomSheetDialogSua;
    View sheetViewSua;

    TinTimPhong dlTin;
    ThongBao thongBao;
    User users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_tin_tim_phong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dlTin = new TinTimPhong();
        getUserInfo();
        findView();
        getData();

        thongBao = new ThongBao();
        lstTienNghi = new ArrayList<>();

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
        myAdapter = new Grid_Facilities_Adapter(getApplicationContext(), R.layout.grid_facilities_items, lstTienNghi);
        gridTienNghi.setAdapter(myAdapter);
        getInformation getInformation = new getInformation();
        getInformation.execute(idItem);

        getBinhLuans getBinhLuans = new getBinhLuans();
        getBinhLuans.execute();

        setEvent();

    }

    public void setHidden() {
        btnEdit.setVisibility(View.GONE);
    }

    public void findView() {
        btnEdit = findViewById(R.id.btnEdit);
        layoutND = findViewById(R.id.layoutND);
        layoutLoad = findViewById(R.id.layoutLoad);
        isXoaBL = false;
        isloading = false;
        isloadings = false;
        profile_image = findViewById(R.id.profile_image);
        txtTen = findViewById(R.id.txtTen);
        txtSoDT = findViewById(R.id.txtSoDT);
        txtTieuDe = findViewById(R.id.txtTieuDe);
        valueLoaiNhaO = findViewById(R.id.valueLoaiNhaO);
        valueSoNguoi = findViewById(R.id.valueSoNguoi);
        valueGiaMonMuon = findViewById(R.id.valueGiaMonMuon);
        valueQH = findViewById(R.id.valueQH);
        valueKhuVuc = findViewById(R.id.valueKhuVuc);
        valueGioGiac = findViewById(R.id.valueGioGiac);
        txtbinhluan = findViewById(R.id.txtbinhluan);
        btnCall = findViewById(R.id.btnCall);
        btnGuiSMS = findViewById(R.id.btnGuiSMS);
        btnFaceBook = findViewById(R.id.btnFaceBook);
        btnMessenger = findViewById(R.id.btnMessenger);
        valueGioiTinh = findViewById(R.id.valueGioiTinh);
        gridTienNghi = findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);
        txtxemthem = findViewById(R.id.txtxemthem);
        progressloadbl = findViewById(R.id.progressloadbl);
        edtNhapBl = findViewById(R.id.edtNhapBl);
        btnSend = findViewById(R.id.btnSend);
        lstBinhLuan = findViewById(R.id.lstBinhLuan);
        lstBinhLuan.setExpanded(true);
        progressloadthembl = findViewById(R.id.progressloadthembl);
        data = new ArrayList<>();
        trang = 1;
        adapter_list_view_binh_luan = new Adapter_List_View_Binh_Luan(getApplicationContext(), R.layout.layout_item_list_binh_luan, data);
        lstBinhLuan.setAdapter(adapter_list_view_binh_luan);
        loadnew = true;
        isnext = true;

        bottomSheetDialog = new BottomSheetDialog(this);
        sheetView = getLayoutInflater().inflate(R.layout.menu_suaxoa_binhluan, null);
        bottomSheetDialog.setContentView(sheetView);
        iduser = 3;

        layoutSua = sheetView.findViewById(R.id.layoutSua);
        layoutXoa = sheetView.findViewById(R.id.layoutXoa);

        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Xác nhận xóa..!!!");
        alertDialogBuilder.setMessage("Bạn có chắc chắn xóa bình luận này không ???");
        alertDialogBuilder.setCancelable(true);

        layoutXoaBL = findViewById(R.id.layoutXoaBL);

        bottomSheetDialogSua = new BottomSheetDialog(this);
        sheetViewSua = getLayoutInflater().inflate(R.layout.dialog_sua_binhluan, null);
        bottomSheetDialogSua.setContentView(sheetViewSua);
        btnSendSuaBL = sheetViewSua.findViewById(R.id.btnSendSuaBL);
        edtSuaBL = sheetViewSua.findViewById(R.id.edtSuaBL);
        progresSuabl = sheetViewSua.findViewById(R.id.progresSuabl);

        //bottomSheetDialogSua.set
    }

    public void getData() {
        Intent callerIntent = getIntent();
        Bundle bundle = callerIntent.getBundleExtra("data");
        idItem = bundle.getString("iditem");
        getSupportActionBar().setTitle(bundle.getString("tieude"));
        txtTieuDe.setText(bundle.getString("tieude"));
    }

    public void setEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo();
                if (users == null) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập trước khi thực hiện thao tác này ...", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isloading && !isXoaBL) {
                        if (edtNhapBl.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Nhập bình luận...", Toast.LENGTH_SHORT).show();
                        } else {
                            BinhLuan binhLuan = new BinhLuan();
                            Date date = new Date();
                            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                            binhLuan.setIdUser(Integer.parseInt(users.getId()));
                            binhLuan.setNoiDungBl(edtNhapBl.getText().toString());
                            binhLuan.setIdPhong(idItem);

                            thongBao.setIdusernhan(dlTin.getIduser());
                            thongBao.setIdtintuc(idItem);
                            thongBao.setIduser2(Integer.parseInt(users.getId()));
                            thongBao.setTieudeTin(dlTin.getTieude());
                            thongBao.setLoai(2);

                            ThemBinhLuan themBinhLuan = new ThemBinhLuan();
                            themBinhLuan.execute(binhLuan);

                        }
                    }
                }

            }
        });

        txtxemthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isloadings && isnext) {
                    TaiThemBinhLuan getBinhLuans = new TaiThemBinhLuan();
                    getBinhLuans.execute();
                }

            }
        });

        lstBinhLuan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                getUserInfo();
                if (users == null) {
                    Toast.makeText(getApplicationContext(), "Đăng nhập trước khi thực hiện thao tác này ...", Toast.LENGTH_SHORT).show();
                } else {
                    if (data.get(position).getIdUser() == Integer.parseInt(users.getId())) {
                        indexxoa = position;
                        bottomSheetDialog.show();
                    }
                }

                return false;
            }
        });

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                bottomSheetDialog.dismiss();
                arg0.cancel();
                XoaBL xoaBL = new XoaBL();
                xoaBL.execute(data.get(indexxoa).getId());
            }
        });
        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });

        alertDialogBuilder.create();
        layoutXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.show();
            }
        });

        layoutSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                bottomSheetDialogSua.show();
                edtSuaBL.setText(data.get(indexxoa).getNoiDungBl());
            }
        });
        btnSendSuaBL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {

                }

                if (edtSuaBL.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Sửa bình luận ...", Toast.LENGTH_SHORT).show();
                } else {
                    if (isXoaBL) {
                        Toast.makeText(getApplicationContext(), "Đang xử lý dữ liệu, vui lòng chờ...", Toast.LENGTH_SHORT).show();
                    } else {
                        BinhLuan temp = new BinhLuan();
                        temp.setId(data.get(indexxoa).getId());
                        temp.setNoiDungBl(edtSuaBL.getText().toString());

                        SuaBL suaBL = new SuaBL();
                        suaBL.execute(temp);
                    }
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("key", 2);
                bundle.putString("iditem", idItem);
                bundle.putSerializable("tintimphong", dlTin);
                Intent intents = new Intent(getApplicationContext(), ThemTinTimPhong.class);
                intents.putExtra("data", bundle);
                startActivityForResult(intents, 90);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public class getInformation extends AsyncTask<String, Void, TinTimPhong> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutLoad.setVisibility(View.VISIBLE);
            layoutND.setVisibility(View.GONE);
        }

        @Override
        protected TinTimPhong doInBackground(String... strings) {
            TinTimPhong tinTimPhong = new TinTimPhong();
            DAL_TinTimPhong dal_tinTimPhong = new DAL_TinTimPhong();
            tinTimPhong = dal_tinTimPhong.chiTietTin(strings[0]);
            return tinTimPhong;
        }

        @Override
        protected void onPostExecute(TinTimPhong tinTimPhong) {
            super.onPostExecute(tinTimPhong);

            if (tinTimPhong == null) {
                Toast.makeText(getApplicationContext(), "Có lỗi khi lấy dữ liệu. Thử lại sau ..", Toast.LENGTH_SHORT).show();
                layoutLoad.setVisibility(View.GONE);
            } else {
                dlTin = tinTimPhong;

                if (users == null) {
                    setHidden();
                } else {
                    if (Integer.parseInt(users.getId()) != dlTin.getIduser()) {
                        setHidden();
                    }
                }

                try {
                    Picasso.with(getApplicationContext()).load(tinTimPhong.getAvatar()).into(profile_image);
                } catch (Exception e) {

                }
                txtTen.setText(tinTimPhong.getHoten());
                txtSoDT.setText(tinTimPhong.getSdt());

                setView(tinTimPhong);

                setClickCall_SMS(tinTimPhong.getSdt(), tinTimPhong.getFacebook());
                layoutLoad.setVisibility(View.GONE);
                layoutND.setVisibility(View.VISIBLE);
            }

        }
    }

    public void setView(TinTimPhong tinTimPhong) {

        txtTieuDe.setText(tinTimPhong.getTieude());
        if (tinTimPhong.getLoaitin() == 1) {
            valueLoaiNhaO.setText("Tìm nhà trọ.");
        } else {
            if (tinTimPhong.getLoaitin() == 2) {
                valueLoaiNhaO.setText("Tìm ở ghép.");
            } else {
                if (tinTimPhong.getLoaitin() == 3) {
                    valueLoaiNhaO.setText("Tìm nhà nguyên căn.");
                }
            }
        }
        valueSoNguoi.setText(tinTimPhong.getSonguoimin() + " - " + tinTimPhong.getSonguoimax() + " người.");
        if (tinTimPhong.getGiamin() == tinTimPhong.getGiamax()) {
            valueGiaMonMuon.setText(tinTimPhong.getGiamin() + " vnđ");
        } else {
            DecimalFormat formatter = new DecimalFormat("###,###,###");
            String tmp = formatter.format(tinTimPhong.getGiamin());
            String tmp2 = formatter.format(tinTimPhong.getGiamax());
            valueGiaMonMuon.setText("Từ " + String.valueOf(tmp) + " - " + tmp2 + " vnđ");
        }

        valueQH.setText(tinTimPhong.getQh() + " / " + tinTimPhong.getTentp());
        if (tinTimPhong.getKhuvuc().equals("")) {
            valueKhuVuc.setText("Không yêu cầu.");
        } else {
            valueKhuVuc.setText(tinTimPhong.getKhuvuc());
        }

        if (tinTimPhong.getGiogiac() == 1) {
            valueGioGiac.setText("Yêu cầu giờ giấc tự do.");
        } else {
            valueGioGiac.setText("Không yêu cầu.");
        }
        if (tinTimPhong.getMotathem().equals("")) {
            txtbinhluan.setText("Không có mô tả thêm");
        } else {
            txtbinhluan.setText(tinTimPhong.getMotathem());
        }

        if (tinTimPhong.getGioitinh() == 3) {
            valueGioiTinh.setText("Không yêu cầu.");
        } else {
            if (tinTimPhong.getGioitinh() == 1) {
                valueGioiTinh.setText("Nam");
            } else {
                if (tinTimPhong.getGioitinh() == 2) {
                    valueGioiTinh.setText("Nữ");
                }
            }
        }
        for (int i = 0; i < lstTienNghi.size(); i++) {
            lstTienNghi.get(i).setSelected(false);
        }
        String[] array = tinTimPhong.getTiennghi().split(",", -1);
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
    }

    public void setClickCall_SMS(String sdt, String facebook) {
        if (!sdt.equals("")) {
            btnGuiSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", sdt);
                    smsIntent.putExtra("sms_body", "");
                    startActivity(smsIntent);
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + sdt));
                    startActivity(intent);
                }
            });
        }

        if (!facebook.equals("")) {
            btnFaceBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebook));
                        startActivity(intent);
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/" + facebook)));
                    }
                }
            });

            btnMessenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + facebook));
                        startActivity(i);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "Oups!Can't open Facebook messenger right now. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public class getBinhLuans extends AsyncTask<Void, Void, ArrayList<BinhLuan>> {
        @Override
        protected void onPostExecute(ArrayList<BinhLuan> binhLuans) {
            super.onPostExecute(binhLuans);
            if (loadnew) {
                if (binhLuans.size() > 0) {
                    trang = trang + 1;
                }
                data.clear();
                data.addAll(binhLuans);
                adapter_list_view_binh_luan.notifyDataSetChanged();
                loadnew = false;
            } else {
                if (binhLuans.size() > 0) {
                    trang = trang + 1;
                }
                data.addAll(binhLuans);
                adapter_list_view_binh_luan.notifyDataSetChanged();
            }
            isloadings = false;
            progressloadbl.setVisibility(View.GONE);
        }

        @Override
        protected ArrayList<BinhLuan> doInBackground(Void... voids) {
            ArrayList<BinhLuan> binhLuans = new ArrayList<>();
            BinhLuans binhLuans1 = new BinhLuans();
            binhLuans = binhLuans1.danhSachBLTinTimPhong(idItem, trang);
            return binhLuans;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressloadbl.setVisibility(View.VISIBLE);
            isloadings = true;
        }
    }

    public class ThemBinhLuan extends AsyncTask<BinhLuan, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressloadbl.setVisibility(View.VISIBLE);
            isloading = true;
        }

        @Override
        protected String doInBackground(BinhLuan... binhLuans) {
            BinhLuans binhLuans1 = new BinhLuans();
            String res = binhLuans1.themBLTinTimPhong(binhLuans[0], thongBao);

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("-1")) {
                Toast.makeText(getApplicationContext(), "Có lỗi xảy ra, thử lại sau ...", Toast.LENGTH_SHORT).show();
                progressloadbl.setVisibility(View.INVISIBLE);
            } else {
                BinhLuan binhLuan = new BinhLuan();
                Date date = new Date();
                DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                binhLuan.setId(s);
                binhLuan.setIdUser(Integer.parseInt(users.getId()));
                binhLuan.setNoiDungBl(edtNhapBl.getText().toString());
                binhLuan.setNgayViet(dateFormat2.format(date).toString());
                binhLuan.setIdPhong(idItem);
                binhLuan.setAvatarUser(users.getAvatar());
                binhLuan.setUserName(users.getUsername());
                binhLuan.setTenUser(users.getHoten());

                data.add(0, binhLuan);
                adapter_list_view_binh_luan.notifyDataSetChanged();

                edtNhapBl.setText("");

                progressloadbl.setVisibility(View.INVISIBLE);
            }

            isloading = false;
        }
    }

    public class TaiThemBinhLuan extends AsyncTask<Void, Void, ArrayList<BinhLuan>> {

        @Override
        protected void onPostExecute(ArrayList<BinhLuan> binhLuans) {
            super.onPostExecute(binhLuans);
            isloadings = false;
            if (binhLuans.size() > 0) {
                trang = trang + 1;
                data.addAll(binhLuans);
                adapter_list_view_binh_luan.notifyDataSetChanged();
            } else {

            }

            progressloadthembl.setVisibility(View.INVISIBLE);
            txtxemthem.setVisibility(View.VISIBLE);

        }

        @Override
        protected ArrayList<BinhLuan> doInBackground(Void... voids) {
            ArrayList<BinhLuan> binhLuans = new ArrayList<>();
            BinhLuans binhLuans1 = new BinhLuans();
            binhLuans = binhLuans1.danhSachBLTinTimPhong(idItem, trang);
            return binhLuans;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtxemthem.setVisibility(View.GONE);
            progressloadthembl.setVisibility(View.VISIBLE);
            isloadings = true;
        }
    }

    public class XoaBL extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isXoaBL = true;
            layoutXoaBL.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            BinhLuans binhLuans = new BinhLuans();

            return binhLuans.xoaBinhLuan(strings[0], 1);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                Toast.makeText(getApplicationContext(), "Xóa thất bại, thử lại sau ...", Toast.LENGTH_SHORT).show();
            } else {
                data.remove(indexxoa);
                adapter_list_view_binh_luan.notifyDataSetChanged();
            }
            layoutXoaBL.setVisibility(View.GONE);
            isXoaBL = false;
        }
    }

    public class SuaBL extends AsyncTask<BinhLuan, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresSuabl.setVisibility(View.VISIBLE);
            bottomSheetDialogSua.setCancelable(false);
            isXoaBL = true;
        }

        @Override
        protected Integer doInBackground(BinhLuan... binhLuans) {
            BinhLuans binhLuansss = new BinhLuans();
            return binhLuansss.suaBL(binhLuans[0].getId(), 1, binhLuans[0].getNoiDungBl());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                Toast.makeText(getApplicationContext(), "Thất bại, thử lại sau ...", Toast.LENGTH_SHORT).show();
            } else {
                data.get(indexxoa).setNoiDungBl(edtSuaBL.getText().toString());
                adapter_list_view_binh_luan.notifyDataSetChanged();
                bottomSheetDialogSua.cancel();
            }

            isXoaBL = false;
            bottomSheetDialogSua.setCancelable(true);
            progresSuabl.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 75) {
                Bundle bundles = data.getBundleExtra("data");
                //tenTP = bundle.getString("tenTP");
                dlTin = (TinTimPhong) bundles.getSerializable("data");
                setView(dlTin);
            }
        }
    }

    public void getUserInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("MyUser", "");
        if (user.equals("") || user == null) {

        } else {
            users = new User();
            Gson gsonUser = new Gson();
            users = gsonUser.fromJson(user, User.class);
        }
    }
}
