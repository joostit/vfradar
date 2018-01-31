package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by Joost on 18-1-2018.
 */

public abstract class DrawableItem {


    public Boolean doHitTest(float hitX, float hitY) {
        return false;
    }

    public abstract void draw(Canvas canvas);

    public abstract boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo);


}
