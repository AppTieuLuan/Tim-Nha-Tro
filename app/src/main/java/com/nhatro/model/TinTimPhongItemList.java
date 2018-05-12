package com.nhatro.model;

/**
 * Created by CongHoang on 4/14/2018.
 */

public class TinTimPhongItemList {
    private int id;
    private String tieude;
    private int loai;
    private int gioitinh;
    private String quanhuyen;
    private String songuoi;
    private String giaTu;
    private String ngaydang;

    public String getNgaydang() {
        return ngaydang;
    }

    public void setNgaydang(String ngaydang) {
        this.ngaydang = ngaydang;
    }

    public TinTimPhongItemList(int id, String tieude, int loai, int gioitinh, String quanhuyen, String songuoi, String giaTu, String ngaydang) {
        this.id = id;
        this.tieude = tieude;
        this.loai = loai;
        this.gioitinh = gioitinh;
        this.quanhuyen = quanhuyen;
        this.songuoi = songuoi;
        this.giaTu = giaTu;
        this.ngaydang = ngaydang;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }

    public String getQuanhuyen() {
        return quanhuyen;
    }

    public void setQuanhuyen(String quanhuyen) {
        this.quanhuyen = quanhuyen;
    }

    public String getSonguoi() {
        return songuoi;
    }

    public void setSonguoi(String songuoi) {
        this.songuoi = songuoi;
    }

    public String getGiaTu() {
        return giaTu;
    }

    public void setGiaTu(String giaTu) {
        this.giaTu = giaTu;
    }
}
