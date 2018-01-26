package com.joostit.vfradar.geodata;

import com.joostit.vfradar.geo.LatLon;

import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoObject {

    public String name;
    public GeoShape shape = new GeoShape();
    public int population;
    public boolean isUrban;

    public GeoObject(){

    }
}
