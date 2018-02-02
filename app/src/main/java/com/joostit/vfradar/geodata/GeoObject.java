package com.joostit.vfradar.geodata;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoObject {

    public String name = "";
    public GeoShape shape = new GeoShape();

    private LatLonRect bounds = null;

    public GeoObject() {

    }

    public LatLonRect getBoundingRect(){
        if(bounds == null){
            recalculateBoundingRect();
        }
        return bounds;
    }

    public void recalculateBoundingRect() {
        bounds = new LatLonRect();
        bounds.bottomLon = Double.MAX_VALUE;
        bounds.leftLat = Double.MAX_VALUE;
        bounds.topLon = Double.MIN_VALUE;
        bounds.rightLat = Double.MIN_VALUE;

        for(GeoPolygon polygon : shape.polygons){
            for(LatLon pos : polygon){
                bounds.bottomLon = (pos.longitude < bounds.bottomLon) ? pos.longitude : bounds.bottomLon;
                bounds.leftLat = (pos.latitude < bounds.leftLat) ? pos.latitude : bounds.leftLat;
                bounds.topLon = (pos.longitude > bounds.topLon) ? pos.longitude : bounds.topLon;
                bounds.rightLat = (pos.latitude > bounds.rightLat) ? pos.latitude : bounds.rightLat;
            }
        }
    }


}
