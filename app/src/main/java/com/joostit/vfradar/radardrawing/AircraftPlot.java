package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.data.TrackedAircraft;

import java.text.DecimalFormat;

/**
 * Created by Joost on 15-1-2018.
 */

public class AircraftPlot extends DrawableItem {

    private static DecimalFormat df1 = new DecimalFormat("#.#");

    private final int txtBackMargin = 2;

    public double lat;
    public double lon;

    public float ScreenX;
    public float ScreenY;

    public Integer Track;

    public Boolean isSelected = false;
    public Boolean isHighlighted = false;
    public Boolean isWarning = false;

    public String DisplayName;
    public String InfoLine;
    public int TrackId;


    private int acForeColor = 0xFF00FF00;
    private int acBackColor = 0xFF008000;
    private int acNameTextColor = 0xFF00FF00;
    private int acInfoTextColor = 0xFF00AA00;
    private int acWarningBoxColor = 0xFFFF0000;
    private int acSelectedBoxColor = 0xFFFFFFFF;
    private int acHighlightBoxColor = 0xFFFFFF00;
    private int acTextGuideLineColor = 0xAA008000;
    private int acTextBackColor = 0xBB000000;

    private Paint acNamePaint;
    private Paint acInfoPaint;
    private Paint aircraftForePaint;
    private Paint aircraftBackPaint;
    private Paint acTextGuideLinePaint;
    private Paint acWarningBoxPaint;
    private Paint acHighlightBoxPaint;
    private Paint acSelectedBoxPaint;
    private Paint acTextBackPaint;

    public AircraftPlot() {
        init();
    }

    private void init() {

        aircraftForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftForePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftForePaint.setStrokeWidth(3);
        aircraftForePaint.setColor(acForeColor);

        aircraftBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftBackPaint.setStrokeWidth(8);
        aircraftBackPaint.setColor(acBackColor);

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

        acTextBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acTextBackPaint.setStyle(Paint.Style.FILL);
        acTextBackPaint.setColor(acTextBackColor);
    }


    @Override
    public void Draw(Canvas canvas) {
        float arrowAngle = 135;
        float longArrowLength = 11;
        float shortArrowLength = 9;

        int boxSize = 25;
        int boxRound = 7;
        double shortLineLength = 20;
        double longLineLength = 22;

        // Copy numerical data only once to prevent multithreading inconsistencies
        Boolean hasTrack = (Track != null);
        double track = hasTrack ? Track : 0;
        float x = ScreenX;
        float y = ScreenY;
        String nameLine = DisplayName;
        String infoLine = InfoLine;

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

        if (isWarning) {
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acWarningBoxPaint);
        }
        if (isHighlighted) {
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acHighlightBoxPaint);
        }

        if (isSelected) {
            boxSize += 5;
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acSelectedBoxPaint);
        }


        float nameTxtX = x + 20;
        float nameTxtY = y - 64;
        float infoTxtX = x + 20;
        float infoTxtY = y - 37;

        if (isWarning || isHighlighted || isSelected) {
            Rect nameTxtBounds = new Rect();
            Rect infoTxtBounds = new Rect();
            acNamePaint.getTextBounds(nameLine, 0, nameLine.length(), nameTxtBounds);
            acInfoPaint.getTextBounds(infoLine, 0, infoLine.length(), infoTxtBounds);

            float nameBackX = nameTxtX - txtBackMargin - 3;
            float nameBackY = nameTxtY - nameTxtBounds.height() - txtBackMargin - 3;
            float nameBackRight = nameTxtX + nameTxtBounds.width() + txtBackMargin + 3;
            float nameBackBottom = nameTxtY + txtBackMargin;
            RectF nameBackRect = new RectF(nameBackX, nameBackY, nameBackRight, nameBackBottom);

            float infoBackX = infoTxtX - txtBackMargin - 3;
            float infoBackY = infoTxtY - infoTxtBounds.height() - txtBackMargin;
            float infoBackRight = infoTxtX + infoTxtBounds.width() + txtBackMargin + 3;
            float infoBackBottom = infoTxtY + txtBackMargin + 3;
            RectF infoBackRect = new RectF(infoBackX, infoBackY, infoBackRight, infoBackBottom);

            canvas.drawRoundRect(nameBackRect, 3, 3, acTextBackPaint);
            canvas.drawRoundRect(infoBackRect, 3, 3, acTextBackPaint);
        }

        canvas.drawLine(x, y, x + 18, y - 55, acTextGuideLinePaint);

        // Track arrow line
        if (hasTrack) {
            canvas.drawLine(shortEndX, shortEndY, shortArrLeftX, shortArrLeftY, aircraftBackPaint);
            canvas.drawLine(shortEndX, shortEndY, shortArrRightX, shortArrRightY, aircraftBackPaint);
            canvas.drawLine(x, y, shortEndX, shortEndY, aircraftBackPaint);
        }

        canvas.drawCircle(x, y, 7, aircraftBackPaint);

        if (hasTrack) {
            canvas.drawLine(x, y, longEndX, longEndY, aircraftForePaint);
        }

        canvas.drawCircle(x, y, 6, aircraftForePaint);

        if (hasTrack) {
            canvas.drawLine(longEndX, longEndY, longArrRightX, longArrRightY, aircraftForePaint);
            canvas.drawLine(longEndX, longEndY, longArrLeftX, longArrLeftY, aircraftForePaint);
        }

        canvas.drawText(nameLine, nameTxtX, nameTxtY, acNamePaint);
        canvas.drawText(infoLine, infoTxtX, infoTxtY, acInfoPaint);
    }


    public void updateAircraftPlotData(TrackedAircraft aircraft) {

        String nameString = "";
        if (!IsNullOrEmpty(aircraft.Data.Reg)) {
            nameString = aircraft.Data.Reg;
            if (!IsNullOrEmpty(aircraft.Data.CallSign)) {
                nameString += " (" + aircraft.Data.CallSign + ")";
            }
        } else if (!IsNullOrEmpty(aircraft.Data.CallSign)) {
            nameString = aircraft.Data.CallSign;
        } else if (!IsNullOrEmpty(aircraft.Data.Icao24)) {
            nameString = aircraft.Data.Icao24;
        } else if (!IsNullOrEmpty(aircraft.Data.FlarmId)) {
            nameString = aircraft.Data.FlarmId;
        } else if (!IsNullOrEmpty(aircraft.Data.OgnId)) {
            nameString = aircraft.Data.OgnId;
        }

        if (!IsNullOrEmpty(aircraft.Data.Cn)) {
            nameString += " (" + aircraft.Data.Cn + ")";
        }
        DisplayName = nameString;

        String infoLineString = "";

        if (aircraft.Data.Alt != null) {

            infoLineString = aircraft.Data.Alt.toString();

            if ((aircraft.Data.VRate != null) && (aircraft.Data.VRate != 0)) {
                double vRateRounded = Math.round(aircraft.Data.VRate * 10) / 10.0;
                String plusSign = (aircraft.Data.VRate > 0.0) ? "+" : "";
                infoLineString += "    " + plusSign + df1.format(vRateRounded);
            }
        }

        InfoLine = infoLineString;

        Track = aircraft.Data.Track;
        lat = aircraft.Data.Lat;
        lon = aircraft.Data.Lon;
        isHighlighted = aircraft.isHighlighted;
        isWarning = aircraft.isWarning;
    }


    private Boolean IsNullOrEmpty(String input) {
        if ((input != null) && (!input.trim().equals(""))) {
            return false;
        } else {
            return true;
        }
    }

}
