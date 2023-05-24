package com.example.draudiminiai_ivykiai.dataStructure;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class CrashEvent {
    String id;
    String vartotojoId;
    String vartotojoAutoId;
    String laikas;
    String data;
    Uri nuotrauka;
    String adresas;
    ArrayList<String> dalys;

    public CrashEvent() {
    }

    public CrashEvent(Uri nuotrauka) {
        this.nuotrauka = nuotrauka;
    }

    public CrashEvent(String vartotojoId, String vartotojoAutoId, String laikas, String data, Uri nuotrauka, String adresas, ArrayList<String> dalys) {
        this.vartotojoId = vartotojoId;
        this.vartotojoAutoId = vartotojoAutoId;
        this.laikas = laikas;
        this.data = data;
        this.nuotrauka = nuotrauka;
        this.adresas = adresas;
        this.dalys = dalys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVartotojoId() {
        return vartotojoId;
    }

    public void setVartotojoId(String vartotojoId) {
        this.vartotojoId = vartotojoId;
    }

    public String getVartotojoAutoId() {
        return vartotojoAutoId;
    }

    public void setVartotojoAutoId(String vartotojoAutoId) {
        this.vartotojoAutoId = vartotojoAutoId;
    }

    public String getLaikas() {
        return laikas;
    }

    public void setLaikas(String laikas) {
        this.laikas = laikas;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Uri getNuotrauka() {
        return nuotrauka;
    }

    public void setNuotrauka(Uri nuotrauka) {
        this.nuotrauka = nuotrauka;
    }

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
    }

    public ArrayList<String> getDalys() {
        return dalys;
    }

    public void setDalys(ArrayList<String> dalys) {
        this.dalys = dalys;
    }
}
