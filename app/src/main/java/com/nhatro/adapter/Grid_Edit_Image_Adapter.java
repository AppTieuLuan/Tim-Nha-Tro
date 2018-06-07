package com.nhatro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nhatro.EditImage;
import com.nhatro.Newpost;
import com.nhatro.R;
import com.nhatro.model.EditImg;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by CongHoang on 6/7/2018.
 */

public class Grid_Edit_Image_Adapter extends ArrayAdapter {
    ArrayList<EditImg> data = new ArrayList<>();


    public Grid_Edit_Image_Adapter(@NonNull Context context, int resource, ArrayList<EditImg> data) {
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
        v = inflater.inflate(R.layout.item_grid_chon_hinh_anh, null);
        ImageView images = (ImageView) v.findViewById(R.id.images);
        ImageView iconRemoveImages = (ImageView) v.findViewById(R.id.iconRemoveImages);

        if (data.get(position).isIslocal()) {
            Picasso.with(getContext()).load(new File(data.get(position).getPath())).resize(150, 150).into(images);
        } else {
            Picasso.with(getContext()).load(data.get(position).getPath()).resize(150, 150).into(images);
        }

        iconRemoveImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        return v;

    }

}