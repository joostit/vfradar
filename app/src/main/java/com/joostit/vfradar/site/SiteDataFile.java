package com.joostit.vfradar.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 31-1-2018.
 */

public class SiteDataFile {
    public String fileName;
    public String name;
    public String description;
    public String author;
    public String lastUpdated;

    public List<ReportingPoint> reportingPoints = new ArrayList<>();
    public List<Runway> runways = new ArrayList<>();
    public List<RouteLine> routes = new ArrayList<>();


    public List<SiteFeature> getAllFeatures(){
        List<SiteFeature> retVal = new ArrayList<>();

        retVal.addAll(runways);
        retVal.addAll(routes);
        retVal.addAll(reportingPoints);
        return retVal;
    }
}
