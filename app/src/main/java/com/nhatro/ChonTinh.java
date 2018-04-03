package com.nhatro;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.nhatro.Location_Helper.LocationHelper;
import com.nhatro.adapter.List_Tinh_TP_Adapter;
import com.nhatro.model.TinhTP;
import com.nhatro.sqlite.SQLiteDataController;
import com.nhatro.sqlite.SQLite_TinhTP;

import java.io.IOException;
import java.util.ArrayList;

public class ChonTinh extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,ActivityCompat.OnRequestPermissionsResultCallback {

    ArrayList<TinhTP> data;
    ArrayList<TinhTP> searchdata;
    List_Tinh_TP_Adapter list_tinh_tp_adapter;
    ListView lstTP;

    private EditText txtTimTinh;
    int tinhTP;

    LinearLayout xacDinhViTri;

    private Location mLastLocation;
    GoogleApiClient mGoogleApiClient;

    double latitude;
    double longitude;

    LocationHelper locationHelper;

    // Đối tượng tương tác với Google API

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

        xacDinhViTri = (LinearLayout) findViewById(R.id.layoutGPS);
        locationHelper=new LocationHelper(this);
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


        // check availability of play services
        if (locationHelper.checkPlayServices()) {

            // Building the GoogleApi client
            locationHelper.buildGoogleApiClient();
        }

        xacDinhViTri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                locationHelper.checkpermission();
                mLastLocation=locationHelper.getLocation();

                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();
                    getAddress();

                } else {
                    showToast("Couldn't get the location. Make sure location is enabled on the device");
                }

                //Toast.makeText(getApplicationContext(),"Đang xác định vị trí", Toast.LENGTH_SHORT).show();

                /*if (checkPlayServices()) {
                    // Building the GoogleApi client
                    buildGoogleApiClient();
                }
*/


            }
        });

    }


    public void getAddress()
    {
        Address locationAddress;

        locationAddress=locationHelper.getAddress(latitude,longitude);

        if(locationAddress!=null)
        {

            String address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();


            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;


                Toast.makeText(getApplicationContext(),currentLocation,Toast.LENGTH_SHORT).show();
             /*   tvEmpty.setVisibility(View.GONE);
                tvAddress.setText(currentLocation);
                tvAddress.setVisibility(View.VISIBLE);*/

            }

        }
        else
            showToast("Something went wrong");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("Connection failed:", " ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        mLastLocation=locationHelper.getLocation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        locationHelper.connectApiClient();
    }


    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);

    }

    public void showToast(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
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
