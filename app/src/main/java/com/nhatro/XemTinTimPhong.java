package com.nhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.model.Item_Grid_Facilities;

import java.util.ArrayList;

public class XemTinTimPhong extends AppCompatActivity {


    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstTienNghi;
    Grid_Facilities_Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_tin_tim_phong);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent callerIntent = getIntent();
        Bundle bundle = callerIntent.getBundleExtra("data");
        int iditem = bundle.getInt("iditem");
        getSupportActionBar().setTitle(bundle.getString("tieude"));

        gridTienNghi = findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);

        lstTienNghi = new ArrayList<>();

        lstTienNghi.add(new Item_Grid_Facilities("Wifi", R.drawable.icons_wi_fi, false));
        lstTienNghi.add(new Item_Grid_Facilities("Gác", R.drawable.icon_gac, false));
        lstTienNghi.add(new Item_Grid_Facilities("Toilet riêng", R.drawable.icon_toilet, true));
        lstTienNghi.add(new Item_Grid_Facilities("Phòng tắm riêng", R.drawable.icon_bathroom, true));
        lstTienNghi.add(new Item_Grid_Facilities("Giường", R.drawable.icon_giuong, true));
        lstTienNghi.add(new Item_Grid_Facilities("Tivi", R.drawable.icon_tv, false));
        lstTienNghi.add(new Item_Grid_Facilities("Tủ lạnh", R.drawable.icon_tulanh, false));
        lstTienNghi.add(new Item_Grid_Facilities("Bếp gas", R.drawable.icon_bepga, false));
        lstTienNghi.add(new Item_Grid_Facilities("Quạt", R.drawable.icon_quat, false));
        lstTienNghi.add(new Item_Grid_Facilities("Tủ đồ", R.drawable.icon_tu_quan_ao, false));
        lstTienNghi.add(new Item_Grid_Facilities("Máy lạnh", R.drawable.icon_may_lanh, false));
        lstTienNghi.add(new Item_Grid_Facilities("Đèn điện", R.drawable.icon_bongden, true));

        lstTienNghi.add(new Item_Grid_Facilities("Bảo vệ", R.drawable.icon_baove, false));
        lstTienNghi.add(new Item_Grid_Facilities("Camera", R.drawable.icon_camera, true));
        lstTienNghi.add(new Item_Grid_Facilities("Khu để xe riêng", R.drawable.icon_doxe, false));

        myAdapter = new Grid_Facilities_Adapter(getApplicationContext(),R.layout.grid_facilities_items,lstTienNghi);

        gridTienNghi.setAdapter(myAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
