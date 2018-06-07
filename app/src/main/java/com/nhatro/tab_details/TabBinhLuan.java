package com.nhatro.tab_details;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.DAL.BinhLuans;
import com.nhatro.R;
import com.nhatro.XemTinTimPhong;
import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.model.BinhLuan;
import com.nhatro.model.ThongBao;
import com.nhatro.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabBinhLuan extends Fragment {

    View v;
    ArrayList<BinhLuan> data;
    Adapter_List_View_Binh_Luan adapter_list_view_binh_luan;

    ListView listViewBinhLuan;
    ImageView imgSend, btnSendSuaBL;
    View footerView;
    View footerTaiThemBl;
    int trang = 1;
    LinearLayout layoutTaiDL, layoutND;
    ProgressBar progressloadbl, progresSuabl;
    boolean issuaxoapost;
    BottomSheetDialog bottomSheetDialog;
    View sheetView;
    LinearLayout layoutSua, layoutXoa, loadingXoaBL;
    AlertDialog.Builder alertDialogBuilder;

    BottomSheetDialog bottomSheetDialogSua;
    View sheetViewSua;

    int iduser = 2;
    String tieude;
    ThongBao thongBao;
    User users;

    public TabBinhLuan() {
        // Required empty public constructor
    }

    String iditem = "";
    EditText edt, edtSuaBL;
    int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_tab_binh_luan, container, false);
        getUserInfo();
        thongBao = new ThongBao();
        issuaxoapost = false;
        layoutTaiDL = v.findViewById(R.id.layoutTaiDL);
        layoutND = v.findViewById(R.id.layoutND);
        progressloadbl = v.findViewById(R.id.progressloadbl);
        loadingXoaBL = v.findViewById(R.id.loadingXoaBL);
        Bundle bundle = this.getArguments();
        if (bundle != null && v != null) {
            iditem = bundle.getString("id");
            tieude = bundle.getString("tieude");
            edt = (EditText) v.findViewById(R.id.edtNhapBl);

            data = new ArrayList<>();
            listViewBinhLuan = (ListView) v.findViewById(R.id.lstBinhLuan);
            imgSend = (ImageView) v.findViewById(R.id.btnSend);
            adapter_list_view_binh_luan = new Adapter_List_View_Binh_Luan(getContext(), R.layout.layout_item_list_binh_luan, data);
            //adapter_list_view_binh_luan.notifyDataSetChanged();

            listViewBinhLuan.setAdapter(adapter_list_view_binh_luan);

            footerTaiThemBl = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view_binh_luan_tai_them, null, false);
            footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_list_view_binh_luan, null, false);

            listViewBinhLuan.addFooterView(footerTaiThemBl);

            footerTaiThemBl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Tải thêm bl", Toast.LENGTH_SHORT).show();
                    listViewBinhLuan.removeFooterView(footerTaiThemBl);
                    listViewBinhLuan.addFooterView(footerView);
                    trang = trang + 1;
                    XemThemBL xemThemBL = new XemThemBL();
                    xemThemBL.execute(iditem);
                }
            });


            imgSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(),edt.getText().toString(),Toast.LENGTH_SHORT).show();
                    getUserInfo();
                    if (users == null) {
                        Toast.makeText(getContext(), "Đăng nhập trước khi thực hiện thao tác này ..", Toast.LENGTH_SHORT).show();
                    } else {
                        if (edt.getText().toString().equals("")) {
                            Snackbar.make(getView(), "Nhập nội dung", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        } else {
                            if (issuaxoapost) {
                                Toast.makeText(getContext(), "Đang xử lý dữ liệu..vui lòng chờ giây lát...", Toast.LENGTH_SHORT).show();
                            } else {
                                issuaxoapost = true;
                                BinhLuan newbl = new BinhLuan();
                                newbl.setNoiDungBl(edt.getText().toString());
                                newbl.setIdPhong(iditem);
                                newbl.setIdUser(Integer.parseInt(users.getId()));

                                thongBao.setLoai(1);

                                themBinhLuan themBinhLuan = new themBinhLuan();
                                themBinhLuan.execute(newbl);
                            }

                        }
                    }
                }
            });

            LoadBL loadBL = new LoadBL();
            loadBL.execute(iditem);

            bottomSheetDialog = new BottomSheetDialog(getContext());
            sheetView = getLayoutInflater().inflate(R.layout.menu_suaxoa_binhluan, null);
            bottomSheetDialog.setContentView(sheetView);

            alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Xác nhận xóa..!!!");
            alertDialogBuilder.setMessage("Bạn có chắc chắn xóa bình luận này không ???");
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    bottomSheetDialog.dismiss();
                    arg0.cancel();
                    /*XemTinTimPhong.XoaBL xoaBL = new XemTinTimPhong.XoaBL();
                    xoaBL.execute(data.get(indexxoa).getId());*/
                    if (issuaxoapost) {
                        Toast.makeText(getContext(), "Đang xử lý dữ liệu.. Thử lại trong giây lát...", Toast.LENGTH_SHORT).show();
                    } else {
                        XoaBL xoaBL = new XoaBL();
                        xoaBL.execute(data.get(index).getId());
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

            listViewBinhLuan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    getUserInfo();
                    if (users != null) {
                        if (Integer.parseInt(users.getId()) == data.get(position).getIdUser()) {
                            if (!issuaxoapost) {
                                index = position;
                                bottomSheetDialog.show();
                            }
                        }

                    }
                    return false;
                }
            });

            layoutSua = sheetView.findViewById(R.id.layoutSua);
            layoutXoa = sheetView.findViewById(R.id.layoutXoa);

            layoutXoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogBuilder.show();
                }
            });

            layoutSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*bottomSheetDialog.dismiss();
                    bottomSheetDialogSua.show();
                    edtSuaBL.setText(data.get(indexxoa).getNoiDungBl());*/
                }
            });

            bottomSheetDialogSua = new BottomSheetDialog(getContext());
            sheetViewSua = getLayoutInflater().inflate(R.layout.dialog_sua_binhluan, null);
            bottomSheetDialogSua.setContentView(sheetViewSua);
            btnSendSuaBL = sheetViewSua.findViewById(R.id.btnSendSuaBL);
            edtSuaBL = sheetViewSua.findViewById(R.id.edtSuaBL);
            progresSuabl = sheetViewSua.findViewById(R.id.progresSuabl);


            layoutSua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialogSua.show();
                    edtSuaBL.setText(data.get(index).getNoiDungBl());
                }
            });
            btnSendSuaBL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputManager = (InputMethodManager)
                            getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    try {
                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {

                    }


                    if (edtSuaBL.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "Nhập nội dung sửa bình luận ...", Toast.LENGTH_SHORT).show();
                    } else {
                        if (issuaxoapost) {
                            Toast.makeText(getContext(), "Đang xử lý dữ liệu, vui lòng chờ...", Toast.LENGTH_SHORT).show();
                        } else {
                            BinhLuan temp = new BinhLuan();
                            temp.setId(data.get(index).getId());
                            temp.setNoiDungBl(edtSuaBL.getText().toString());

                            SuaBL suaBL = new SuaBL();
                            suaBL.execute(temp);
                        }
                    }
                }
            });
        }
        return v;
    }

    public void getUserInfo() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("MyUser", "");
        if (user.equals("") || user == null) {
            users = null;
        } else {
            users = new User();
            Gson gsonUser = new Gson();
            users = gsonUser.fromJson(user, User.class);
        }
    }

    public class XemThemBL extends AsyncTask<String, Void, ArrayList<BinhLuan>> {

        @Override
        protected ArrayList<BinhLuan> doInBackground(String... strings) {
            BinhLuans binhLuans = new BinhLuans();
            ArrayList<BinhLuan> temp = new ArrayList<>();
            temp = binhLuans.danhSachBL(strings[0], trang);
            return temp;
        }

        @Override
        protected void onPostExecute(ArrayList<BinhLuan> binhLuans) {
            super.onPostExecute(binhLuans);

            if (binhLuans != null) {
                data.addAll(binhLuans);
                //data.add(new BinhLuan(1, "https://www.google.com.vn/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png", "45", "Ten", "Dfgdfgdfg", "sdf"));
                /*adapter_list_view_binh_luan = new Adapter_List_View_Binh_Luan(getContext(), R.layout.layout_item_list_binh_luan, data);
                listViewBinhLuan.setAdapter(adapter_list_view_binh_luan);*/
                adapter_list_view_binh_luan.notifyDataSetChanged();
            }
            listViewBinhLuan.removeFooterView(footerView);
            listViewBinhLuan.addFooterView(footerTaiThemBl);
        }
    }

    public class LoadBL extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            BinhLuans binhLuans = new BinhLuans();
            ArrayList<BinhLuan> temp = new ArrayList<>();
            temp = binhLuans.danhSachBL(strings[0], 1);
            if (temp != null) {
                data.addAll(temp);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            /*adapter_list_view_binh_luan = new Adapter_List_View_Binh_Luan(getContext(), R.layout.layout_item_list_binh_luan, data);
            listViewBinhLuan.setAdapter(adapter_list_view_binh_luan);*/
            adapter_list_view_binh_luan.notifyDataSetChanged();
            layoutTaiDL.setVisibility(View.GONE);
            layoutND.setVisibility(View.VISIBLE);
        }
    }

    public class themBinhLuan extends AsyncTask<BinhLuan, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            issuaxoapost = true;
            progressloadbl.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(BinhLuan... binhLuans) {
            BinhLuans bl = new BinhLuans();
            String rs = bl.themBL(binhLuans[0], 1);


            return rs;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != "-1") {
                ;
                Date date = new Date();
                DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                BinhLuan binhLuan = new BinhLuan(s, users.getAvatar(), users.getUsername(), users.getHoten(), edt.getText().toString(), dateFormat2.format(date).toString());
                binhLuan.setIdUser(Integer.parseInt(users.getId()));
                data.add(0, binhLuan);
                adapter_list_view_binh_luan.notifyDataSetChanged();
                edt.setText("");
            } else {
                Toast.makeText(getContext(), "Thêm thất bại, thử lại sau ...", Toast.LENGTH_SHORT);
            }

            progressloadbl.setVisibility(View.GONE);
            issuaxoapost = false;
        }
    }

    public class XoaBL extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            issuaxoapost = true;
            loadingXoaBL.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(String... strings) {
            BinhLuans binhLuans = new BinhLuans();

            return binhLuans.xoaBinhLuan(strings[0], 0);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                Toast.makeText(getContext(), "Xóa thất bại, thử lại sau ...", Toast.LENGTH_SHORT).show();
            } else {
                data.remove(index);
                adapter_list_view_binh_luan.notifyDataSetChanged();
            }
            loadingXoaBL.setVisibility(View.GONE);
            issuaxoapost = false;
        }
    }

    public class SuaBL extends AsyncTask<BinhLuan, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progresSuabl.setVisibility(View.VISIBLE);
            bottomSheetDialogSua.setCancelable(false);
            issuaxoapost = true;
        }

        @Override
        protected Integer doInBackground(BinhLuan... binhLuans) {
            BinhLuans binhLuansss = new BinhLuans();
            return binhLuansss.suaBL(binhLuans[0].getId(), 0, binhLuans[0].getNoiDungBl());
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {
                Toast.makeText(getContext(), "Thất bại, thử lại sau ...", Toast.LENGTH_SHORT).show();
            } else {
                data.get(index).setNoiDungBl(edtSuaBL.getText().toString());
                adapter_list_view_binh_luan.notifyDataSetChanged();
                bottomSheetDialogSua.cancel();
            }

            issuaxoapost = false;
            bottomSheetDialogSua.setCancelable(true);
            progresSuabl.setVisibility(View.GONE);
        }
    }

}
