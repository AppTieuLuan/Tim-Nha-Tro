package com.nhatro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nhatro.R;
import com.nhatro.model.ThongBao;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by CongHoang on 5/23/2018.
 */

public class ListThongBao_Adapter extends BaseAdapter {
    Context context;
    ArrayList<ThongBao> data;
    private OnHideNotify onHideNotify;

    public ListThongBao_Adapter(Context activity, ArrayList<ThongBao> data, OnHideNotify onHideNotify) {
        this.context = activity;
        this.data = data;
        this.onHideNotify = onHideNotify;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public ThongBao getItem(int i) {
        return this.data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.item_list_thongbao, null);

        TextView noidung = (TextView) view.findViewById(R.id.noidung);
        TextView ngay = (TextView) view.findViewById(R.id.ngay);
        ConstraintLayout container = (ConstraintLayout) view.findViewById(R.id.container);
        ImageView setting = view.findViewById(R.id.setting);
        de.hdodenhof.circleimageview.CircleImageView profile_image = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.profile_image);


        // getting movie data for the row
        ThongBao tb = data.get(i);
        try {
            Picasso.with(context).load(tb.getAvataruser2()).into(profile_image);
        } catch (Exception e) {

        }

        if (tb.getDadoc() == 0) {
            container.setBackgroundColor(Color.parseColor("#c8dae7"));
        } else {

        }
        if (tb.getLoai() == 1 || tb.getLoai() == 2) {
            // Comment vào tin đăng phòng trọ

            noidung.setText(Html.fromHtml("<b>" + tb.getTenuser2() + "</b>" + " đã bình luận vào bài viết '" +
                    "<b>" + tb.getTieudeTin() + "</b>" + "' của bạn"));

        } else {
            if (tb.getLoai() == 3) {
                // Có phòng trọ mới phù hợp...
                noidung.setText("Phòng trọ mới được thêm phù hợp với các tiêu chí bạn đưa ra...Xem chi tiết");
            }
        }

        ngay.setText(tb.getMngay() + " tháng " + tb.getMthang() + " lúc " + tb.getMgio() + ":" + tb.getMphut());


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onHideNotify != null) {
                    onHideNotify.onHide(i);
                }
            }
        });
        return view;
    }

}
