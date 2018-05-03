package com.nhatro.model;

/**
 * Created by CongHoang on 5/1/2018.
 */

public class Marker_TabBanDo {
    private double lat;
    private double lng;
    String tieude;
    String diachi;

    public Marker_TabBanDo() {
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
}
