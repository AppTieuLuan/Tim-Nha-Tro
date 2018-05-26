package com.nhatro.model;

import java.io.Serializable;

/**
 * Created by CongHoang on 5/22/2018.
 */

public class ThongBao implements Serializable {
    private int id;
    private int idusernhan;
    private int iduser2;
    private String tenuser2;
    private int loai;
    private String idtintuc;
    private String avataruser2;
    private String ngay;
    private int dadoc;
    private int tinhtrang;

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    private int isnew;
    public ThongBao() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdusernhan() {
        return idusernhan;
    }

    public void setIdusernhan(int iduser) {
        this.idusernhan = iduser;
    }

    public int getIduser2() {
        return iduser2;
    }

    public void setIduser2(int iduser2) {
        this.iduser2 = iduser2;
    }

    public String getTenuser2() {
        return tenuser2;
    }

    public void setTenuser2(String tenuser2) {
        this.tenuser2 = tenuser2;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }

    public String getIdtintuc() {
        return idtintuc;
    }

    public void setIdtintuc(String idtintuc) {
        this.idtintuc = idtintuc;
    }

    public String getAvataruser2() {
        return avataruser2;
    }

    public void setAvataruser2(String avataruser2) {
        this.avataruser2 = avataruser2;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getDadoc() {
        return dadoc;
    }

    public void setDadoc(int dadoc) {
        this.dadoc = dadoc;
    }

    public int getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(int tinhtrang) {
        this.tinhtrang = tinhtrang;
    }
    private String tieudeTin;

    public String getTieudeTin() {
        return tieudeTin;
    }

    public void setTieudeTin(String tieudeTin) {
        this.tieudeTin = tieudeTin;
    }
    private int mngay;
    private int mthang;
    private int mnam;

    private int mgio;
    private int mphut;

    public ThongBao(int id, int idusernhan, int iduser2, String tenuser2, int loai, String idtintuc, String avataruser2, String ngay, int dadoc, int tinhtrang, String tieudeTin, int mngay, int mthang, int mnam, int mgio, int mphut) {
        this.id = id;
        this.idusernhan = idusernhan;
        this.iduser2 = iduser2;
        this.tenuser2 = tenuser2;
        this.loai = loai;
        this.idtintuc = idtintuc;
        this.avataruser2 = avataruser2;
        this.ngay = ngay;
        this.dadoc = dadoc;
        this.tinhtrang = tinhtrang;
        this.tieudeTin = tieudeTin;
        this.mngay = mngay;
        this.mthang = mthang;
        this.mnam = mnam;
        this.mgio = mgio;
        this.mphut = mphut;
    }

    public int getMngay() {

        return mngay;
    }

    public void setMngay(int mngay) {
        this.mngay = mngay;
    }

    public int getMthang() {
        return mthang;
    }

    public void setMthang(int mthang) {
        this.mthang = mthang;
    }

    public int getMnam() {
        return mnam;
    }

    public void setMnam(int mnam) {
        this.mnam = mnam;
    }

    public int getMgio() {
        return mgio;
    }

    public void setMgio(int mgio) {
        this.mgio = mgio;
    }

    public int getMphut() {
        return mphut;
    }

    public void setMphut(int mphut) {
        this.mphut = mphut;
    }
}
