package com.nhatro;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.DAL.ThongBaos;
import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.adapter.ListThongBao_Adapter;
import com.nhatro.adapter.OnHideNotify;
import com.nhatro.model.BinhLuan;
import com.nhatro.model.ThongBao;
import com.nhatro.model.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyFragment extends Fragment {

    View v;
    LinearLayout layout1;
    ListView listView;
    ArrayList<ThongBao> data;
    int trang; // Trang
    boolean isloading; // Đang load dữ liệu hay ko
    boolean isnext; // Còn dữ liệu hay ko

    ConstraintLayout contain;
    ListThongBao_Adapter listThongBao_adapter;
    BottomSheetDialog bottomSheetDialog;
    View sheetView;
    int index;
    User users;
    LinearLayout layoutLoadNew;
    ViewGroup footerTaiThem, footerLoadMore;
    boolean changeduser; // ĐÃ thay đổi user hay chưa
    int olduser;

    public NotifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_notify, container, false);
        LayoutInflater inflaters = getLayoutInflater();

        findView(v);
        footerTaiThem = (ViewGroup) inflater.inflate(R.layout.footer_list_view_binh_luan_tai_them, listView, false);
        footerLoadMore = (ViewGroup) inflater.inflate(R.layout.footer_load_more_bl, listView, false);
        listView.addFooterView(footerTaiThem);
        getUserInfo();

        setUp();
        if (users != null) {
            olduser = Integer.parseInt(users.getId());
            GetNews getThongBao = new GetNews();
            getThongBao.execute();
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

    public void findView(View v) {
        layout1 = v.findViewById(R.id.layout1);
        listView = v.findViewById(R.id.listView);
        contain = v.findViewById(R.id.contain);
        layoutLoadNew = v.findViewById(R.id.layoutLoadNew);


    }

    public void setUp() {
        changeduser = false;
        trang = 1;
        isloading = false;
        isnext = true;
        data = new ArrayList<>();
        bottomDiaLogSetup();
        listThongBao_adapter = new ListThongBao_Adapter(getActivity(), data, new OnHideNotify() {
            @Override
            public void onHide(int position) {
                index = position;
                bottomSheetDialog.show();
            }
        });
        listView.setAdapter(listThongBao_adapter);

        if (users == null) {
            layout1.setVisibility(View.VISIBLE);
            contain.setVisibility(View.GONE);
        } else {

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (data.get(position).getLoai() == 1) {
                    Intent intent = new Intent(getActivity(), Details.class);

                    Bundle bundle = new Bundle();
                    String idItem = data.get(position).getIdtintuc();
                    bundle.putString("iditem", idItem);
                    bundle.putString("tieude", data.get(position).getTieudeTin());
                    intent.putExtra("iditem", bundle);
                    getActivity().startActivity(intent);

                } else {
                    if (data.get(position).getLoai() == 2) {
                        Intent intent = new Intent(getActivity(), XemTinTimPhong.class);

                        Bundle bundle = new Bundle();
                        String idItem = data.get(position).getIdtintuc();
                        bundle.putString("iditem", idItem);
                        bundle.putString("tieude", data.get(position).getTieudeTin());
                        intent.putExtra("data", bundle);
                        getActivity().startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), Details.class);

                        Bundle bundle = new Bundle();
                        String idItem = data.get(position).getIdtintuc();
                        bundle.putString("iditem", idItem);
                        bundle.putString("tieude", data.get(position).getTieudeTin());
                        intent.putExtra("iditem", bundle);
                        getActivity().startActivity(intent);
                    }
                }
            }
        });

        footerTaiThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getThongBao getThongBao = new getThongBao();
                getThongBao.execute();
            }
        });

    }

    public void bottomDiaLogSetup() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        sheetView = getLayoutInflater().inflate(R.layout.menu_suaxoa_binhluan, null);
        bottomSheetDialog.setContentView(sheetView);
    }

    public void check() {
        layout1.setVisibility(View.VISIBLE);
        contain.setVisibility(View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*if (!hidden) {
            check();
        }*/
        if (!hidden) {
            /*SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            String user = sharedPreferences.getString("MyUser", "");*/

            getUserInfo();

            if (users == null) {
                changeduser = true;
                layout1.setVisibility(View.VISIBLE);
                contain.setVisibility(View.GONE);
            } else {
                if (olduser != Integer.parseInt(users.getId())) {
                    trang = 1;
                    olduser = Integer.parseInt(users.getId());
                    GetNews getNews = new GetNews();
                    getNews.execute();
                } else {
                    layout1.setVisibility(View.GONE);
                    contain.setVisibility(View.VISIBLE);
                }


            }
        }

    }

    public class GetNews extends AsyncTask<Void, Void, ArrayList<ThongBao>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout1.setVisibility(View.GONE);
            contain.setVisibility(View.GONE);
            layoutLoadNew.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<ThongBao> doInBackground(Void... voids) {
            ThongBaos thongBaos = new ThongBaos();
            ArrayList<ThongBao> thongBaos1 = new ArrayList<>();
            thongBaos1 = thongBaos.dsThongBao(Integer.parseInt(users.getId()), trang);
            return thongBaos1;
        }

        @Override
        protected void onPostExecute(ArrayList<ThongBao> thongBaos) {
            super.onPostExecute(thongBaos);
            data.clear();
            data.addAll(thongBaos);
            listThongBao_adapter.notifyDataSetChanged();

            contain.setVisibility(View.VISIBLE);
            layoutLoadNew.setVisibility(View.GONE);
            if (thongBaos.size() == 0) {
                isnext = false;
            } else {
                trang = trang + 1;
            }

            changeduser = false;
        }
    }

    public class getThongBao extends AsyncTask<Void, Void, ArrayList<ThongBao>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView.removeFooterView(footerTaiThem);
            listView.addFooterView(footerLoadMore);
        }

        @Override
        protected ArrayList<ThongBao> doInBackground(Void... voids) {
            ThongBaos thongBaos = new ThongBaos();
            ArrayList<ThongBao> thongBaos1 = new ArrayList<>();
            thongBaos1 = thongBaos.dsThongBao(Integer.parseInt(users.getId()), trang);
            return thongBaos1;
        }

        @Override
        protected void onPostExecute(ArrayList<ThongBao> thongBaos) {
            super.onPostExecute(thongBaos);
            data.addAll(thongBaos);
            if (thongBaos.size() == 0) {
                isnext = false;
            } else {
                trang = trang + 1;
            }

            listView.removeFooterView(footerLoadMore);
            listView.addFooterView(footerTaiThem);
        }
    }
}
