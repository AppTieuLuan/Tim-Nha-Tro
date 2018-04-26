package com.nhatro;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionMenu;
import com.nhatro.adapter.List_Tin_Tim_Phong_Adapter;
import com.nhatro.model.TinTimPhongItemList;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class tim_o_ghep extends Fragment {

    private ListView listTinTimPhong;
    private List_Tin_Tim_Phong_Adapter list_tin_tim_phong_adapter;
    ArrayList<TinTimPhongItemList> data;
    boolean dangLoadDL = false;
    boolean conDL = true;
    boolean openmenu = false;
    mHadler mHadlerr;
    ProgressBar loadingData;
    //FloatingActionButton iconAdd;


    com.github.clans.fab.FloatingActionButton iconAdd;
    LinearLayout layoutOverlay;

    public tim_o_ghep() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v;
        v = inflater.inflate(R.layout.fragment_tim_o_ghep, container, false);
        layoutOverlay = v.findViewById(R.id.layoutOverlay);
        iconAdd = v.findViewById(R.id.iconAdd);

        //iconAdd = v.findViewById(R.id.iconAdd);
        loadingData = v.findViewById(R.id.loadingData);
        mHadlerr = new mHadler();
        data = new ArrayList<>();

        data.add(new TinTimPhongItemList(1, "Cần thuê phòng trọ tại quận thủ đức", 1, 1, "Quận Thủ Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        data.add(new TinTimPhongItemList(2, "Cần tìm phòng ở ghép tại quận 2", 3, 2, "Quận 2 / Hồ Chí Mình", 1, "1.500.000 - 1.700.000"));
        data.add(new TinTimPhongItemList(3, "Cần thuê phòng trọ tại quận 7", 1, 1, "Quận 7 / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        data.add(new TinTimPhongItemList(4, "Tìm nhà nguyên căn tại quận 9", 2, 3, "Quận Thử Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        data.add(new TinTimPhongItemList(5, "Cần thuê phòng trọ tại quận 9", 1, 0, "Quận Thử Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        data.add(new TinTimPhongItemList(6, "Cần thuê phòng trọ tại quận 9", 1, 1, "Quận Thử Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));

        listTinTimPhong = v.findViewById(R.id.listTinTimPhong);

        list_tin_tim_phong_adapter = new List_Tin_Tim_Phong_Adapter(getContext(), R.layout.item_list_tin_tim_phong, data);

        listTinTimPhong.setAdapter(list_tin_tim_phong_adapter);


        listTinTimPhong.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (absListView.getLastVisiblePosition() == i2 - 1 && dangLoadDL == false && conDL) {
                    //data.add(new PhongTro(9,"1111","Số 1 Võ Văn Ngân, Quận Thủ Đức, TPHCM",1200000,30,10,3,"Nam"));
                    //adapter.notifyDataSetChanged();
                    dangLoadDL = true;
                    Thread thread = new ThreadData();
                    thread.start();
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
                    int idItem = data.get(position).getId();
                    bundle.putInt("iditem", idItem);
                    bundle.putString("tieude", data.get(position).getTieude());

                    intent.putExtra("data", bundle);
                    getActivity().startActivity(intent);
                }
            }
        });

        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ThemTinTimPhong.class);
                startActivityForResult(intent, 777);
            }
        });

        return v;
    }

    public ArrayList<TinTimPhongItemList> getData() {
        ArrayList<TinTimPhongItemList> tmp = new ArrayList<>();
        tmp.add(new TinTimPhongItemList(1, "Cần thuê phòng trọ tại quận thủ đức", 1, 1, "Quận Thủ Đức / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));
        tmp.add(new TinTimPhongItemList(2, "Cần tìm phòng ở ghép tại quận 2", 3, 2, "Quận 2 / Hồ Chí Mình", 1, "1.500.000 - 1.700.000"));
        tmp.add(new TinTimPhongItemList(3, "Cần thuê phòng trọ tại quận 7", 1, 1, "Quận 7 / Hồ Chí Mình", 3, "1.500.000 - 1.800.000"));

        return tmp;
    }

    public class mHadler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                //lstDanhSach.addFooterView(footerview);
                loadingData.setVisibility(View.VISIBLE);
            } else {
                //data.addAll(getData());
                data.clear();
                data.addAll(getData());
                list_tin_tim_phong_adapter.notifyDataSetChanged();
                dangLoadDL = false;
                conDL = false;
                loadingData.setVisibility(View.GONE);
            }
        }

    }

    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHadlerr.sendEmptyMessage(0);
            //
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = mHadlerr.obtainMessage(1, 1);
            mHadlerr.sendMessage(message);

        }
    }

}
