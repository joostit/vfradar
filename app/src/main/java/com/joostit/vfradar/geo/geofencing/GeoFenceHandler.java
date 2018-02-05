package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.GeoObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 2-2-2018.
 */

public class GeoFenceHandler {


    private List<FencedArea> areas = new ArrayList<>();

    public List<FencedArea> getAllAreas(){
        return areas;
    }

    public void loadFencedAreas() {
        GeoFenceLoader loader = new GeoFenceLoader();
        areas = loader.loadAllFilesInFolder();
    }

    public void applyFences(List<TrackedAircraft> trackedAircraft){

        for(TrackedAircraft ac : trackedAircraft){
            for(FencedArea area : areas){
                boolean isInArea = area.isInArea(ac);

                if(isInArea){
                    if(!ac.isInside.areas.contains(area)){
                        ac.isInside.areas.add(area);
                    }
                }
                else{
                    if(ac.isInside.areas.contains(area)){
                        ac.isInside.areas.remove(area);
                    }
                }
            }
        }

    }


}
