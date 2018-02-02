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

    private void loadZoomLevels() {

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 90;
            ringRadius1 = 30;
            ringRadius2 = 60;
            ringRadius3 = 90;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 150;
            ringRadius1 = 50;
            ringRadius2 = 100;
            ringRadius3 = 150;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 210;
            ringRadius1 = 70;
            ringRadius2 = 140;
            ringRadius3 = 210;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 300;
            ringRadius1 = 100;
            ringRadius2 = 200;
            ringRadius3 = 300;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 450;
            ringRadius1 = 150;
            ringRadius2 = 300;
            ringRadius3 = 450;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 600;
            ringRadius1 = 200;
            ringRadius2 = 400;
            ringRadius3 = 600;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 900;
            ringRadius1 = 300;
            ringRadius2 = 600;
            ringRadius3 = 900;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 1500;
            ringRadius1 = 500;
            ringRadius2 = 1000;
            ringRadius3 = 1500;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 2100;
            ringRadius1 = 700;
            ringRadius2 = 1400;
            ringRadius3 = 2100;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 3000;
            ringRadius1 = 1000;
            ringRadius2 = 2000;
            ringRadius3 = 3000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 4500;
            ringRadius1 = 1500;
            ringRadius2 = 3000;
            ringRadius3 = 4500;
        }});


        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 6000;
            ringRadius1 = 2000;
            ringRadius2 = 4000;
            ringRadius3 = 6000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 9000;
            ringRadius1 = 3000;
            ringRadius2 = 6000;
            ringRadius3 = 9000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 15000;
            ringRadius1 = 5000;
            ringRadius2 = 10000;
            ringRadius3 = 15000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 21000;
            ringRadius1 = 7000;
            ringRadius2 = 14000;
            ringRadius3 = 21000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 30000;
            ringRadius1 = 10000;
            ringRadius2 = 20000;
            ringRadius3 = 30000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 42000;
            ringRadius1 = 14000;
            ringRadius2 = 28000;
            ringRadius3 = 42000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 60000;
            ringRadius1 = 20000;
            ringRadius2 = 40000;
            ringRadius3 = 60000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 84000;
            ringRadius1 = 28000;
            ringRadius2 = 56000;
            ringRadius3 = 84000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 120000;
            ringRadius1 = 40000;
            ringRadius2 = 80000;
            ringRadius3 = 120000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 168000;
            ringRadius1 = 56000;
            ringRadius2 = 112000;
            ringRadius3 = 168000;
        }});

        zoomLevels.add(new ZoomLevelInfo() {{
            rangeRadius = 240000;
            ringRadius1 = 80000;
            ringRadius2 = 160000;
            ringRadius3 = 240000;
        }});
    }


    public ZoomLevelInfo getZoomLevelInfo() {
        return zoomLevels.get(currentZoomLevel);
    }

    public int getCurrentZoomLevel(){
        return currentZoomLevel;
    }

    public int size(){
        return zoomLevels.size();
    }

    public ZoomLevelInfo getZoomLevelInfoAt(int index){
        return zoomLevels.get(index);
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

    public void zoomTo(int newZoomLevel) {
        if((newZoomLevel >= 0) && (newZoomLevel < zoomLevels.size())) {
            currentZoomLevel = newZoomLevel;
        }
    }

}
