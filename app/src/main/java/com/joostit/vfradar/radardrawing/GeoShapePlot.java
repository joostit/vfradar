package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
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

    private int lineColor = 0xAAb3b300;
    private Paint linePaint;
    private Paint textPaint;
    private int textColor = 0xFFb3b300;
    private PointF textPoint = new PointF(0,0);


    public GeoShapePlot(GeoObject source){
        this.source = source;
        init();
    }


    private void init(){
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(20);

    }


    @Override
    public void draw(Canvas canvas) {
        if(doDraw){
            if(screenPath != null){
                canvas.drawPath(screenPath, linePaint);
                canvas.drawText(source.name, 0, source.name.length(), textPoint.x, textPoint.y, textPaint);
            }
        }
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds) {
        Path newPath = new Path();
        boolean isInView = false;

        double sumX = 0;
        double sumY = 0;
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

            for (int i = 1; i < polygon.size(); i++) {
                LatLon pos = polygon.get(i);
                PointF screenPoint = projection.toScreenPoint(pos);

                if (bounds.contains(screenPoint.x, screenPoint.y)) {
                    isInView = true;
                }

                newPath.lineTo(screenPoint.x, screenPoint.y);

                sumX += screenPoint.x;
                sumY += screenPoint.y;
                pointCount++;
            }

            newPath.close();
        }

        if (isInView){
            textPoint.x = (float) (sumX / pointCount);
            textPoint.y = (float) (sumY / pointCount);
        }

        screenPath = newPath;
        doDraw = isInView;

        return doDraw;
    }
}
