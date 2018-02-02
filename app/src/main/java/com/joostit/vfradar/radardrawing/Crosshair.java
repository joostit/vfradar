package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.utilities.DistanceString;

/**
 * Created by Joost on 18-1-2018.
 */

public class Crosshair extends DrawableItem {


    private float ring1Radius = 0;
    private float ring2Radius = 0;
    private float ring3Radius = 0;
    private String ring1Annot = "";
    private String ring2Annot = "";
    private String ring3Annot = "";

    private Paint crosshairPaint;
    private Paint crosshairTextPaint;

    private int crosshairColor = 0xFF003300;
    private int crosshairTextColor = 0xFF008000;

    private float width;
    private float height;

    public Crosshair() {
        init();
    }


    private void init() {

        crosshairTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairTextPaint.setStyle(Paint.Style.STROKE);
        crosshairTextPaint.setColor(crosshairTextColor);
        crosshairTextPaint.setTextSize(20);

        crosshairPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairPaint.setStyle(Paint.Style.STROKE);
        crosshairPaint.setStrokeWidth(3);
        crosshairPaint.setColor(crosshairColor);
    }


    public void updateDrawing(SphericalMercatorProjection projection, ZoomLevelInfo zlInfo, LatLon centerPosition, float width, float height) {

        this.width = width;
        this.height = height;
        float centerY = height / 2;

        ring1Annot = DistanceString.getString(zlInfo.ringRadius1);
        ring2Annot = DistanceString.getString(zlInfo.ringRadius2);
        ring3Annot = DistanceString.getString(zlInfo.ringRadius3);

        LatLon ring1NorthPoint = centerPosition.Move(0, zlInfo.ringRadius1);
        LatLon ring2NorthPoint = centerPosition.Move(0, zlInfo.ringRadius2);
        LatLon ring3NorthPoint = centerPosition.Move(0, zlInfo.ringRadius3);

        PointF ring1ScreenTop = projection.toScreenPoint(ring1NorthPoint.latitude, ring1NorthPoint.longitude);
        PointF ring2ScreenTop = projection.toScreenPoint(ring2NorthPoint.latitude, ring2NorthPoint.longitude);
        PointF ring3ScreenTop = projection.toScreenPoint(ring3NorthPoint.latitude, ring3NorthPoint.longitude);

        ring1Radius = centerY - ring1ScreenTop.y;
        ring2Radius = centerY - ring2ScreenTop.y;
        ring3Radius = centerY - ring3ScreenTop.y;
    }

    @Override
    public void draw(Canvas canvas) {

        float centerY = height / 2;
        float centerX = width / 2;

        canvas.drawLine(centerX, 0, centerX, height, crosshairPaint);

        canvas.drawLine(0, centerY, width, centerY, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring1Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring2Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring3Radius, crosshairPaint);

        canvas.drawText(ring1Annot, centerX + 5, centerY - ring1Radius - 10, crosshairTextPaint);
        canvas.drawText(ring2Annot, centerX + 5, centerY - ring2Radius - 10, crosshairTextPaint);
        canvas.drawText(ring3Annot, centerX + 5, centerY - ring3Radius - 10, crosshairTextPaint);
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {
        // No specific implementation. Use the other overload for this
        return false;
    }
}
