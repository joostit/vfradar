package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

import com.joostit.vfradar.config.SysConfig;
import com.joostit.vfradar.config.UserUnitConvert;
import com.joostit.vfradar.data.TrackedAircraft;
import com.joostit.vfradar.geo.LatLon;
import com.joostit.vfradar.geo.geofencing.FenceAlerts;
import com.joostit.vfradar.geo.geofencing.FencedArea;

/**
 * Created by Joost on 15-1-2018.
 */

public class AircraftPlot extends DrawableItem {

    private final int txtBackMargin = 7;
    public int trackId;
    public float ScreenX;
    public float ScreenY;
    public Boolean isSelected = false;
    public Boolean isHighlighted = false;
    public Boolean isWarning = false;
    private int selectedBoxRadius = 40;
    private final int warnCircleRadius = 35;
    private final int highlightCircleRadius = 30;
    private final int backCircleRadius = 35;
    private boolean doDraw = false;
    public LatLon position = new LatLon();
    private Integer Track;
    private String displayName;
    private String infoLine;



    private int acForeColor = 0xFF00FF00;
    private int acBackColor = 0xFF008000;
    private int acNameTextColor = 0xFF00FF00;
    private int acInfoTextColor = 0xFF00DD00;
    private int acWarningBoxColor = 0xFFFF0000;
    private int acSelectedoutlineColor = 0xFF00FF00;
    private int acHighlightBoxColor = 0xFFFFFF00;
    private int acTextGuideLineColor = 0xFF00AA00;

    private int selectedBackCenterColor = 0x22660066;
    private int selectedBackMiddleColor = 0xFFcc00cc;
    private int selectedBackBorderColor = 0xFFffccff;

    private int txtBackCenterColor = 0xFF800080;
    private int txtBackBorderColor = 0xFFffccff;

    private int highlightBackEndColor = 0x00000000;
    private int highlightBackCenterColor = 0xFFffff00;

    private int backEndColor = 0x00000000;
    private int backCenterColor = 0xAA000000;

    private int warnBackEndColor = 0x00000000;
    private int warnBackCenterColor = 0xFFFF0000;

    private UserUnitConvert unitConverter = new UserUnitConvert();

    private float[] txtBackGradientstops = new float[]{0, .15f, .85f, 1};
    private int[] txtBackGradientColors = new int[]{txtBackBorderColor, txtBackCenterColor, txtBackCenterColor, txtBackBorderColor};

    private float[] selectedGradientstops = new float[]{0, 0.1f, 0.8f, 1};
    private int[] selectedGradientColors = new int[]{selectedBackCenterColor, selectedBackCenterColor, selectedBackMiddleColor, selectedBackBorderColor};

    private float[] highlightGradientstops = new float[]{0, 1};
    private int[] highlightGradientColors = new int[]{highlightBackCenterColor, highlightBackEndColor};

    private float[] backGradientstops = new float[]{0, 1};
    private int[] backGradientColors = new int[]{backCenterColor, backEndColor};

    private float[] warnGradientstops = new float[]{0, 1};
    private int[] warnGradientColors = new int[]{warnBackCenterColor, warnBackEndColor};

    private Paint acNamePaint;
    private Paint acInfoPaint;
    private Paint aircraftForePaint;
    private Paint aircraftBackPaint;
    private Paint acTextGuideLinePaint;
    private Paint backCirclePaint;
    private Paint acWarningBoxPaint;
    private Paint acHighlightBoxPaint;
    private Paint acSelectedBoxPaint;
    private Paint acSelectedOutlinePaint;
    private Paint txtBackGradientPaint;

    public AircraftPlot() {
        init();
    }

