package com.joostit.vfradar.radardrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 15-1-2018.
 */

public class RadarView extends View {


    private float TOUCH_ACCURACY = 50;
    private int currentZoomLevel = 3;
    private static final LatLon centerPosition = new LatLon(52.278758, 6.899437);
    private static DecimalFormat df1 = new DecimalFormat("#.#");

    private enum AircraftStates{
        None,
        Note,
        Warning,
        Selected
    }

    private float ring1Radius = 0;
    private float ring2Radius = 0;
    private float ring3Radius = 0;
    private String ring1Annot = "";
    private String ring2Annot = "";
    private String ring3Annot = "";


    private List<AircraftPlot> plots = new ArrayList<>();
    private ZoomLevelCalculator zoomLevels = new ZoomLevelCalculator();


    private Paint mTextPaint;
    private int mTextColor = Color.BLUE;
    private float mTextHeight;

    private Paint acNamePaint;
    private Paint acInfoPaint;
    private Paint crosshairPaint;
    private Paint aircraftForePaint;
    private Paint aircraftBackPaint;
    private Paint acTextGuideLinePaint;
    private Paint sitePaint;
    private Paint acWarningBoxPaint;
    private Paint acHighlightBoxPaint;
    private Paint acSelectedBoxPaint;
    private Paint crosshairTextPaint;

    private int crosshairColor = 0xFF003300;
    private int siteColor = 0x50ff9900;
    private int acForeColor = 0xFF00FF00;
    private int acBackColor = 0xFF008000;
    private int acNameTextColor = 0xFF00FF00;
    private int acInfoTextColor = 0xFF00AA00;
    private int acWarningBoxColor = 0xFFFF0000;
    private int acSelectedBoxColor = 0xFFFFFFFF;
    private int acHighlightBoxColor = 0xFFFFFF00;
    private int acTextGuideLineColor = 0xAA008000;
    private int crosshairTextColor = 0xAA008000;

