package com.nhatro.model;

import java.io.Serializable;

/**
 * Created by CongHoang on 6/7/2018.
 */

public class EditImg implements Serializable {
    String path;
    boolean islocal;

    public EditImg(String path) {
        this.path = path;
        this.islocal = false;
    }

    public EditImg(String path, boolean islocal) {
        this.path = path;
        this.islocal = islocal;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isIslocal() {
        return islocal;
    }

    public void setIslocal(boolean islocal) {
        this.islocal = islocal;
    }
}
