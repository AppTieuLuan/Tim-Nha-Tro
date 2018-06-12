package com.nhatro.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyManagerRoom {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("daluu")
    @Expose
    private String daluu;
    @SerializedName("loaitintuc")
    @Expose
    private String loaitintuc;
    @SerializedName("tieude")
    @Expose
    private String tieude;
    @SerializedName("gia")
    @Expose
    private String gia;
    @SerializedName("diachi")
    @Expose
    private String diachi;
    @SerializedName("dientich")
    @Expose
    private String dientich;
    @SerializedName("chieudai")
    @Expose
    private String chieudai;
    @SerializedName("chieurong")
    @Expose
    private String chieurong;
    @SerializedName("doituong")
    @Expose
    private String doituong;
    @SerializedName("hinhanh")
    @Expose
    private String hinhanh;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDaluu() {
        return daluu;
    }

    public void setDaluu(String daluu) {
        this.daluu = daluu;
    }

    public String getLoaitintuc() {
        return loaitintuc;
    }

    public void setLoaitintuc(String loaitintuc) {
        this.loaitintuc = loaitintuc;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getDientich() {
        return dientich;
    }

    public void setDientich(String dientich) {
        this.dientich = dientich;
    }

    public String getChieudai() {
        return chieudai;
    }

    public void setChieudai(String chieudai) {
        this.chieudai = chieudai;
    }

    public String getChieurong() {
        return chieurong;
    }

    public void setChieurong(String chieurong) {
        this.chieurong = chieurong;
    }

    public String getDoituong() {
        return doituong;
    }

    public void setDoituong(String doituong) {
        this.doituong = doituong;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

}