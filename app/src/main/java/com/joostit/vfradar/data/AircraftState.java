package com.joostit.vfradar.data;

/**
 * Created by Joost on 22-1-2018.
 */

public class AircraftState {

    public int Trackid;
    public String flarmId;
    public String ognId;
    public String icao24;
    public String reg;
    public String cn;
    public String callSign;
    public String model;
    public String type;
    public double lat;
    public double lon;
    public Integer speed;
    public Integer track;
    public Double turnRate;
    public Integer alt;
    public Double vRate;
    public String rxStation;
    public String rxChannel;
    public Integer squawk;


    public boolean hasAltitude(){
        return (alt != null);
    }

    public boolean hasVRate(){
        return (vRate != null);
    }

    public boolean hasTrack(){
        return (track != null);
    }

    public boolean hasCn(){
        return !isNullOrEmpty(cn);
    }

    public boolean hasRegistration(){
        return !isNullOrEmpty(reg);
    }

    public boolean hasCallsign(){
        return !isNullOrEmpty(callSign);
    }

    public boolean hasModel(){
        return !isNullOrEmpty(model);
    }

    public boolean hasOgnId(){
        return !isNullOrEmpty(ognId);
    }

    public boolean hasIcao24Id(){
        return !isNullOrEmpty(icao24);
    }

    public boolean hasFlarmId(){
        return !isNullOrEmpty(flarmId);
    }

    private Boolean isNullOrEmpty(String input) {
        return ((input == null) || (input.trim().equals("")));
    }

}
