package com.joostit.vfradar.radardrawing;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.joostit.vfradar.geodata.GeoDataPolygon;
import com.joostit.vfradar.site.RouteLine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoPlotter extends DrawableItem {

    Bitmap screenBuffer = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

    private Path screenPath = new Path();
    private List<GeoDataPolygon> geoData = new ArrayList<>();
    private int lineColor = 0xAAb3b300;
    private Paint linePaint;
    private Paint bitmapPaint;

    @Override
    public void draw(Canvas canvas) {
        if(screenBuffer != null){
            //canvas.drawb
        }
    }


    private void init() {

        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setStrokeWidth(1);
        bitmapPaint.setColor(Color.BLUE);
        bitmapPaint.setStyle(Paint.Style.STROKE);
        bitmapPaint.setDither(false);



        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
        linePaint.setColor(lineColor);
    }

    @Override
    public void updateDrawing(SphericalMercatorProjection projection) {

        screenBuffer = null;
        Bitmap drawBuffer = Bitmap.createBitmap(projection.screenWidth(), projection.screenHight(), Bitmap.Config.ARGB_8888);

        screenPath.rewind();
//
//        if(source.points.size() ==0){
//            return;
//        }
//
//        PointF point = projection.toScreenPoint(source.points.get(0));
//        screenPath.moveTo(point.x, point.y);
//
//        for(int i = 1; i < source.points.size(); i++){
//            point = projection.toScreenPoint(source.points.get(i));
//            screenPath.lineTo(point.x, point.y);
//        }

    }


    public void setData(List<GeoDataPolygon> data) {
        this.geoData = data;
    }
}
