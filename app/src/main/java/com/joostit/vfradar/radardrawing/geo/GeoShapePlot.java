package com.joostit.vfradar.radardrawing.geo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.geo.GeoShape;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.geo.GeoPolygon;
import com.joostit.vfradar.geo.LatLonRect;
import com.joostit.vfradar.radardrawing.DrawableItem;
import com.joostit.vfradar.radardrawing.SphericalMercatorProjection;
import com.joostit.vfradar.radardrawing.ZoomLevelInfo;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoShapePlot extends DrawableItem {

    public GeoObject source;

    private boolean doDraw = false;
    private Path screenPath = new Path();
    private Paint polygonStrokePaint;
    private Paint polygonFillPaint;
    private Paint textPaint;
    private PointF textPoint = new PointF(0, 0);


    public GeoShapePlot(GeoObject source) {
        this.source = source;
        init();
    }

    private void init() {
        polygonStrokePaint = GeoShapePaints.createPolygonForePaint(source.getObjectType());
        polygonFillPaint = GeoShapePaints.createPolygonBackPaint(source.getObjectType());
        textPaint = GeoShapePaints.createPolygonTextPaint(source.getObjectType());
    }


    @Override
    public void draw(Canvas canvas) {
        if (doDraw) {
            if (screenPath != null) {

                if(polygonFillPaint != null) {
                    canvas.drawPath(screenPath, polygonFillPaint);
                }

                if(polygonStrokePaint != null){
                    canvas.drawPath(screenPath, polygonStrokePaint);
                }

                if (textPoint != null) {
                    if(textPaint != null) {
                        canvas.drawText(source.name, 0, source.name.length(), textPoint.x, textPoint.y, textPaint);
                    }
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


        // Determine if the square bounding rectangle is anywhere within the screen bounds.
        // Using only the bounding rectangle saves us from iterating though tens of thousands of Points
        // on each redraw, thus increasing performance
        LatLonRect geoBounds = source.getBoundingRect();
        LatLon bottomLeft = new LatLon(geoBounds.leftLat, geoBounds.bottomLon);
        LatLon topLeft = new LatLon(geoBounds.leftLat, geoBounds.topLon);
        LatLon topRight = new LatLon(geoBounds.rightLat, geoBounds.topLon);
        LatLon bottomRight = new LatLon(geoBounds.rightLat, geoBounds.bottomLon);

        PointF bottomLeftScreen = projection.toScreenPoint(bottomLeft);
        PointF topLeftScreen = projection.toScreenPoint(topLeft);
        PointF topRightScreen = projection.toScreenPoint(topRight);
        PointF bottomRightScreen = projection.toScreenPoint(bottomRight);

        if (bounds.contains(bottomLeftScreen.x, bottomLeftScreen.y)
                || bounds.contains(topLeftScreen.x, topLeftScreen.y)
                || bounds.contains(topRightScreen.x, topRightScreen.y)
                || bounds.contains(bottomRightScreen.x, bottomRightScreen.y)) {
            isInView = true;
        } else {
            doDraw = false;
            return false;
        }

        for (GeoPolygon polygon : source.shape.polygons) {

            if (polygon.points.size() < 2) {
                continue;
            }

            LatLon startlatLon = polygon.points.get(0);
            PointF startPoint = projection.toScreenPoint(startlatLon);

            newPath.moveTo(startPoint.x, startPoint.y);
            sumX += startPoint.x;
            sumY += startPoint.y;
            pointCount++;

            for (int i = 1; i < polygon.points.size(); i++) {
                LatLon pos = polygon.points.get(i);
                PointF screenPoint = projection.toScreenPoint(pos);
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

        if (isInView) {
            float centerX = (float) (sumX / pointCount);
            float centerY = (float) (sumY / pointCount);
            float polyStretchX = maxX - minX;
            float polyStretchY = maxY - minY;

            Rect textBounds = new Rect();
            textPaint.getTextBounds(source.name, 0, source.name.length(), textBounds);

            int textStretchX = textBounds.right;
            int textStretchY = textBounds.bottom - textBounds.top;

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
