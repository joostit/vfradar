package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.joostit.vfradar.site.RouteLine;

/**
 * Created by Joost on 24-1-2018.
 */

public class RouteLinePlot extends DrawableItem {

    private RouteLine source;
    private boolean doDraw;
    private Path screenPath = new Path();

    private int lineColor = 0xFF4d4d00;
    private Paint linePaint;

    public RouteLinePlot(RouteLine source) {
        this.source = source;
        init();

    }


    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(lineColor);
    }

    @Override
    public void draw(Canvas canvas) {
        if (doDraw) {
            canvas.drawPath(screenPath, linePaint);
        }
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {

        Path newPath = new Path();
        boolean isInView = false;

        if (source.points.size() == 0) {
            doDraw = false;
            return doDraw;
        }


        PointF point = projection.toScreenPoint(source.points.get(0));
        newPath.moveTo(point.x, point.y);
        if (bounds.contains(point.x, point.y)) {
            isInView = true;
        }

        float previousX = point.x;
        float previousY = point.y;

        for (int i = 1; i < source.points.size(); i++) {
            point = projection.toScreenPoint(source.points.get(i));

            RectF lineRect = new RectF(previousX, previousY, point.x, point.y);
            previousX = point.x;
            previousY = point.y;

            if (RectF.intersects(lineRect, bounds)) {
                isInView = true;
            }

            newPath.lineTo(point.x, point.y);
        }

        screenPath = newPath;
        doDraw = isInView;
        return doDraw;
    }
}
