package com.joostit.vfradar.site;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 24-1-2018.
 */

public class Runway extends SiteFeature {

    public String name;
    public LatLon pointA;
    public LatLon pointB;
    public double widthM;

    public Runway() {
        super(SiteFeatureTypes.Runway);
    }

    public Runway(String name, LatLon from, LatLon to, double widthM){
        this();
        this.name = name;
        this.pointA = from;
        this.pointB = to;
        this.widthM = widthM;
    }
}
