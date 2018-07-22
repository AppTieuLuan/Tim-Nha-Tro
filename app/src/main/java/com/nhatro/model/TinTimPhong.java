package com.nhatro.model;

import java.io.Serializable;

/**
 * Created by CongHoang on 5/10/2018.
 */

public class TinTimPhong implements Serializable {
    private String id;
    private String tieude;
    private String hoten;
    private String sdt;
    private String facebook;
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private int loaitin;
    private int songuoimin;
    private int songuoimax;
    private int giamin;
    private int giamax;
    private int idtp;
    private String idqh;
    private String khuvuc;
    private int giogiac;
    private double lat;
    private double lng;
    private int bankinh;
    private String tiennghi;
    private String motathem;
    private int iduser;
    private int gioitinh;
    private String qh;
    private String tentp;


    public String getTentp() {
        return tentp;
    }

    public void setTentp(String tentp) {
        this.tentp = tentp;
    }

    public String getQh() {
        return qh;
    }

    public void setQh(String qh) {
        this.qh = qh;
    }

    private String ngay;

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getNhanthongbao() {
        return nhanthongbao;
    }

    public void setNhanthongbao(int nhanthongbao) {
        this.nhanthongbao = nhanthongbao;
    }

    private int nhanthongbao;
    public TinTimPhong() {

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

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
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

    public int getGiamin() {
        return giamin;
    }

    public void setGiamin(int giamin) {
        this.giamin = giamin;
    }

    public int getGiamax() {
        return giamax;
    }

    public void setGiamax(int giamax) {
        this.giamax = giamax;
    }

    public int getIdtp() {
        return idtp;
    }

    public void setIdtp(int idtp) {
        this.idtp = idtp;
    }

    public String getIdqh() {
        return idqh;
    }

    public void setIdqh(String idqh) {
        this.idqh = idqh;
    }

    public String getKhuvuc() {
        return khuvuc;
    }

    public void setKhuvuc(String khuvuc) {
        this.khuvuc = khuvuc;
    }

    public int getGiogiac() {
        return giogiac;
    }

    public void setGiogiac(int giogiac) {
        this.giogiac = giogiac;
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

    public int getBankinh() {
        return bankinh;
    }

    public void setBankinh(int bankinh) {
        this.bankinh = bankinh;
    }

    public String getTiennghi() {
        return tiennghi;
    }

    public void setTiennghi(String tiennghi) {
        this.tiennghi = tiennghi;
    }

    public String getMotathem() {
        return motathem;
    }

    public void setMotathem(String motathem) {
        this.motathem = motathem;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }
}
