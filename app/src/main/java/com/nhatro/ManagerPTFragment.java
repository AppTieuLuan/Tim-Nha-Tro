package com.nhatro;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nhatro.DAL.DAL_PhongTro;
import com.nhatro.adapter.CustomListViewAdapter;
import com.nhatro.model.LocDL;
import com.nhatro.model.MyManagerRoom;
import com.nhatro.model.PhongTro;
import com.nhatro.model.Token;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class ManagerPTFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView lstDanhSach;
    ArrayList<PhongTro> data;
    CustomListViewAdapter adapter;
    View footerview;
    boolean isLoading = false; // chưa load DL
    boolean isnext = true; // Còn dữ liệu để load tiếp
    LinearLayout layoutList;
    ProgressBar loadingData;
    boolean loadnewisdone = false;

    ConstraintLayout layoutLoading;

    SwipeRefreshLayout swipeRefreshLayout;

    int trang = 2;
    AlertDialog.Builder builder;

    public ManagerPTFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manager_pt, container, false);

        footerview = inflater.inflate(R.layout.footer_listivew, null);
        loadingData = v.findViewById(R.id.loadingData);
        layoutList = v.findViewById(R.id.layoutList);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        layoutLoading = v.findViewById(R.id.layoutLoadingList);

        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
        data = new ArrayList<>();

        lstDanhSach = (ListView) v.findViewById(R.id.lstDanhSachTin);
        adapter = new CustomListViewAdapter(getContext(), data);
        adapter.notifyDataSetChanged();

        lstDanhSach.setAdapter(adapter);

        lstDanhSach.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), Details.class);

                Bundle bundle = new Bundle();
                String idItem = data.get(i).getId();
                bundle.putString("iditem", idItem);
                bundle.putString("tieude", data.get(i).getTieude());
                intent.putExtra("iditem", bundle);
                getActivity().startActivity(intent);
            }
        });

        lstDanhSach.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNews(data.get(position).getId(), data.get(position).getTieude());
                return false;
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
                        ManagerPTFragment.LoadMoreDataAsyn loadMoreDataAsyn = new ManagerPTFragment.LoadMoreDataAsyn();
                        loadMoreDataAsyn.execute();
                    }
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        getData();
        return v;
    }
    public void deleteNews(String id, String title){
        builder.setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xóa tin \""+ title +"\" này không!")
                .setNeutralButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ok
                        DataClient dataClient = APIUtils.getData();
                        retrofit2.Call<String> callback = dataClient.deleteNews(id);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String mess = response.body();
                                if(mess.equals("Success")){
                                    Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                    getData();
                                }else{
                                    Toast.makeText(getContext(), "Xóa không thành công! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getContext(), "Xóa không thành công! Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // hủy
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public String getUserInfo() {
        SharedPreferences pre = getActivity().getSharedPreferences("Mydata", MODE_PRIVATE);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Mydata", Context.MODE_PRIVATE);
            int ss = sharedPreferences.getInt("ischangedState", -1);
            if (ss == 1) {
                SharedPreferences mPrefs = getActivity().getSharedPreferences("Mydata", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("ischangedState", 0);
                editor.commit();
                getData();
            }
        }
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
                if(s.equals("done")){
                    ArrayList<PhongTro> dsPhong = new ArrayList<>();

                    DataClient dataClient = new APIUtils().getData();
                    retrofit2.Call<List<PhongTro>> callback = dataClient.getNews(getUserInfo(), 1);

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
                            Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
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
                if(s.equals("done")){
                    ArrayList<PhongTro> dsPhong = new ArrayList<>();

                    DataClient dataClient = new APIUtils().getData();
                    retrofit2.Call<List<PhongTro>> callback = dataClient.getNews(getUserInfo(), 1);

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
                            Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
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
            if(s.equals("done")) {
                ArrayList<PhongTro> dsPhong = new ArrayList<>();
                DataClient dataClient = new APIUtils().getData();
                retrofit2.Call<List<PhongTro>> callback = dataClient.getNews(getUserInfo(), trang);

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
                        }else{
                            trang ++;
                        }
                        data.addAll(dsPhong);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                        loadingData.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<PhongTro>> call, Throwable t) {
                        Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
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
}
