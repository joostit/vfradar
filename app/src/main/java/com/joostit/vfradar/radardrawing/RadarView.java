package com.joostit.vfradar.radardrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.joostit.vfradar.SysConfig;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geodata.GeoObject;
import com.joostit.vfradar.site.ReportingPoint;
import com.joostit.vfradar.site.RouteLine;
import com.joostit.vfradar.site.Runway;
import com.joostit.vfradar.site.SiteFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joost on 15-1-2018.
 */

public class RadarView extends View implements GeoPlotter.OnRedrawRequestHandler {

    private final float maxMovementWhilePressing = 20;
    private final String ZOOM_IN = "ZoomIn";
    private final String ZOOM_OUT = "ZoomOut";
    private float touchDownX = -1;
    private float touchDownY = -1;
    private boolean isPressing;
    private float TOUCH_ACCURACY = 50;
    private float zoomButtonDimension = 70;
    private float zoomButtonSpacing = 70;
    private float zoomButtonY = 40;
    private OnRadarViewInteractionListener selectionListener;

    private List<DrawableItem> siteFeatures = new ArrayList<>();
    private List<AircraftPlot> plots = new ArrayList<>();
    private GeoPlotter geoPlot = new GeoPlotter(this);
    private Map<String, Button> buttons = new HashMap<>();
    private Crosshair crosshair;

    private ZoomLevelCalculator zoomLevels = new ZoomLevelCalculator();

    private Paint sitePaint;
    private int siteColor = 0x50ff9900;

