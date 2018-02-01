package com.joostit.vfradar.InfoBar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.joostit.vfradar.data.AircraftTrackingUpdate;

/**
 * Created by Joost on 1-2-2018.
 */

public class StatusBarView extends View {

    private final int boundingRectMargin = 4;
    private final int statusIndicatorsY = 15;
    private final int statusRounding = 3;
    private final int statusHeight = 38;

    private int statusTextSize = 21;
    private int dataTextSize = 30;

    private final int statusTrueBackColor = 0xFF00DD00;
    private final int statusFalseBackColor = 0xFFff0000;
    private final int statusTrueForeColor = 0xFF002200;
    private final int statusFalseForeColor = 0xFF110000;
    private int statusRectForeColor = 0xFF009900;
    private int boundingRectColor = 0xFF00DD00;
    private int backColor = 0xFF002200;
    private int dataTextColor = 0xFF00FF00;
    private int dataTextBackColor = 0xFF001100;
    private int dataTextBoundColor = 0xFF00AA00;

    private Paint statusTrueForePaint;
    private Paint statusFalseForePaint;
    private Paint statusTrueBackPaint;
    private Paint statusFalseBackPaint;
    private Paint statusRectForePaint;
    private Paint boundingRectPaint;
    private Paint backPaint;
    private Paint dataTextPaint;
    private Paint dataTextBackPaint;
    private Paint dataTextBoundPaint;

    private AircraftTrackingUpdate lastUpdate = new AircraftTrackingUpdate();

    public StatusBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        init();
    }


    public void updateStatus(AircraftTrackingUpdate update){
        this.lastUpdate = update;
        this.invalidate();
    }


    private void init() {
        boundingRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boundingRectPaint.setStyle(Paint.Style.STROKE);
        boundingRectPaint.setColor(boundingRectColor);
        boundingRectPaint.setStrokeWidth(2);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setColor(backColor);

        statusRectForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusRectForePaint.setStyle(Paint.Style.STROKE);
        statusRectForePaint.setColor(statusRectForeColor);
        statusRectForePaint.setStrokeWidth(1.5f);

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

        dataTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataTextPaint.setStyle(Paint.Style.FILL);
        dataTextPaint.setColor(dataTextColor);
        dataTextPaint.setTextSize(dataTextSize);

        dataTextBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataTextBackPaint.setStyle(Paint.Style.FILL);
        dataTextBackPaint.setColor(dataTextBackColor);


        dataTextBoundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dataTextBoundPaint.setStyle(Paint.Style.STROKE);
        dataTextBoundPaint.setColor(dataTextBoundColor);
        dataTextBoundPaint.setStrokeWidth(1.5f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF boundingRect = new RectF(boundingRectMargin, boundingRectMargin, this.getWidth() - boundingRectMargin, this.getHeight() - boundingRectMargin);
        canvas.drawRect(boundingRect, backPaint);
        canvas.drawRect(boundingRect, boundingRectPaint);

        String scenarioName = "TZC_ZuidCircuit_23";

        Rect textBounds = new Rect();
        dataTextPaint.getTextBounds(scenarioName, 0, scenarioName.length(), textBounds);

        float txtX = 30;
        float txtY = 45;

        canvas.drawText(scenarioName, 0, scenarioName.length(), txtX, txtY, dataTextPaint);

        drawStatusRect(canvas, 462, statusIndicatorsY, 90, "Network", lastUpdate.getUpdateSuccess());
    }

    private void drawStatusRect(Canvas canvas, float x, float y, float width, String text, boolean status){

        RectF statusBack = new RectF(x, y, x + width, y + statusHeight);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, getStatusBackPaint(status));
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, statusRectForePaint);

        PointF textPoint = centerText(statusBack, text);
        canvas.drawText(text, textPoint.x, textPoint.y - 1, getStatusForePaint(status));
    }

    private PointF centerText(RectF drawBounds, String text){
        Rect textBounds = new Rect();
        statusTrueForePaint.getTextBounds(text, 0, text.length(), textBounds);
        int textStretchX = textBounds.right;
        int textStretchY = textBounds.bottom - textBounds.top;

        PointF newTextPoint = new PointF();
        newTextPoint.x = drawBounds.centerX() - (textStretchX / 2);
        newTextPoint.y = drawBounds.centerY() + (textStretchY / 2);
        return newTextPoint;
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
}
