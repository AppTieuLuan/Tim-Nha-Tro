package com.nhatro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.login.CallBackListener;
import com.nhatro.model.PhongTro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManagerAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhongTro> data;
    CallBackListener callBackListener;
    public ManagerAdapter(Context activity, ArrayList<PhongTro> data) {
        this.context = activity;
        this.data = data;
        if (activity instanceof CallBackListener)
            callBackListener = (CallBackListener) activity;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public PhongTro getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.itemlistview, null);

        TextView title = (TextView) view.findViewById(R.id.txtTitle);
        TextView address = (TextView) view.findViewById(R.id.txtAddress);
        TextView price = (TextView) view.findViewById(R.id.txtPrice);
        TextView area = (TextView) view.findViewById(R.id.area);
        TextView sex = (TextView) view.findViewById(R.id.sex);
        ImageView img = (ImageView) view.findViewById(R.id.imageItemListView);

        ImageView imgSave = (ImageView) view.findViewById(R.id.iconSavePost);
        LinearLayout buttonSavePost = (LinearLayout) view.findViewById(R.id.layoutIconSavePost);


        Picasso.with(context).load("https://www.theworlds50best.com/latinamerica/en/filestore/jpg/HC-HarrySas-slider-home.jpg").into(img);


        // getting movie data for the row
        PhongTro pt = data.get(i);

        // title
        title.setText(pt.getTieude());
        address.setText(pt.getDiachi());
        price.setText(String.valueOf(pt.getGia()));
        area.setText("Diện tích: "+ String.valueOf(pt.getDientich()) + "("+ String.valueOf(pt.getChieudai())+ "m x " +
                String.valueOf(pt.getChieurong()) + "m)");

        sex.setText(pt.getGioitinh());

        if(pt.isDaluu()) {
            imgSave.setColorFilter(Color.parseColor("#008efc"));
        } else {
            imgSave.setColorFilter(Color.parseColor("#ffffff"));
        }

        buttonSavePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pt.isDaluu()) {
                    imgSave.setColorFilter(Color.parseColor("#ffffff"));
                    pt.setDaluu(false);
                    //refresh lại danh sách
                    data.remove(i);
                    callBackListener.onCallBack();

                    Toast.makeText(v.getContext(),"Hủy lưu",Toast.LENGTH_SHORT).show();
                } else {
                    imgSave.setColorFilter(Color.parseColor("#008efc"));
                    pt.setDaluu(true);

                    Toast.makeText(v.getContext(),"Đã lưu",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


}
