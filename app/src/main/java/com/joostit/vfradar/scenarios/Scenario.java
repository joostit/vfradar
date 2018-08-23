package com.joostit.vfradar.scenarios;

import com.joostit.vfradar.geo.LatLon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 23-3-2018.
 */

public class Scenario {

    public String fileName;
    public String name;
    public String author;
    public String description;
    public String lastUpdated;

    public List<String> siteDataFiles = new ArrayList<>();
    public List<String> geoFenceFiles = new ArrayList<>();

    public LatLon centerLocation = new LatLon();

    public Scenario() {
    }

    ;


    @Override
    public String toString() {
        if (name != null) {
            return name;
        } else if (fileName != null) {
            return fileName;
        } else {
            return name;
        }
    }
}
