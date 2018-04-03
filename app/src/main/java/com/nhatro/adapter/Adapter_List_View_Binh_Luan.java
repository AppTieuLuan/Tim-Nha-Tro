package com.nhatro.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhatro.R;
import com.nhatro.model.BinhLuan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CongHoang on 3/30/2018.
 */

public class Adapter_List_View_Binh_Luan extends ArrayAdapter {
    ArrayList<BinhLuan> data = new ArrayList<>();
    private int layoutResouce;

    public Adapter_List_View_Binh_Luan(@NonNull Context context, int resource, ArrayList<BinhLuan> data) {
        super(context, resource);
        this.data = data;
        this.layoutResouce = resource;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        BinhLuan bl = data.get(position);

        View view = convertView;
        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.layoutResouce, null);

            viewHolder = new ViewHolder();

            viewHolder.txtTenUser = (TextView) view.findViewById(R.id.txtTenNguoiDung);
            viewHolder.txtBinhLuan = (TextView) view.findViewById(R.id.txtbinhluan);
            viewHolder.avatar = (ImageView) view.findViewById(R.id.profile_image);
            viewHolder.txtNgayBl = (TextView) view.findViewById(R.id.ngaybl);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txtNgayBl.setText(bl.getNgayViet());
        viewHolder.txtTenUser.setText(bl.getTenUser());
        viewHolder.txtBinhLuan.setText(bl.getNoiDungBl());

        Picasso.with(getContext()).load(bl.getAvatarUser()).into(viewHolder.avatar);

        return view;
    }

    static class ViewHolder {
        ImageView avatar;
        TextView txtTenUser;
        TextView txtBinhLuan;
        TextView txtNgayBl;
    }
}
