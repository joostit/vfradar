package com.joostit.vfradar.radardrawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.joostit.vfradar.geodata.GeoObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */

public class GeoPlotter extends DrawableItem {

    private boolean doDraw = false;
    private List<GeoShapePlot> shapePlots = new ArrayList<>();
    private Paint bitmapPaint;
    private RedrawGeoDataTask redrawTask;
    private OnRedrawRequestHandler redrawHandler;

    public GeoPlotter(OnRedrawRequestHandler redrawHandler) {
        this.redrawHandler = redrawHandler;
        init();
    }

    @Override
    public void draw(Canvas canvas) {

        if (doDraw) {
            for (GeoShapePlot plot : shapePlots) {
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

        if(redrawTask != null){
            redrawTask.cancel(true);
        }

        doDraw = false;

        redrawTask = new RedrawGeoDataTask(this, projection, bounds);
        redrawTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return false;
    }


    public void setData(List<GeoObject> data) {

        shapePlots.clear();

        for (GeoObject shape :
                data) {
            GeoShapePlot plot = new GeoShapePlot(shape);
            shapePlots.add(plot);
        }

    }


    private class RedrawGeoDataTask extends AsyncTask<Object, Void, Object> {

        private GeoPlotter initiator;
        private SphericalMercatorProjection projection;
        private RectF bounds;

        RedrawGeoDataTask(GeoPlotter initiator, SphericalMercatorProjection projection, RectF bounds) {
            this.initiator = initiator;
            this.bounds = bounds;
            this.projection = projection;
        }

        @Override
        protected Object doInBackground(Object... params) {

            boolean itemsToDraw = false;

            for (GeoShapePlot plot : shapePlots) {
                if(isCancelled()){
                    itemsToDraw = false;
                    break;
                }
                itemsToDraw = plot.updateDrawing(projection, bounds) ? true : itemsToDraw;
            }

            doDraw = itemsToDraw;

            return true;
        }

        @Override
        protected void onPostExecute(Object na) {
            if((redrawHandler != null) && (doDraw)){
                redrawHandler.onRedrawRequest();
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    public interface OnRedrawRequestHandler{
        void onRedrawRequest();
    }
}
