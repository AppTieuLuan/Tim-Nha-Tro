package com.nhatro.tab_details;


import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.adapter.ExpandableHeightGridView;
import com.nhatro.adapter.Grid_Facilities_Adapter;
import com.nhatro.model.Item_Grid_Facilities;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabChiTiet extends Fragment {

    int idItem;
    TextView valueDatCoc;

    ImageView btnSMS, btnCall, btnFacebook, btnMessenger;

    ExpandableHeightGridView gridTienNghi;
    ArrayList<Item_Grid_Facilities> lstTienNghi;
    Grid_Facilities_Adapter myAdapter;


    public TabChiTiet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_tab_chi_tiet, container, false);

        lstTienNghi = new ArrayList<>();
        gridTienNghi = v.findViewById(R.id.gridTienNghi);
        gridTienNghi.setExpanded(true);
        gridTienNghi.setFocusable(false);


        valueDatCoc = v.findViewById(R.id.valueDatCoc);
        Bundle bundle = this.getArguments();
        if (bundle != null && v!= null) {
            idItem = bundle.getInt("id");
            valueDatCoc.setText(String.valueOf(idItem) + " vnđ");

            btnMessenger = v.findViewById(R.id.iconMessenger);
            btnCall = v.findViewById(R.id.iconCall);
            btnFacebook = v.findViewById(R.id.iconfacebook);
            btnSMS = v.findViewById(R.id.iconSMS);
            btnSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", "0969340320");
                    smsIntent.putExtra("sms_body", "");
                    startActivity(smsIntent);
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:0969340320"));
                    startActivity(intent);
                }
            });

            btnFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100006230731600"));
                        startActivity(intent);
                    } catch(Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/100006230731600")));
                    }
                }
            });

            btnMessenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + "100006230731600"));
                        startActivity(i);
                    } catch (ActivityNotFoundException ex) {
                        Toast.makeText(getContext(), "Oups!Can't open Facebook messenger right now. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //lstTienNghi.clear();

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
            lstTienNghi.add(new Item_Grid_Facilities("Đèn điện", R.drawable.icon_bongden, false));

            lstTienNghi.add(new Item_Grid_Facilities("Bảo vệ", R.drawable.icon_baove, false));
            lstTienNghi.add(new Item_Grid_Facilities("Camera", R.drawable.icon_camera, true));
            lstTienNghi.add(new Item_Grid_Facilities("Khu để xe riêng", R.drawable.icon_doxe, false));



            myAdapter = new Grid_Facilities_Adapter(getContext(), R.layout.grid_facilities_items, lstTienNghi);
            myAdapter.notifyDataSetChanged();

            //grid.setAdapter(adapter);
            gridTienNghi.setAdapter(myAdapter);




        }
        return v;
    }

}
