package com.nhatro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.model.QuanHuyen;

import java.util.ArrayList;

/**
 * Created by CongHoang on 4/20/2018.
 */

public class AdapterRecyclerViewChonQuan extends RecyclerView.Adapter<AdapterRecyclerViewChonQuan.RecyclerChonQuanViewHolder> {
    private ArrayList<QuanHuyen> data = new ArrayList<>();
    Context context;

    public AdapterRecyclerViewChonQuan(ArrayList<QuanHuyen> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerChonQuanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemview = inflater.inflate(R.layout.item_recycle_view_chon_quan, parent, false);
        return new RecyclerChonQuanViewHolder(itemview);
    }

    public void updateItem(int position) {
        data.get(position).setSelect(true);
        notifyItemChanged(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerChonQuanViewHolder holder, int position) {
        holder.tenQuan.setText(data.get(position).getTen());
        if (data.get(position).isSelect()) {
            holder.isCheck.setVisibility(View.VISIBLE);
            holder.tenQuan.setTextColor(Color.parseColor("#46960c"));
        } else {
            holder.isCheck.setVisibility(View.GONE);
            holder.tenQuan.setTextColor(Color.parseColor("#808080"));
        }

        holder.setItemClickListener(new OnItemRecycleClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean tmp = data.get(position).isSelect();
                data.get(position).setSelect(!tmp);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RecyclerChonQuanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tenQuan;
        ImageView isCheck;
        private OnItemRecycleClickListener onItemRecycleClickListener;

        public RecyclerChonQuanViewHolder(View itemView) {
            super(itemView);
            tenQuan = itemView.findViewById(R.id.tenQuan);
            isCheck = itemView.findViewById(R.id.isCheck);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(OnItemRecycleClickListener itemClickListener) {
            this.onItemRecycleClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemRecycleClickListener.onClick(v, getAdapterPosition());
        }
    }
}
