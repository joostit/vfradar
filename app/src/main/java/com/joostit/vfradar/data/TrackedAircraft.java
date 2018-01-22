package com.joostit.vfradar.data;

/**
 * Created by Joost on 15-1-2018.
 */

public class TrackedAircraft {
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
}
