package com.example.draudiminiai_ivykiai.dataStructure;

import com.google.firebase.database.Exclude;

public class Car {

    String autoId;
    String marke;
    String modelis;
    String metai;
    String kuro_tipas;
    String pavaru_deze;
    String kebulo_tipas;
    Integer rinkos_kaina;

    public Car() {
    }

    public Car(String marke, String modelis, String metai, String kuro_tipas, String pavaru_deze, String kebulo_tipas, Integer rinkos_kaina) {
        this.marke = marke;
        this.modelis = modelis;
        this.metai = metai;
        this.kuro_tipas = kuro_tipas;
        this.pavaru_deze = pavaru_deze;
        this.kebulo_tipas = kebulo_tipas;
        this.rinkos_kaina = rinkos_kaina;
    }

    @Exclude
    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }


    public String getMetai() {
        return metai;
    }

    public void setMetai(String metai) {
        this.metai = metai;
    }

    public String getKuro_tipas() {
        return kuro_tipas;
    }

    public void setKuro_tipas(String kuro_tipas) {
        this.kuro_tipas = kuro_tipas;
    }

    public String getPavaru_deze() {
        return pavaru_deze;
    }

    public void setPavaru_deze(String pavaru_deze) {
        this.pavaru_deze = pavaru_deze;
    }

    public String getKebulo_tipas() {
        return kebulo_tipas;
    }

    public void setKebulo_tipas(String kebulo_tipas) {
        this.kebulo_tipas = kebulo_tipas;
    }

    public String getMarke() {
        return marke;
    }

    public void setMarke(String marke) {
        this.marke = marke;
    }

    public String getModelis() {
        return modelis;
    }

    public void setModelis(String modelis) {
        this.modelis = modelis;
    }

    public Integer getRinkos_kaina() {
        return rinkos_kaina;
    }

    public void setRinkos_kaina(Integer rinkos_kaina) {
        this.rinkos_kaina = rinkos_kaina;
    }
}
