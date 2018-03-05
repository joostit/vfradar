package com.joostit.vfradar.infolisting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joostit.vfradar.geo.geofencing.FenceAlerts;
import com.joostit.vfradar.utilities.DrawUtils;

/**
 * Created by Joost on 28-1-2018.
 */

public class ListItemView extends View {

    private final int defaultWidth = 570;
    private final int defaultHeight = 150;
    private final int selectedHeight = 150;
    private final int geoFenceRectHeight = 30;

    private final float maxMovementWhilePressing = 20;
    private final int statusTrueBackColor = 0xFF00FF00;
    private final int statusFalseBackColor = 0xFF004400;
    private final int statusTrueForeColor = 0xFF002200;
    private final int statusFalseForeColor = 0xFF002200;
    private float touchDownX = -1;
    private float touchDownY = -1;
    private boolean isPressing;
    private ListItemViewEventHandler eventHandler;
    private int selectedBackColor = 0xFF333333;
    private int boundingRectColor = 0xFF00FF00;
    private int brightTextColor = 0xFF00FF00;
    private int darkTextColor = 0xFF00CC00;
    private int backColor = 0xFF000000;
    private int modelTextColor = 0xFF00EE00;
    private int dataTextColor = 0xFF00DD00;
    private int relativeBearingArrowColor = 0xFF00DD00;
    private int statusRectBoundsColor = 0xFF009900;
    private int bearingArrowBackColor = 0xFF005500;

    private int noteRectForeColor = 0xFF333300;
    private int noteRectBackColor = 0xFFe6e600;
    private int warnRectForeColor = 0xFF330000;
    private int warnRectBackColor = 0xFFff0000;

    private int nameTextSize = 55;
    private int nameTypeTextSize = 18;
    private int cnTextSize = 30;
    private int dataTextSize = 22;
    private int statusTextSize = 18;

    private Paint selectedBackPaint;
    private Paint backPaint;
    private Paint selectedBoundingRectPaint;
    private Paint boundingRectPaint;
    private Paint nameTypePaint;
    private Paint cnPaint;
    private Paint modelTextPaint;
    private Paint dataTextPaint;
    private Paint bearingArrowBackPaint;

    private Paint statusTrueForePaint;
    private Paint statusFalseForePaint;
    private Paint statusTrueBackPaint;
    private Paint statusFalseBackPaint;
    private Paint relativeBearingArrowPaint;
    private Paint statusRectBoundsPaint;

    private Paint noteRectForePaint;
    private Paint noteRectBackPaint;

    private Paint warnRectForePaint;
    private Paint warnRectBackPaint;

    private Paint namePaint;
    private InfoListItemData currentState = new InfoListItemData();

    public ListItemView(Context context) {
        super(context);

        init();
        setLayoutParams(new ViewGroup.LayoutParams(defaultWidth, selectedHeight));
    }

    public ListItemView(Context context, ListItemViewEventHandler eventHandler) {
        this(context);

        this.eventHandler = eventHandler;
    }

