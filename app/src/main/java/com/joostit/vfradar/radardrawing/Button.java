package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Joost on 18-1-2018.
 */

public class Button extends DrawableItem {

    private final float lineGrowSize = -3;
    private final int textSize = 50;
    private final float foreStrokeWidth = 8;
    private final float lineStrokeWidth = 2;

    RectF buttonBounds = new RectF();
    RectF lineBounds = new RectF();
    private String character;
    private float x;
    private float y;
    private float dimension;
    private int buttonForeColor = 0xFF009900;
    private int buttonBackColor = 0xFF002200;
    private int buttonLineColor = 0xFF00DD00;
    private Paint buttonLinePaint;
    private Paint buttonForePaint;
    private Paint buttonBackPaint;

    public Button(String character, float x, float y, float dimension) {
        this.character = character;
        this.x = x;
        this.y = y;
        this.dimension = dimension;

        init();
    }

    private void init() {

        buttonLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonLinePaint.setStyle(Paint.Style.STROKE);
        buttonLinePaint.setColor(buttonLineColor);
        buttonLinePaint.setStrokeWidth(lineStrokeWidth);

        buttonForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonForePaint.setStyle(Paint.Style.STROKE);
        buttonForePaint.setColor(buttonForeColor);
        buttonForePaint.setStrokeWidth(foreStrokeWidth);
        buttonForePaint.setTextAlign(Paint.Align.CENTER);
        buttonForePaint.setTextSize(textSize);

        buttonBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonBackPaint.setStyle(Paint.Style.FILL);
        buttonBackPaint.setColor(buttonBackColor);
        buttonBackPaint.setStrokeWidth(20);

    }


    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;

        buttonBounds = new RectF(x, y, (float) x + dimension, y + dimension);
        lineBounds = new RectF(x - lineGrowSize, y - lineGrowSize, (float) x + dimension + lineGrowSize, y + dimension + lineGrowSize);
    }


    @Override
    public void draw(Canvas canvas) {

        canvas.drawRoundRect(buttonBounds, 10, 10, buttonBackPaint);
        canvas.drawRoundRect(buttonBounds, 10, 10, buttonForePaint);
        canvas.drawRoundRect(lineBounds, 10, 10, buttonLinePaint);

        float textHeight = buttonForePaint.descent() - buttonForePaint.ascent();
        float textOffset = (textHeight / 2) - buttonForePaint.descent();

        canvas.drawText(character, buttonBounds.centerX(), buttonBounds.centerY() + textOffset, buttonForePaint);
    }

    @Override
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds) {
        // No specific implementation
        return true;
    }

    @Override
    public Boolean doHitTest(float hitX, float hitY) {
        return buttonBounds.contains(hitX, hitY);
    }
}
