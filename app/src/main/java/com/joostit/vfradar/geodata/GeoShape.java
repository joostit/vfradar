package com.joostit.vfradar.geodata;

import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 26-1-2018.
 */

public class GeoShape {

    public List<GeoPolygon> polygons = new ArrayList<>();

    public boolean isInshape(LatLon position) {
        for (GeoPolygon polygon : polygons) {
            if(polygon.isInPolygon(position)){
                return true;
            }
        }
        return true;
    }
}
