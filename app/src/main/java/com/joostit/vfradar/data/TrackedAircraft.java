package com.joostit.vfradar.data;

import com.joostit.vfradar.geofencing.GeoFenceStatus;

/**
 * Created by Joost on 15-1-2018.
 */

public class TrackedAircraft {

    public AircraftState data;
    public Boolean isSelected = false;
    public Boolean isHighlighted = false;
    public Boolean isWarning = false;

    public GeoFenceStatus isInside = new GeoFenceStatus();

    public String getIdString() {
        String nameString = "";
        if (data.hasCallsign()) {
            nameString = data.callSign;
        } else if (data.hasRegistration()) {
            nameString = data.reg;
        } else if (data.hasIcao24Id()) {
            nameString = data.icao24;
        } else if (data.hasFlarmId()) {
            nameString = data.flarmId;
        } else if (data.hasOgnId()) {
            nameString = data.ognId;
        }
        return nameString;
    }

    public String getId(IdTypes type) {
        switch (type) {

            case Callsign:
                return data.callSign;
            case Registration:
                return data.reg;
            case Icao24:
                return data.icao24;
            case FlarmId:
                return data.flarmId;
            case OgnId:
                return data.ognId;
            case Cn:
                return data.cn;
            default:
                return "UNSUPPORTED ID TYPE";
        }
    }

    public IdTypes getUserIdType() {

        if (data.hasCallsign()) {
            return IdTypes.Callsign;
        } else if (data.hasRegistration()) {
            return IdTypes.Registration;
        } else if (data.hasIcao24Id()) {
            return IdTypes.Icao24;
        } else if (data.hasFlarmId()) {
            return IdTypes.FlarmId;
        } else if (data.hasOgnId()) {
            return IdTypes.OgnId;
        }

        return IdTypes.Icao24;
    }

    public enum IdTypes {
        Callsign,
        Registration,
        Icao24,
        FlarmId,
        OgnId,
        Cn
    }
}
