package com.example.draudiminiai_ivykiai.dataStructure;

import java.util.ArrayList;

public class CarDamage {
    String id;
    String ivykioId;
    ArrayList<String> apgadintos_dalys;
    Integer daliu_kaina;
    Integer remonto_kaina;
    Integer dazymo_kaina;
    double isskaiciavimo_procentas;
    double isviso_suma;

    public CarDamage() {
    }

    public CarDamage(String ivykioId, ArrayList<String> apgadintos_dalys, Integer daliu_kaina, Integer remonto_kaina, Integer dazymo_kaina, double isskaiciavimo_procentas, double isviso_suma) {
        this.ivykioId = ivykioId;
        this.apgadintos_dalys = apgadintos_dalys;
        this.daliu_kaina = daliu_kaina;
        this.remonto_kaina = remonto_kaina;
        this.dazymo_kaina = dazymo_kaina;
        this.isskaiciavimo_procentas = isskaiciavimo_procentas;
        this.isviso_suma = isviso_suma;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIvykioId() {
        return ivykioId;
    }

    public void setIvykioId(String ivykioId) {
        this.ivykioId = ivykioId;
    }

    public ArrayList<String> getApgadintos_dalys() {
        return apgadintos_dalys;
    }

    public void setApgadintos_dalys(ArrayList<String> apgadintos_dalys) {
        this.apgadintos_dalys = apgadintos_dalys;
    }

    public Integer getDaliu_kaina() {
        return daliu_kaina;
    }

    public void setDaliu_kaina(Integer daliu_kaina) {
        this.daliu_kaina = daliu_kaina;
    }

    public Integer getRemonto_kaina() {
        return remonto_kaina;
    }

    public void setRemonto_kaina(Integer remonto_kaina) {
        this.remonto_kaina = remonto_kaina;
    }

    public Integer getDazymo_kaina() {
        return dazymo_kaina;
    }

    public void setDazymo_kaina(Integer dazymo_kaina) {
        this.dazymo_kaina = dazymo_kaina;
    }

    public double getIsskaiciavimo_procentas() {
        return isskaiciavimo_procentas;
    }

    public void setIsskaiciavimo_procentas(double isskaiciavimo_procentas) {
        this.isskaiciavimo_procentas = isskaiciavimo_procentas;
    }

    public double getIsviso_suma() {
        return isviso_suma;
    }

    public void setIsviso_suma(double isviso_suma) {
        this.isviso_suma = isviso_suma;
    }
}
