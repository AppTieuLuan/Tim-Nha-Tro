package com.nhatro.model;

/**
 * Created by CongHoang on 2/11/2018.
 */

public class PhongTro {
    private String id;
    private String hinhanh;

    public PhongTro(){

    }
    public PhongTro(String id, String hinhanh, String tieude, String diachi, float gia, float dientich, float chieudai, float chieurong, String gioitinh, boolean daluu) {
        this.id = id;
        this.hinhanh = hinhanh;
        this.tieude = tieude;
        this.diachi = diachi;
        this.gia = gia;
        this.dientich = dientich;
        this.chieudai = chieudai;
        this.chieurong = chieurong;
        this.gioitinh = gioitinh;
        this.daluu = daluu;
    }

    public String getHinhanh() {

        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    private String tieude;
    private String diachi;
    private float gia;
    private float dientich;
    private float chieudai;
    private float chieurong;
    private String gioitinh;
    private boolean daluu;
    private int loaitintuc;
    private double lat;
    private double lng;

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

    public int getLoaitintuc() {
        return loaitintuc;
    }

    public void setLoaitintuc(int loaitintuc) {
        this.loaitintuc = loaitintuc;
    }

    public PhongTro(String id, String tieude, String diachi, float gia, float dientich, float chieudai, float chieurong, String gioitinh, boolean daluu) {
        this.id = id;
        this.tieude = tieude;
        this.diachi = diachi;
        this.gia = gia;
        this.dientich = dientich;
        this.chieudai = chieudai;
        this.chieurong = chieurong;
        this.gioitinh = gioitinh;
        this.daluu = daluu;
    }

    public boolean isDaluu() {

        return daluu;
    }

    public void setDaluu(boolean daluu) {
        this.daluu = daluu;
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

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public float getGia() {
        return gia;
    }



    public void setGia(float gia) {

        this.gia = gia;
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

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public PhongTro(String id, String tieude, String diachi, float gia, float dientich, float chieudai, float chieurong, String gioitinh) {
        this.id = id;
        this.tieude = tieude;
        this.diachi = diachi;
        this.gia = gia;
        this.dientich = dientich;
        this.chieudai = chieudai;
        this.chieurong = chieurong;
        this.gioitinh = gioitinh;
        this.daluu = false;
    }
}
