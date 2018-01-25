package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.site.RouteLine;
import com.joostit.vfradar.site.Runway;

/**
 * Created by Joost on 24-1-2018.
 */

public class RunwayPlot extends DrawableItem{


    private Runway source;
    private boolean doDraw;
    private Path screenPath = new Path();

    private int lineColor = 0xAA999900;
    private Paint linePaint;

    public RunwayPlot(Runway source){
        this.source = source;
        init();

    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);
    }

    @Override
    public void draw(Canvas canvas) {
        if(doDraw) {
            canvas.drawPath(screenPath, linePaint);
        }
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds) {

        boolean isInView = false;
        double bearing = source.pointA.BearingTo(source.pointB);

        LatLon p1 = source.pointA.Move(bearing - 45, source.widthM / 2);
        LatLon p2 = source.pointA.Move(bearing + 45, source.widthM / 2);
        LatLon p3 = source.pointB.Move(bearing + 45, source.widthM / 2);
        LatLon p4 = source.pointB.Move(bearing - 45, source.widthM / 2);

        Path newPath = new Path();

        PointF point1 = projection.toScreenPoint(p1);
        newPath.moveTo(point1.x, point1.y);

        PointF point2 = projection.toScreenPoint(p2);
        newPath.lineTo(point2.x, point2.y);

        PointF point3 = projection.toScreenPoint(p3);
        newPath.lineTo(point3.x, point3.y);

        PointF point4 = projection.toScreenPoint(p4);
        newPath.lineTo(point4.x, point4.y);

        newPath.close();

        isInView = bounds.contains(point1.x, point2.y) ? true : isInView;
        isInView = bounds.contains(point2.x, point2.y) ? true : isInView;
        isInView = bounds.contains(point2.x, point2.y) ? true : isInView;
        isInView = bounds.contains(point2.x, point2.y) ? true : isInView;

        screenPath = newPath;
        doDraw = isInView;
        return doDraw;
    }
}
