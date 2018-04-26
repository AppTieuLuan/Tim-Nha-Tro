package com.nhatro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.nhatro.adapter.AdapterRecyclerViewChonQuan;
import com.nhatro.adapter.SpinnerTinhTP;
import com.nhatro.model.QuanHuyen;
import com.nhatro.model.TinhTP;
import com.nhatro.sqlite.SQLite_QuanHuyen;
import com.nhatro.sqlite.SQLite_TinhTP;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    CrystalRangeSeekbar seekSoNguoi, seekGia;

    TextView minSoNguoi, maxSoNguoi, minGia, maxGia;

    ArrayList<TinhTP> arrTinhTP;
    Spinner spinnerTinhTP;

    SQLite_TinhTP sqLite_tinhTP;
    SpinnerTinhTP spinnerTinhTPAdapter;
    SQLite_QuanHuyen sqLite_quanHuyen;
    //int indexSpinnerTinhTp;

   // LinearLayout layoutButtonOK;



    RecyclerView recycleQH;
    ArrayList<QuanHuyen> quanHuyens;
    AdapterRecyclerViewChonQuan adapterRecyclerViewChonQuan;


    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        seekGia = findViewById(R.id.seekGia);
        minGia = findViewById(R.id.minGia);
        maxGia = findViewById(R.id.maxGia);
        seekSoNguoi = (CrystalRangeSeekbar) findViewById(R.id.seekSoNguoi);
        minSoNguoi = findViewById(R.id.minSoNguoi);
        maxSoNguoi = findViewById(R.id.maxSoNguoi);
      //  layoutButtonOK = findViewById(R.id.layoutButtonOK);
        sqLite_quanHuyen = new SQLite_QuanHuyen(TestActivity.this);
        quanHuyens = new ArrayList<>();

        quanHuyens = sqLite_quanHuyen.getDSQH(1);

        arrTinhTP = new ArrayList<>();
        spinnerTinhTP = findViewById(R.id.spinnerTinhTP);
        sqLite_tinhTP = new SQLite_TinhTP(TestActivity.this);

        arrTinhTP = sqLite_tinhTP.getDSTP();
        spinnerTinhTPAdapter = new SpinnerTinhTP(getApplicationContext(), arrTinhTP);
        spinnerTinhTP.setAdapter(spinnerTinhTPAdapter);


        // set listener
        seekSoNguoi.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minSoNguoi.setText(String.valueOf(minValue));
                maxSoNguoi.setText(String.valueOf(maxValue));
            }
        });


        minGia.setText("0 VNĐ");
        maxGia.setText("10.000.000 VNĐ");


        seekGia.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {

                DecimalFormat formatter = new DecimalFormat("###,###,###");
                String tmp = formatter.format(minValue) + " VNĐ";
                minGia.setText(tmp);

                tmp = formatter.format(maxValue) + " VNĐ";
                maxGia.setText(tmp);


            }
        });


        spinnerTinhTP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), String.valueOf(arrTinhTP.get(position).getId()) + " - " + arrTinhTP.get(position).getTen(),Toast.LENGTH_SHORT).show();
                //arrQuanHuyen = sqLite_quanHuyen.getDSQH(arrTinhTP.get(position).getId());
                //spinnerQuanHuyen_adapter.notifyDataSetChanged();

                quanHuyens = sqLite_quanHuyen.getDSQH(arrTinhTP.get(position).getId());
                adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
                recycleQH.setAdapter(adapterRecyclerViewChonQuan);

                //spinnerQuanHuyen_adapter = new SpinnerQuanHuyen_Adapter(getApplicationContext(), arrQuanHuyen);
                //spinnerQuanHuyen_adapter.notifyDataSetChanged();
                //spinnerQuanHuyen.setAdapter(spinnerQuanHuyen_adapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                 recycleQH.setLayoutManager(layoutManager);
                */

        recycleQH = (RecyclerView) findViewById(R.id.recycleQH);


        layoutManager = new LinearLayoutManager(TestActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycleQH.setLayoutManager(layoutManager);

        adapterRecyclerViewChonQuan = new AdapterRecyclerViewChonQuan(quanHuyens, getApplicationContext());
        recycleQH.setAdapter(adapterRecyclerViewChonQuan);

        /*layoutButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),String.valueOf(quanHuyens.get(0).isSelect()),Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
