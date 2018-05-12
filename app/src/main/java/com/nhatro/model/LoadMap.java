package com.nhatro.model;

import com.nhatro.adapter.CardMapViewAdapter;

import java.util.ArrayList;

/**
 * Created by CongHoang on 5/5/2018.
 */

public class LoadMap {
    LocDL locDL;
    CardMapViewAdapter cardMapViewAdapter;
    ArrayList<PhongTro> phongTros;

    public LoadMap(){
        phongTros = new ArrayList<>();
    }
    public ArrayList<PhongTro> getPhongTros() {
        return phongTros;
    }

    public void setPhongTros(ArrayList<PhongTro> phongTros) {
        this.phongTros = phongTros;
    }

    public LocDL getLocDL() {
        return locDL;
    }

    public void setLocDL(LocDL locDL) {
        this.locDL = locDL;
    }

    public CardMapViewAdapter getCardMapViewAdapter() {
        return cardMapViewAdapter;
    }

    public void setCardMapViewAdapter(CardMapViewAdapter cardMapViewAdapter) {
        this.cardMapViewAdapter = cardMapViewAdapter;
    }
}