    private void init() {


        selectedBoundingRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedBoundingRectPaint.setStyle(Paint.Style.STROKE);
        selectedBoundingRectPaint.setColor(boundingRectColor);
        selectedBoundingRectPaint.setStrokeWidth(6);

        boundingRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundingRectPaint.setStyle(Paint.Style.STROKE);
        boundingRectPaint.setColor(boundingRectColor);
        boundingRectPaint.setStrokeWidth(2);

        selectedBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedBackPaint.setStyle(Paint.Style.FILL);
        selectedBackPaint.setColor(selectedBackColor);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(backColor);

        nameTypePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nameTypePaint.setStyle(Paint.Style.FILL);
        nameTypePaint.setColor(darkTextColor);
        nameTypePaint.setTextSize(nameTypeTextSize);

        namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setStyle(Paint.Style.FILL);
        namePaint.setColor(brightTextColor);
        namePaint.setTextSize(nameTextSize);

        cnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cnPaint.setStyle(Paint.Style.FILL);
        cnPaint.setColor(brightTextColor);
        cnPaint.setTextSize(cnTextSize);

        modelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        modelTextPaint.setStyle(Paint.Style.FILL);
        modelTextPaint.setColor(modelTextColor);
        modelTextPaint.setTextSize(dataTextSize);

        dataTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataTextPaint.setStyle(Paint.Style.FILL);
        dataTextPaint.setColor(dataTextColor);
        dataTextPaint.setTextSize(dataTextSize);

        statusRectBoundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusRectBoundsPaint.setStyle(Paint.Style.STROKE);
        statusRectBoundsPaint.setColor(statusRectBoundsColor);
        statusRectBoundsPaint.setStrokeWidth(1.5f);

        statusTrueBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusTrueBackPaint.setStyle(Paint.Style.FILL);
        statusTrueBackPaint.setColor(statusTrueBackColor);

        statusFalseBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusFalseBackPaint.setStyle(Paint.Style.FILL);
        statusFalseBackPaint.setColor(statusFalseBackColor);

        statusTrueForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusTrueForePaint.setStyle(Paint.Style.FILL);
        statusTrueForePaint.setColor(statusTrueForeColor);
        statusTrueForePaint.setTextSize(statusTextSize);

        statusFalseForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusFalseForePaint.setStyle(Paint.Style.FILL);
        statusFalseForePaint.setColor(statusFalseForeColor);
        statusFalseForePaint.setTextSize(statusTextSize);

        relativeBearingArrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        relativeBearingArrowPaint.setStyle(Paint.Style.STROKE);
        relativeBearingArrowPaint.setColor(relativeBearingArrowColor);
        relativeBearingArrowPaint.setStrokeWidth(1.5f);

        bearingArrowBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bearingArrowBackPaint.setStyle(Paint.Style.FILL);
        bearingArrowBackPaint.setColor(bearingArrowBackColor);


        statusRectBoundsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusRectBoundsPaint.setStyle(Paint.Style.STROKE);
        statusRectBoundsPaint.setColor(statusRectBoundsColor);
        statusRectBoundsPaint.setStrokeWidth(1.5f);

        noteRectBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteRectBackPaint.setStyle(Paint.Style.FILL);
        noteRectBackPaint.setColor(noteRectBackColor);

        noteRectForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        noteRectForePaint.setStyle(Paint.Style.FILL);
        noteRectForePaint.setColor(noteRectForeColor);
        noteRectForePaint.setTextSize(statusTextSize);

        warnRectBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        warnRectBackPaint.setStyle(Paint.Style.FILL);
        warnRectBackPaint.setColor(warnRectBackColor);

        warnRectForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        warnRectForePaint.setStyle(Paint.Style.FILL);
        warnRectForePaint.setColor(warnRectForeColor);
        warnRectForePaint.setTextSize(statusTextSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF boundingRect = new RectF(4, 4, this.getWidth() - 4, this.getHeight() - 4);

        canvas.drawRoundRect(boundingRect, 3, 3, getBackPaint(currentState.isSelected));
        canvas.drawRoundRect(boundingRect, 3, 3, getBoundingRectPaint(currentState.isSelected));

        int columnRuler1 = 15;
        canvas.drawText(currentState.nameType, columnRuler1 + 3, 28, nameTypePaint);
        canvas.drawText(currentState.name, columnRuler1, 75, namePaint);

        canvas.drawText(currentState.subNameType, columnRuler1 + 4, 106, nameTypePaint);
        canvas.drawText(currentState.subName, columnRuler1 + 3, 135, cnPaint);

        int columnRuler2 = 290;
        int columnRuler3 = 385;
        int row1 = 68;
        int row2 = 98;
        int row3 = 130;
        canvas.drawText(currentState.model, columnRuler2, row1, modelTextPaint);
        canvas.drawText(currentState.altitude, columnRuler2, row2, dataTextPaint);
        canvas.drawText(currentState.vRate, columnRuler3, row2, dataTextPaint);
        canvas.drawText(currentState.relativeDistance, columnRuler2, row3, dataTextPaint);
        canvas.drawText(currentState.relativeBearing, columnRuler3, row3, dataTextPaint);

        drawRelativeBearingArrow(canvas, columnRuler3 + 70, row3 - 8, currentState.relativeDegrees);

        int statusColumn = 495;
        int statusWidth = 62;
        int statusHeight = 26;
        int statusRounding = 3;
        int statusRow1 = 75;
        int statusRow2 = 110;
        int StatusTextHeight = 19;

        RectF statusBack = new RectF(statusColumn, statusRow1, statusColumn + statusWidth, statusRow1 + statusHeight);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, getStatusBackPaint(currentState.hasAdsb));
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, statusRectBoundsPaint);
        canvas.drawText("ADS-B", statusColumn + 5, statusRow1 + StatusTextHeight, getStatusForePaint(currentState.hasAdsb));

