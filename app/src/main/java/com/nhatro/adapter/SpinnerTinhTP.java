package com.nhatro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nhatro.R;
import com.nhatro.model.TinhTP;

import java.util.ArrayList;

/**
 * Created by CongHoang on 4/9/2018.
 */

public class SpinnerTinhTP extends BaseAdapter {

    Context context;
    ArrayList<TinhTP> data;
    LayoutInflater inflter;


    public SpinnerTinhTP(Context applicationContext , ArrayList<TinhTP> data) {
        this.context = applicationContext;
        this.data = data;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public TinhTP getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflter.inflate(R.layout.item_spinner_tp, null);
        TextView txtTenTP = (TextView) convertView.findViewById(R.id.txtTenTP);

        txtTenTP.setText(data.get(position).getTen());
        return convertView;
    }
}
