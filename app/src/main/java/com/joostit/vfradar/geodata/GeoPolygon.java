package com.joostit.vfradar.geodata;

import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 26-1-2018.
 */

public class GeoPolygon{

    public List<LatLon> points = new ArrayList<>();


    public boolean isInPolygon(LatLon position) {
        int i, j;
        boolean isInside = false;
        int sides = points.size();
        for (i = 0, j = sides - 1; i < sides; j = i++) {

            if (
                    (
                            (
                                    (points.get(i).longitude <= position.longitude) && (position.longitude < points.get(j).longitude)
                            ) || (
                                    (points.get(j).longitude <= position.longitude) && (position.longitude < points.get(i).longitude)
                            )
                    ) &&
                            (position.latitude < (points.get(j).latitude - points.get(i).latitude) * (position.longitude - points.get(i).longitude) / (points.get(j).longitude - points.get(i).longitude) + points.get(i).latitude)
                    ) {
                isInside = !isInside;
            }
        }
        return isInside;

    }

}
