package com.nhatro.tab_details;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabChiTiet extends Fragment {

    int idItem;
    TextView valueDatCoc;
    public TabChiTiet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_chi_tiet, container, false);
        valueDatCoc = v.findViewById(R.id.valueDatCoc);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idItem = bundle.getInt("id");
            valueDatCoc.setText(String.valueOf(idItem) + " vnđ");
        }
        return v;
    }

}
