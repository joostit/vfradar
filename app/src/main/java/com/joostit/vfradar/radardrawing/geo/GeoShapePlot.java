package com.joostit.vfradar.radardrawing.geo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.geo.GeoPolygon;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.LatLonRect;
import com.joostit.vfradar.radardrawing.DrawableItem;
import com.joostit.vfradar.radardrawing.SphericalMercatorProjection;
import com.joostit.vfradar.radardrawing.ZoomLevelInfo;
import com.joostit.vfradar.utilities.XyUtils;

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

                if (polygonFillPaint != null) {
                    canvas.drawPath(screenPath, polygonFillPaint);
                }

                if (polygonStrokePaint != null) {
                    canvas.drawPath(screenPath, polygonStrokePaint);
                }

                if (textPoint != null) {
                    if (textPaint != null) {
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
        PointF newTextPoint = null;
        double sumX = 0;
        double sumY = 0;
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        int pointCount = 0;
        boolean shouldDraw = false;
        boolean hasPointInScreenBounds = false;
        boolean isInScreenBounds = false;
        boolean hasIntersectInScreenBounds = false;

        LatLonRect geoBounds = source.getBoundingRect();
        RectF screenBounds = projection.toScreenRect(geoBounds);

        boolean shapeBoundsIntersectWithScreen = RectF.intersects(screenBounds, bounds);

        if (shapeBoundsIntersectWithScreen) {
            isInScreenBounds = true;
        } else {
            doDraw = false;
            return false;
        }

        for (GeoPolygon polygon : source.shape.polygons) {
            if (polygon.points.size() < 2) {
                continue;
            }

            PointF previousPoint = projection.toScreenPoint(polygon.points.get(polygon.points.size() - 1));
            LatLon startlatLon = polygon.points.get(0);
            PointF startPoint = projection.toScreenPoint(startlatLon);
            hasIntersectInScreenBounds = lineIntersectsBounds(bounds, previousPoint, startPoint) ? true : hasPointInScreenBounds;

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

                if (bounds.contains(screenPoint.x, screenPoint.y)) {
                    hasPointInScreenBounds = true;
                }

                // as long as we don't know whether we have points on screen and we don't know
                // if there's a line crossing the screen, keep searching for it.
                // If either one becomes true, we will draw this shape
                if (!hasPointInScreenBounds) {
                    if (!hasIntersectInScreenBounds) {
                        hasIntersectInScreenBounds = lineIntersectsBounds(bounds, previousPoint, screenPoint) ? true : hasPointInScreenBounds;
                    }
                }

                sumX += screenPoint.x;
                sumY += screenPoint.y;
                pointCount++;
                previousPoint = screenPoint;
            }

            newPath.close();
        }


        // Only draw if the shape is within view of the screen and if there ar points or lines in view.
        // If we're fully zoomed in on the shape and don't see the outlines of it, we don't want to draw
        shouldDraw = isInScreenBounds && (hasPointInScreenBounds || hasIntersectInScreenBounds);

        if (shouldDraw) {
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
        doDraw = shouldDraw;

        return doDraw;
    }


    private boolean lineIntersectsBounds(RectF bounds, PointF p1, PointF p2) {

        PointF intersectLeft = XyUtils.getLineIntersection(p1, p2, new PointF(bounds.left, bounds.top), new PointF(bounds.left, bounds.bottom));
        PointF intersectTop = XyUtils.getLineIntersection(p1, p2, new PointF(bounds.left, bounds.top), new PointF(bounds.right, bounds.top));
        PointF intersectRight = XyUtils.getLineIntersection(p1, p2, new PointF(bounds.right, bounds.top), new PointF(bounds.right, bounds.bottom));
        PointF intersectBottom = XyUtils.getLineIntersection(p1, p2, new PointF(bounds.right, bounds.bottom), new PointF(bounds.left, bounds.bottom));

        return ((intersectLeft != null)
                || (intersectTop != null)
                || (intersectRight != null)
                || (intersectBottom != null));


    }

}
