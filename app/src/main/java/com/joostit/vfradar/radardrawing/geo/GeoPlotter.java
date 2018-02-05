package com.joostit.vfradar.radardrawing.geo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;

import com.joostit.vfradar.geo.GeoObject;
import com.joostit.vfradar.radardrawing.DrawableItem;
import com.joostit.vfradar.radardrawing.SphericalMercatorProjection;
import com.joostit.vfradar.radardrawing.ZoomLevelInfo;
import com.joostit.vfradar.utilities.Numbers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 25-1-2018.
 */
public class GeoPlotter extends DrawableItem {

    private Bitmap screenBuffer;
    private List<GeoShapePlot> shapePlots = new ArrayList<>();
    private Paint bitmapPaint;
    private Paint bitmapScaledPaint;
    private int bitmapScaledColor = 0xFF000000;
    private RedrawGeoDataTask redrawTask;
    private OnRedrawRequestHandler redrawHandler;
    private ZoomLevelInfo lastZoomlevel = null;
    private RectF screenBufferBounds;
    private double screenBufferScale = 1;

    public GeoPlotter(OnRedrawRequestHandler redrawHandler) {
        this.redrawHandler = redrawHandler;
        init();
    }

    @Override
    public void draw(Canvas canvas) {

        if (screenBuffer != null) {
            Rect bmpBounds = new Rect((int) screenBufferBounds.left, (int) screenBufferBounds.top, (int) screenBufferBounds.right, (int) screenBufferBounds.bottom);

            double newWidth = bmpBounds.width() * screenBufferScale;
            double newHeight = bmpBounds.height() * screenBufferScale;

            int xDisplacement = (int) Math.round((bmpBounds.width() - newWidth) / 2.0);
            int yDisplacement = (int) Math.round((bmpBounds.height() - newHeight) / 2.0);
            int plotWidth = (int) Math.round(newWidth);
            int plotHeight = (int) Math.round(newHeight);

            Rect plotBounds = new Rect(
                    bmpBounds.left + xDisplacement,
                    bmpBounds.top + yDisplacement,
                    bmpBounds.left + xDisplacement + plotWidth,
                    bmpBounds.top + yDisplacement + plotHeight);

            canvas.drawBitmap(screenBuffer, bmpBounds, plotBounds, getBitmapPaint());
        } else {
            canvas.toString();
        }
    }


    private Paint getBitmapPaint() {
        if (Numbers.isDoubleZero(screenBufferScale - 1)) {
            return bitmapPaint;
        } else {
            return bitmapScaledPaint;
        }
    }

    private void init() {
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(false);
        bitmapPaint.setFilterBitmap(false);
        bitmapPaint.setStyle(Paint.Style.STROKE);
        bitmapPaint.setDither(false);

        bitmapScaledPaint = new Paint();
        bitmapScaledPaint.setAntiAlias(false);
        bitmapScaledPaint.setFilterBitmap(false);
        bitmapScaledPaint.setStyle(Paint.Style.STROKE);
        bitmapScaledPaint.setDither(false);
        bitmapScaledPaint.setColor(bitmapScaledColor);
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {

        if (lastZoomlevel != null) {
            screenBufferScale = lastZoomlevel.rangeRadius / zoomLevelInfo.rangeRadius;
        } else {
            screenBufferScale = 1;
        }
        lastZoomlevel = zoomLevelInfo;

        if (redrawTask != null) {
            redrawTask.cancel(true);
        }

        redrawTask = new RedrawGeoDataTask(projection, bounds, zoomLevelInfo);
        redrawTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return false;
    }


    public void setData(List<GeoObject> data) {
        shapePlots.clear();

        for (GeoObject shape : data) {
            GeoShapePlot plot = new GeoShapePlot(shape);
            shapePlots.add(plot);
        }
    }


    private class RedrawGeoDataTask extends AsyncTask<Object, Void, Object> {

        private SphericalMercatorProjection projection;
        private RectF bounds;
        private ZoomLevelInfo newZoomLevel;

        RedrawGeoDataTask(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo newZoomLevel) {
            this.bounds = bounds;
            this.projection = projection;
            this.newZoomLevel = newZoomLevel;
        }

        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bmp = null;
            boolean itemsToDraw = false;

            for (GeoShapePlot plot : shapePlots) {
                if (isCancelled()) {
                    itemsToDraw = false;
                    break;
                }
                itemsToDraw = plot.updateDrawing(projection, bounds, newZoomLevel) ? true : itemsToDraw;
            }

            bmp = Bitmap.createBitmap((int) bounds.width(), (int) bounds.height(), Bitmap.Config.ARGB_8888);
            Canvas drawCanvas = new Canvas(bmp);

            if (itemsToDraw) {
                for (GeoShapePlot plot : shapePlots) {
                    if (isCancelled()) {
                        return false;
                    }
                    plot.draw(drawCanvas);
                }
            }

            screenBufferBounds = bounds;
            screenBuffer = bmp;
            screenBufferScale = 1;
            bmp.prepareToDraw();

            return true;
        }

        @Override
        protected void onPostExecute(Object na) {
            redrawHandler.onRedrawRequest();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    public interface OnRedrawRequestHandler {
        void onRedrawRequest();
    }
}
