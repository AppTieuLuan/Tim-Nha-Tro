package com.nhatro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nhatro.adapter.List_Tinh_TP_Adapter;
import com.nhatro.model.TinhTP;
import com.nhatro.sqlite.SQLiteDataController;
import com.nhatro.sqlite.SQLite_TinhTP;

import java.io.IOException;
import java.util.ArrayList;

public class ChonTinh extends AppCompatActivity {

    ArrayList<TinhTP> data;
    ArrayList<TinhTP> searchdata;
    List_Tinh_TP_Adapter list_tinh_tp_adapter;
    ListView lstTP;

    private EditText txtTimTinh;
    int tinhTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_tinh);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTimTinh = findViewById(R.id.txtTimTinh);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        tinhTP = bundle.getInt("tinhTP");


        lstTP = (ListView) findViewById(R.id.lstTP);
        //getSupportActionBar().hide();
        getSupportActionBar().setTitle("Chọn Tỉnh/Thành Phố");
        data = new ArrayList<>();
        searchdata = new ArrayList<TinhTP>();
        //createDB();

        getListTP();

        /*SQLiteDataController sql = new SQLiteDataController(this);
        sql.deleteDatabase();*/


        //data.get(indexSelected).setSelect(true);
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == tinhTP) {
                data.get(i).setSelect(true);
                break;
            }
        }
        //data.indexOf()

        list_tinh_tp_adapter = new List_Tinh_TP_Adapter(getApplicationContext(), R.layout.item_list_tinh_tp, searchdata);
        list_tinh_tp_adapter.notifyDataSetChanged();

        lstTP.setAdapter(list_tinh_tp_adapter);

        lstTP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //data.get(tinhTP).setSelect(false);

                //data.get(i).setSelect(false);
                if (txtTimTinh.getText().toString().equals("")) {
                    for (int i1 = 0; i1 < data.size(); i1++) {
                        if (data.get(i1).getId() == tinhTP) {
                            data.get(i1).setSelect(false);
                            break;
                        }
                    }

                    tinhTP = data.get(i).getId();

                    data.get(i).setSelect(!data.get(i).isSelect());
                    list_tinh_tp_adapter.notifyDataSetChanged();

                    Intent intent1 = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tinhTP", tinhTP);
                    bundle.putString("tenTP", data.get(i).getTen());
                    intent1.putExtra("datas", bundle);

                    setResult(21, intent1); // phương thức này sẽ trả kết quả cho Activity Filter
                    finish(); // Đóng Activity hiện tại
                } else {

                    for (int i1 = 0; i1 < searchdata.size(); i1++) {
                        if (searchdata.get(i1).getId() == tinhTP) {
                            searchdata.get(i1).setSelect(false);
                            break;
                        }
                    }

                    tinhTP = searchdata.get(i).getId();

                    searchdata.get(i).setSelect(!searchdata.get(i).isSelect());
                    list_tinh_tp_adapter.notifyDataSetChanged();

                    Intent intent1 = getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("tinhTP", tinhTP);
                    bundle.putString("tenTP", searchdata.get(i).getTen());
                    intent1.putExtra("datas", bundle);

                    setResult(21, intent1); // phương thức này sẽ trả kết quả cho Activity Filter
                    finish(); // Đóng Activity hiện tại

                    //Toast.makeText(getApplicationContext(),searchdata.get(i).getTen() + String.valueOf(searchdata.get(i).getId()),Toast.LENGTH_SHORT).show();
                }

            }
        });

        txtTimTinh.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {
                    //Toast.makeText(getApplicationContext(),txtTimTinh.getText().toString(),Toast.LENGTH_SHORT).show();
                    int textlength = s.length();
                    ArrayList<TinhTP> tempArrayList = new ArrayList<>();
                    for (TinhTP tp : data) {
                        if (textlength <= tp.getTen().length()) {
                            if (tp.getTen().toLowerCase().contains(s.toString().toLowerCase())) {
                                tempArrayList.add(tp);
                            }
                        }
                    }
                    searchdata = tempArrayList;

                    //list_tinh_tp_adapter.notifyDataSetChanged();
                    list_tinh_tp_adapter = new List_Tinh_TP_Adapter(getApplicationContext(), R.layout.item_list_tinh_tp, searchdata);
                    list_tinh_tp_adapter.notifyDataSetChanged();

                    lstTP.setAdapter(list_tinh_tp_adapter);

                } else {
                    list_tinh_tp_adapter = new List_Tinh_TP_Adapter(getApplicationContext(), R.layout.item_list_tinh_tp, data);
                    list_tinh_tp_adapter.notifyDataSetChanged();

                    lstTP.setAdapter(list_tinh_tp_adapter);
                }
            }
        });

    }

    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getListTP() {
        SQLite_TinhTP sqLite_tinhTP = new SQLite_TinhTP(getApplicationContext());
        data = new ArrayList<TinhTP>();
        data = sqLite_tinhTP.getDSTP();
        searchdata = data;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
