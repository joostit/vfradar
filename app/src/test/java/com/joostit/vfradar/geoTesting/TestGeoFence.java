package com.joostit.vfradar.geoTesting;

import com.joostit.vfradar.data.AircraftState;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.geofencing.FencedArea;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Created by Joost on 4-2-2018.
 */

public class TestGeoFence {



    @Test
    public void TestPointsInsideGeoFence(){
        FencedArea area = GeoFenceTestData.createArea();

        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon(  52.518529, 6.639177), 500));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon( 52.332467,  6.987891), 600));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon(  52.515420, 7.106871 ),700 ));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon( 52.105877, 6.791978 ), 800));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon(52.045100 ,  6.583557 ),900 ));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon( 52.122115,  6.404213 ), 1000));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon( 51.966696 , 6.311605 ), 715 ));
        assertTrue("Aircraft is expected to be inside fence", putAircraftInArea(area, new LatLon(  52.380422,  6.637388), 624));
    }

    @Test
    public void TestPointsOutsideGeoFence() {
        FencedArea area = GeoFenceTestData.createArea();

        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.717273, 7.701779), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.057757, 7.685933), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(51.686440, 7.084222), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(51.336613, 6.328438), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(51.702369, 5.300259), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.297622, 5.880702), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(49.838167, 7.459600), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.226526, 6.626233), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.549630, 6.845892), 700));
        assertFalse("Aircraft is expected to be outside fence", putAircraftInArea(area, new LatLon(52.626979, 6.905855), 700));
    }



    private boolean putAircraftInArea(FencedArea area, LatLon point, Integer alt){
        TrackedAircraft ac = new TrackedAircraft();
        ac.data = new AircraftState();
        ac.data.position = point;
        ac.data.alt = alt;
        return area.isInArea(ac);
    }

}
