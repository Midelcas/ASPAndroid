package com.sn_g2.greenhouse;

public class Species {
    private String idSpecie;
    private float maxTemp;
    private float minTemp;
    private float maxHum;
    private float minHum;
    private float maxPH;
    private float minPH;
    private float maxOxyg;
    private float minOxyg;
    private float maxCond;
    private float minCond;
    private float maxWL;
    private float minWL;

    public Species(String aIdSpecie, float aMaxTemp, float aMinTemp, float aMaxHum, float aMinHum, float aMaxPH, float aMinPH, float aMaxOxyg, float aMinOxyg, float aMaxCond, float aMinCond, float aMaxWL, float aMinWL){
        idSpecie=aIdSpecie;
        maxCond=aMaxCond;
        minCond=aMinCond;
        maxHum=aMaxHum;
        minHum=aMinHum;
        maxOxyg=aMaxOxyg;
        minOxyg=aMinOxyg;
        maxPH=aMaxPH;
        minPH=aMinPH;
        maxTemp=aMaxTemp;
        minTemp=aMinTemp;
        maxWL=aMaxWL;
        minWL=aMinWL;
    }
}
