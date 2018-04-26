package com.nhatro.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.nhatro.MainActivity;
import com.nhatro.R;
import com.nhatro.model.Add_Images;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Base64;

/**
 * Created by CongHoang on 4/13/2018.
 */

public class Grid_Add_Image_Adapter extends ArrayAdapter {
    ArrayList<String> data = new ArrayList<>();


    public Grid_Add_Image_Adapter(@NonNull Context context, int resource, ArrayList<String> data) {
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

        Picasso.with(getContext()).load(new File(data.get(position))).resize(150, 150).into(images);

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
