package com.nhatro.model;

/**
 * Created by CongHoang on 3/30/2018.
 */

public class BinhLuan {
    private String id;
    private String avatarUser;
    private String userName;
    private String tenUser;
    private String noiDungBl;
    private String ngayViet;
    private String idPhong;
    private int idUser;

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getIdPhong() {
        return idPhong;
    }

    public void setIdPhong(String idPhong) {
        this.idPhong = idPhong;
    }

    public BinhLuan(){

    }
    public BinhLuan(String id, String avatarUser, String userName, String tenUser, String noiDungBl, String ngayViet) {
        this.id = id;
        this.avatarUser = avatarUser;
        this.userName = userName;
        this.tenUser = tenUser;
        this.noiDungBl = noiDungBl;
        this.ngayViet = ngayViet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenUser() {
        return tenUser;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
    }

    public String getNoiDungBl() {
        return noiDungBl;
    }

    public void setNoiDungBl(String noiDungBl) {
        this.noiDungBl = noiDungBl;
    }

    public String getNgayViet() {
        return ngayViet;
    }

    public void setNgayViet(String ngayViet) {
        this.ngayViet = ngayViet;
    }
}
