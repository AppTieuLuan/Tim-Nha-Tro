package com.nhatro;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.DAL.ThongBaos;
import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.adapter.ListThongBao_Adapter;
import com.nhatro.adapter.OnHideNotify;
import com.nhatro.model.BinhLuan;
import com.nhatro.model.ThongBao;

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

    public NotifyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_notify, container, false);
        findView(v);
        setUp();
        return v;
    }

    public void findView(View v) {
        layout1 = v.findViewById(R.id.layout1);
        listView = v.findViewById(R.id.listView);
        contain = v.findViewById(R.id.contain);
    }

    public void setUp() {
        trang = 1;
        isloading = false;
        isnext = true;
        data = new ArrayList<>();

        bottomDiaLogSetup();

        /*thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 1, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));


        thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 1, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));
        thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 1, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));
        thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 1, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));
        thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 1, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));
        thongBaos.add(new ThongBao(1, 1, 2, "Nguyễn Văn A", 1, "1234", "https://i-vnexpress.vnecdn.net/2018/05/23/IMG2336abc-1527040856-8955-1527040948_500x300.jpg",
                "", 0, 1, "Cho thuê phòng trọ tại quận 9", 12, 4, 2018, 12, 04));
*/
        listThongBao_adapter = new ListThongBao_Adapter(getActivity(), data, new OnHideNotify() {
            @Override
            public void onHide(int position) {
                index = position;
                bottomSheetDialog.show();
            }
        });
        listView.setAdapter(listThongBao_adapter);
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
        if (!hidden) {
            check();
        }
    }

    public class getThongBao extends AsyncTask<Void, Void, ArrayList<ThongBao>> {

        @Override
        protected ArrayList<ThongBao> doInBackground(Void... voids) {
            ThongBaos thongBaos = new ThongBaos();
            ArrayList<ThongBao> thongBaos1 = new ArrayList<>();
            thongBaos1 = thongBaos.dsThongBao(1, trang);
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
        }
    }
}
