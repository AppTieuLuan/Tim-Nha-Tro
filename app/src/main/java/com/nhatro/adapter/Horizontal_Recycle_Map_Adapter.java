package com.nhatro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.Details;
import com.nhatro.R;
import com.nhatro.model.PhongTro;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CongHoang on 5/6/2018.
 */

public class Horizontal_Recycle_Map_Adapter extends RecyclerView.Adapter<Horizontal_Recycle_Map_Adapter.ItemHolder> {
    ArrayList<PhongTro> data;
    Context context;

    public Horizontal_Recycle_Map_Adapter(Context context, ArrayList<PhongTro> a) {
        this.data = a;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcardview, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        ViewGroup.LayoutParams layoutParams = holder.cardView.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;

        //layoutParams.width = 350;
        holder.cardView.setLayoutParams(layoutParams);

        int type = data.get(position).getLoaitintuc();
        if (type == 1) {
            holder.txttype.setText("Nhà trọ cho thuê");
        } else {
            if (type == 2) {
                holder.txttype.setText("Tìm bạn ở ghép");
            } else {
                if (type == 3) {
                    holder.txttype.setText("Nhà nguyên căn");
                }
            }
        }

        String sx = data.get(position).getGioitinh();
        if (sx.equals("3")) {
            holder.txtSex.setVisibility(View.GONE);
        } else {
            if (sx.equals("1")) {
                holder.txtSex.setText("Nam");
            } else {
                holder.txtSex.setText("Nữ");
            }
        }

        holder.txtTitle.setText(data.get(position).getTieude());
        holder.txtAddress.setText(data.get(position).getDiachi());
        holder.area.setText("Diện tích: " + data.get(position).getDientich() + "m2 (" + data.get(position).getChieudai() + "m x " + data.get(position).getChieurong() + "m)");
        holder.txtPrice.setText(data.get(position).getGia() + " vnd");
        try {
            Picasso.with(this.context).load(data.get(position).getHinhanh()).into(holder.avatar);
        } catch (Exception e) {

        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Details.class);
                Bundle bundle = new Bundle();
                String idItem = data.get(position).getId();
                bundle.putString("iditem", idItem);
                bundle.putString("tieude", data.get(position).getTieude());
                intent.putExtra("iditem", bundle);
                context.startActivity(intent);

                /*Toast.makeText(context,"Êtrt",Toast.LENGTH_SHORT).show();*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;
        @BindView(R.id.txttype)
        TextView txttype;
        @BindView(R.id.txtSex)
        TextView txtSex;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtAddress)
        TextView txtAddress;
        @BindView(R.id.area)
        TextView area;
        @BindView(R.id.txtPrice)
        TextView txtPrice;

        @BindView(R.id.cardView)
        CardView cardView;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
