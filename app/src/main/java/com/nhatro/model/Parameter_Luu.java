package com.nhatro.model;

/**
 * Created by CongHoang on 6/6/2018.
 */

public class Parameter_Luu {
    String idphong;
    int iduser;
    int luu;

    public Parameter_Luu() {
    }

    public Parameter_Luu(String idphong, int iduser, int luu) {

        this.idphong = idphong;
        this.iduser = iduser;
        this.luu = luu;
    }

    public String getIdphong() {

        return idphong;
    }

    public void setIdphong(String idphong) {
        this.idphong = idphong;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getLuu() {
        return luu;
    }

    public void setLuu(int luu) {
        this.luu = luu;
    }
}
