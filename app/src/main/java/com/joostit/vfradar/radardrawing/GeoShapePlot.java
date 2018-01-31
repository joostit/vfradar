package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geodata.GeoObject;
import com.joostit.vfradar.geodata.GeoPolygon;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoShapePlot extends DrawableItem {

    public GeoObject source;

    private boolean doDraw = false;
    private Path screenPath = new Path();

    private int pathColor = 0xFF1a1a1a;
    private Paint pathPaint;
    private Paint textPaint;
    private int textColor = 0xFF4d4d4d;
    private PointF textPoint = new PointF(0, 0);


    public GeoShapePlot(GeoObject source) {
        this.source = source;
        init();
    }


    private void init() {
        pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pathPaint.setStyle(Paint.Style.FILL);
        pathPaint.setStrokeWidth(2);
        pathPaint.setColor(pathColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(25);

    }


    @Override
    public void draw(Canvas canvas) {
        if (doDraw) {
            if (screenPath != null) {
                canvas.drawPath(screenPath, pathPaint);
                if (textPoint != null) {
                    canvas.drawText(source.name, 0, source.name.length(), textPoint.x, textPoint.y, textPaint);
                }
            }
        }
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {
        Path newPath = new Path();
        newPath.setFillType(Path.FillType.EVEN_ODD);
        boolean isInView = false;
        PointF newTextPoint = null;
        double sumX = 0;
        double sumY = 0;
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        int pointCount = 0;


        if (source.name.equalsIgnoreCase("Losser")) {
            source.toString();
        }

        for (GeoPolygon polygon : source.shape.polygons) {

            if (polygon.size() < 2) {
                continue;
            }

            LatLon startlatLon = polygon.get(0);
            PointF startPoint = projection.toScreenPoint(startlatLon);
            if (bounds.contains(startPoint.x, startPoint.y)) {
                isInView = true;
            }
            newPath.moveTo(startPoint.x, startPoint.y);
            sumX += startPoint.x;
            sumY += startPoint.y;
            pointCount++;

            for (int i = 1; i < polygon.size(); i++) {
                LatLon pos = polygon.get(i);
                PointF screenPoint = projection.toScreenPoint(pos);

                if (bounds.contains(screenPoint.x, screenPoint.y)) {
                    isInView = true;
                }

                newPath.lineTo(screenPoint.x, screenPoint.y);

                if (screenPoint.x > maxX) {
                    maxX = screenPoint.x;
                }
                if (screenPoint.x < minX) {
                    minX = screenPoint.x;
                }
                if (screenPoint.y > maxY) {
                    maxY = screenPoint.y;
                }
                if (screenPoint.y < minY) {
                    minY = screenPoint.y;
                }

                sumX += screenPoint.x;
                sumY += screenPoint.y;
                pointCount++;
            }

            newPath.close();
        }

        if (source.name.equalsIgnoreCase("Lonneker")) {
            source.toString();
        }

        if (isInView) {

            float centerX = (float) (sumX / pointCount);
            float centerY = (float) (sumY / pointCount);
            float polyStretchX = maxX - minX;
            float polyStretchY = maxY - minY;

            Rect textBounds = new Rect();
            textPaint.getTextBounds(source.name, 0, source.name.length(), textBounds);

            int textStretchX = textBounds.right;
            int textStretchY = textBounds.bottom - textBounds.top;  // text is drawn both on top and below the Y position

            if ((polyStretchX > textStretchX) && (polyStretchY > textStretchY)) {
                newTextPoint = new PointF();
                newTextPoint.x = centerX - (textStretchX / 2);
                newTextPoint.y = centerY + (textStretchY / 2);
            } else {
                newTextPoint = null;
            }


        } else {
            newTextPoint = null;
        }

        textPoint = newTextPoint;
        screenPath = newPath;
        doDraw = isInView;

        return doDraw;
    }
}
