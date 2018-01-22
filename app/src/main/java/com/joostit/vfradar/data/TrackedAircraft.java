package com.joostit.vfradar.data;

/**
 * Created by Joost on 15-1-2018.
 */

public class TrackedAircraft {

    public enum IdTypes{
        Callsign,
        Registration,
        Icao24,
        FlarmId,
        OgnId,
        Cn
    }

    public AircraftState Data;

    public Boolean isSelected = false;
    public Boolean isHighlighted = false;
    public Boolean isWarning = false;


    public String getIdString(){
        String nameString = "";
        if (Data.hasCallsign()) {
            nameString = Data.callSign;
        } else if (Data.hasRegistration()) {
            nameString = Data.reg;
        } else if (Data.hasIcao24Id()) {
            nameString = Data.icao24;
        } else if (Data.hasFlarmId()) {
            nameString = Data.flarmId;
        } else if (Data.hasOgnId()) {
            nameString = Data.ognId;
        }
        return nameString;
    }

    public String getId(IdTypes type){
        switch (type){

            case Callsign:
                return Data.callSign;
            case Registration:
                return Data.reg;
            case Icao24:
                return Data.icao24;
            case FlarmId:
                return Data.flarmId;
            case OgnId:
                return Data.ognId;
            case Cn:
                return Data.cn;
            default:
                return "UNSUPPORTED ID TYPE";
        }
    }

    public IdTypes getUserIdType(){

        if (Data.hasCallsign()) {
            return IdTypes.Callsign;
        } else if (Data.hasRegistration()) {
            return IdTypes.Registration;
        } else if (Data.hasIcao24Id()) {
            return IdTypes.Icao24;
        } else if (Data.hasFlarmId()) {
            return IdTypes.FlarmId;
        } else if (Data.hasOgnId()) {
            return IdTypes.OgnId;
        }

        return IdTypes.Icao24;
    }
}
