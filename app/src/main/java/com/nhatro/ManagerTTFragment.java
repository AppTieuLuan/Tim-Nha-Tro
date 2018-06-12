package com.nhatro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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

import com.google.gson.Gson;
import com.nhatro.DAL.DAL_TinTimPhong;
import com.nhatro.adapter.List_Tin_Tim_Phong_Adapter;
import com.nhatro.model.LocDLTinTimPhong;
import com.nhatro.model.PhongTro;
import com.nhatro.model.TinTimPhong;
import com.nhatro.model.TinTimPhongItemList;
import com.nhatro.model.User;
import com.nhatro.retrofit.APIUtils;
import com.nhatro.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerTTFragment extends Fragment {
    private ListView listTinTimPhong;
    private List_Tin_Tim_Phong_Adapter list_tin_tim_phong_adapter;
    LinearLayout layoutOverlay2;
    ArrayList<TinTimPhongItemList> data;
    boolean dangLoadDL = false;
    boolean conDL = true;
    boolean openmenu = false;

    ProgressBar loadingData;
    //FloatingActionButton iconAdd;

    int trang = 2;
    AlertDialog.Builder builder;
    boolean first = false; // đánh dấu đã load xong lần đầu hay chưa để load tiếp

    LinearLayout layoutOverlay;
    User users;
    public ManagerTTFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v;
        v = inflater.inflate(R.layout.fragment_manager_tt, container, false);

        getUserInfo();

        layoutOverlay = v.findViewById(R.id.layoutOverlay);
        layoutOverlay2 = v.findViewById(R.id.layoutOverlay2);

        loadingData = v.findViewById(R.id.loadingData);
        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);

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
                    dangLoadDL = true;
                    ManagerTTFragment.LoadMoreDataRetrofit loadMoreData = new ManagerTTFragment.LoadMoreDataRetrofit();
                    loadMoreData.execute();
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
        listTinTimPhong.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNews(data.get(position).getId(), data.get(position).getTieude());
                return false;
            }
        });
        ManagerTTFragment.LoadNewDataRetrofit loadNewData = new ManagerTTFragment.LoadNewDataRetrofit();
        loadNewData.execute();
        return v;
    }

    public class LoadNewDataRetrofit extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dangLoadDL = true;
            layoutOverlay2.setVisibility(View.VISIBLE);
            data.clear();
            list_tin_tim_phong_adapter.notifyDataSetChanged();
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

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("done")) {
                ArrayList<TinTimPhongItemList> ds = new ArrayList<>();
                DataClient dataClient = APIUtils.getData();
                retrofit2.Call<List<TinTimPhongItemList>> callback = dataClient.getNewsFind(users.getId(), 1);
                callback.enqueue(new Callback<List<TinTimPhongItemList>>() {
                    @Override
                    public void onResponse(Call<List<TinTimPhongItemList>> call, Response<List<TinTimPhongItemList>> response) {
                        List<TinTimPhongItemList> json = response.body();
                        for (int i = 0; i < json.size(); i++) {
                            TinTimPhongItemList pt = new TinTimPhongItemList();
                            pt.setId(json.get(i).getId());
                            pt.setLoai(json.get(i).getLoai());
                            pt.setTieude(json.get(i).getTieude());
                            pt.setGioitinh(json.get(i).getGioitinh());
                            pt.setQuanhuyen(json.get(i).getQuanhuyen());
                            pt.setSonguoi(json.get(i).getSonguoi());
                            pt.setGiaTu(json.get(i).getGiaTu());
                            pt.setNgaydang(json.get(i).getNgaydang());
                            ds.add(pt);
                        }
                        if (ds.size() > 0) {
                            conDL = true;
                        } else {
                            conDL = false;
                        }
                        data.clear();
                        data.addAll(ds);
                        list_tin_tim_phong_adapter.notifyDataSetChanged();
                        dangLoadDL = false;
                        first = true;
                        layoutOverlay2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<TinTimPhongItemList>> call, Throwable t) {
                        Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                        layoutOverlay2.setVisibility(View.GONE);
                        data.clear();
                        list_tin_tim_phong_adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public class LoadMoreDataRetrofit extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dangLoadDL = true;
            loadingData.setVisibility(View.VISIBLE);
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

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("done")) {
                ArrayList<TinTimPhongItemList> ds = new ArrayList<>();
                DataClient dataClient = APIUtils.getData();
                retrofit2.Call<List<TinTimPhongItemList>> callback = dataClient.getNewsFind(users.getId(), trang);
                callback.enqueue(new Callback<List<TinTimPhongItemList>>() {
                    @Override
                    public void onResponse(Call<List<TinTimPhongItemList>> call, Response<List<TinTimPhongItemList>> response) {
                        List<TinTimPhongItemList> json = response.body();
                        for (int i = 0; i < json.size(); i++) {
                            TinTimPhongItemList pt = new TinTimPhongItemList();
                            pt.setId(json.get(i).getId());
                            pt.setLoai(json.get(i).getLoai());
                            pt.setTieude(json.get(i).getTieude());
                            pt.setGioitinh(json.get(i).getGioitinh());
                            pt.setQuanhuyen(json.get(i).getQuanhuyen());
                            pt.setSonguoi(json.get(i).getSonguoi());
                            pt.setGiaTu(json.get(i).getGiaTu());
                            pt.setNgaydang(json.get(i).getNgaydang());
                            ds.add(pt);
                        }
                        if (ds.size() > 0) {
                            conDL = true;
                            trang ++;
                        } else {
                            conDL = false;
                        }
                        dangLoadDL = false;
                        data.addAll(ds);
                        list_tin_tim_phong_adapter.notifyDataSetChanged();
                        first = true;
                        loadingData.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<TinTimPhongItemList>> call, Throwable t) {
                        Toast.makeText(getContext(), "Vui lòng kiểm tra lại đường truyền!", Toast.LENGTH_SHORT).show();
                        loadingData.setVisibility(View.GONE);
                    }
                });
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

    public void deleteNews(String id, String title){
        builder.setTitle("Thông báo")
                .setMessage("Bạn có chắc muốn xóa tin \""+ title +"\" này không!")
                .setNeutralButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // ok
                        DataClient dataClient = APIUtils.getData();
                        retrofit2.Call<String> callback = dataClient.deleteNewsFind(id);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String mess = response.body();
                                if(mess.equals("Success")){
                                    Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                                    ManagerTTFragment.LoadNewDataRetrofit loadNewDataRetrofit = new ManagerTTFragment.LoadNewDataRetrofit();
                                    loadNewDataRetrofit.execute();
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

}
