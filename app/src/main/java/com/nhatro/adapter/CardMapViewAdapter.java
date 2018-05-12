package com.nhatro.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.Details;
import com.nhatro.R;
import com.nhatro.model.ItemOnMapView;
import com.nhatro.model.PhongTro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 2/25/2018.
 */

public class CardMapViewAdapter extends PagerAdapter implements CardAdapter {
    private List<CardView> mViews;
    private ArrayList<PhongTro> mData;
    private float mBaseElevation;

    public CardMapViewAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public CardMapViewAdapter(ArrayList<PhongTro> data) {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            mViews.add(null);
        }
        this.mData = data;

    }

    public void addCardItem(PhongTro item) {
        mViews.add(null);
        mData.add(item);
    }

    public void removeall() {
        for (int i = 0; i < mData.size(); i++) {
            mViews.set(i, null);
        }
        mData.clear();
        notifyDataSetChanged();
    }

    public PhongTro getItem(int position) {
        return mData.get(position);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.itemcardview, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),String.valueOf(mData.get(position).getId()),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), Details.class);
                Bundle bundle = new Bundle();
                String idItem = mData.get(position).getId();
                bundle.putString("iditem", idItem);
                bundle.putString("tieude", mData.get(position).getTieude());
                intent.putExtra("iditem", bundle);
                view.getContext().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(PhongTro item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.txtTitle);
        TextView address = (TextView) view.findViewById(R.id.txtAddress);
        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        TextView price = (TextView) view.findViewById(R.id.txtPrice);
        TextView txttype = (TextView) view.findViewById(R.id.txttype);
        TextView txtSex = (TextView) view.findViewById(R.id.txtSex);
        TextView area = (TextView) view.findViewById(R.id.area);

        titleTextView.setText(item.getTieude());
        address.setText(item.getDiachi());
        price.setText(String.valueOf(Math.round(item.getGia())) + " VNĐ");

        if (item.getLoaitintuc() == 1) {
            txttype.setText("Phòng trọ cho thuê");
        } else {
            if (item.getLoaitintuc() == 2) {
                txttype.setText("Tìm bạn ở ghép");
            } else {
                if (item.getLoaitintuc() == 3) {
                    txttype.setText("Nhà nguyên căn");
                }
            }
        }

        if (item.getGioitinh().equals("3")) {
            txtSex.setVisibility(View.INVISIBLE);
        } else {
            if (item.getGioitinh().equals("1")) {
                txtSex.setText("Nam");
            } else {
                txtSex.setText("Nữ");
            }
        }

        area.setText("Diện tích: " + item.getDiachi() + "m2 " + "(" + item.getChieudai() + "m x " + item.getChieurong() + "m)");
        try {
            Picasso.with(view.getContext()).load(item.getHinhanh()).into(avatar);
        } catch (Exception e) {

        }


        //price.setText(String.valueOf(item.getPrice()));
    }
}
