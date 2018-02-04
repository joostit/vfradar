package com.joostit.vfradar.geodata;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.utilities.PolyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 26-1-2018.
 */

public class GeoPolygon{

    public List<LatLon> points = new ArrayList<>();

    public boolean isInPolygon(LatLon position) {

        return PolyUtil.containsLocation(position, points, false);
    }

}
