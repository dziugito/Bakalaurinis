package com.example.draudiminiai_ivykiai.dataStructure;

public class UserCar {
    String userAutoId;
    String autoId;
    String userId;
    String vin;
    String valstybinis_numeris;
    Integer kilometrazas;
    String bukle;

    public UserCar(String autoId, String userId, String vin, String valstybinis_numeris, Integer kilometrazas, String bukle) {
        this.autoId = autoId;
        this.userId = userId;
        this.vin = vin;
        this.valstybinis_numeris = valstybinis_numeris;
        this.kilometrazas = kilometrazas;
        this.bukle = bukle;
    }

    public UserCar() {
    }

    public String getUserAutoId() {
        return userAutoId;
    }

    public void setUserAutoId(String userAutoId) {
        this.userAutoId = userAutoId;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getValstybinis_numeris() {
        return valstybinis_numeris;
    }

    public void setValstybinis_numeris(String valstybinis_numeris) {
        this.valstybinis_numeris = valstybinis_numeris;
    }

    public Integer getKilometrazas() {
        return kilometrazas;
    }

    public void setKilometrazas(Integer kilometrazas) {
        this.kilometrazas = kilometrazas;
    }

    public String getBukle() {
        return bukle;
    }

    public void setBukle(String bukle) {
        this.bukle = bukle;
    }

    public String toString(){
        return "Vin: " + this.vin +"\nValstybinis nr.: " + valstybinis_numeris + "\nRida: " + kilometrazas + "\nBūklė: " + bukle;
    }
}
