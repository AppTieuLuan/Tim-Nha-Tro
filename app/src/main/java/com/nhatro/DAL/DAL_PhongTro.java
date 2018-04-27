package com.nhatro.DAL;

import com.nhatro.model.LocDL;
import com.nhatro.model.PhongTro;
import com.nhatro.model.PhongTros;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

        args.add(new BasicNameValuePair("images", images.toString()));
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

    public ArrayList<PhongTro> danhSachPhong(LocDL locDL) {
        ArrayList<PhongTro> dsP = new ArrayList<>();

        /*if (locDL.isDanhsach() == 1) {*/

        String URL_NEW = "https://nhatroservice.000webhostapp.com/danhSachPhong.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idtp", String.valueOf(locDL.getIdtp())));
        args.add(new BasicNameValuePair("idqh", locDL.getIdqh()));
        args.add(new BasicNameValuePair("giamin", String.valueOf(locDL.getGiamin())));
        args.add(new BasicNameValuePair("giamax", String.valueOf(locDL.getGiamax())));
        args.add(new BasicNameValuePair("dientichmin", String.valueOf(locDL.getDientichmin())));
        args.add(new BasicNameValuePair("dientichmax", String.valueOf(locDL.getDientichmax())));
        args.add(new BasicNameValuePair("songuoio", String.valueOf(locDL.getSonguoio())));
        args.add(new BasicNameValuePair("loaitin", locDL.getLoaitin()));
        args.add(new BasicNameValuePair("tiennghi", locDL.getTiennghi()));
        args.add(new BasicNameValuePair("doituong", String.valueOf(locDL.getDoituong())));
        args.add(new BasicNameValuePair("giogiac", String.valueOf(locDL.getGiogiac())));
        args.add(new BasicNameValuePair("danhsach", String.valueOf(locDL.isDanhsach())));
        args.add(new BasicNameValuePair("lat", String.valueOf(locDL.getLat())));
        args.add(new BasicNameValuePair("lng", String.valueOf(locDL.getLng())));
        args.add(new BasicNameValuePair("bankinh", String.valueOf(locDL.getBankinh())));
        args.add(new BasicNameValuePair("trang", String.valueOf(locDL.getTrang())));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray hotros = jsonObj.getJSONArray("phongs");
                    for (int i = 0; i < hotros.length(); i++) {
                        JSONObject obj = (JSONObject) hotros.get(i);
                        PhongTro pt = new PhongTro();
                        pt.setId(obj.getInt("id"));
                        pt.setTieude(obj.getString("tieude"));
                        pt.setGia(Integer.parseInt(obj.getString("gia")));
                        pt.setDiachi(obj.getString("diachi"));
                        pt.setDientich(Integer.parseInt(obj.getString("dientich")));
                        pt.setChieudai(Integer.parseInt(obj.getString("chieudai")));
                        pt.setChieurong(Integer.parseInt(obj.getString("chieurong")));
                        pt.setHinhanh(obj.getString("hinhanh"));
                        pt.setGioitinh(obj.getString("doituong"));
                        dsP.add(pt);
                    }
                }
            } catch (JSONException e) {
                return dsP;
            }
        }
        /*} else {

            try {

                String link = "https://nhatroservice.000webhostapp.com/danhSachPhong.php";
                String data = URLEncoder.encode("idtp", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getIdtp()), "UTF-8");
                data += "&" + URLEncoder.encode("giamin", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getGiamin()), "UTF-8");
                data += "&" + URLEncoder.encode("giamax", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getGiamax()), "UTF-8");
                data += "&" + URLEncoder.encode("dientichmin", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getDientichmin()), "UTF-8");
                data += "&" + URLEncoder.encode("dientichmax", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getDientichmax()), "UTF-8");
                data += "&" + URLEncoder.encode("songuoio", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getSonguoio()), "UTF-8");
                data += "&" + URLEncoder.encode("loaitin", "UTF-8") + "=" + URLEncoder.encode(locDL.getLoaitin(), "UTF-8");
                data += "&" + URLEncoder.encode("tiennghi", "UTF-8") + "=" + URLEncoder.encode(locDL.getTiennghi(), "UTF-8");
                data += "&" + URLEncoder.encode("doituong", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getDoituong()), "UTF-8");
                data += "&" + URLEncoder.encode("giogiac", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getGiogiac()), "UTF-8");
                data += "&" + URLEncoder.encode("danhsach", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.isDanhsach()), "UTF-8");
                data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getLat()), "UTF-8");
                data += "&" + URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getLng()), "UTF-8");
                data += "&" + URLEncoder.encode("bankinh", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getBankinh()), "UTF-8");
                data += "&" + URLEncoder.encode("trang", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(locDL.getTrang()), "UTF-8");


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }*/
        return dsP;
    }
}