    private void init() {

        aircraftForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftForePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftForePaint.setStrokeWidth(2);
        aircraftForePaint.setColor(acForeColor);

        aircraftBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        aircraftBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aircraftBackPaint.setStrokeWidth(6);
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

        backCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backCirclePaint.setStyle(Paint.Style.FILL);
        backCirclePaint.setColor(warnBackCenterColor);

        acWarningBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acWarningBoxPaint.setStyle(Paint.Style.FILL);
        acWarningBoxPaint.setColor(acWarningBoxColor);

        acHighlightBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acHighlightBoxPaint.setStyle(Paint.Style.FILL);
        acHighlightBoxPaint.setColor(acHighlightBoxColor);

        acSelectedBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acSelectedBoxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        acSelectedBoxPaint.setColor(selectedBackBorderColor);

        txtBackGradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        txtBackGradientPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        txtBackGradientPaint.setColor(txtBackCenterColor);

        acSelectedOutlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        acSelectedOutlinePaint.setStyle(Paint.Style.STROKE);
        acSelectedOutlinePaint.setStrokeWidth(3);
        acSelectedOutlinePaint.setColor(acSelectedoutlineColor);

    }


    @Override
    public void draw(Canvas canvas) {

        if (!doDraw) {
            return;
        }

        float arrowAngle = 135;
        float longArrowLength = 10;
        float shortArrowLength = 9;

        int boxRound = 7;
        double shortLineLength = 20;
        double longLineLength = 22;

        // Copy numerical data only once to prevent multithreading inconsistencies
        Boolean hasTrack = (Track != null);
        double track = hasTrack ? Track : 0;
        float x = ScreenX;
        float y = ScreenY;
        String nameLine = displayName;
        String infoLine = this.infoLine;

        double arrRightBearing = track + arrowAngle;
        double arrLeftBearing = track - arrowAngle;

        double trackRad = Math.toRadians(track);
        double arrRightRad = Math.toRadians(arrRightBearing);
        double arrLeftRad = Math.toRadians(arrLeftBearing);

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


        RadialGradient backGradient = new RadialGradient(x, y, backCircleRadius, backGradientColors, backGradientstops, Shader.TileMode.CLAMP);
        backCirclePaint.setShader(backGradient);
        canvas.drawCircle(x, y, backCircleRadius, backCirclePaint);

        if (isSelected) {
            float gradientOversize = 10;
            RadialGradient selectedGradient = new RadialGradient(x, y, selectedBoxRadius + gradientOversize, selectedGradientColors, selectedGradientstops, Shader.TileMode.CLAMP);
            acSelectedBoxPaint.setShader(selectedGradient);
            RectF selectBoxRect = new RectF(x - selectedBoxRadius, y - selectedBoxRadius, x + selectedBoxRadius, y + selectedBoxRadius);
            canvas.drawRoundRect(selectBoxRect, boxRound, boxRound, acSelectedBoxPaint);
            canvas.drawRoundRect(selectBoxRect, boxRound, boxRound, acSelectedOutlinePaint);
        }

        if (isWarning) {
            RadialGradient warningGradient = new RadialGradient(x, y, warnCircleRadius, warnGradientColors, warnGradientstops, Shader.TileMode.CLAMP);
            acWarningBoxPaint.setShader(warningGradient);
            canvas.drawCircle(x, y, warnCircleRadius, acWarningBoxPaint);
        } else {
            if (isHighlighted) {
                RadialGradient highlightedGradient = new RadialGradient(x, y, highlightCircleRadius, highlightGradientColors, highlightGradientstops, Shader.TileMode.CLAMP);
                acHighlightBoxPaint.setShader(highlightedGradient);
                canvas.drawCircle(x, y, highlightCircleRadius, acHighlightBoxPaint);
            }
        }

        float txtXDisplacement = 35;
        float txtYDisplacement = 37;

        float nameTxtX = x + txtXDisplacement;
        float nameTxtY = y - (txtYDisplacement + 27);
        float infoTxtX = x + txtXDisplacement;
        float infoTxtY = y - txtYDisplacement;

        if(!isSelected) {
            float connectionX = x + (txtXDisplacement - 8);
            float connectionY = y - (txtXDisplacement + 18);
            canvas.drawLine(x, y, connectionX, connectionY, acTextGuideLinePaint);
        }

        if (isSelected) {
            Rect nameTxtBounds = new Rect();
            Rect infoTxtBounds = new Rect();
            acNamePaint.getTextBounds(nameLine, 0, nameLine.length(), nameTxtBounds);
            acInfoPaint.getTextBounds(infoLine, 0, infoLine.length(), infoTxtBounds);

            float nameBackX = nameTxtX - txtBackMargin - 3;
            float nameBackY = nameTxtY - nameTxtBounds.height() - txtBackMargin;
            float nameBackRight = nameTxtX + nameTxtBounds.width() + txtBackMargin + 3;
            float nameBackBottom = nameTxtY + txtBackMargin;
            RectF nameBackRect = new RectF(nameBackX, nameBackY, nameBackRight, nameBackBottom);

            float infoBackX = infoTxtX - txtBackMargin - 3;
            float infoBackY = infoTxtY - infoTxtBounds.height() - txtBackMargin;
            float infoBackRight = infoTxtX + infoTxtBounds.width() + txtBackMargin + 3;
            float infoBackBottom = infoTxtY + txtBackMargin + 3;
            RectF infoBackRect = new RectF(infoBackX, infoBackY, infoBackRight, infoBackBottom);

            RectF txtBackRect = new RectF();
            txtBackRect.left = nameBackRect.left < infoBackRect.left ? nameBackRect.left: infoBackRect.left;
            txtBackRect.top = nameBackRect.top < infoBackRect.top ? nameBackRect.top: infoBackRect.top;
            txtBackRect.bottom = nameBackRect.bottom > infoBackRect.bottom ? nameBackRect.bottom: infoBackRect.bottom;
            txtBackRect.right = nameBackRect.right > infoBackRect.right ? nameBackRect.right: infoBackRect.right;

            LinearGradient gradient = new LinearGradient(txtBackRect.left, txtBackRect.top, txtBackRect.left ,txtBackRect.bottom, txtBackGradientColors, txtBackGradientstops, Shader.TileMode.CLAMP);
            txtBackGradientPaint.setShader(gradient);

            canvas.drawRoundRect(txtBackRect, 3, 3, txtBackGradientPaint);
            canvas.drawRoundRect(txtBackRect, 3, 3, acSelectedOutlinePaint);
        }

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

        String nameString = aircraft.getIdString();

        if (aircraft.data.hasCn()) {
            nameString += " (" + aircraft.data.cn + ")";
        }
        displayName = nameString;

        String infoLineString = "";

        if (aircraft.data.hasAltitude()) {
            String altString = unitConverter.getHeight(aircraft.data.alt) + " " + unitConverter.getHeightUnitIndicator();
            infoLineString = altString;
            infoLineString += "  " + unitConverter.getVerticalRateString(aircraft.data.vRate);
        }

        infoLine = infoLineString;
        Track = aircraft.data.track;
        position = aircraft.data.position;

        boolean areaHighlight = false;
        boolean areaWarning = false;

        for (FencedArea area : aircraft.isInside.areas) {
            if (area.getAlerType() == FenceAlerts.Notification) {
                areaHighlight = true;
            }

            if(area.getAlerType() == FenceAlerts.Warning){
                areaWarning = true;
            }
        }

        isHighlighted = areaHighlight;
        isWarning = areaWarning;
    }


    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {
        PointF screenPoint = projection.toScreenPoint(position);
        ScreenX = screenPoint.x;
        ScreenY = screenPoint.y;

        doDraw = bounds.contains(ScreenX, ScreenY);
        return doDraw;
    }

    public double getDistanceFromCenter() {
        double distanceM = SysConfig.getCenterPosition().DistanceTo(position);
        return distanceM;
    }
}
