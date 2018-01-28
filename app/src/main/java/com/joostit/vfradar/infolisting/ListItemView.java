package com.joostit.vfradar.infolisting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Joost on 28-1-2018.
 */

public class ListItemView extends View {

    private final int width = 570;
    private final int height = 150;
    private final float maxMovementWhilePressing = 20;

    private float touchDownX = -1;
    private float touchDownY = -1;
    private boolean isPressing;


    private ListItemViewEventHandler eventHandler;

    private final int statusTrueBackColor = 0xFF00FF00;
    private final int statusFalseBackColor = 0xFF005500;

    private final int statusTrueForeColor = 0xFF000000;
    private final int statusFalseForeColor = 0xFF000000;

    private int boundingRectColor = 0xFF00FF00;
    private int brightTextColor = 0xFF00FF00;
    private int darkTextColor = 0xFF00CC00;
    private int backColor = 0xFF000000;
    private int modelTextColor = 0xFF00EE00;
    private int dataTextColor = 0xFF00DD00;
    private int relativeBearingArrowColor = 0xFF00DD00;

    private int nameTextSize = 60;
    private int nameTypeTextSize = 18;
    private int cnTextSize = 30;
    private int dataTextSize = 22;
    private int statusTextSize = 16;

    private Paint fillRectPaint;
    private Paint boundingRectPaint;
    private Paint nameTypePaint;
    private Paint cnPaint;
    private Paint modelTextPaint;
    private Paint dataTextPaint;

    private Paint statusTrueForePaint;
    private Paint statusFalseForePaint;
    private Paint statusTrueBackPaint;
    private Paint statusFalseBackPaint;
    private Paint relativeBearingArrowPaint;

    private Paint namePaint;
    private InfoListItemData currentState = new InfoListItemData();

    public ListItemView(Context context) {
        super(context);

        init();
        setLayoutParams(new ViewGroup.LayoutParams(width, height));
    }

    public ListItemView(Context context, ListItemViewEventHandler eventHandler) {
        this(context);

        this.eventHandler = eventHandler;
    }

    private void init() {

        boundingRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundingRectPaint.setStyle(Paint.Style.STROKE);
        boundingRectPaint.setColor(boundingRectColor);
        boundingRectPaint.setStrokeWidth(2);

        fillRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillRectPaint.setStyle(Paint.Style.FILL);
        fillRectPaint.setColor(backColor);

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
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF boundingRect = new RectF(4, 4, this.width - 4, this.height - 4);

        canvas.drawRoundRect(boundingRect, 3, 3, fillRectPaint);
        canvas.drawRoundRect(boundingRect, 3, 3, boundingRectPaint);

        int columnRuler1 = 15;
        canvas.drawText(currentState.nameType, columnRuler1, 33, nameTypePaint);
        canvas.drawText(currentState.name, columnRuler1, 95, namePaint);
        canvas.drawText(currentState.cn, columnRuler1, 130, cnPaint);

        int columnRuler2 = 270;
        int columnRuler3 = 370;
        int row1 = 65;
        int row2 = 95;
        int row3 = 130;
        canvas.drawText(currentState.model, columnRuler2, row1, modelTextPaint);
        canvas.drawText(currentState.altitude, columnRuler2, row2, dataTextPaint);
        canvas.drawText(currentState.vRate, columnRuler3, row2, dataTextPaint);
        canvas.drawText(currentState.relativeDistance, columnRuler2, row3, dataTextPaint);
        canvas.drawText(currentState.relativeBearing, columnRuler3 + 25, row3, dataTextPaint);

        drawRelativeBearingArrow(canvas, columnRuler3 + 8, row3 - 5, currentState.relativeDegrees);

        int statusColumn = 500;
        int statusWidth = 60;
        int statusHeight = 25;
        int statusRounding = 3;
        int statusRow1 = 75;
        int statusRow2 = 110;
        int StatusTextHeight = 18;

        RectF statusBack = new RectF(statusColumn, statusRow1, statusColumn + statusWidth, statusRow1 + statusHeight);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, getStatusBackPaint(currentState.hasAdsb));
        canvas.drawText("ADS/B", statusColumn + 5, statusRow1 + StatusTextHeight, getStatusForePaint(currentState.hasAdsb));

        statusBack = new RectF(statusColumn, statusRow2, statusColumn + statusWidth, statusRow2 + statusHeight);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, getStatusBackPaint(currentState.hasOgn));
        canvas.drawText("Flarm", statusColumn + 8, statusRow2 + StatusTextHeight, getStatusForePaint(currentState.hasOgn));
    }

    private void drawRelativeBearingArrow(Canvas canvas, float x, float y, int bearing) {
        int arrowLength = 12;
        int arrowArmLength = 6;
        float arrowAngle = 135;

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

        float xMove = lineStartX - lineEndX;
        float yMove = lineStartY - lineEndY;

        lineStartX += xMove;
        lineStartY += yMove;

        lineEndX += xMove;
        lineEndY += yMove;

        arrRightX += xMove;
        arrRightY += yMove;

        arrLeftX += xMove;
        arrLeftY += yMove;

        canvas.drawLine(lineEndX, lineEndY, arrLeftX, arrLeftY, relativeBearingArrowPaint);
        canvas.drawLine(lineEndX, lineEndY, arrRightX, arrRightY, relativeBearingArrowPaint);
        canvas.drawLine(lineStartX, lineStartY, lineEndX, lineEndY, relativeBearingArrowPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(width, height);

        setMeasuredDimension(width, height);
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

        if (update.cn != currentState.cn) {
            currentState.cn = update.cn;
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
}