    private SphericalMercatorProjection projection;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        init();
    }

    private void init() {

        sitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sitePaint.setStyle(Paint.Style.STROKE);
        sitePaint.setColor(siteColor);
        sitePaint.setStrokeWidth(20);

        // The world width is a fake value. It will be overwritten on the first drawing pass
        projection = new SphericalMercatorProjection(800);

        buttons.put(ZOOM_IN, new Button("+", 0, 0, zoomButtonDimension));
        buttons.put(ZOOM_OUT, new Button("-", 0, 0, zoomButtonDimension));

        crosshair = new Crosshair();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        redrawGraphics();
    }

    public void AttachSelectionListener(OnRadarViewInteractionListener radarViewFragment) {
        selectionListener = radarViewFragment;
    }

    public synchronized void UpdateAircraft(List<TrackedAircraft> ac) {
        updateAircraftPlotData(ac);
        redrawAircraftPlots();
        invalidate();
    }

    public synchronized void updateSiteFeatures(List<SiteFeature> site) {
        updateSiteFeaturePlotData(site);
        RecalculateSite();
        invalidate();
    }

    public synchronized void updateGeoData(List<GeoObject> geoData) {
        geoPlot.setData(geoData);
        geoPlot.updateDrawing(projection, getViewBounds(), zoomLevels.getZoomLevelInfo());
        invalidate();
    }

    private void updateSiteFeaturePlotData(List<SiteFeature> site) {

        siteFeatures.clear();
        for (SiteFeature feature : site) {

            DrawableItem plot = null;
            switch (feature.getType()) {

                case ReportingPoint:
                    plot = new ReportingPointPlot((ReportingPoint) feature);
                    break;

                case RouteLine:
                    plot = new RouteLinePlot((RouteLine) feature);
                    break;

                case Runway:
                    plot = new RunwayPlot((Runway) feature);
                    break;

                default:
                    System.out.println("Unsupported Site Feature type");
            }

            if (plot != null) {
                siteFeatures.add(plot);
            }
        }

    }


    private synchronized void redrawGraphics() {

        projection.setScreen(this.getHeight(), this.getWidth(), this.getHeight(), zoomLevels.getZoomLevelInfo().RangeRadius * 1.08, SysConfig.getCenterPosition());
        geoPlot.updateDrawing(projection, getViewBounds(), zoomLevels.getZoomLevelInfo());
        RecalculateSite();
        RecalculateButtons();
        calculateCrosshair();
        redrawAircraftPlots();
        invalidate();
    }

    private RectF getViewBounds() {
        return new RectF(0, 0, this.getWidth(), this.getHeight());
    }

    private void RecalculateSite() {
        for (DrawableItem feature : siteFeatures) {
            feature.updateDrawing(projection, getViewBounds(), zoomLevels.getZoomLevelInfo());
        }
    }

    private void RecalculateButtons() {
        buttons.get(ZOOM_IN).updatePosition(getZoomInButtonX(), getZoomButtonY());
        buttons.get(ZOOM_OUT).updatePosition(getZoomOutButtonX(), getZoomButtonY());
    }

    private void calculateCrosshair() {
        crosshair.updateDrawing(projection, zoomLevels.getZoomLevelInfo(), SysConfig.getCenterPosition(), this.getWidth(), this.getHeight());
    }

    @Override
    public void onRedrawRequest() {
        this.invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Black background
        canvas.drawARGB(255, 0, 0, 0);
        geoPlot.draw(canvas);
        crosshair.draw(canvas);
        drawSite(canvas);
        drawAllAircraft(canvas);
        drawButtons(canvas);
    }

    private float getZoomOutButtonX() {
        return this.getWidth() - (2 * zoomButtonSpacing) - (2 * zoomButtonDimension);
    }


    private float getZoomInButtonX() {
        return this.getWidth() - zoomButtonSpacing - zoomButtonDimension;
    }

    private float getZoomButtonY() {
        return zoomButtonY;
    }

    private void drawButtons(Canvas canvas) {

        for (Button button : buttons.values()) {
            button.draw(canvas);
        }

    }

    private synchronized void updateAircraftPlotData(List<TrackedAircraft> tracks) {

        List<AircraftPlot> toRemove = new ArrayList<AircraftPlot>(plots);

        for (TrackedAircraft track : tracks) {
            AircraftPlot plot = findPlotByTrackid(track.Data.trackId);

            if (plot == null) {
                plot = new AircraftPlot();
                plot.TrackId = track.Data.trackId;
                plots.add(plot);
            } else {
                toRemove.remove(plot);
            }

            plot.updateAircraftPlotData(track);
        }


        for (AircraftPlot removeMe : toRemove) {
            plots.remove(removeMe);
        }
    }


    private synchronized AircraftPlot findPlotByTrackid(int trackId) {
        AircraftPlot found = null;

        for (AircraftPlot plot : plots) {
            if (plot.TrackId == trackId) {
                found = plot;
                break;
            }
        }

        return found;
    }


    private synchronized void redrawAircraftPlots() {
        for (AircraftPlot plot : plots) {
            plot.updateDrawing(projection, getViewBounds(), zoomLevels.getZoomLevelInfo());
        }
    }


    private void drawSite(Canvas canvas) {

        for (DrawableItem feature : siteFeatures) {
            feature.draw(canvas);
        }

    }

    private synchronized void drawAllAircraft(Canvas canvas) {

        AircraftPlot deferredPlot = null;

        for (AircraftPlot ac : plots) {
            if (ac.isSelected) {
                deferredPlot = ac;
            } else {
                ac.draw(canvas);
            }
        }

        // Make sure to plot a selected aircraft always last, so on top of the Z-order
        if (deferredPlot != null) {
            deferredPlot.draw(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = event.getX();
                touchDownY = event.getY();
                isPressing = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (isPressing) {
                    float xMovement = Math.abs(event.getX() - touchDownX);
                    float yMovement = Math.abs(event.getY() - touchDownY);
                    if ((xMovement > maxMovementWhilePressing) || (yMovement > maxMovementWhilePressing)) {
                        isPressing = false;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if (isPressing) {
                    isPressing = false;
                    processTouchPressed(event);
                }
                break;
        }

        return true;
    }

    private synchronized void processTouchPressed(MotionEvent event) {

        if (processButtonTouchEvent(event)) {
            return;
        }

        if (processAircraftTouchEvent(event)) {
            return;
        }
    }

    private boolean processButtonTouchEvent(MotionEvent event) {

        boolean isHandled = false;
        float x = event.getX();
        float y = event.getY();

        if (buttons.get(ZOOM_IN).doHitTest(x, y)) {
            zoomIn();
            isHandled = true;
        } else if (buttons.get(ZOOM_OUT).doHitTest(x, y)) {
            zoomOut();
            isHandled = true;
        }

        return isHandled;
    }

    private void zoomOut() {
        zoomLevels.zoomOut();
        redrawGraphics();
    }

    private void zoomIn() {
        zoomLevels.zoomIn();
        redrawGraphics();
    }

    private boolean processAircraftTouchEvent(MotionEvent event) {

        boolean isHandled = true;

        float x = event.getX();
        float y = event.getY();
        double dist;

        AircraftPlot nearestHit = new AircraftPlot();
        double nearestDist = Double.MAX_VALUE;

        for (AircraftPlot ac : plots) {

            dist = getScreenDistance(x, y, ac.ScreenX, ac.ScreenY);
            if (dist < nearestDist) {
                nearestHit = ac;
                nearestDist = dist;
            }
        }

        if (nearestDist <= TOUCH_ACCURACY) {
            boolean wasSelected = nearestHit.isSelected;
            deselectAllPlots();
            nearestHit.isSelected = !wasSelected;
        } else {
            deselectAllPlots();
        }

        redrawAircraftPlots();
        invalidate();

        if (nearestHit.isSelected) {
            dispatchSelectionChanged(nearestHit.TrackId);
        } else {
            dispatchSelectionChanged(null);
        }

        return isHandled;
    }

    private void selectPlot(int trackId) {
        for (AircraftPlot ac : plots) {
            if (ac.TrackId == trackId) {
                ac.isSelected = true;
            }
        }
    }


    private void deselectAllPlots() {
        for (AircraftPlot ac : plots) {
            ac.isSelected = false;
        }
    }


    private void dispatchSelectionChanged(Integer trackId) {
        selectionListener.onUserSelectedAircraftChanged(trackId);
    }

    private double getScreenDistance(float x1, float y1, float x2, float y2) {
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return dist;
    }

    public void selectAircraft(Integer trackId) {

        deselectAllPlots();

        if (trackId != null) {
            selectPlot(trackId);
        }
        redrawAircraftPlots();
        invalidate();
    }


    public interface OnRadarViewInteractionListener {
        void onUserSelectedAircraftChanged(Integer TrackId);
    }


}
