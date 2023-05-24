package com.example.draudiminiai_ivykiai.dataStructure;

public class CarParts {
    String id;
    String autoId;
    String pavadinimas;
    String lokacija;
    Integer kaina;
    Integer tvarkymo_kaina;
    Integer dazymo_kaina;

    public CarParts() {
    }

    public CarParts(String autoId, String pavadinimas, String lokacija, Integer kaina, Integer tvarkymo_kaina, Integer dazymo_kaina) {
        this.autoId = autoId;
        this.pavadinimas = pavadinimas;
        this.lokacija = lokacija;
        this.kaina = kaina;
        this.tvarkymo_kaina = tvarkymo_kaina;
        this.dazymo_kaina = dazymo_kaina;
    }

    public CarParts(String id, String autoId, String pavadinimas, String lokacija, Integer kaina, Integer tvarkymo_kaina, Integer dazymo_kaina) {
        this.id = id;
        this.autoId = autoId;
        this.pavadinimas = pavadinimas;
        this.lokacija = lokacija;
        this.kaina = kaina;
        this.tvarkymo_kaina = tvarkymo_kaina;
        this.dazymo_kaina = dazymo_kaina;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getLokacija() {
        return lokacija;
    }

    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }

    public Integer getKaina() {
        return kaina;
    }

    public void setKaina(Integer kaina) {
        this.kaina = kaina;
    }

    public Integer getTvarkymo_kaina() {
        return tvarkymo_kaina;
    }

    public void setTvarkymo_kaina(Integer tvarkymo_kaina) {
        this.tvarkymo_kaina = tvarkymo_kaina;
    }

    public Integer getDazymo_kaina() {
        return dazymo_kaina;
    }

    public void setDazymo_kaina(Integer dazymo_kaina) {
        this.dazymo_kaina = dazymo_kaina;
    }
}
