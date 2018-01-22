package com.joostit.vfradar.data;

import java.util.List;

/**
 * Created by Joost on 22-1-2018.
 */

public interface AircraftDataListener {
    void newAircraftDataReceived(List<AircraftState> ac);
}
