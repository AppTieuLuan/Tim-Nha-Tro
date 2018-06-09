package com.nhatro.DAL;

import android.util.Log;

import com.nhatro.model.ThongBao;
import com.nhatro.model.TinTimPhongItemList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CongHoang on 5/23/2018.
 */

public class ThongBaos {
    Variable variable = new Variable();

    public boolean themThongBao(ThongBao thongBao) {

        String URL_NEW = variable.getWebservice() + "upTinPhongTro.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("loai", String.valueOf(thongBao.getLoai())));
        args.add(new BasicNameValuePair("tieudetin", thongBao.getTieudeTin()));
        args.add(new BasicNameValuePair("idusernhan", String.valueOf(thongBao.getIdusernhan())));
        args.add(new BasicNameValuePair("idphong", thongBao.getIdtintuc()));
        args.add(new BasicNameValuePair("dadoc", String.valueOf(thongBao.getDadoc())));
        args.add(new BasicNameValuePair("iduser2", String.valueOf(thongBao.getIduser2())));

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
        return false;
    }

    public ArrayList<ThongBao> dsThongBao(int iduser, int trang) {
        ArrayList<ThongBao> rs = new ArrayList<>();

        String URL_NEW = variable.getWebservice() + "dsThongBao.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));
        args.add(new BasicNameValuePair("trang", String.valueOf(trang)));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);

        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray hotros = jsonObj.getJSONArray("tbs");
                    for (int i = 0; i < hotros.length(); i++) {
                        JSONObject obj = (JSONObject) hotros.get(i);
                        ThongBao tb = new ThongBao();
                        tb.setId(obj.getInt("id"));
                        tb.setLoai(obj.getInt("loai"));
                        tb.setTieudeTin(obj.getString("tieudetin"));
                        tb.setIdusernhan(obj.getInt("idusernhan"));
                        tb.setIdtintuc(obj.getString("idphong"));
                        tb.setDadoc(obj.getInt("dadoc"));
                        tb.setIduser2(obj.getInt("iduser2"));
                        tb.setNgay(obj.getString("ngay"));
                        tb.setIsnew(obj.getInt("new"));
                        tb.setTenuser2(obj.getString("hoten"));
                        tb.setAvataruser2(obj.getString("avatar"));

                        /*DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
                        DateTime dt = formatter.parseDateTime(string);*/

                        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        //String s = formatter.parse(obj.getString("ngay")).getMonth();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            Date d = sdf.parse(obj.getString("ngay"));
                            tb.setMngay(d.getDate());
                            tb.setMthang(d.getMonth() + 1);
                            tb.setMgio(d.getHours());
                            tb.setMphut(d.getMinutes());
                        } catch (ParseException ex) {
                            //Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        rs.add(tb);
                    }
                    return rs;
                }
            } catch (JSONException e) {
                return rs;
            }
        }

        return rs;
    }

    public void setDaDoc(int id) {
        String URL_NEW = variable.getWebservice() + "setDaDocThongBao.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("id", String.valueOf(id)));

        MyService jsonParser = new MyService();
        try {
            jsonParser.callService(URL_NEW, MyService.POST, args);
        } catch (Exception e) {

        }
    }
}
