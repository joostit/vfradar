package com.joostit.vfradar.radardrawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.joostit.vfradar.data.JSonTrackedAircraft;
import com.joostit.vfradar.data.TrackedAircraft;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joost on 15-1-2018.
 */

public class RadarView extends View {

    private static DecimalFormat df1 = new DecimalFormat("#.#");

    private enum AircraftStates{
        None,
        Note,
        Warning,
        Selected
    }

    private List<AircraftPlot> plots = new ArrayList<>();

    private Paint mTextPaint;
    private int mTextColor = Color.BLUE;
    private float mTextHeight;
    private Boolean mShowText = true;
    private int mTextWidth = 70;

    private Paint acNamePaint;
    private Paint acInfoPaint;
    private Paint crosshairPaint;
    private Paint aircraftForePaint;
    private Paint aircraftBackPaint;
    private Paint acTextGuideLinePaint;
    private Paint sitePaint;
    private Paint acWarningBoxPaint;
    private Paint acNoteBoxPaint;
    private Paint acSelectedBoxPaint;
    private Paint circuitPaint;

    private int crosshairColor = 0xFF003300;
    private int siteColor = 0x50ff9900;
    private int acForeColor = 0xFF00FF00;
    private int acBackColor = 0xFF008000;
    private int acTextGuideLineColor = 0xAA008000;
    private int acNameTextColor = 0xFF00FF00;
    private int acInfoTextColor = 0xFF00AA00;
    private int acWarningBoxColor = 0xFFFF0000;
    private int acSelectedBoxColor = 0xFFFFFFFF;
    private int acNoteBoxColor = 0xFFFFFF00;
    private int circuitColor = 0x70ff9900;

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

        acNoteBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acNoteBoxPaint.setStyle(Paint.Style.FILL);
        acNoteBoxPaint.setColor(acNoteBoxColor);

        acSelectedBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acSelectedBoxPaint.setStyle(Paint.Style.FILL);
        acSelectedBoxPaint.setColor(acSelectedBoxColor);


        circuitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circuitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circuitPaint.setColor(circuitColor);
        circuitPaint.setStrokeWidth(20);
}



    public synchronized void UpdateAircraft(List<TrackedAircraft> ac){
        updateAircraftPlotObjects(ac);
        invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Black background
        canvas.drawARGB(255, 0, 0, 0);

        drawCrosshair(canvas);
        drawSite(canvas);

        float width = getWidth();
        float height = getHeight();
        float cX = width / 2;
        float cY = height / 2;

        plotAllAircraft(canvas);

    }



    private void drawCrosshair(Canvas canvas){
        float width = getWidth();
        float height = getHeight();
        float centerX = width / 2;
        float centerY = height / 2;

        // Draw crosshair
        float minDimension = width < height ? width : height;
        float circle1Radius = minDimension / 8;
        float circle2Radius = minDimension / 4;
        float circle3Radius = minDimension / 2;

        canvas.drawLine(centerX, 0, centerX, height, crosshairPaint);
        canvas.drawLine(0, centerY, width, centerY, crosshairPaint);
        canvas.drawCircle(centerX, centerY, circle1Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, circle2Radius, crosshairPaint);
        canvas.drawCircle(centerX, centerY, circle3Radius, crosshairPaint);

    }

    private synchronized void updateAircraftPlotObjects(List<TrackedAircraft> tracks){
        for(TrackedAircraft track : tracks){
            AircraftPlot plot = findPlotByTrackid(track.Data.Trackid);

            if(plot == null){
                plot = new AircraftPlot();
                plot.TrackId = track.Data.Trackid;
                plots.add(plot);
            }

            updateAircraftPlot(track, plot);
        }
    }


    private AircraftPlot findPlotByTrackid(int trackId){
        AircraftPlot found = null;

        for(AircraftPlot plot : plots){
            if(plot.TrackId == trackId){
                found = plot;
                break;
            }
        }

        return found;
    }


    private synchronized void updateAircraftPlot(TrackedAircraft aircraft, AircraftPlot plot){

        String vRateString = "";
        String nameString = "";

        if(!IsNullOrEmpty(aircraft.Data.Reg)){
            nameString = aircraft.Data.Reg;
            if(IsNullOrEmpty(aircraft.Data.CallSign)){
                nameString += " (" + aircraft.Data.CallSign + ")";
            }
        }
        else if(IsNullOrEmpty(aircraft.Data.CallSign)){
            nameString = aircraft.Data.CallSign;
        }
        else if(IsNullOrEmpty(aircraft.Data.Icao24)){
            nameString = aircraft.Data.Icao24;
        }
        else if(IsNullOrEmpty(aircraft.Data.FlarmId)){
            nameString = aircraft.Data.FlarmId;
        }
        else if(IsNullOrEmpty(aircraft.Data.OgnId)){
            nameString = aircraft.Data.OgnId;
        }

        if(!IsNullOrEmpty(aircraft.Data.Cn)){
            nameString += " (" + aircraft.Data.Cn + ")";
        }
        plot.DisplayName = nameString;


        if(aircraft.Data.VRate != 0) {
            double vRateRounded = Math.round(aircraft.Data.VRate * 10) / 10.0;
            String plusSign = (aircraft.Data.VRate > 0.0) ? "+" : "";
            vRateString = "   " + plusSign + df1.format(vRateRounded);
        }

        plot.InfoLine = aircraft + vRateString;

        plot.Track = aircraft.Data.Track;

        // ToDo: Calculate screen coordinates from Lat/Lon

        plot.ScreenX = 400;
        plot.ScreenY = 400;
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

        for(AircraftPlot ac : plots){
            drawAircraft(canvas, ac);
        }
    }


    private void drawAircraft(Canvas canvas, AircraftPlot ac){

        float arrowAngle = 135;
        float longArrowLength = 11;
        float shortArrowLength = 9;

        int boxSize = 25;
        int boxRound = 7;
        double shortLineLength = 20;
        double longLineLength = 22;

        // Copy numerical data only once to prevent multithreading inconsistencies
        double bearing = ac.Track;
        float x  = ac.ScreenX;
        float y = ac.ScreenY;
        String nameLine = ac.DisplayName;
        String infoLine = ac.InfoLine;

        double arrRightBearing = bearing + arrowAngle;
        double arrLeftBearing = bearing - arrowAngle;

        double rad = bearing * Math.PI / 180.0;
        double arrRightRad = arrRightBearing * Math.PI / 180.0;
        double arrLeftRad = arrLeftBearing * Math.PI / 180.0;

        float shortEndX = x + (float) (shortLineLength * Math.sin(rad));
        float shortEndY = y - (float) (shortLineLength * Math.cos(rad));


        float longEndX = x + (float) (longLineLength * Math.sin(rad));
        float longEndY = y - (float) (longLineLength * Math.cos(rad));

        float longArrRightX = shortEndX + (float) (longArrowLength * Math.sin(arrRightRad));
        float longArrRightY = shortEndY - (float) (longArrowLength * Math.cos(arrRightRad));

        float shortArrRightX = shortEndX + (float) (shortArrowLength * Math.sin(arrRightRad));
        float shortArrRightY = shortEndY - (float) (shortArrowLength * Math.cos(arrRightRad));

        float longArrLeftX = shortEndX + (float) (longArrowLength * Math.sin(arrLeftRad));
        float longArrLeftY = shortEndY - (float) (longArrowLength * Math.cos(arrLeftRad));

        float shortArrLeftX = shortEndX + (float) (shortArrowLength * Math.sin(arrLeftRad));
        float shortArrLeftY = shortEndY - (float) (shortArrowLength * Math.cos(arrLeftRad));

        if(ac.IsWarningMarker){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acWarningBoxPaint);
        }
        if(ac.IsNotificationMarker){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acNoteBoxPaint);
        }

        if(ac.IsSelectedMarker){
            boxSize += 5;
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acSelectedBoxPaint);
        }

        canvas.drawLine(x, y, x + 18, y - 55, acTextGuideLinePaint);

        canvas.drawLine(shortEndX, shortEndY, shortArrLeftX, shortArrLeftY, aircraftBackPaint);
        canvas.drawLine(shortEndX, shortEndY, shortArrRightX, shortArrRightY, aircraftBackPaint);
        canvas.drawLine(x, y, shortEndX, shortEndY, aircraftBackPaint);
        canvas.drawCircle(x, y, 7, aircraftBackPaint);

        canvas.drawLine(x, y, longEndX, longEndY, aircraftForePaint);
        canvas.drawCircle(x, y, 6, aircraftForePaint);

        canvas.drawLine(longEndX, longEndY, longArrRightX, longArrRightY, aircraftForePaint);
        canvas.drawLine(longEndX, longEndY, longArrLeftX, longArrLeftY, aircraftForePaint);

        canvas.drawText(nameLine, x + 20, y - 64, acNamePaint);
        canvas.drawText(infoLine, x + 20, y - 40, acInfoPaint);
    }


}
