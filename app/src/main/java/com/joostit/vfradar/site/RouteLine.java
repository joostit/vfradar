package com.joostit.vfradar.site;

import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class RouteLine extends SiteFeature {

    public List<LatLon> points = new ArrayList<>();

    public RouteLine() {
        super(SiteFeatureTypes.RouteLine);
    }


    public void addPoint(LatLon point){
        points.add(point);
    }

    public void addPoint(ReportingPoint point){
        points.add(point.position);
    }
}
