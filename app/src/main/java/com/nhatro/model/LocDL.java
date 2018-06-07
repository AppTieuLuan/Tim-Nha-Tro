package com.nhatro.model;

/**
 * Created by CongHoang on 4/26/2018.
 */

public class LocDL {
    int orderby;

    public int getOrderby() {
        return orderby;
    }

    public void setOrderby(int orderby) {
        this.orderby = orderby;
    }
    int iduser;

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    int idtp;
    String idqh;
    int giamin;
    int giamax;
    int dientichmin;
    int dientichmax;
    int songuoio;
    String loaitin;
    String tiennghi;
    int doituong;
    int giogiac;

    int danhsach;
    double lat;
    double lng;
    int bankinh;
    int trang;

    public int getTrang() {
        return trang;
    }

    public void setTrang(int trang) {
        this.trang = trang;
    }

    public LocDL(){
        this.orderby = 1;
        this.danhsach = 1;
        this.bankinh = 2;
        this.idtp = 50;
        this.idqh = "";
        this.giamin = 0;
        this.giamax = 10000000;
        this.dientichmin = 0;
        this.dientichmax = 200;
        this.songuoio = -1;
        this.loaitin = "";
        this.tiennghi = "";
        this.doituong = 3;
        this.giogiac = 0;
        this.trang = 1;
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

    public int getDientichmin() {
        return dientichmin;
    }

    public void setDientichmin(int dientichmin) {
        this.dientichmin = dientichmin;
    }

    public int getDientichmax() {
        return dientichmax;
    }

    public void setDientichmax(int dientichmax) {
        this.dientichmax = dientichmax;
    }

    public int getSonguoio() {
        return songuoio;
    }

    public void setSonguoio(int songuoio) {
        this.songuoio = songuoio;
    }

    public String getLoaitin() {
        return loaitin;
    }

    public void setLoaitin(String loaitin) {
        this.loaitin = loaitin;
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

    public int getGiogiac() {
        return giogiac;
    }

    public void setGiogiac(int giogiac) {
        this.giogiac = giogiac;
    }

    public int isDanhsach() {
        return danhsach;
    }

    public void setDanhsach(int danhsach) {
        this.danhsach = danhsach;
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
}
