package com.joostit.vfradar.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 2-2-2018.
 */

public class GeoFenceHandler {


    private List<FencedArea> areas = new ArrayList<>();


    public void loadFencedAreas() {

        DebugFencedAreaCreator creator = new DebugFencedAreaCreator();
        areas.addAll(creator.getFencedAreas());

    }

    public void applyFences(List<TrackedAircraft> trackedAircraft){

        for(TrackedAircraft ac : trackedAircraft){
            for(FencedArea area : areas){
                //boolean isInArea = area.isInArea(ac.data)
            }
        }

    }


}
