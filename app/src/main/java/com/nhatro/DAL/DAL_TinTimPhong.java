package com.nhatro.DAL;

import android.util.Log;

import com.nhatro.model.LocDLTinTimPhong;
import com.nhatro.model.TinTimPhong;
import com.nhatro.model.TinTimPhongItemList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CongHoang on 5/11/2018.
 */

public class DAL_TinTimPhong {
    Variable variable = new Variable();

    public boolean themTinMoi(TinTimPhong tinTimPhong) {
        boolean rs = false;

        String URL_NEW = variable.getWebservice() + "themTinTimPhong.php";
        //String URL_NEW = "http://192.168.1.9:8080/firebase/upTinPhongTro.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("tieude", tinTimPhong.getTieude()));
        args.add(new BasicNameValuePair("loaitin", String.valueOf(tinTimPhong.getLoaitin())));
        args.add(new BasicNameValuePair("songuoimin", String.valueOf(tinTimPhong.getSonguoimin())));
        args.add(new BasicNameValuePair("songuoimax", String.valueOf(tinTimPhong.getSonguoimax())));
        args.add(new BasicNameValuePair("giamin", String.valueOf(tinTimPhong.getGiamin())));
        args.add(new BasicNameValuePair("giamax", String.valueOf(tinTimPhong.getGiamax())));
        args.add(new BasicNameValuePair("giogiac", String.valueOf(tinTimPhong.getGiogiac())));
        args.add(new BasicNameValuePair("idtp", String.valueOf(tinTimPhong.getIdtp())));
        args.add(new BasicNameValuePair("idqh", tinTimPhong.getIdqh()));
        args.add(new BasicNameValuePair("lat", String.valueOf(tinTimPhong.getLat())));
        args.add(new BasicNameValuePair("lng", String.valueOf(tinTimPhong.getLng())));
        args.add(new BasicNameValuePair("bankinh", String.valueOf(tinTimPhong.getBankinh())));
        args.add(new BasicNameValuePair("tiennghi", tinTimPhong.getTiennghi()));
        args.add(new BasicNameValuePair("motathem", tinTimPhong.getMotathem()));
        args.add(new BasicNameValuePair("nhanthongbao", String.valueOf(tinTimPhong.getNhanthongbao())));
        args.add(new BasicNameValuePair("iduser", String.valueOf(tinTimPhong.getIduser())));
        args.add(new BasicNameValuePair("gioitinh", String.valueOf(tinTimPhong.getGioitinh())));
        args.add(new BasicNameValuePair("khuvuc", String.valueOf(tinTimPhong.getKhuvuc())));
        args.add(new BasicNameValuePair("qh", tinTimPhong.getQh()));
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

    public ArrayList<TinTimPhongItemList> danhSachTin(LocDLTinTimPhong locDLTinTimPhong) {
        ArrayList<TinTimPhongItemList> tinTimPhongItemLists = new ArrayList<>();

        String URL_NEW = variable.getWebservice() + "danhSachTinTimPhong.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("idtp", String.valueOf(locDLTinTimPhong.getIdtp())));
        args.add(new BasicNameValuePair("idqh", String.valueOf(locDLTinTimPhong.getIdqh())));
        args.add(new BasicNameValuePair("gia", String.valueOf(locDLTinTimPhong.getGia())));
        args.add(new BasicNameValuePair("loaitin", locDLTinTimPhong.getLoaitin()));
        args.add(new BasicNameValuePair("giogiac", String.valueOf(locDLTinTimPhong.getGiogiac())));
        args.add(new BasicNameValuePair("tiennghi", locDLTinTimPhong.getTiennghi()));
        args.add(new BasicNameValuePair("doituong", String.valueOf(locDLTinTimPhong.getDoituong())));
        args.add(new BasicNameValuePair("orderby", String.valueOf(locDLTinTimPhong.getOrderby())));
        args.add(new BasicNameValuePair("trang", String.valueOf(locDLTinTimPhong.getTrang())));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray hotros = jsonObj.getJSONArray("tins");

                    Log.d("JSONNNNNNN", jsonObj.getString("sql"));
                    for (int i = 0; i < hotros.length(); i++) {
                        JSONObject obj = (JSONObject) hotros.get(i);
                        TinTimPhongItemList pt = new TinTimPhongItemList();
                        pt.setId(obj.getString("id"));
                        pt.setLoai(obj.getInt("loai"));
                        pt.setTieude(obj.getString("tieude"));
                        pt.setGioitinh(obj.getInt("gioitinh"));
                        pt.setQuanhuyen(obj.getString("quanhuyen"));
                        pt.setSonguoi(obj.getString("songuoi"));
                        pt.setGiaTu(obj.getString("giaTu"));
                        pt.setNgaydang(obj.getString("ngaydang"));
                        tinTimPhongItemLists.add(pt);
                    }
                    return tinTimPhongItemLists;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return tinTimPhongItemLists;
    }

    public TinTimPhong chiTietTin(String id) {
        TinTimPhong tinTimPhong = new TinTimPhong();
        String URL_NEW = variable.getWebservice() + "chiTietTinTimPhong.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);
        Log.d("JSONNNNNNNNNNNNNNNNNN", json);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");

                if (success == 1) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("tin");
                    //a.setUsername(jsonObject1.getString("username"));
                    tinTimPhong.setId(jsonObject1.getString("id"));
                    tinTimPhong.setTieude(jsonObject1.getString("tieude"));
                    tinTimPhong.setLoaitin(jsonObject1.getInt("loaitin"));
                    tinTimPhong.setSonguoimin(jsonObject1.getInt("songuoimin"));
                    tinTimPhong.setSonguoimax(jsonObject1.getInt("songuoimax"));
                    tinTimPhong.setTentp(jsonObject1.getString("tentp"));
                    tinTimPhong.setQh(jsonObject1.getString("tenqh"));
                    tinTimPhong.setGiamin(jsonObject1.getInt("giamin"));
                    tinTimPhong.setGiamax(jsonObject1.getInt("giamax"));
                    tinTimPhong.setKhuvuc(jsonObject1.getString("khuvuc"));
                    tinTimPhong.setGiogiac(jsonObject1.getInt("giogiac"));
                    tinTimPhong.setLat(jsonObject1.getDouble("lat"));
                    tinTimPhong.setLng(jsonObject1.getDouble("lng"));
                    tinTimPhong.setBankinh(jsonObject1.getInt("bankinh"));
                    tinTimPhong.setTiennghi(jsonObject1.getString("tiennghi"));
                    tinTimPhong.setMotathem(jsonObject1.getString("motathem"));
                    tinTimPhong.setGioitinh(jsonObject1.getInt("gioitinh"));
                    tinTimPhong.setHoten(jsonObject1.getString("hoten"));
                    tinTimPhong.setSdt(jsonObject1.getString("sodt"));
                    tinTimPhong.setFacebook(jsonObject1.getString("facebook"));
                    tinTimPhong.setAvatar(jsonObject1.getString("avatar"));

                    return tinTimPhong;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }
}
