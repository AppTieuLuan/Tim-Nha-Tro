package com.nhatro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.nhatro.adapter.Adapter_List_View_Binh_Luan;
import com.nhatro.model.BinhLuan;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotifyFragment extends Fragment {

    View v;

    public NotifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_notify, container, false);
        return v;
    }

}
