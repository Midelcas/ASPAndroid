package com.sn_g2.greenhouse;

public class AmbientModuleLimits {
    private int idRoom;
    private float maxTemp;
    private float minTemp;
    private float maxHum;
    private float minHum;
    private float maxPres;
    private float minPres;

    public AmbientModuleLimits(int aIdRoom, float aMaxTemp, float aMinTemp, float aMaxHum, float aMinHum, float aMaxPres, float aMinPres){
        idRoom=aIdRoom;
        maxTemp=aMaxTemp;
        minTemp=aMinTemp;
        maxHum=aMaxHum;
        minHum=aMinHum;
        maxPres=aMaxPres;
        minPres=aMinPres;
    }

    public float getMaxHum() {
        return maxHum;
    }

    public float getMaxPres() {
        return maxPres;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinHum() {
        return minHum;
    }

    public float getMinPres() {
        return minPres;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setMaxTemp(float aMaxTemp){
        maxTemp=aMaxTemp;
    }

    public void setMinTemp(float aMinTemp){
        minTemp=aMinTemp;
    }

    public void setMaxHum(float aMaxHum){
        maxHum=aMaxHum;
    }

    public void setMinHum(float aMinHum){
        minHum=aMinHum;
    }

    public void setMaxPres(float aMaxPres){
        maxPres=aMaxPres;
    }

    public void setMinPres(float aMinPres){
        minPres=aMinPres;
    }
}
