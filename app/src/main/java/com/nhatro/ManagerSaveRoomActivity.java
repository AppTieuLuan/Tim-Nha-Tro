package com.nhatro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.adapter.MyCustomListViewAdapter;
import com.nhatro.login.CallBackListener;
import com.nhatro.model.PhongTro;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerSaveRoomActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CallBackListener  {

    ListView lstDanhSach;
    ArrayList<PhongTro> data;
    MyCustomListViewAdapter adapter;
    View footerview;
    boolean isLoading = false; // chưa load DL
    boolean isnext = true; // Còn dữ liệu để load tiếp
    LinearLayout layoutList;
    ProgressBar loadingData;
    boolean loadnewisdone = false;

    ConstraintLayout layoutLoading;

    SwipeRefreshLayout swipeRefreshLayout;
    Boolean first = true;

    int trang = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_save_room);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tin đã lưu");

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerview = inflater.inflate(R.layout.footer_listivew, null);
        loadingData = findViewById(R.id.loadingData);
        layoutList = findViewById(R.id.layoutList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        layoutLoading = findViewById(R.id.layoutLoadingList);

        data = new ArrayList<>();

        lstDanhSach = (ListView) findViewById(R.id.lstDanhSachTin);
        adapter = new MyCustomListViewAdapter(ManagerSaveRoomActivity.this, data);
        adapter.notifyDataSetChanged();

        lstDanhSach.setAdapter(adapter);

        lstDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ManagerSaveRoomActivity.this, Details.class);

                Bundle bundle = new Bundle();
                String idItem = data.get(i).getId();
                bundle.putString("iditem", idItem);
                bundle.putString("tieude", data.get(i).getTieude());
                intent.putExtra("iditem", bundle);
                startActivityForResult(intent, -1);
            }
        });

        lstDanhSach.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getLastVisiblePosition() == i2 - 1) {

                    Log.d("ĐÁY", isLoading + " - " + isnext + "  -  " + loadnewisdone);
                    if (isLoading == false && isnext && loadnewisdone) {
                        ManagerSaveRoomActivity.LoadMoreDataAsyn loadMoreDataAsyn = new ManagerSaveRoomActivity.LoadMoreDataAsyn();
                        loadMoreDataAsyn.execute();
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        getData();
        first = true;
    }


    public String getUserInfo() {
        SharedPreferences pre = getSharedPreferences("Mydata", MODE_PRIVATE);
        String json = pre.getString("MyUser", "");
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user.getId();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        pullRefresh();
    }

    public void getData() {
        lstDanhSach.smoothScrollToPosition(0);
        isnext = true;
        isLoading = true;

        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loading = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(1000);// sleep 1s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "done";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                data.clear();
                adapter.notifyDataSetChanged();
                layoutList.setVisibility(View.GONE);
                layoutLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("done")) {
                    ArrayList<PhongTro> dsPhong = new ArrayList<>();

                    DataClient dataClient = new APIUtils().getData();
                    retrofit2.Call<List<PhongTro>> callback = dataClient.getSaveNews(getUserInfo(), 1);

                    callback.enqueue(new Callback<List<PhongTro>>() {
                        @Override
                        public void onResponse(Call<List<PhongTro>> call, Response<List<PhongTro>> response) {
                            List<PhongTro> json = response.body();
                            for (int i = 0; i < json.size(); i++) {
                                PhongTro pt = new PhongTro();
                                pt.setId(json.get(i).getId());
                                pt.setTieude(json.get(i).getTieude());
                                pt.setGia(json.get(i).getGia());
                                pt.setDiachi(json.get(i).getDiachi());
                                pt.setDientich(json.get(i).getDientich());
                                pt.setChieudai(json.get(i).getChieudai());
                                pt.setChieurong((json.get(i).getChieurong()));
                                pt.setHinhanh(json.get(i).getHinhanh());
                                pt.setGioitinh(json.get(i).getGioitinh());
                                pt.setLoaitintuc(json.get(i).getLoaitintuc());

                                int s = Integer.parseInt(json.get(i).getDaluu());
                                if (s == 1) {
                                    pt.setDaluu(true);
                                } else {
                                    pt.setDaluu(false);
                                }
                                dsPhong.add(pt);
                            }
                            isLoading = false;
                            if (dsPhong.size() == 0) {
                                isnext = false;
                            }
                            data.addAll(dsPhong);
                            adapter.notifyDataSetChanged();
                            layoutList.setVisibility(View.VISIBLE);
                            layoutLoading.setVisibility(View.GONE);
                            loadnewisdone = true;
                        }

                        @Override
                        public void onFailure(Call<List<PhongTro>> call, Throwable t) {
                            Toast.makeText(ManagerSaveRoomActivity.this, "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                            data.clear();
                            adapter.notifyDataSetChanged();
                            layoutList.setVisibility(View.VISIBLE);
                            layoutLoading.setVisibility(View.GONE);
                        }
                    });
                }
            }
        };
        loading.execute();
    }

    public void pullRefresh() {
        lstDanhSach.smoothScrollToPosition(0);
        isnext = true;
        isLoading = true;

        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loading = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    Thread.sleep(1000);// sleep 1s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "done";
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                data.clear();
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("done")) {
                    ArrayList<PhongTro> dsPhong = new ArrayList<>();

                    DataClient dataClient = new APIUtils().getData();
                    retrofit2.Call<List<PhongTro>> callback = dataClient.getSaveNews(getUserInfo(), 1);

                    callback.enqueue(new Callback<List<PhongTro>>() {
                        @Override
                        public void onResponse(Call<List<PhongTro>> call, Response<List<PhongTro>> response) {
                            List<PhongTro> json = response.body();
                            for (int i = 0; i < json.size(); i++) {
                                PhongTro pt = new PhongTro();
                                pt.setId(json.get(i).getId());
                                pt.setTieude(json.get(i).getTieude());
                                pt.setGia(json.get(i).getGia());
                                pt.setDiachi(json.get(i).getDiachi());
                                pt.setDientich(json.get(i).getDientich());
                                pt.setChieudai(json.get(i).getChieudai());
                                pt.setChieurong((json.get(i).getChieurong()));
                                pt.setHinhanh(json.get(i).getHinhanh());
                                pt.setGioitinh(json.get(i).getGioitinh());
                                pt.setLoaitintuc(json.get(i).getLoaitintuc());

                                int s = Integer.parseInt(json.get(i).getDaluu());
                                if (s == 1) {
                                    pt.setDaluu(true);
                                } else {
                                    pt.setDaluu(false);
                                }
                                dsPhong.add(pt);
                            }
                            isLoading = false;
                            if (dsPhong.size() == 0) {
                                isnext = false;
                            }
                            data.addAll(dsPhong);
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            loadnewisdone = true;
                        }

                        @Override
                        public void onFailure(Call<List<PhongTro>> call, Throwable t) {
                            Toast.makeText(ManagerSaveRoomActivity.this, "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                            data.clear();
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        };
        loading.execute();
    }

    public class LoadMoreDataAsyn extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingData.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("done")) {
                ArrayList<PhongTro> dsPhong = new ArrayList<>();
                DataClient dataClient = new APIUtils().getData();
                retrofit2.Call<List<PhongTro>> callback = dataClient.getSaveNews(getUserInfo(), trang);

                callback.enqueue(new Callback<List<PhongTro>>() {
                    @Override
                    public void onResponse(Call<List<PhongTro>> call, Response<List<PhongTro>> response) {
                        List<PhongTro> json = response.body();
                        for (int i = 0; i < json.size(); i++) {
                            PhongTro pt = new PhongTro();
                            pt.setId(json.get(i).getId());
                            pt.setTieude(json.get(i).getTieude());
                            pt.setGia(json.get(i).getGia());
                            pt.setDiachi(json.get(i).getDiachi());
                            pt.setDientich(json.get(i).getDientich());
                            pt.setChieudai(json.get(i).getChieudai());
                            pt.setChieurong((json.get(i).getChieurong()));
                            pt.setHinhanh(json.get(i).getHinhanh());
                            pt.setGioitinh(json.get(i).getGioitinh());
                            pt.setLoaitintuc(json.get(i).getLoaitintuc());

                            int s = Integer.parseInt(json.get(i).getDaluu());
                            if (s == 1) {
                                pt.setDaluu(true);
                            } else {
                                pt.setDaluu(false);
                            }
                            dsPhong.add(pt);
                        }
                        isLoading = false;
                        if (dsPhong.size() == 0) {
                            isnext = false;
                        } else {
                            trang++;
                        }
                        data.addAll(dsPhong);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        loadingData.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<PhongTro>> call, Throwable t) {
                        Toast.makeText(ManagerSaveRoomActivity.this, "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                        loadingData.setVisibility(View.GONE);
                    }
                });

            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(1000);// sleep 1s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "done";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void onCallBack() {
        //set lại list
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(!first) {
            getData();
        }
        first = false;
    }
}

