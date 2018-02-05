package com.joostit.vfradar.geo;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoObject {

    public String name = "";
    public GeoShape shape = new GeoShape();
    private GeoObjectTypes objectType;
    private LatLonRect bounds = null;

    public GeoObject(GeoObjectTypes objectType) {
        this.objectType = objectType;
    }

    public LatLonRect getBoundingRect(){
        if(bounds == null){
            updateBoundingRect();
        }
        return bounds;
    }

    protected void setObjectType(GeoObjectTypes objectType){
        this.objectType = objectType;
    }

    public GeoObjectTypes getObjectType(){
        return objectType;
    }

    public void updateBoundingRect() {
        bounds = new LatLonRect();
        bounds.bottomLon = Double.MAX_VALUE;
        bounds.leftLat = Double.MAX_VALUE;
        bounds.topLon = Double.MIN_VALUE;
        bounds.rightLat = Double.MIN_VALUE;

        for(GeoPolygon polygon : shape.polygons){
            for(LatLon pos : polygon.points){
                bounds.bottomLon = (pos.longitude < bounds.bottomLon) ? pos.longitude : bounds.bottomLon;
                bounds.leftLat = (pos.latitude < bounds.leftLat) ? pos.latitude : bounds.leftLat;
                bounds.topLon = (pos.longitude > bounds.topLon) ? pos.longitude : bounds.topLon;
                bounds.rightLat = (pos.latitude > bounds.rightLat) ? pos.latitude : bounds.rightLat;
            }
        }
    }


}
