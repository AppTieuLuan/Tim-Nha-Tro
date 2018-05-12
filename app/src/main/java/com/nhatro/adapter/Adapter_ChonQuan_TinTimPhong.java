package com.nhatro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhatro.R;
import com.nhatro.model.QuanHuyen;

import java.util.ArrayList;

/**
 * Created by CongHoang on 5/12/2018.
 */

public class Adapter_ChonQuan_TinTimPhong extends ArrayAdapter {

    ArrayList<QuanHuyen> data = new ArrayList<>();


    public Adapter_ChonQuan_TinTimPhong(@NonNull Context context, int resource, ArrayList<QuanHuyen> data) {
        super(context, resource);
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_recycle_view_chon_quan, null);
        TextView textView = (TextView) v.findViewById(R.id.tenQuan);
        ImageView imageView = (ImageView) v.findViewById(R.id.isCheck);
        textView.setText(data.get(position).getTen());

        boolean tmp = data.get(position).isSelect();
        if (tmp) {
            imageView.setVisibility(View.VISIBLE);
            textView.setTextColor(Color.parseColor("#46960c"));
        } else {
            imageView.setVisibility(View.GONE);
            textView.setTextColor(Color.parseColor("#808080"));
        }

        return v;

    }

}
