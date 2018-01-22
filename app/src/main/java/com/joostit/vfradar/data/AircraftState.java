package com.joostit.vfradar.data;

import com.joostit.vfradar.utilities.StringValue;

/**
 * Created by Joost on 22-1-2018.
 */

public class AircraftState {

    public int trackId;
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
    public String ognStation;
    public Integer ognAge;
    public String adsbStation;
    public Integer adsbAge;
    public String rxChannel;
    public Integer squawk;

    public boolean hasOgnStation(){
        return ognStation != null;
    }

    public boolean hasOgnAge(){
        return ognAge != null;
    }

    public boolean hasAdsbStation(){
        return adsbStation != null;
    }


    public boolean hasAdsbAge(){
        return adsbAge != null;
    }

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
        return !StringValue.nullOrEmpty(cn);
    }

    public boolean hasRegistration(){
        return !StringValue.nullOrEmpty(reg);
    }

    public boolean hasCallsign(){
        return !StringValue.nullOrEmpty(callSign);
    }

    public boolean hasModel(){
        return !StringValue.nullOrEmpty(model);
    }

    public boolean hasOgnId(){
        return !StringValue.nullOrEmpty(ognId);
    }

    public boolean hasIcao24Id(){
        return !StringValue.nullOrEmpty(icao24);
    }

    public boolean hasFlarmId(){
        return !StringValue.nullOrEmpty(flarmId);
    }



}
