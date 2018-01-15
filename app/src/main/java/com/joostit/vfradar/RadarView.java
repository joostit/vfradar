package com.joostit.vfradar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

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

    Paint mTextPaint;
    int mTextColor = Color.BLUE;
    float mTextHeight;
    Boolean mShowText = true;
    int mTextWidth = 70;

    Paint acNamePaint;
    Paint acInfoPaint;
    Paint crosshairPaint;
    Paint aircraftForePaint;
    Paint aircraftBackPaint;
    Paint acTextGuideLinePaint;
    Paint sitePaint;
    Paint acWarningBoxPaint;
    Paint acNoteBoxPaint;
    Paint acSelectedBoxPaint;
    Paint circuitPaint;

    int crosshairColor = 0xFF003300;
    int siteColor = 0x50ff9900;
    int acForeColor = 0xFF00FF00;
    int acBackColor = 0xFF008000;
    int acTextGuideLineColor = 0xAA008000;
    int acNameTextColor = 0xFF00FF00;
    int acInfoTextColor = 0xFF00AA00;
    int acWarningBoxColor = 0xFFFF0000;
    int acSelectedBoxColor = 0xFFFFFFFF;
    int acNoteBoxColor = 0xFFFFFF00;
    int circuitColor = 0x70ff9900;

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
// Account for padding
        float xpad = (float)(getPaddingLeft() + getPaddingRight());
        float ypad = (float)(getPaddingTop() + getPaddingBottom());

        // Account for the label
        if (mShowText) xpad += mTextWidth;

        float ww = (float)w - xpad;
        float hh = (float)h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
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

        DrawAircraft(canvas, cX - 400, cY + 300, 230, "PH-TWM", 331, 7.4, AircraftStates.None);
        DrawAircraft(canvas, cX - 420, cY - 340, 178, "PH-TWK", 950, 0, AircraftStates.None);
        DrawAircraft(canvas, cX - 220, cY - 40, 50, "PH-RRR", 335, -0.1, AircraftStates.Warning);
        DrawAircraft(canvas, cX + 100, cY - 90, 230, "PH-798", 80, -0.7, AircraftStates.Note);
        DrawAircraft(canvas, cX + 50, cY + 30, 45, "PH-712 (T4)", 140, -0.8, AircraftStates.Note);
        DrawAircraft(canvas, cX + 250, cY + 230, 281, "PH-648", 1270, 0.5, AircraftStates.None);
        DrawAircraft(canvas, cX + 400, cY - 370, 346, "PH-1471 (T8)", 867, 2.8, AircraftStates.Selected);
        DrawAircraft(canvas, cX + 400, cY + 450, 160, "PH-1480 (T7)", 1560, 3.1, AircraftStates.None);
        DrawAircraft(canvas, cX + 348, cY + 412, 20, "PH-764", 764, -3.2, AircraftStates.None);
        DrawAircraft(canvas, cX + 358, cY + 432, 170, "PH-973 (T6)", 1420, 1.4, AircraftStates.None);
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

    private void drawSite(Canvas canvas){
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        Path runway = new Path();
        runway.moveTo(centerX + 30, centerY - 60);
        runway.lineTo(centerX - 276, centerY + 197);
        canvas.drawPath(runway, sitePaint);

        //Path circuit = new Path();
        //circuit.moveTo(centerX + 80, centerY - 10);
        //circuit.lineTo(centerX - 226, centerY + 247);
        //canvas.drawPath(circuit, circuitPaint);
    }



    private void DrawAircraft(Canvas canvas, float x, float y, double bearing, String name, int altitude, double vRate, AircraftStates marker) {

        float arrowAngle = 135;
        float longArrowLength = 11;
        float shortArrowLength = 9;

        int boxSize = 25;
        int boxRound = 7;
        double shortLineLength = 20;
        double longLineLength = 22;
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



        if(marker == AircraftStates.Warning){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acWarningBoxPaint);
        }
        if(marker == AircraftStates.Note){
            canvas.drawRoundRect(x - boxSize, y - boxSize, x + boxSize, y + boxSize, boxRound, boxRound, acNoteBoxPaint);
        }

        if(marker == AircraftStates.Selected){
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

        String vRateString = "";
        if(vRate != 0) {
            double vRateRounded = Math.round(vRate * 10) / 10.0;
            String plusSign = (vRate > 0.0) ? "+" : "";
            vRateString = "   " + plusSign + df1.format(vRateRounded);
        }

        String altLine = altitude + vRateString;

        canvas.drawText(name, x + 20, y - 64, acNamePaint);
        canvas.drawText(altLine, x + 20, y - 40, acInfoPaint);
    }


}
