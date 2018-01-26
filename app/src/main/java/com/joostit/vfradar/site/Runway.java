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
    public String nameA;
    public String nameB;

    public Runway() {
        super(SiteFeatureTypes.Runway);
    }

    public Runway(String name, LatLon pointA, LatLon pointB, String nameA, String nameB, double widthM){
        this();
        this.name = name;
        this.pointA = pointA;
        this.pointB = pointB;
        this.widthM = widthM;
        this.nameA = nameA;
        this.nameB = nameB;
    }
}
