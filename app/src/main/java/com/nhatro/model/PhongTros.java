package com.nhatro.model;

/**
 * Created by CongHoang on 4/24/2018.
 */

public class PhongTros {
    private String id;
    private String tieude;
    private int gia;
    private String diachi;
    private float dientich;
    private float chieudai;
    private float chieurong;
    private int loaitin;
    private int songuoimin;
    private int songuoimax;
    private String tiennghi;
    private int doituong;
    private double lat;
    private double lng;
    private String ngay;
    private int iduser;
    private String motathem;
    private int giadien;
    private String donvidien;
    private int gianuoc;
    private String donvinuoc;
    private int daluu;

    public int getDaluu() {
        return daluu;
    }

    public void setDaluu(int daluu) {
        this.daluu = daluu;
    }

    int tiencoc;
    String donvicoc;
    String giogiac;
    int idtp;
    int idqh;
    int conphong;

    public int getConphong() {
        return conphong;
    }

    public void setConphong(int conphong) {
        this.conphong = conphong;
    }

    private String username;
    private String hoten;
    private String sdt;
    private String tentp;
    private String tenqh;
    private String facebook;

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTentp() {
        return tentp;
    }

    public void setTentp(String tentp) {
        this.tentp = tentp;
    }

    public String getTenqh() {
        return tenqh;
    }

    public void setTenqh(String tenqh) {
        this.tenqh = tenqh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public PhongTros() {
    }

    public PhongTros(String id, String tieude, int gia, String diachi, float dientich, float chieudai, float chieurong, int loaitin, int songuoimin, int songuoimax, String tiennghi, int doituong, double lat, double lng, String ngay, int iduser, String motathem, int giadien, String donvidien, int gianuoc, String donvinuoc, int tiencoc, String donvicoc, String giogiac, int idtp, int idqh, int soreport) {

        this.id = id;
        this.tieude = tieude;
        this.gia = gia;
        this.diachi = diachi;
        this.dientich = dientich;
        this.chieudai = chieudai;
        this.chieurong = chieurong;
        this.loaitin = loaitin;
        this.songuoimin = songuoimin;
        this.songuoimax = songuoimax;
        this.tiennghi = tiennghi;
        this.doituong = doituong;
        this.lat = lat;
        this.lng = lng;
        this.ngay = ngay;
        this.iduser = iduser;
        this.motathem = motathem;
        this.giadien = giadien;
        this.donvidien = donvidien;
        this.gianuoc = gianuoc;
        this.donvinuoc = donvinuoc;
        this.tiencoc = tiencoc;
        this.donvicoc = donvicoc;
        this.giogiac = giogiac;
        this.idtp = idtp;
        this.idqh = idqh;
        this.soreport = soreport;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public float getDientich() {
        return dientich;
    }

    public void setDientich(float dientich) {
        this.dientich = dientich;
    }

    public float getChieudai() {
        return chieudai;
    }

    public void setChieudai(float chieudai) {
        this.chieudai = chieudai;
    }

    public float getChieurong() {
        return chieurong;
    }

    public void setChieurong(float chieurong) {
        this.chieurong = chieurong;
    }

    public int getLoaitin() {
        return loaitin;
    }

    public void setLoaitin(int loaitin) {
        this.loaitin = loaitin;
    }

    public int getSonguoimin() {
        return songuoimin;
    }

    public void setSonguoimin(int songuoimin) {
        this.songuoimin = songuoimin;
    }

    public int getSonguoimax() {
        return songuoimax;
    }

    public void setSonguoimax(int songuoimax) {
        this.songuoimax = songuoimax;
    }

    public String getTiennghi() {
        return tiennghi;
    }

    public void setTiennghi(String tiennghi) {
        this.tiennghi = tiennghi;
    }

    public int getDoituong() {
        return doituong;
    }

    public void setDoituong(int doituong) {
        this.doituong = doituong;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getMotathem() {
        return motathem;
    }

    public void setMotathem(String motathem) {
        this.motathem = motathem;
    }

    public int getGiadien() {
        return giadien;
    }

    public void setGiadien(int giadien) {
        this.giadien = giadien;
    }

    public String getDonvidien() {
        return donvidien;
    }

    public void setDonvidien(String donvidien) {
        this.donvidien = donvidien;
    }

    public int getGianuoc() {
        return gianuoc;
    }

    public void setGianuoc(int gianuoc) {
        this.gianuoc = gianuoc;
    }

    public String getDonvinuoc() {
        return donvinuoc;
    }

    public void setDonvinuoc(String donvinuoc) {
        this.donvinuoc = donvinuoc;
    }

    public int getTiencoc() {
        return tiencoc;
    }

    public void setTiencoc(int tiencoc) {
        this.tiencoc = tiencoc;
    }

    public String getDonvicoc() {
        return donvicoc;
    }

    public void setDonvicoc(String donvicoc) {
        this.donvicoc = donvicoc;
    }

    public String getGiogiac() {
        return giogiac;
    }

    public void setGiogiac(String giogiac) {
        this.giogiac = giogiac;
    }

    public int getIdtp() {
        return idtp;
    }

    public void setIdtp(int idtp) {
        this.idtp = idtp;
    }

    public int getIdqh() {
        return idqh;
    }

    public void setIdqh(int idqh) {
        this.idqh = idqh;
    }

    public int getSoreport() {
        return soreport;
    }

    public void setSoreport(int soreport) {
        this.soreport = soreport;
    }

    private int soreport;
}
