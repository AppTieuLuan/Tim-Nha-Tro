package com.nhatro;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.nhatro.DAL.DAL_TinTimPhong;
import com.nhatro.adapter.List_Tin_Tim_Phong_Adapter;
import com.nhatro.model.LocDLTinTimPhong;
import com.nhatro.model.TinTimPhongItemList;
import com.nhatro.model.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class tim_o_ghep extends Fragment {

    private ListView listTinTimPhong;
    private List_Tin_Tim_Phong_Adapter list_tin_tim_phong_adapter;
    LinearLayout btnBoLoc, layoutOverlay2;
    ArrayList<TinTimPhongItemList> data;
    boolean dangLoadDL = false;
    boolean conDL = true;
    boolean openmenu = false;

    ProgressBar loadingData;
    //FloatingActionButton iconAdd;

    int trang;
    boolean first = false; // đánh dấu đã load xong lần đầu hay chưa để load tiếp
    LocDLTinTimPhong locDLTinTimPhong;

    /// Lưu các bộ lọc
    private boolean[] chontiennghi = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private int giogiac = 0;
    int namnu = 3;
    int gia = 0;
    boolean tinnhatro = false;
    boolean tinoghep = false;
    boolean tinnhanguyencan = false;
    int idtp = 50;
    private String tenTP = "TP.HCM";
    int idqh = -1;

    ///////////////

    com.github.clans.fab.FloatingActionButton iconAdd;
    LinearLayout layoutOverlay;
    User users;

    public tim_o_ghep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v;
        v = inflater.inflate(R.layout.fragment_tim_o_ghep, container, false);

        getUserInfo();

        locDLTinTimPhong = new LocDLTinTimPhong();
        locDLTinTimPhong.setLoaitin("(1,2,3)");
        layoutOverlay = v.findViewById(R.id.layoutOverlay);
        iconAdd = v.findViewById(R.id.iconAdd);
        btnBoLoc = v.findViewById(R.id.btnBoLoc);
        layoutOverlay2 = v.findViewById(R.id.layoutOverlay2);

        //iconAdd = v.findViewById(R.id.iconAdd);
        loadingData = v.findViewById(R.id.loadingData);

        data = new ArrayList<>();
        listTinTimPhong = v.findViewById(R.id.listTinTimPhong);

        list_tin_tim_phong_adapter = new List_Tin_Tim_Phong_Adapter(getContext(), R.layout.item_list_tin_tim_phong, data);

        listTinTimPhong.setAdapter(list_tin_tim_phong_adapter);
        listTinTimPhong.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getLastVisiblePosition() == i2 - 1 && !dangLoadDL && conDL && first) {
                    //data.add(new PhongTro(9,"1111","Số 1 Võ Văn Ngân, Quận Thủ Đức, TPHCM",1200000,30,10,3,"Nam"));
                    //adapter.notifyDataSetChanged();
                    dangLoadDL = true;
                    LoadMoreData loadMoreData = new LoadMoreData();
                    loadMoreData.execute(locDLTinTimPhong);
                    //lstDanhSach.addFooterView(footerview);
                }
            }
        });

        listTinTimPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!openmenu) {
                    Intent intent = new Intent(getActivity(), XemTinTimPhong.class);

                    Bundle bundle = new Bundle();
                    String idItem = data.get(position).getId();
                    bundle.putString("iditem", idItem);
                    bundle.putString("tieude", data.get(position).getTieude());
                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            }
        });

        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("MyUser", "");
