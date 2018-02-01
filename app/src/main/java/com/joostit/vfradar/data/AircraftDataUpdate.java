package com.joostit.vfradar.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 1-2-2018.
 */

public class AircraftDataUpdate {

    private boolean updateSuccess;
    public List<AircraftState> trackedAircraft = new ArrayList<>();


    public AircraftDataUpdate(List<AircraftState> ac, boolean updateSuccess){
        this.trackedAircraft = ac;
        this.updateSuccess = updateSuccess;
    }

    public boolean getUpdateSuccess(){
        return updateSuccess;
    }
}
