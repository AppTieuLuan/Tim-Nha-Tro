package com.nhatro.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nhatro.Image_View;
import com.nhatro.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CongHoang on 2/22/2018.
 */

public class MyCustomPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> images;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context,  ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.viewpageritem, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewPager);
        Picasso.with(context).load(images.get(position)).into(imageView);

        //imageView.setImageResource(images[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context.getApplicationContext(), Image_View.class);

                Bundle bundle = new Bundle();
                bundle.putInt("index",position);

                bundle.putStringArrayList("data",images);
                intent.putExtra("data",bundle);


                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
