package com.joostit.vfradar.geoTesting;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.GeoPolygon;
import com.joostit.vfradar.geo.geofencing.FenceAlerts;
import com.joostit.vfradar.geo.geofencing.FencedArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 4-2-2018.
 */

public class GeoFenceTestData {
    public static List<FencedArea> getFencedAreas(){

        List<FencedArea> retVal = new ArrayList<>();

        retVal.add(createArea());

        return retVal;
    }

    public static FencedArea createArea(){
        FencedArea area = new FencedArea();

        area.name = "TestArea";
        area.alertType = FenceAlerts.Notification;
        area.topFt = 1000;
        area.bottomFt = 500;
        GeoPolygon polygon = new GeoPolygon();

        polygon.points.add(new LatLon(52.26923886292269, 6.882929583130513));

        polygon.points.add(new LatLon (52.6904312737833, 6.617817234939025));
        polygon.points.add(new LatLon (51.94372962764766, 6.197737626705766));
        polygon.points.add(new LatLon (51.85574770869437, 6.621247138099515));
        polygon.points.add(new LatLon (52.16276687943588, 7.056639726010627));
        polygon.points.add(new LatLon (52.54087545763829, 7.326754495405865));
        polygon.points.add(new LatLon (52.61079045084414, 6.953677480390834));
        polygon.points.add(new LatLon (52.14940444368699, 6.643869456053269));
        polygon.points.add(new LatLon (52.17605856305226, 6.542469476207781));
        polygon.points.add(new LatLon (52.64025393617844, 6.83303447038017));
        polygon.points.add(new LatLon (52.6904312737833, 6.617817234939025));

        area.shape.polygons.add(polygon);

        return area;
    }
}
