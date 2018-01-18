package com.joostit.vfradar.radardrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.utilities.DistanceString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Joost on 15-1-2018.
 */

public class RadarView extends View {


    private float TOUCH_ACCURACY = 50;
    private static final LatLon centerPosition = new LatLon(52.278758, 6.899437);

    private float zoomButtonDimension = 70;
    private float zoomButtonSpacing = 70;

    private final String ZOOM_IN = "ZoomIn";
    private final String ZOOM_OUT = "ZoomOUT";


    private OnRadarViewInteractionListener selectionListener;

    private float ring1Radius = 0;
    private float ring2Radius = 0;
    private float ring3Radius = 0;
    private String ring1Annot = "";
    private String ring2Annot = "";
    private String ring3Annot = "";

    private List<AircraftPlot> plots = new ArrayList<>();
    private Map<String, Button> buttons = new HashMap<>();
    private ZoomLevelCalculator zoomLevels = new ZoomLevelCalculator();

    private Paint crosshairPaint;
    private Paint sitePaint;
    private Paint crosshairTextPaint;

    private int crosshairColor = 0xFF003300;
    private int siteColor = 0x50ff9900;
    private int crosshairTextColor = 0xAA008000;

    private SphericalMercatorProjection projection;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {

        crosshairPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairPaint.setStyle(Paint.Style.STROKE);
        crosshairPaint.setStrokeWidth(3);
        crosshairPaint.setColor(crosshairColor);

        sitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sitePaint.setStyle(Paint.Style.STROKE);
        sitePaint.setColor(siteColor);
        sitePaint.setStrokeWidth(20);

        crosshairTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairTextPaint.setStyle(Paint.Style.STROKE);
        crosshairTextPaint.setColor(crosshairTextColor);
        crosshairTextPaint.setTextSize(20);

        // The world width is a fake value. It will be overwritten on the first drawing pass
        projection = new SphericalMercatorProjection(800);

        buttons.put(ZOOM_IN, new Button("+", 0, 0, zoomButtonDimension));
        buttons.put(ZOOM_OUT, new Button("-", 0, 0, zoomButtonDimension));
}


    public void AttachSelectionListener(OnRadarViewInteractionListener radarViewFragment) {
        selectionListener = radarViewFragment;
    }

    public synchronized void UpdateAircraft(List<TrackedAircraft> ac){

        updateAircraftPlotData(ac);
        refreshDrawing();
    }

    private synchronized void refreshDrawing(){

        projection.setScreen(this.getHeight(), this.getWidth(), this.getHeight(), zoomLevels.getZoomLevelInfo().RangeRadius * 1.08, centerPosition);
        RecalculateButtons();
        RecalculateAircraftPlots();
        calculateCrosshair();
        invalidate();
    }

    private void RecalculateButtons() {
        buttons.get(ZOOM_IN).updatePosition(getZoomInButtonX(), getZoomButtonY());
        buttons.get(ZOOM_OUT).updatePosition(getZoomOutButtonX(), getZoomButtonY());
    }

    private void calculateCrosshair(){

        ZoomLevelInfo zlInfo = zoomLevels.getZoomLevelInfo();

        ring1Annot = DistanceString.getString(zlInfo.RingRadius1);
        ring2Annot = DistanceString.getString(zlInfo.RingRadius2);
        ring3Annot = DistanceString.getString(zlInfo.RingRadius3);

        LatLon ring1NorthPoint = centerPosition.Move(0, zlInfo.RingRadius1);
        LatLon ring2NorthPoint = centerPosition.Move(0, zlInfo.RingRadius2);
        LatLon ring3NorthPoint = centerPosition.Move(0, zlInfo.RingRadius3);

        PointF ring1ScreenTop = projection.toScreenPoint(ring1NorthPoint.Latitude, ring1NorthPoint.Longitude);
        PointF ring2ScreenTop = projection.toScreenPoint(ring2NorthPoint.Latitude, ring2NorthPoint.Longitude);
        PointF ring3ScreenTop = projection.toScreenPoint(ring3NorthPoint.Latitude, ring3NorthPoint.Longitude);

        float centerY = getCenterY();

        ring1Radius = centerY - ring1ScreenTop.y;
        ring2Radius = centerY - ring2ScreenTop.y;
        ring3Radius = centerY - ring3ScreenTop.y;
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Black background
        canvas.drawARGB(255, 0, 0, 0);

        drawCrosshair(canvas);
        drawSite(canvas);

        drawAllAircraft(canvas);

        drawButtons(canvas);

    }

    private float getZoomOutButtonX(){
       return this.getWidth() - (2 * zoomButtonSpacing) - (2 * zoomButtonDimension);
    }


    private float getZoomInButtonX(){
        return this.getWidth() - zoomButtonSpacing - zoomButtonDimension;
    }

    private float getZoomButtonY(){
        return zoomButtonSpacing;
    }

    private void drawButtons(Canvas canvas) {

        for(Button button : buttons.values()) {
            button.Draw(canvas);
        }

    }

