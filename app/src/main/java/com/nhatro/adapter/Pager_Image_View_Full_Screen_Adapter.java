package com.nhatro.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nhatro.R;
import com.squareup.picasso.Picasso;

/**
 * Created by CongHoang on 4/1/2018.
 */

public class Pager_Image_View_Full_Screen_Adapter extends PagerAdapter {
    Context context;
    String images[];
    LayoutInflater layoutInflater;

    public Pager_Image_View_Full_Screen_Adapter(Context context,  String images[]) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.image_view_full_screen_item_viewpager, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.images);
        Picasso.with(context).load(images[position]).into(imageView);

        //imageView.setImageResource(images[position]);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

