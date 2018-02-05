package com.joostit.vfradar.geo.geofencing;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.GeoPolygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 2-2-2018.
 */

public class DebugFencedAreaCreator {

    public List<FencedArea> getFencedAreas(){

        List<FencedArea> retVal = new ArrayList<>();

        retVal.add(createArea1());
        retVal.add(createArea2());

        return retVal;
    }

    private FencedArea createArea1(){
        FencedArea area = new FencedArea();

        area.name = "Noord circuit";

        area.alertType = FenceAlerts.Warning;
        area.topFt = 40000;
        area.bottomFt = 300;
        GeoPolygon polygon = new GeoPolygon();

        polygon.points.add(new LatLon (52.26923886292269, 6.882929583130513));

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


    private FencedArea createArea2(){
        FencedArea area = new FencedArea();

        area.name = "Zuid circuit";

        area.alertType = FenceAlerts.Notification;
        area.topFt = 40000;
        area.bottomFt = 300;
        GeoPolygon polygon = new GeoPolygon();

        polygon.points.add(new LatLon (52.61245345338619,6.957017620786203));
        polygon.points.add(new LatLon (51.88436316254424,6.485656307350965));
        polygon.points.add(new LatLon (51.72838157649869, 7.194026681830872));
        polygon.points.add(new LatLon (52.48079025169675,7.693363379160041));
        polygon.points.add(new LatLon (52.61245345338619,6.957017620786203));

        area.shape.polygons.add(polygon);

        return area;
    }

}