    private float getCenterX(){
        return getWidth() / 2;
    }

    private float getCenterY(){
        return getHeight() / 2;
    }

    private void drawCrosshair(Canvas canvas){
        float width = getWidth();
        float height = getHeight();
        float centerX = getCenterX();
        float centerY = getCenterY();

        canvas.drawLine(centerX, 0, centerX, height, crosshairPaint);

        canvas.drawLine(0, centerY, width, centerY, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring1Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring2Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, ring3Radius, crosshairPaint);

        canvas.drawText(ring1Annot, centerX + 5, centerY - ring1Radius - 10, crosshairTextPaint);
        canvas.drawText(ring2Annot, centerX + 5, centerY - ring2Radius - 10, crosshairTextPaint);
        canvas.drawText(ring3Annot, centerX + 5, centerY - ring3Radius - 10, crosshairTextPaint);
    }

    private synchronized void updateAircraftPlotData(List<TrackedAircraft> tracks){
        // ToDo: remove deleted aircraft

        for(TrackedAircraft track : tracks){
            AircraftPlot plot = findPlotByTrackid(track.Data.Trackid);

            if(plot == null){
                plot = new AircraftPlot();
                plot.TrackId = track.Data.Trackid;
                plots.add(plot);
            }

            plot.updateAircraftPlotData(track);
        }
    }


    private synchronized AircraftPlot findPlotByTrackid(int trackId){
        AircraftPlot found = null;

        for(AircraftPlot plot : plots){
            if(plot.TrackId == trackId){
                found = plot;
                break;
            }
        }

        return found;
    }





    private synchronized void RecalculateAircraftPlots(){
        for(AircraftPlot plot : plots) {
            PointF screenPoint = projection.toScreenPoint(plot.lat, plot.lon);
            plot.ScreenX = screenPoint.x;
            plot.ScreenY = screenPoint.y;
        }
    }



    private void drawSite(Canvas canvas){
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        Path runway = new Path();
        runway.moveTo(centerX + 30, centerY - 60);
        runway.lineTo(centerX - 276, centerY + 197);
        canvas.drawPath(runway, sitePaint);
    }

    private synchronized void drawAllAircraft(Canvas canvas){

        AircraftPlot deferredPlot = null;

        for(AircraftPlot ac : plots){
            if(ac.isSelected) {
                deferredPlot = ac;
            }
            else{
                ac.Draw(canvas);
            }
        }

        // Make sure to plot a selected aircraft always last, so on top of the Z-order
        if(deferredPlot != null){
            deferredPlot.Draw(canvas);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processTouchDown(event);
                break;
        }

        return true;
    }

    private synchronized void processTouchDown(MotionEvent event) {

        if(processButtonTouchEvent(event)){
            return;
        }

        if(processAircraftTouchEvent(event)){
            return;
        }
    }

    private boolean processButtonTouchEvent(MotionEvent event) {

        boolean isHandled = false;
        float x = event.getX();
        float y = event.getY();

        if(buttons.get(ZOOM_IN).DoHitTest(x, y)){
            zoomIn();
            isHandled = true;
        }
        else if(buttons.get(ZOOM_OUT).DoHitTest(x, y)){
            zoomOut();
            isHandled = true;
        }

        return isHandled;
    }

    private void zoomOut() {
        zoomLevels.zoomOut();
        refreshDrawing();
    }

    private void zoomIn() {
        zoomLevels.zoomIn();
        refreshDrawing();
    }

    private boolean processAircraftTouchEvent(MotionEvent event){

        boolean isHandled = true;

        float x = event.getX();
        float y = event.getY();
        double dist;

        AircraftPlot nearestHit = new AircraftPlot();
        double nearestDist = Double.MAX_VALUE;

        for(AircraftPlot ac : plots){

            dist = getScreenDistance(x, y, ac.ScreenX, ac.ScreenY);
            if(dist < nearestDist){
                nearestHit = ac;
                nearestDist = dist;
            }
        }

        if (nearestDist <= TOUCH_ACCURACY) {
            boolean wasSelected = nearestHit.isSelected;
            deselectAllPlots();
            nearestHit.isSelected = !wasSelected;
        }
        else{
            deselectAllPlots();
        }

        refreshDrawing();

        if(nearestHit.isSelected){
            dispatchSelectionChanged(nearestHit.TrackId);
        }
        else{
            dispatchSelectionChanged(null);
        }

        return isHandled;
    }

    private void dispatchSelectionChanged(Integer trackId) {
        selectionListener.onUserSelectedAircraftChanged(trackId);
    }

    private void deselectAllPlots(){
        for (AircraftPlot ac : plots) {
            ac.isSelected = false;
        }
    }


    private double getScreenDistance(float x1, float y1, float x2, float y2){
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        return dist;
    }


    public interface OnRadarViewInteractionListener{
        void onUserSelectedAircraftChanged(Integer TrackId);
    }


}
