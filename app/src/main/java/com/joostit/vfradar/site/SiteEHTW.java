package com.joostit.vfradar.site;

import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 24-1-2018.
 */

public class SiteEHTW {


    public List<SiteFeature> getSite(){
        List<SiteFeature> site = new ArrayList<>();

        ReportingPoint tango;
        ReportingPoint xRay;
        ReportingPoint yankee;
        ReportingPoint oscar;

        // Reporting shape
        site.add(tango = new ReportingPoint("Tango", new LatLon(52.2929031, 6.6399750)));
        site.add(xRay = new ReportingPoint("X-Ray", new LatLon(52.323495, 6.724956)));
        site.add(yankee = new ReportingPoint("Yankee", new LatLon(52.312747, 6.851478)));
        site.add(oscar = new ReportingPoint("Oscar", new LatLon(52.292427, 6.871874)));

        // reporting shape route
        RouteLine vfrInboundRoute = new RouteLine();
        vfrInboundRoute.addPoint(tango);
        vfrInboundRoute.addPoint(xRay);
        vfrInboundRoute.addPoint(yankee);
        vfrInboundRoute.addPoint(oscar);
        site.add(vfrInboundRoute);

        Runway runway = new Runway("EHTW", new LatLon(52.268293, 6.871564), new LatLon(52.283316, 6.906594), 45);
        site.add(runway);

        return site;
    }


}
