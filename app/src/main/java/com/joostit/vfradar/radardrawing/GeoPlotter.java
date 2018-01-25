package com.joostit.vfradar.radardrawing;

import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.joostit.vfradar.geodata.GeoShapeData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoPlotter extends DrawableItem {

    Bitmap screenBuffer = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    private boolean doDraw = false;
    private List<GeoShapePlot> shapePlots = new ArrayList<>();
    private Paint bitmapPaint;


    public GeoPlotter(){
        init();
    }

    @Override
    public void draw(Canvas canvas) {

        if (doDraw) {
            for (GeoShapePlot plot: shapePlots){
                plot.draw(canvas);
            }

        }
    }


    private void init() {
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setStrokeWidth(1);
        bitmapPaint.setColor(Color.TRANSPARENT);
        bitmapPaint.setStyle(Paint.Style.STROKE);
        bitmapPaint.setDither(false);
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds) {

        doDraw = false;
        boolean itemsToDraw = false;

        for (GeoShapePlot plot: shapePlots) {
            itemsToDraw = plot.updateDrawing(projection, bounds) ? true : itemsToDraw;
        }

        doDraw = itemsToDraw;
        return doDraw;
    }


    public void setData(List<GeoShapeData> data) {

        shapePlots.clear();

        for (GeoShapeData shape :
                data) {
            GeoShapePlot plot = new GeoShapePlot(shape);
            shapePlots.add(plot);

            if(shape.name.equalsIgnoreCase("Enschede")){
                shape.toString();
            }
        }

    }
}
