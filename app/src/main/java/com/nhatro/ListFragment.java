package com.nhatro;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.elyeproj.loaderviewlibrary.LoaderImageView;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.google.gson.Gson;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.EventBus.Event_DangNhapThanhCong;
import com.nhatro.adapter.CustomListViewAdapter;
import com.nhatro.model.LocDL;
import com.nhatro.model.PhongTro;
import com.nhatro.model.User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView lstDanhSach;
    ArrayList<PhongTro> data;
    CustomListViewAdapter adapter;
    View footerview;
    boolean isLoading = false; // chưa load DL
    boolean isnext = true; // Còn dữ liệu để load tiếp
    LinearLayout layoutList;
    ProgressBar loadingData;
    boolean loadnewisdone = false;
    LocDL locDL;
    com.github.clans.fab.FloatingActionButton btnAdd;

    ConstraintLayout layoutLoading;

    mHadler mHadlerr;
    HandlerFilter handlerFilter;
    SwipeRefreshLayout swipeRefreshLayout;

    ConstraintLayout emptyview;
    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle bundle = getArguments();
        locDL = new LocDL();
        locDL.setBankinh(2);
        locDL.setGiamin(bundle.getInt("giamin"));
        locDL.setGiamax(bundle.getInt("giamax"));
        locDL.setDientichmin(bundle.getInt("dientichmin"));
        locDL.setDientichmax(bundle.getInt("dientichmax"));
        locDL.setSonguoio(bundle.getInt("songuoio"));
        locDL.setLoaitin(bundle.getString("loaitin"));
        locDL.setTiennghi(bundle.getString("tiennghi"));
        locDL.setDoituong(bundle.getInt("doituong"));
        locDL.setGiogiac(bundle.getInt("giogiac"));
        locDL.setIdtp(bundle.getInt("idtp"));
        locDL.setIdqh("");
        locDL.setTrang(bundle.getInt("trang"));
        locDL.setTrang(1);

        footerview = inflater.inflate(R.layout.footer_listivew, null);
        loadingData = v.findViewById(R.id.loadingData);
        layoutList = v.findViewById(R.id.layoutList);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        btnAdd = v.findViewById(R.id.iconAdd);
        layoutLoading = v.findViewById(R.id.layoutLoadingList);

        mHadlerr = new mHadler();
        handlerFilter = new HandlerFilter();

        data = new ArrayList<>();

        lstDanhSach = (ListView) v.findViewById(R.id.lstDanhSachTin);

        adapter = new CustomListViewAdapter(getContext(), data);
        adapter.notifyDataSetChanged();

        lstDanhSach.setAdapter(adapter);

        lstDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (checkConnection()) {
                    Intent intent = new Intent(getActivity(), Details.class);

                    Bundle bundle = new Bundle();
                    String idItem = data.get(i).getId();
                    bundle.putString("iditem", idItem);
                    bundle.putString("tieude", data.get(i).getTieude());
                    intent.putExtra("iditem", bundle);
                    getActivity().startActivity(intent);
                }
            }
        });

        lstDanhSach.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (checkConnection()) {
                    if (absListView.getLastVisiblePosition() == i2 - 1) {

                        Log.d("ĐÁY", isLoading + " - " + isnext + "  -  " + loadnewisdone);
                        if (isLoading == false && isnext && loadnewisdone) {
                            LoadMoreDataAsyn loadMoreDataAsyn = new LoadMoreDataAsyn();
                            loadMoreDataAsyn.execute(locDL);
                        }
                    }
                }


                /*if (i + i1 == i2 && i2 == 0) {
                    if (isLoading == false && isnext && loadnewisdone) {
                        LoadMoreDataAsyn loadMoreDataAsyn = new LoadMoreDataAsyn();
                        loadMoreDataAsyn.execute(locDL);
                    }
                }*/
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (checkLogin().equals("-1")) {
                    Toast.makeText(getContext(), "Đăng nhập trước khi thực hiện thao tác này !", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intents = new Intent(getContext(), Newpost.class);
                    startActivity(intents);
                }*/

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
                    Intent intents = new Intent(getContext(), Newpost.class);
                    startActivity(intents);
                }

            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);

        filterData(locDL);

        //loadnewData();
        return v;
    }

    public boolean checkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            Toast.makeText(getContext(), "Lỗi kết nối mạng. Thử lại sau ..", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void filterData(LocDL locDL) {
       /* Toast.makeText(getContext(),"Đang Lọc DL",Toast.LENGTH_SHORT).show();*/
        lstDanhSach.smoothScrollToPosition(0);
        this.locDL = locDL;
        isnext = true;
        isLoading = true;
        /*Thread thread = new ThreadFilter();
        thread.start();*/

        SharedPreferences pre = getActivity().getSharedPreferences("Mydata", MODE_PRIVATE);
        String user = pre.getString("MyUser", "");

        if (user.equals("")) {
            locDL.setIduser(-1);
        } else {
            User user1 = new User();
            Gson gsonUser = new Gson();
            user1 = gsonUser.fromJson(user, User.class);
            locDL.setIduser(Integer.parseInt(user1.getId()));
        }
        loadNewData(locDL);

    }

    public void loadNewData(LocDL locDL) {
        LoadNewDataAsyn loadDataAsyn = new LoadNewDataAsyn();
        loadDataAsyn.execute(locDL);
    }

    @Override
    public void onRefresh() {
        locDL.setTrang(1);
        SharedPreferences pre = getActivity().getSharedPreferences("Mydata", MODE_PRIVATE);
        String user = pre.getString("MyUser", "");

        if (user.equals("")) {
            locDL.setIduser(-1);
        } else {
            User user1 = new User();
            Gson gsonUser = new Gson();
            user1 = gsonUser.fromJson(user, User.class);
            locDL.setIduser(Integer.parseInt(user1.getId()));
        }
        LoadNewDataRefresh loadNewDataRefresh = new LoadNewDataRefresh();
        loadNewDataRefresh.execute(locDL);
    }

    public class mHadler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                //lstDanhSach.addFooterView(footerview);
                loadingData.setVisibility(View.VISIBLE);
            } else {
                ArrayList<PhongTro> tmep = new ArrayList<>();

                tmep = (ArrayList<PhongTro>) msg.obj;

                if (tmep.size() == 0) {
                    isnext = false;
                }
                data.addAll(tmep);
                adapter.notifyDataSetChanged();
                isLoading = false;
                loadingData.setVisibility(View.GONE);
            }
        }

    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            isLoading = true;
            mHadlerr.sendEmptyMessage(0);
            ArrayList<PhongTro> mangData = new ArrayList<>();

            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            mangData = dal_phongTro.danhSachPhong(locDL);
            Message message = mHadlerr.obtainMessage(1, mangData);
            mHadlerr.sendMessage(message);

        }
    }

    public class HandlerFilter extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                data.clear();
                adapter.notifyDataSetChanged();
                layoutList.setVisibility(View.GONE);
                layoutLoading.setVisibility(View.VISIBLE);
            } else {
                ArrayList<PhongTro> aa = new ArrayList<>();
                aa = (ArrayList<PhongTro>) msg.obj;
                locDL.setTrang(locDL.getTrang() + 1);
                isLoading = false;
                if (aa.size() == 0) {
                    isnext = false;
                }
                data.addAll(aa);
                adapter.notifyDataSetChanged();
                layoutList.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.GONE);
            }
        }

    }

    public class ThreadFilter extends Thread {
        @Override
        public void run() {
            handlerFilter.sendEmptyMessage(0);
            isLoading = true;
            ArrayList<PhongTro> mangData = new ArrayList<>();
            DAL_PhongTro sss = new DAL_PhongTro();
            mangData = sss.danhSachPhong(locDL);
            Message message = handlerFilter.obtainMessage(1, mangData);
            handlerFilter.sendMessage(message);
        }
    }

    public class LoadNewDataRefresh extends AsyncTask<LocDL, Void, ArrayList<PhongTro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            locDL.setTrang(1);
            data.clear();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(ArrayList<PhongTro> phongTros) {
            super.onPostExecute(phongTros);

            locDL.setTrang(2);
            isLoading = false;
            if (phongTros.size() == 0) {
                isnext = false;
                //lstDanhSach.getEmptyView();
                //Log.d("ádasd", "111111111111111111111111111");
                //lstDanhSach.getEmptyView();
            }
            data.addAll(phongTros);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            loadnewisdone = true;

            //Log.d("JSONN", String.valueOf(locDL.getTrang()) + " --- " + String.valueOf(isnext) + " --- " + String.valueOf(isLoading) + "--- " + String.valueOf(loadnewisdone));

        }

        @Override
        protected ArrayList<PhongTro> doInBackground(LocDL... locDLS) {
            ArrayList<PhongTro> phongTros = new ArrayList<>();
            isLoading = true;

            DAL_PhongTro sss = new DAL_PhongTro();
            phongTros = sss.danhSachPhong(locDLS[0]);
            return phongTros;
        }
    }

    public class LoadNewDataAsyn extends AsyncTask<LocDL, Void, ArrayList<PhongTro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data.clear();
            adapter.notifyDataSetChanged();
            layoutList.setVisibility(View.GONE);
            layoutLoading.setVisibility(View.VISIBLE);

            //Log.d("BD","Bắt đầu load dữ liệu");
        }

        @Override
        protected void onPostExecute(ArrayList<PhongTro> phongTros) {
            super.onPostExecute(phongTros);

            //Nó get đc da ta mà
            locDL.setTrang(2);
            isLoading = false;
            if (phongTros.size() == 0) {
                isnext = false;
            }
            data.addAll(phongTros);
            adapter.notifyDataSetChanged();
            layoutList.setVisibility(View.VISIBLE);
            layoutLoading.setVisibility(View.GONE);
            loadnewisdone = true;

            Log.d("JSONN", String.valueOf(locDL.getTrang()) + " --- " + String.valueOf(isnext) + " --- " + String.valueOf(isLoading) + "--- " + String.valueOf(loadnewisdone));


            //Log.d("KT","Kết thúc load dữ liệu");

        }

        @Override
        protected ArrayList<PhongTro> doInBackground(LocDL... locDLS) {
            ArrayList<PhongTro> phongTros = new ArrayList<>();
            isLoading = true;

            DAL_PhongTro sss = new DAL_PhongTro();
            phongTros = sss.danhSachPhong(locDLS[0]);
            return phongTros;
        }
    }

    public class LoadMoreDataAsyn extends AsyncTask<LocDL, Void, ArrayList<PhongTro>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingData.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<PhongTro> phongTros) {
            super.onPostExecute(phongTros);

            isLoading = false;
            if (phongTros.size() == 0) {
                isnext = false;
            } else {
                locDL.setTrang(locDL.getTrang() + 1);
            }
            data.addAll(phongTros);
            adapter.notifyDataSetChanged();
            isLoading = false;
            loadingData.setVisibility(View.GONE);
            Log.d("JSONN", String.valueOf(locDL.getTrang()) + " ____ " + String.valueOf(isnext));
        }

        @Override
        protected ArrayList<PhongTro> doInBackground(LocDL... locDLS) {
            ArrayList<PhongTro> phongTros = new ArrayList<>();
            isLoading = true;
            DAL_PhongTro dal_phongTro = new DAL_PhongTro();
            phongTros = dal_phongTro.danhSachPhong(locDLS[0]);
            return phongTros;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            int ss = sharedPreferences.getInt("ischangedState", -1);

            //Toast.makeText(getContext(), String.valueOf(ss), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getContext(), String.valueOf(ss), Toast.LENGTH_SHORT).show();
            if (ss == 1) {
                SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("ischangedState", 0);
                editor.commit();
                locDL.setTrang(1);
                filterData(locDL);
            }


        }

    }


}
