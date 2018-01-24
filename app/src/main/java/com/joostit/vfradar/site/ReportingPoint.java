package com.joostit.vfradar.site;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 24-1-2018.
 */

public class ReportingPoint extends SiteFeature {

    public LatLon position;
    public String name;

    public ReportingPoint() {
        super(SiteFeatureTypes.ReportingPoint);
    }

    public ReportingPoint(String name, LatLon position) {
        this();
        this.name = name;
        this.position = position;
    }
}
