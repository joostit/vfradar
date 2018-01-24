package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.joostit.vfradar.site.RouteLine;

/**
 * Created by Joost on 24-1-2018.
 */

public class RouteLinePlot extends DrawableItem {

    private RouteLine source;

    private Path screenPath = new Path();

    private int lineColor = 0xAAb3b300;
    private Paint linePaint;

    public RouteLinePlot(RouteLine source){
        this.source = source;
        init();
        
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(screenPath, linePaint);
    }

    @Override
    public void updateDrawing(SphericalMercatorProjection projection) {

        screenPath.rewind();

        if(source.points.size() ==0){
            return;
        }

        PointF point = projection.toScreenPoint(source.points.get(0));
        screenPath.moveTo(point.x, point.y);

        for(int i = 1; i < source.points.size(); i++){
            point = projection.toScreenPoint(source.points.get(i));
            screenPath.lineTo(point.x, point.y);
        }

    }
}
