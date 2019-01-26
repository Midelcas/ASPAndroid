package com.sn_g2.greenhouse;

public class Measures {
    private int idRoom;
    private float Temp;
    private float Hum;
    private float Pres;

    public int getIdRoom(){
        return idRoom;
    }

    public float getHum() {
        return Hum;
    }

    public float getPres() {
        return Pres;
    }

    public float getTemp() {
        return Temp;
    }

    public Measures(int aIdRoom, float aTemp, float aHum, float aPres){
        idRoom=aIdRoom;
        Temp=aTemp;
        Hum=aHum;
        Pres=aPres;
    }
}
