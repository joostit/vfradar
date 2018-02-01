package com.joostit.vfradar.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 1-2-2018.
 */

public class AircraftTrackingUpdate {

    public List<TrackedAircraft> trackedAircraft = new ArrayList<>();
    public boolean updateSuccess;

    public AircraftTrackingUpdate(List<TrackedAircraft> trackedAircraft, boolean updateSuccess){
        this.updateSuccess = updateSuccess;
        this.trackedAircraft = trackedAircraft;
    }

    public AircraftTrackingUpdate(){
        this.updateSuccess = false;
        this.trackedAircraft = new ArrayList<>();
    }

    public boolean getUpdateSuccess(){
        return updateSuccess;
    }
}
