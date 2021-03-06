package com.nhatro.DAL;

import android.util.Log;

import com.nhatro.model.LocDL;
import com.nhatro.model.Marker_TabBanDo;
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
    Variable variable = new Variable();

    public boolean themTinPhong(PhongTros a, ArrayList<String> listImages) {
        boolean rs = false;

        String URL_NEW = variable.getWebservice() + "upTinPhongTro.php";
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

        args.add(new BasicNameValuePair("hoten", a.getHoten()));
        args.add(new BasicNameValuePair("sdt", a.getSdt()));
        args.add(new BasicNameValuePair("facebook", a.getFacebook()));


        JSONArray images = new JSONArray();

        for (int i = 0; i < listImages.size(); i++) {
            images.put(listImages.get(i));
        }

        args.add(new BasicNameValuePair("images", images.toString()));
        // Lấy đối tượng JSON
        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        Log.d("HKKKKKKKKK", json);
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

        String URL_NEW = variable.getWebservice() + "danhSachPhong.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/danhSachPhong.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("orderby", String.valueOf(locDL.getOrderby())));
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
        args.add(new BasicNameValuePair("iduser", String.valueOf(locDL.getIduser())));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);

       /* Log.d("JSON LOAD DS", json);*/
        Log.d("JSON TRANGSSS", String.valueOf(locDL.getTrang()));
        if (json != null) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                if (jsonObj != null) {
                    JSONArray hotros = jsonObj.getJSONArray("phongs");

                    Log.d("JSONNNNNNN", jsonObj.getString("sql"));
                    if (locDL.isDanhsach() == 1) {
                        for (int i = 0; i < hotros.length(); i++) {
                            JSONObject obj = (JSONObject) hotros.get(i);
                            PhongTro pt = new PhongTro();
                            pt.setId(obj.getString("id"));
                            pt.setTieude(obj.getString("tieude"));
                            pt.setGia(Integer.parseInt(obj.getString("gia")));
                            pt.setDiachi(obj.getString("diachi"));
                            pt.setDientich(Integer.parseInt(obj.getString("dientich")));
                            pt.setChieudai(Integer.parseInt(obj.getString("chieudai")));
                            pt.setChieurong(Integer.parseInt(obj.getString("chieurong")));
                            pt.setHinhanh(obj.getString("hinhanh"));
                            pt.setGioitinh(obj.getString("doituong"));
                            pt.setLoaitintuc(obj.getInt("loaitintuc"));
                            int s = obj.getInt("daluu");
                            if (s == 1) {
                                pt.setDaluu(true);
                            } else {
                                pt.setDaluu(false);
                            }
                            dsP.add(pt);
                        }
                    } else {
                        for (int i = 0; i < hotros.length(); i++) {
                            JSONObject obj = (JSONObject) hotros.get(i);
                            PhongTro pt = new PhongTro();
                            pt.setId(obj.getString("id"));
                            pt.setTieude(obj.getString("tieude"));
                            pt.setGia(Integer.parseInt(obj.getString("gia")));
                            pt.setDiachi(obj.getString("diachi"));
                            pt.setDientich(Integer.parseInt(obj.getString("dientich")));
                            pt.setChieudai(Integer.parseInt(obj.getString("chieudai")));
                            pt.setChieurong(Integer.parseInt(obj.getString("chieurong")));
                            pt.setHinhanh(obj.getString("hinhanh"));
                            pt.setGioitinh(obj.getString("doituong"));
                            pt.setLoaitintuc(obj.getInt("loaitintuc"));
                            pt.setLat(obj.getDouble("lat"));
                            pt.setLng(obj.getDouble("lng"));
                            dsP.add(pt);
                        }
                    }

                }
            } catch (JSONException e) {
                return dsP;
            }
        }
        return dsP;
    }

    public PhongTros thongTinPhong(String id, int idusers) {
        PhongTros a = new PhongTros();
        String URL_NEW = variable.getWebservice() + "layThongTinChiTiet.php";
        //String URL_NEW = "http://192.168.1.23:8080/firebase/layThongTinChiTiet.php";
        // Tạo danh sách tham số gửi đến máy chủ
        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("idusers", String.valueOf(idusers)));

        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);
        Log.d("JSONNNNNNNNNNNNNNNNNN", json);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");

                if (success == 1) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("phong");
                    a.setUsername(jsonObject1.getString("username"));
                    a.setHoten(jsonObject1.getString("hoten"));
                    a.setSdt(jsonObject1.getString("sodt"));
                    a.setFacebook(jsonObject1.getString("facebook"));
                    a.setId(jsonObject1.getString("id"));
                    a.setTieude(jsonObject1.getString("tieude"));
                    a.setGia(jsonObject1.getInt("gia"));
                    a.setDiachi(jsonObject1.getString("diachi"));
                    a.setDientich(jsonObject1.getInt("dientich"));
                    a.setChieudai(jsonObject1.getInt("chieudai"));
                    a.setChieurong(jsonObject1.getInt("chieurong"));
                    a.setLoaitin(jsonObject1.getInt("loaitintuc"));
                    a.setSonguoimin(jsonObject1.getInt("songuoimin"));
                    a.setSonguoimax(jsonObject1.getInt("songuoimax"));
                    a.setTiennghi(jsonObject1.getString("tiennghi"));
                    a.setDoituong(jsonObject1.getInt("doituong"));
                    a.setMotathem(jsonObject1.getString("motathem"));
                    a.setGiadien(jsonObject1.getInt("giadien"));
                    a.setDonvidien(jsonObject1.getString("donvidien"));
                    a.setGianuoc(jsonObject1.getInt("gianuoc"));
                    a.setDonvinuoc(jsonObject1.getString("donvinuoc"));
                    a.setTiencoc(jsonObject1.getInt("tiencoc"));
                    a.setDonvicoc(jsonObject1.getString("donvicoc"));
                    a.setGiogiac(jsonObject1.getString("giogiac"));
                    a.setTentp(jsonObject1.getString("tentp"));
                    a.setTenqh(jsonObject1.getString("tenquanhuyen"));
                    a.setIduser(jsonObject1.getInt("iduser"));
                    a.setIdtp(jsonObject1.getInt("idtp"));
                    a.setIdqh(jsonObject1.getInt("idquanhuyen"));
                    a.setConphong(jsonObject1.getInt("conphong"));
                    a.setDaluu(jsonObject1.getInt("daluu"));
                    return a;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    public Marker_TabBanDo layViTriTrenBanDo(String id) {
        Marker_TabBanDo a = new Marker_TabBanDo();
        String URL_NEW = variable.getWebservice() + "layViTriTrenBanDo.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();
        args.add(new BasicNameValuePair("id", id));
        MyService jsonParser = new MyService();

        String json = jsonParser.callService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("vitri");
                    a.setDiachi(jsonObject1.getString("diachi") + "," + jsonObject1.getString("tenquanhuyen") + "," + jsonObject1.getString("thanhpho"));
                    a.setLat(jsonObject1.getDouble("lat"));
                    a.setLng(jsonObject1.getDouble("lng"));
                    a.setTieude(jsonObject1.getString("tieude"));
                    return a;
                } else {
                    return null;
                }
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    public int capNhatThongTinChiTiet(PhongTros a) {
        String URL_NEW = variable.getWebservice() + "capNhatThongTinPhong.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", a.getId()));
        args.add(new BasicNameValuePair("tieude", a.getTieude()));
        args.add(new BasicNameValuePair("loaitin", String.valueOf(a.getLoaitin())));
        args.add(new BasicNameValuePair("chieudai", String.valueOf(a.getChieudai())));
        args.add(new BasicNameValuePair("chieurong", String.valueOf(a.getChieurong())));
        args.add(new BasicNameValuePair("tiencoc", String.valueOf(a.getTiencoc())));
        args.add(new BasicNameValuePair("donvicoc", "VNĐ/Phòng"));
        args.add(new BasicNameValuePair("gia", String.valueOf(a.getGia())));
        args.add(new BasicNameValuePair("doituong", String.valueOf(a.getDoituong())));
        args.add(new BasicNameValuePair("tiendien", String.valueOf(a.getGiadien())));
        args.add(new BasicNameValuePair("donvidien", a.getDonvidien()));
        args.add(new BasicNameValuePair("tiennuoc", String.valueOf(a.getGianuoc())));
        args.add(new BasicNameValuePair("donvinuoc", a.getDonvinuoc()));
        args.add(new BasicNameValuePair("giogiac", a.getGiogiac()));
        args.add(new BasicNameValuePair("songuoimin", String.valueOf(a.getSonguoimin())));
        args.add(new BasicNameValuePair("songuoimax", String.valueOf(a.getSonguoimax())));


        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }


    public int capNhatTienNghi(String id, String tiennghi) {
        String URL_NEW = variable.getWebservice() + "capNhatTienNghiPhong.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("tiennghi", tiennghi));


        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int capNhatMoTaThem(String id, String motathem) {
        String URL_NEW = variable.getWebservice() + "capNhatMoTaThem.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("motathem", motathem));


        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int capNhatLienLac(String id, String hoten, String sdt, String facebook, int idtp, int idquanhuyen, String diachi) {
        String URL_NEW = variable.getWebservice() + "capNhatLienLac.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("hoten", hoten));
        args.add(new BasicNameValuePair("sdt", sdt));
        args.add(new BasicNameValuePair("facebook", facebook));
        args.add(new BasicNameValuePair("idtp", String.valueOf(idtp)));
        args.add(new BasicNameValuePair("idquanhuyen", String.valueOf(idquanhuyen)));
        args.add(new BasicNameValuePair("diachi", diachi));


        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int Report(String id, String nd, int iduser) {
        String URL_NEW = variable.getWebservice() + "report.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("nd", nd));
        args.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));

        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int SetConPhong(String id, int conphong) {
        String URL_NEW = variable.getWebservice() + "setConPhong.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("id", id));
        args.add(new BasicNameValuePair("conphong", String.valueOf(conphong)));
        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int Luu_BoLuu(String id, int iduser, int luu) {
        String URL_NEW = variable.getWebservice() + "Luu_HuyLuu.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idphong", id));
        args.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));
        args.add(new BasicNameValuePair("luu", String.valueOf(luu)));
        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int isLuu(String idphong, int iduser) {
        String URL_NEW = variable.getWebservice() + "isLuu.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idphong", idphong));
        args.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));

        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        Log.d("SSSSS", json);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int isOwner(String idphong, int iduser) {
        String URL_NEW = variable.getWebservice() + "isOwner.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idphong", idphong));
        args.add(new BasicNameValuePair("iduser", String.valueOf(iduser)));

        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        //Log.d("SSSSS", json);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }

    public int upDateImage(String idphong, ArrayList<String> addnewImg, ArrayList<String> deleteImg) {

        String URL_NEW = variable.getWebservice() + "upDateImage.php";

        List<NameValuePair> args = new ArrayList<NameValuePair>();

        args.add(new BasicNameValuePair("idphong", idphong));
        JSONArray images = new JSONArray();
        for (int i = 0; i < addnewImg.size(); i++) {
            images.put(addnewImg.get(i));
        }
        args.add(new BasicNameValuePair("addnewImg", images.toString()));

        JSONArray images2 = new JSONArray();
        for (int i = 0; i < deleteImg.size(); i++) {
            images2.put(deleteImg.get(i));
        }
        args.add(new BasicNameValuePair("deleteImg", images2.toString()));


        MyService jsonParser = new MyService();

        String json = jsonParser.makeService(URL_NEW, MyService.POST, args);
        if (json != null) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                int success = jsonObject.getInt("kq");
                if (success == 1) {
                    return 1;
                } else {
                    return 0;
                }
            } catch (JSONException e) {
                return 0;
            }
        }
        return 0;
    }
}