/*

                Log.d("USERSSS", user);
                Gson gsonUser = new Gson();
                User user1 = gsonUser.fromJson(user, User.class);
*/

                if (user == null || user.equals("")) {
                    Toast.makeText(getContext(), "Đăng nhập trước khi thực hiện thao tác này !!", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("key", 1);
                    Intent intent = new Intent(getContext(), ThemTinTimPhong.class);
                    intent.putExtra("data", bundle);
                    startActivityForResult(intent, 777);
                }


            }
        });
        btnBoLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt("idtp", idtp);
                bundle.putString("tentp", tenTP);
                bundle.putInt("idqh", idqh);
                bundle.putInt("gia", gia);
                bundle.putBoolean("tinnhatro", tinnhatro);
                bundle.putBoolean("tinoghep", tinoghep);
                bundle.putBoolean("tinnhanguyencan", tinnhanguyencan);
                bundle.putInt("giogiac", giogiac);
                bundle.putBooleanArray("chontiennghi", chontiennghi);
                bundle.putInt("namnu", namnu);
                Intent intents = new Intent(getActivity(), Filter2.class);

                intents.putExtra("data", bundle);
                startActivityForResult(intents, 77);
            }
        });

        LoadNewData loadNewData = new LoadNewData();
        loadNewData.execute(locDLTinTimPhong);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 1112) {
                Bundle bundle = data.getBundleExtra("data");
                idtp = bundle.getInt("idtp");
                tenTP = bundle.getString("tentp");
                namnu = bundle.getInt("namnu");
                gia = bundle.getInt("gia");
                idqh = bundle.getInt("idqh");
                chontiennghi = bundle.getBooleanArray("chontiennghi");
                giogiac = bundle.getInt("giogiac");
                tinnhanguyencan = bundle.getBoolean("tinnhanguyencan");
                tinnhatro = bundle.getBoolean("tinnhatro");
                tinoghep = bundle.getBoolean("tinoghep");
                String temp1 = "(";
                if (tinnhatro) {
                    temp1 = temp1 + "1,";
                }
                if (tinoghep) {
                    temp1 = temp1 + "2,";
                }
                if (tinnhanguyencan) {
                    temp1 = temp1 + "3,";
                }
                if (temp1.length() > 1) {
                    temp1 = temp1.substring(0, temp1.length() - 1);
                }

                temp1 = temp1 + ")";

                locDLTinTimPhong.setIdtp(idtp);
                locDLTinTimPhong.setIdqh(idqh);
                locDLTinTimPhong.setDoituong(namnu);
                locDLTinTimPhong.setGia(gia);
                locDLTinTimPhong.setGiogiac(giogiac);
                locDLTinTimPhong.setLoaitin(temp1);
                locDLTinTimPhong.setTrang(1);
                locDLTinTimPhong.setTiennghi(bundle.getString("tiennghi"));

                /*ThreadData threadData = new ThreadData();
                threadData.run();*/
                LoadNewData loadNewData = new LoadNewData();
                loadNewData.execute(locDLTinTimPhong);
            }
        }
    }

    public ArrayList<TinTimPhongItemList> getData() {
        ArrayList<TinTimPhongItemList> tmp = new ArrayList<>();
       /* tmp.add(new TinTimPhongItemList(1, "Cần thuê phòng trọ tại quận thủ đức", 1, 1, "Quận Thủ Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        tmp.add(new TinTimPhongItemList(2, "Cần tìm phòng ở ghép tại quận 2", 3, 2, "Quận 2 / Hồ Chí Mình", 1, "1.500.000 - 1.700.000"));
        tmp.add(new TinTimPhongItemList(3, "Cần thuê phòng trọ tại quận 7", 1, 1, "Quận 7 / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
*/
        return tmp;
    }

    public class LoadNewData extends AsyncTask<LocDLTinTimPhong, Void, ArrayList<TinTimPhongItemList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dangLoadDL = true;
            //loadingData.setVisibility(View.VISIBLE);
            layoutOverlay2.setVisibility(View.VISIBLE);
            data.clear();
            list_tin_tim_phong_adapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<TinTimPhongItemList> doInBackground(LocDLTinTimPhong... locDLTinTimPhongs) {
            DAL_TinTimPhong dal_tinTimPhong = new DAL_TinTimPhong();
            ArrayList<TinTimPhongItemList> tinTimPhongItemLists = new ArrayList<>();
            tinTimPhongItemLists = dal_tinTimPhong.danhSachTin(locDLTinTimPhongs[0]);
            return tinTimPhongItemLists;
        }

        @Override
        protected void onPostExecute(ArrayList<TinTimPhongItemList> tinTimPhongItemLists) {
            super.onPostExecute(tinTimPhongItemLists);
            if (tinTimPhongItemLists == null) {
                Toast.makeText(getContext(), "Có lỗi xảy ra ... Thử lại sau..", Toast.LENGTH_SHORT).show();
            } else {
                if (tinTimPhongItemLists.size() > 0) {
                    conDL = true;
                    locDLTinTimPhong.setTrang(2);

                } else {
                    conDL = false;
                }

                data.clear();
                data.addAll(tinTimPhongItemLists);
                //list_tin_tim_phong_adapter = new List_Tin_Tim_Phong_Adapter(getContext(), R.layout.item_list_tin_tim_phong, data);
                //listTinTimPhong.setAdapter(list_tin_tim_phong_adapter);

                list_tin_tim_phong_adapter.notifyDataSetChanged();
                dangLoadDL = false;
                first = true;
                //loadingData.setVisibility(View.GONE);

                layoutOverlay2.setVisibility(View.GONE);


            }
        }
    }

    public class LoadMoreData extends AsyncTask<LocDLTinTimPhong, Void, ArrayList<TinTimPhongItemList>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dangLoadDL = true;
            loadingData.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<TinTimPhongItemList> doInBackground(LocDLTinTimPhong... locDLTinTimPhongs) {
            ArrayList<TinTimPhongItemList> tinTimPhongItemLists = new ArrayList<>();
            DAL_TinTimPhong dal_tinTimPhong = new DAL_TinTimPhong();
            tinTimPhongItemLists = dal_tinTimPhong.danhSachTin(locDLTinTimPhongs[0]);
            return tinTimPhongItemLists;
        }

        @Override
        protected void onPostExecute(ArrayList<TinTimPhongItemList> tinTimPhongItemLists) {
            super.onPostExecute(tinTimPhongItemLists);
            if (tinTimPhongItemLists == null) {
                Toast.makeText(getContext(), "Có lỗi xảy ra, thử lại sau..", Toast.LENGTH_SHORT).show();
            } else {
                if (tinTimPhongItemLists.size() > 0) {
                    conDL = true;
                    locDLTinTimPhong.setTrang(locDLTinTimPhong.getTrang() + 1);
                } else {
                    conDL = false;
                }

                dangLoadDL = false;

                data.addAll(tinTimPhongItemLists);
                //list_tin_tim_phong_adapter = new List_Tin_Tim_Phong_Adapter(getContext(), R.layout.item_list_tin_tim_phong, data);
                //listTinTimPhong.setAdapter(list_tin_tim_phong_adapter);
                list_tin_tim_phong_adapter.notifyDataSetChanged();
                first = true;
                loadingData.setVisibility(View.GONE);
            }
        }
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

}
