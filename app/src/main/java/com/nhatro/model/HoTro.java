package com.nhatro.model;

/**
 * Created by CongHoang on 4/22/2018.
 */

public class HoTro {
    private int id;
    private String emal;
    private String noidung;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmal() {
        return emal;
    }

    public void setEmal(String emal) {
        this.emal = emal;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public HoTro(int id, String emal, String noidung) {

        this.id = id;
        this.emal = emal;
        this.noidung = noidung;
    }
}
