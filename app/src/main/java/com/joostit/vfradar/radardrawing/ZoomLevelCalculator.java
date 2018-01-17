package com.joostit.vfradar.radardrawing;

import com.joostit.vfradar.geo.LatLon;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 17-1-2018.
 */

public class ZoomLevelCalculator {

    private List<ZoomLevelInfo> zoomLevels = new ArrayList<>();

    public ZoomLevelCalculator(){
        loadZoomLevels();
    }

    public ZoomLevelInfo getZoomLevelInfo(int zoomLevel){
        return zoomLevels.get(zoomLevel);
    }

    private void loadZoomLevels() {

        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 6000;
            RingRadius1 = 200;
            RingRadius2 = 400;
            RingRadius3 = 600;
        }});

        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 1200;
            RingRadius1 = 400;
            RingRadius2 = 800;
            RingRadius3 = 1200;
        }});

        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 3000;
            RingRadius1 = 500;
            RingRadius2 = 1500;
            RingRadius3 = 3000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 6000;
            RingRadius1 = 1000;
            RingRadius2 = 3000;
            RingRadius3 = 6000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 15000;
            RingRadius1 = 5000;
            RingRadius2 = 10000;
            RingRadius3 = 15000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 30000;
            RingRadius1 = 10000;
            RingRadius2 = 20000;
            RingRadius3 = 30000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 60000;
            RingRadius1 = 20000;
            RingRadius2 = 40000;
            RingRadius3 = 60000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 120000;
            RingRadius1 = 40000;
            RingRadius2 = 80000;
            RingRadius3 = 120000;
        }});


        zoomLevels.add(new ZoomLevelInfo()
        {{
            RangeRadius = 240000;
            RingRadius1 = 80000;
            RingRadius2 = 160000;
            RingRadius3 = 240000;
        }});
    }


}
