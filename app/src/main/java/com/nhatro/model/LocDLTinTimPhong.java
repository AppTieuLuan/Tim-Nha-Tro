package com.nhatro.model;

/**
 * Created by CongHoang on 5/13/2018.
 */

public class LocDLTinTimPhong {

    private int idtp;
    private int idqh;
    private int gia;
    private String loaitin;
    private int giogiac;
    private String tiennghi;
    private int doituong;
    private int orderby;
    private int trang;

    public LocDLTinTimPhong(){
        this.idtp = 50;
        this.idqh = -1;
        this.gia = 0;
        this.loaitin = "1,2,3";
        this.giogiac = 0;
        this.tiennghi = "";
        this.doituong = 3;
        this.orderby = 1;
        this.trang = 1;
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

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getLoaitin() {
        return loaitin;
    }

    public void setLoaitin(String loaitin) {
        this.loaitin = loaitin;
    }

    public int getGiogiac() {
        return giogiac;
    }

    public void setGiogiac(int giogiac) {
        this.giogiac = giogiac;
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

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }

    public int getTrang() {
        return trang;
    }

    public void setTrang(int trang) {
        this.trang = trang;
    }
}
