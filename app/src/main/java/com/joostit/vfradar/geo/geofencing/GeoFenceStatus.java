package com.joostit.vfradar.geo.geofencing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 2-2-2018.
 */

public class GeoFenceStatus {

    public List<FencedArea> areas = new ArrayList<>();

    public boolean isInArea(String name){
        for(FencedArea area : areas){
            if(name.equals(area.name)){
                return true;
            }
        }
        return false;
    }

}
