package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.site.Runway;

/**
 * Created by Joost on 24-1-2018.
 */

public class RunwayPlot extends DrawableItem {


    private Runway source;
    private boolean doDraw;
    private Path screenPath = new Path();

    private int lineColor = 0xAA999900;
    private Paint linePaint;
    private int textColor = 0xFFb3b300;
    private Paint textPaint;

    public RunwayPlot(Runway source) {
        this.source = source;
        init();

    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(20);
    }

    @Override
    public void draw(Canvas canvas) {
        if (doDraw) {
            canvas.drawPath(screenPath, linePaint);
        }
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {

        boolean isInView;
        double bearing = source.pointA.BearingTo(source.pointB);

        // ToDo: Extend bearing and put runway numbers at the end of them

        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        ;
        float maxY = Float.MIN_VALUE;

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

        minX = point1.x < minX ? point1.x : minX;
        minX = point2.x < minX ? point2.x : minX;
        minX = point3.x < minX ? point3.x : minX;
        minX = point4.x < minX ? point4.x : minX;

        maxX = point1.x > maxX ? point1.x : maxX;
        maxX = point2.x > maxX ? point2.x : maxX;
        maxX = point3.x > maxX ? point3.x : maxX;
        maxX = point4.x > maxX ? point4.x : maxX;

        minY = point1.y < minY ? point1.y : minY;
        minY = point2.y < minY ? point2.y : minY;
        minY = point3.y < minY ? point3.y : minY;
        minY = point4.y < minY ? point4.y : minY;

        maxY = point1.y > maxY ? point1.y : maxY;
        maxY = point2.y > maxY ? point2.y : maxY;
        maxY = point3.y > maxY ? point3.y : maxY;
        maxY = point4.y > maxY ? point4.y : maxY;

        RectF rwBounds = new RectF(minX, minY, maxX, maxY);

        isInView = RectF.intersects(bounds, rwBounds);

        screenPath = newPath;
        doDraw = isInView;
        return doDraw;
    }
}