    private SphericalMercatorProjection projection;

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();


    }


    private void init() {


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        crosshairPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairPaint.setStyle(Paint.Style.STROKE);
        crosshairPaint.setStrokeWidth(3);
        crosshairPaint.setColor(crosshairColor);

        aircraftForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftForePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftForePaint.setStrokeWidth(3);
        aircraftForePaint.setColor(acForeColor);

        aircraftBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftBackPaint.setStrokeWidth(8);
        aircraftBackPaint.setColor(acBackColor);

        sitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        sitePaint.setStyle(Paint.Style.STROKE);
        sitePaint.setColor(siteColor);
        sitePaint.setStrokeWidth(20);

        acNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acNamePaint.setStyle(Paint.Style.FILL);
        acNamePaint.setColor(acNameTextColor);
        acNamePaint.setTextSize(26);

        acInfoPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acInfoPaint.setStyle(Paint.Style.FILL);
        acInfoPaint.setColor(acInfoTextColor);
        acInfoPaint.setTextSize(22);

        acTextGuideLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acTextGuideLinePaint.setStyle(Paint.Style.STROKE);
        acTextGuideLinePaint.setStrokeWidth(2);
        acTextGuideLinePaint.setColor(acTextGuideLineColor);

        acWarningBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acWarningBoxPaint.setStyle(Paint.Style.FILL);
        acWarningBoxPaint.setColor(acWarningBoxColor);

        acHighlightBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acHighlightBoxPaint.setStyle(Paint.Style.FILL);
        acHighlightBoxPaint.setColor(acHighlightBoxColor);

        acSelectedBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acSelectedBoxPaint.setStyle(Paint.Style.FILL);
        acSelectedBoxPaint.setColor(acSelectedBoxColor);

        crosshairTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crosshairTextPaint.setStyle(Paint.Style.STROKE);
        crosshairTextPaint.setColor(crosshairTextColor);
        crosshairTextPaint.setTextSize(20);

        // The world width is a fake value. It will be overwritten on the first drawing pass
        projection = new SphericalMercatorProjection(800);
}



    public synchronized void UpdateAircraft(List<TrackedAircraft> ac){

        updateAircraftPlotData(ac);
        refreshDrawing();


    }

    private synchronized void refreshDrawing(){

        projection.setScreen(this.getHeight(), this.getWidth(), this.getHeight(), zoomLevels.getZoomLevelInfo(currentZoomLevel).RangeRadius * 1.08, centerPosition);
        RecalculateAircraftPlots();
        calculateCrosshair();
        invalidate();
    }

    private void calculateCrosshair(){

        ZoomLevelInfo zlInfo = zoomLevels.getZoomLevelInfo(currentZoomLevel);

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

        float width = getWidth();
        float height = getHeight();

        plotAllAircraft(canvas);

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

            updateAircraftPlotData(track, plot);
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


    private synchronized void updateAircraftPlotData(TrackedAircraft aircraft, AircraftPlot plot){

        String nameString = "";
        if(!IsNullOrEmpty(aircraft.Data.Reg)){
            nameString = aircraft.Data.Reg;
            if(!IsNullOrEmpty(aircraft.Data.CallSign)){
                nameString += " (" + aircraft.Data.CallSign + ")";
            }
        }
        else if(!IsNullOrEmpty(aircraft.Data.CallSign)){
            nameString = aircraft.Data.CallSign;
        }
        else if(!IsNullOrEmpty(aircraft.Data.Icao24)){
            nameString = aircraft.Data.Icao24;
        }
        else if(!IsNullOrEmpty(aircraft.Data.FlarmId)){
            nameString = aircraft.Data.FlarmId;
        }
        else if(!IsNullOrEmpty(aircraft.Data.OgnId)){
            nameString = aircraft.Data.OgnId;
        }

        if(!IsNullOrEmpty(aircraft.Data.Cn)){
            nameString += " (" + aircraft.Data.Cn + ")";
        }
        plot.DisplayName = nameString;

        String infoLineString = "";
        if(aircraft.Data.VRate != 0) {
            double vRateRounded = Math.round(aircraft.Data.VRate * 10) / 10.0;
            String plusSign = (aircraft.Data.VRate > 0.0) ? "+" : "";
            infoLineString = "    " + plusSign + df1.format(vRateRounded);
        }
        plot.InfoLine = aircraft.Data.Alt + infoLineString;

        plot.Track = aircraft.Data.Track;
        plot.lat = aircraft.Data.Lat;
        plot.lon = aircraft.Data.Lon;
        plot.isHighlighted = aircraft.isHighlighted;
        plot.isWarning = aircraft.isWarning;
    }


    private synchronized void RecalculateAircraftPlots(){
        for(AircraftPlot plot : plots) {
            PointF screenPoint = projection.toScreenPoint(plot.lat, plot.lon);
            plot.ScreenX = screenPoint.x;
            plot.ScreenY = screenPoint.y;
        }
    }

    private Boolean IsNullOrEmpty(String input){
        if((input != null) && (!input.trim().equals(""))){
            return false;
        }
        else{
            return true;
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

    private synchronized void plotAllAircraft(Canvas canvas){

        AircraftPlot deferredPlot = null;

        for(AircraftPlot ac : plots){
            if(ac.isSelected) {
                deferredPlot = ac;
            }
            else{
                drawAircraft(canvas, ac);
            }
        }

        // Make sure to plot a selected aircraft always last, so on top of the Z-order
        if(deferredPlot != null){
            drawAircraft(canvas, deferredPlot);
        }
    }

    private synchronized void drawAircraft(Canvas canvas, AircraftPlot ac){

        float arrowAngle = 135;
        float longArrowLength = 11;
        float shortArrowLength = 9;

        int boxSize = 25;
        int boxRound = 7;
        double shortLineLength = 20;
        double longLineLength = 22;

        // Copy numerical data only once to prevent multithreading inconsistencies
        Boolean hasTrack = (ac.Track != null);
        double track = hasTrack ? ac.Track : 0;
        float x  = ac.ScreenX;
        float y = ac.ScreenY;
        String nameLine = ac.DisplayName;
        String infoLine = ac.InfoLine;

        double arrRightBearing = track + arrowAngle;
        double arrLeftBearing = track - arrowAngle;

        double trackRad = track * Math.PI / 180.0;
        double arrRightRad = arrRightBearing * Math.PI / 180.0;
        double arrLeftRad = arrLeftBearing * Math.PI / 180.0;

        float shortEndX = x + (float) (shortLineLength * Math.sin(trackRad));
        float shortEndY = y - (float) (shortLineLength * Math.cos(trackRad));

        float longEndX = x + (float) (longLineLength * Math.sin(trackRad));
        float longEndY = y - (float) (longLineLength * Math.cos(trackRad));

        float longArrRightX = shortEndX + (float) (longArrowLength * Math.sin(arrRightRad));
        float longArrRightY = shortEndY - (float) (longArrowLength * Math.cos(arrRightRad));

        float shortArrRightX = shortEndX + (float) (shortArrowLength * Math.sin(arrRightRad));
        float shortArrRightY = shortEndY - (float) (shortArrowLength * Math.cos(arrRightRad));

        float longArrLeftX = shortEndX + (float) (longArrowLength * Math.sin(arrLeftRad));
        float longArrLeftY = shortEndY - (float) (longArrowLength * Math.cos(arrLeftRad));

        float shortArrLeftX = shortEndX + (float) (shortArrowLength * Math.sin(arrLeftRad));
        float shortArrLeftY = shortEndY - (float) (shortArrowLength * Math.cos(arrLeftRad));

        if(ac.isWarning){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acWarningBoxPaint);
        }
        if(ac.isHighlighted){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acHighlightBoxPaint);
        }

        if(ac.isSelected){
            boxSize += 5;
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acSelectedBoxPaint);
        }

        canvas.drawLine(x, y, x + 18, y - 55, acTextGuideLinePaint);

        // Track arrow line
        if(hasTrack) {
            canvas.drawLine(shortEndX, shortEndY, shortArrLeftX, shortArrLeftY, aircraftBackPaint);
            canvas.drawLine(shortEndX, shortEndY, shortArrRightX, shortArrRightY, aircraftBackPaint);
            canvas.drawLine(x, y, shortEndX, shortEndY, aircraftBackPaint);
        }

        canvas.drawCircle(x, y, 7, aircraftBackPaint);

        if(hasTrack) {
            canvas.drawLine(x, y, longEndX, longEndY, aircraftForePaint);
        }

        canvas.drawCircle(x, y, 6, aircraftForePaint);

        if(hasTrack) {
            canvas.drawLine(longEndX, longEndY, longArrRightX, longArrRightY, aircraftForePaint);
            canvas.drawLine(longEndX, longEndY, longArrLeftX, longArrLeftY, aircraftForePaint);
        }

        canvas.drawText(nameLine, x + 20, y - 64, acNamePaint);
        canvas.drawText(infoLine, x + 20, y - 40, acInfoPaint);
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


}
