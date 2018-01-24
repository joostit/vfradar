package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Joost on 18-1-2018.
 */

public class Button extends DrawableItem {

    private String character;
    private float x;
    private float y;
    private float dimension;

    private int buttonForeColor = 0xFF009900;
    private int buttonBackColor = 0xFF002200;

    private Paint buttonForePaint;
    private Paint buttonBackPaint;

    RectF buttonBounds = new RectF();

    public Button(String character, float x, float y, float dimension) {
        this.character = character;
        this.x = x;
        this.y = y;
        this.dimension = dimension;

        init();
    }

    private void init() {

        buttonForePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonForePaint.setStyle(Paint.Style.STROKE);
        buttonForePaint.setColor(buttonForeColor);
        buttonForePaint.setStrokeWidth(10);
        buttonForePaint.setTextAlign(Paint.Align.CENTER);
        buttonForePaint.setTextSize(60);

        buttonBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        buttonBackPaint.setColor(buttonBackColor);
        buttonBackPaint.setStrokeWidth(20);

    }


    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;

        buttonBounds = new RectF(x, y, (float) x + dimension, y + dimension);
    }


    @Override
    public void draw(Canvas canvas) {

        canvas.drawRoundRect(buttonBounds, 10, 10, buttonBackPaint);
        canvas.drawRoundRect(buttonBounds, 10, 10, buttonForePaint);

        float textHeight = buttonForePaint.descent() - buttonForePaint.ascent();
        float textOffset = (textHeight / 2) - buttonForePaint.descent();

        canvas.drawText(character, buttonBounds.centerX(), buttonBounds.centerY() + textOffset, buttonForePaint);
    }

    @Override
    public Boolean doHitTest(float hitX, float hitY) {
        return buttonBounds.contains(hitX, hitY);
    }
}