        statusBack = new RectF(statusColumn, statusRow2, statusColumn + statusWidth, statusRow2 + statusHeight);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, getStatusBackPaint(currentState.hasOgn));
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, statusRectBoundsPaint);
        canvas.drawText("OGN", statusColumn + 12, statusRow2 + StatusTextHeight, getStatusForePaint(currentState.hasOgn));

        int noteX = columnRuler2;
        int noteY = 13;
        int noteSize = 200;

        InfoListNotification notificationToDisplay = null;
        boolean multipleNotifications = currentState.notifications.size() > 1;

        for(InfoListNotification notification : currentState.notifications){

            if(notificationToDisplay == null){
                notificationToDisplay = notification;
            }
            else{
                if(notificationToDisplay.isOtherMoreSevere(notification)){
                    notificationToDisplay = notification;
                }
            }
        }

        if(notificationToDisplay != null) {
            String notificationName = notificationToDisplay.name;

            if(multipleNotifications){
                notificationName += " , ...";
            }

            DrawUtils.drawStatusRect(canvas, noteX, noteY, noteSize, geoFenceRectHeight,
                    notificationName,
                    statusRectBoundsPaint,
                    getStatusForePaint(notificationToDisplay.notificationType),
                    getStatusBackPaint(notificationToDisplay.notificationType));
        }

    }


    private Paint getStatusForePaint(FenceAlerts alertType){
        switch (alertType) {

            case Notification:
                return noteRectForePaint;

            case Warning:
                return warnRectForePaint;

            default:
                return null;
        }

    }

    private Paint getStatusBackPaint(FenceAlerts alertType){
        switch (alertType) {

            case Notification:
                return noteRectBackPaint;

            case Warning:
                return warnRectBackPaint;

            default:
                return null;

        }
    }

    private void drawRelativeBearingArrow(Canvas canvas, float x, float y, int bearing) {
        int arrowLength = 12;
        int arrowArmLength = 6;
        float arrowAngle = 150;

        float lineStartX = x;
        float lineStartY = y;

        double arrRightBearing = bearing + arrowAngle;
        double arrLeftBearing = bearing - arrowAngle;

        double bearingRad = Math.toRadians(bearing);
        double arrRightRad = Math.toRadians(arrRightBearing);
        double arrLeftRad = Math.toRadians(arrLeftBearing);

        float lineEndX = lineStartX + (float) (arrowLength * Math.sin(bearingRad));
        float lineEndY = lineStartY - (float) (arrowLength * Math.cos(bearingRad));

        float arrRightX = lineEndX + (float) (arrowArmLength * Math.sin(arrRightRad));
        float arrRightY = lineEndY - (float) (arrowArmLength * Math.cos(arrRightRad));

        float arrLeftX = lineEndX + (float) (arrowArmLength * Math.sin(arrLeftRad));
        float arrLeftY = lineEndY - (float) (arrowArmLength * Math.cos(arrLeftRad));

        canvas.drawCircle(x, y, arrowLength, bearingArrowBackPaint);
        canvas.drawLine(lineEndX, lineEndY, arrLeftX, arrLeftY, relativeBearingArrowPaint);
        canvas.drawLine(lineEndX, lineEndY, arrRightX, arrRightY, relativeBearingArrowPaint);
        canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, relativeBearingArrowPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = defaultWidth;
        int h;
        if (currentState.isSelected) {
            h = selectedHeight;
        } else {
            h = defaultHeight;
        }

        super.onMeasure(w, h);
        setMeasuredDimension(w, h);
    }

    private void updateHight() {
        if (currentState.isSelected) {
            setLayoutParams(new LinearLayout.LayoutParams(defaultWidth, selectedHeight));
        } else {
            setLayoutParams(new LinearLayout.LayoutParams(defaultWidth, defaultHeight));
        }
        requestLayout();
    }

    public void updateAircraftInfo(InfoListItemData update) {

        boolean hasChanged = false;

        if (update.trackId != currentState.trackId) {
            currentState.trackId = update.trackId;
        }

        if (update.nameType != currentState.nameType) {
            currentState.nameType = update.nameType;
            hasChanged = true;
        }

        if (update.name != currentState.name) {
            currentState.name = update.name;
            hasChanged = true;
        }

        if (update.subName != currentState.subName) {
            currentState.subName = update.subName;
            hasChanged = true;
        }

        if (update.subNameType != currentState.subNameType) {
            currentState.subNameType = update.subNameType;
            hasChanged = true;
        }

        if (update.model != currentState.model) {
            currentState.model = update.model;
            hasChanged = true;
        }

        if (update.altitude != currentState.altitude) {
            currentState.altitude = update.altitude;
            hasChanged = true;
        }

        if (update.vRate != currentState.vRate) {
            currentState.vRate = update.vRate;
            hasChanged = true;
        }

        if (update.relativeBearing != currentState.relativeBearing) {
            currentState.relativeBearing = update.relativeBearing;
            currentState.relativeDegrees = update.relativeDegrees;
            hasChanged = true;
        }

        if (update.relativeDistance != currentState.relativeDistance) {
            currentState.relativeDistance = update.relativeDistance;
            hasChanged = true;
        }

        if (update.hasAdsb != currentState.hasAdsb) {
            currentState.hasAdsb = update.hasAdsb;
            hasChanged = true;
        }

        if (update.hasOgn != currentState.hasOgn) {
            currentState.hasOgn = update.hasOgn;
            hasChanged = true;
        }

        currentState.notifications = update.notifications;

        if (hasChanged) {
            this.invalidate();
        }

    }


    private Paint getStatusBackPaint(boolean valueTrue) {
        if (valueTrue) {
            return statusTrueBackPaint;
        } else {
            return statusFalseBackPaint;
        }
    }

    private Paint getStatusForePaint(boolean valueTrue) {
        if (valueTrue) {
            return statusTrueForePaint;
        } else {
            return statusFalseForePaint;
        }
    }

    private Paint getBackPaint(boolean isSelected) {
        if (isSelected) {
            return selectedBackPaint;
        } else {
            return backPaint;
        }
    }


    private Paint getBoundingRectPaint(boolean isSelected) {
        if (isSelected) {
            return selectedBoundingRectPaint;
        } else {
            return boundingRectPaint;
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
                    processPressed();
                }
                break;
        }

        return true;
    }

    private void processPressed() {
        if (currentState.isSelected) {
            eventHandler.onPressed(null);
        } else {
            eventHandler.onPressed(currentState.trackId);
        }
    }

    public void setSelected(boolean selected) {
        currentState.isSelected = selected;
        updateHight();
        this.invalidate();
    }
}
