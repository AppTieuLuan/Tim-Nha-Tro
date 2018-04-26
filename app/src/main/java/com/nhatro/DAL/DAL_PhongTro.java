package com.nhatro.DAL;

import com.nhatro.model.PhongTros;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 4/24/2018.
 */

public class DAL_PhongTro {
    public boolean themTinPhong(PhongTros a, ArrayList<String> listImages) {
        boolean rs = false;

        String URL_NEW = "https://nhatroservice.000webhostapp.com/upTinPhongTro.php";
        //String URL_NEW = "http://192.168.1.9:8080/firebase/upTinPhongTro.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("tieude", a.getTieude()));
        args.add(new BasicNameValuePair("gia", String.valueOf(a.getGia())));
        args.add(new BasicNameValuePair("diachi", a.getDiachi()));
        args.add(new BasicNameValuePair("chieudai", String.valueOf(a.getChieudai())));
        args.add(new BasicNameValuePair("chieurong", String.valueOf(a.getChieurong())));
        args.add(new BasicNameValuePair("loaitin", String.valueOf(a.getLoaitin())));
        args.add(new BasicNameValuePair("songuoimin", String.valueOf(a.getSonguoimin())));
        args.add(new BasicNameValuePair("songuoimax", String.valueOf(a.getSonguoimax())));
        args.add(new BasicNameValuePair("tiennghi", a.getTiennghi()));
        args.add(new BasicNameValuePair("doituong", String.valueOf(a.getDoituong())));
        args.add(new BasicNameValuePair("lat", String.valueOf(a.getLat())));
        args.add(new BasicNameValuePair("lng", String.valueOf(a.getLng())));
        args.add(new BasicNameValuePair("ngay", "123123"));
        args.add(new BasicNameValuePair("iduser", String.valueOf(a.getIduser())));
        args.add(new BasicNameValuePair("motathem", a.getMotathem()));
        args.add(new BasicNameValuePair("giadien", String.valueOf(a.getGiadien())));
        args.add(new BasicNameValuePair("donvidien", a.getDonvidien()));
        args.add(new BasicNameValuePair("gianuoc", String.valueOf(a.getGianuoc())));
        args.add(new BasicNameValuePair("donvinuoc", a.getDonvinuoc()));
        args.add(new BasicNameValuePair("tiencoc", String.valueOf(a.getTiencoc())));
        args.add(new BasicNameValuePair("donvicoc", a.getDonvicoc()));
        args.add(new BasicNameValuePair("giogiac", a.getGiogiac()));
        args.add(new BasicNameValuePair("idtp", String.valueOf(a.getIdtp())));
        args.add(new BasicNameValuePair("idquanhuyen", String.valueOf(a.getIdqh())));

        JSONArray images = new JSONArray();

        for (int i = 0; i < listImages.size(); i++) {
            images.put(listImages.get(i));
        }

        args.add(new BasicNameValuePair("images",images.toString()));
        // Lấy đối tượng JSON
        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("success");
                if (success == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                return false;
            }
        }

        return rs;
    }
}
