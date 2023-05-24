package com.example.draudiminiai_ivykiai.dataStructure;

import java.util.ArrayList;

public class Reports {
    String vartotojoId;
    String ivykioId;
    String zalosId;
    String savininkas;
    String automobilis;
    String valstybinis_numeris;
    String adresas;
    String laikas;
    String data;
    ArrayList<String> dalys;
    Integer daliu_kaina;
    Integer keitimo_kaina;
    Integer dazymo_kaina;
    double isviso_kaina;
    String nuotrauka;
    String registravimo_data;

    public Reports() {
    }

    public Reports(String vartotojoId, String ivykioId, String zalosId, String savininkas, String automobilis, String valstybinis_numeris, String adresas, String laikas, String data, ArrayList<String> dalys, Integer daliu_kaina, Integer keitimo_kaina, Integer dazymo_kaina, double isviso_kaina, String nuotrauka, String registravimo_data) {
        this.vartotojoId = vartotojoId;
        this.ivykioId = ivykioId;
        this.zalosId = zalosId;
        this.savininkas = savininkas;
        this.automobilis = automobilis;
        this.valstybinis_numeris = valstybinis_numeris;
        this.adresas = adresas;
        this.laikas = laikas;
        this.data = data;
        this.dalys = dalys;
        this.daliu_kaina = daliu_kaina;
        this.keitimo_kaina = keitimo_kaina;
        this.dazymo_kaina = dazymo_kaina;
        this.isviso_kaina = isviso_kaina;
        this.nuotrauka = nuotrauka;
        this.registravimo_data = registravimo_data;
    }

    public String getVartotojoId() {
        return vartotojoId;
    }

    public void setVartotojoId(String vartotojoId) {
        this.vartotojoId = vartotojoId;
    }

    public String getIvykioId() {
        return ivykioId;
    }

    public void setIvykioId(String ivykioId) {
        this.ivykioId = ivykioId;
    }

    public String getZalosId() {
        return zalosId;
    }

    public void setZalosId(String zalosId) {
        this.zalosId = zalosId;
    }

    public String getSavininkas() {
        return savininkas;
    }

    public void setSavininkas(String savininkas) {
        this.savininkas = savininkas;
    }

    public String getAutomobilis() {
        return automobilis;
    }

    public void setAutomobilis(String automobilis) {
        this.automobilis = automobilis;
    }

    public String getValstybinis_numeris() {
        return valstybinis_numeris;
    }

    public void setValstybinis_numeris(String valstybinis_numeris) {
        this.valstybinis_numeris = valstybinis_numeris;
    }

    public String getAdresas() {
        return adresas;
    }

    public void setAdresas(String adresas) {
        this.adresas = adresas;
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

    public ArrayList<String> getDalys() {
        return dalys;
    }

    public void setDalys(ArrayList<String> dalys) {
        this.dalys = dalys;
    }

    public Integer getDaliu_kaina() {
        return daliu_kaina;
    }

    public void setDaliu_kaina(Integer daliu_kaina) {
        this.daliu_kaina = daliu_kaina;
    }

    public Integer getKeitimo_kaina() {
        return keitimo_kaina;
    }

    public void setKeitimo_kaina(Integer keitimo_kaina) {
        this.keitimo_kaina = keitimo_kaina;
    }

    public Integer getDazymo_kaina() {
        return dazymo_kaina;
    }

    public void setDazymo_kaina(Integer dazymo_kaina) {
        this.dazymo_kaina = dazymo_kaina;
    }

    public double getIsviso_kaina() {
        return isviso_kaina;
    }

    public void setIsviso_kaina(double isviso_kaina) {
        this.isviso_kaina = isviso_kaina;
    }

    public String getNuotrauka() {
        return nuotrauka;
    }

    public void setNuotrauka(String nuotrauka) {
        this.nuotrauka = nuotrauka;
    }

    public String getRegistravimo_data() {
        return registravimo_data;
    }

    public void setRegistravimo_data(String registravimo_data) {
        this.registravimo_data = registravimo_data;
    }
}
