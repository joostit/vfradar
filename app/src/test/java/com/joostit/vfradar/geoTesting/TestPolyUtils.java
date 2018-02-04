package com.joostit.vfradar.geoTesting;


import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geodata.GeoPolygon;
import com.joostit.vfradar.geofencing.FencedArea;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Joost on 4-2-2018.
 */

public class TestPolyUtils {


    @Test
    public void validatePointsInsidePolygon(){
        FencedArea area = GeoFenceTestData.createArea();
        GeoPolygon polygon = area.shape.polygons.get(0);
        assertTrue("Point is expected to be inside polygon area", polygon.isInPolygon(new LatLon(52.245272, 6.864358)));
        assertTrue("Point is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.522680, 7.088316)));
        assertTrue("Point is expected to be inside polygon area", polygon.isInPolygon(new LatLon(51.988154,  6.650865)));
        assertTrue("Point is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.030937,  6.346046)));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon(52.682038 ,  6.622104 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.640072 , 6.832582 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.176171,  6.541188 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon(51.890623 , 6.461301 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon(  51.857464,  6.620293 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.166907 ,  7.052826 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon(  52.015423,   6.841942 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.538298,  7.321743 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.434476,  6.836803 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon( 52.148100 , 6.649501 )));
        assertTrue("Point near edge is expected to be inside polygon area", polygon.isInPolygon(new LatLon(52.162104 ,  6.594684 )));

    }

    @Test
    public void validatePointsOutsidePolygon() {
        FencedArea area = GeoFenceTestData.createArea();
        GeoPolygon polygon = area.shape.polygons.get(0);
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(44.560970, 43.263657)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(17.017341, 99.910080)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(5.705278, 173.432338)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(-25.488628, 126.773333)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(-44.229610, 171.108811)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(-31.169257, 23.247635)));
        assertFalse("Point is expected to be very far outside polygon area", polygon.isInPolygon(new LatLon(-38.213285, -64.751702)));

        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(51.332644, 7.443118)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(52.098305, 8.466206)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(53.294484, 7.752559)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(53.457793, 6.309951)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(52.648055, 5.137300)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(51.692331, 5.618247)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(51.706443, 6.446497)));
        assertFalse("Point is expected to be far outside polygon area", polygon.isInPolygon(new LatLon(51.915436, 6.948594)));

        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon(51.855574, 6.619953)));
        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon(51.855852, 6.621939)));
        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon( 52.278661,   6.384811)));
        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.653502, 6.594822)));
        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.665671, 6.728606)));
        assertFalse("Point near edge is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.581423, 7.133677)));

        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.629441, 6.896230)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.613544, 6.890345)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.601544, 6.947138)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon( 52.370917,   6.791667)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.149988, 6.643377)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.176140, 6.543150)));
        assertFalse("Point inside polygon cavity is expected to be outside polygon area", polygon.isInPolygon(new LatLon(52.257305, 6.656538)));

    }


}
