package com.joostit.vfradar.radardrawing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 17-1-2018.
 */

public class ZoomLevelCalculator {

    private List<ZoomLevelInfo> zoomLevels = new ArrayList<>();
    private int currentZoomLevel = 12;

    public ZoomLevelCalculator() {
        loadZoomLevels();
    }

    public ZoomLevelInfo getZoomLevelInfo() {
        return zoomLevels.get(currentZoomLevel);
    }

    private void loadZoomLevels() {

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 90;
            RingRadius1 = 30;
            RingRadius2 = 60;
            RingRadius3 = 90;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 150;
            RingRadius1 = 50;
            RingRadius2 = 100;
            RingRadius3 = 150;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 210;
            RingRadius1 = 70;
            RingRadius2 = 140;
            RingRadius3 = 210;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 300;
            RingRadius1 = 100;
            RingRadius2 = 200;
            RingRadius3 = 300;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 450;
            RingRadius1 = 150;
            RingRadius2 = 300;
            RingRadius3 = 450;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 600;
            RingRadius1 = 200;
            RingRadius2 = 400;
            RingRadius3 = 600;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 900;
            RingRadius1 = 300;
            RingRadius2 = 600;
            RingRadius3 = 900;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 1500;
            RingRadius1 = 500;
            RingRadius2 = 1000;
            RingRadius3 = 1500;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 2100;
            RingRadius1 = 700;
            RingRadius2 = 1400;
            RingRadius3 = 2100;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 3000;
            RingRadius1 = 1000;
            RingRadius2 = 2000;
            RingRadius3 = 3000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 4500;
            RingRadius1 = 1500;
            RingRadius2 = 3000;
            RingRadius3 = 4500;
        }});


        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 6000;
            RingRadius1 = 2000;
            RingRadius2 = 4000;
            RingRadius3 = 6000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 9000;
            RingRadius1 = 3000;
            RingRadius2 = 6000;
            RingRadius3 = 9000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 15000;
            RingRadius1 = 5000;
            RingRadius2 = 10000;
            RingRadius3 = 15000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 21000;
            RingRadius1 = 7000;
            RingRadius2 = 14000;
            RingRadius3 = 21000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 30000;
            RingRadius1 = 10000;
            RingRadius2 = 20000;
            RingRadius3 = 30000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 42000;
            RingRadius1 = 14000;
            RingRadius2 = 28000;
            RingRadius3 = 42000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 60000;
            RingRadius1 = 20000;
            RingRadius2 = 40000;
            RingRadius3 = 60000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 84000;
            RingRadius1 = 28000;
            RingRadius2 = 56000;
            RingRadius3 = 84000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 120000;
            RingRadius1 = 40000;
            RingRadius2 = 80000;
            RingRadius3 = 120000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 168000;
            RingRadius1 = 56000;
            RingRadius2 = 112000;
            RingRadius3 = 168000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            RangeRadius = 240000;
            RingRadius1 = 80000;
            RingRadius2 = 160000;
            RingRadius3 = 240000;
        }});
    }


    public void zoomIn() {
        if (currentZoomLevel > 0) {
            currentZoomLevel--;
        }
    }

    public void zoomOut() {
        if (currentZoomLevel < zoomLevels.size() - 1) {
            currentZoomLevel++;
        }
    }

}
