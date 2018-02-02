package com.joostit.vfradar.utilities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Joost on 2-2-2018.
 */

public final class DrawUtils {

    private static final int statusRounding = 3;

    public static RectF drawStatusRect(Canvas canvas, float x, float y, float width, float height, String text, Paint boundsPaint, Paint forePaint, Paint backPaint){

        RectF statusBack = new RectF(x, y, x + width, y + height);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, backPaint);
        canvas.drawRoundRect(statusBack, statusRounding, statusRounding, boundsPaint);

        PointF textPoint = centerText(forePaint, statusBack, text);
        canvas.drawText(text, textPoint.x, textPoint.y - 1, forePaint);
        return statusBack;
    }

    public static PointF centerText(Paint paint, RectF drawBounds, String text){
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        int textStretchX = textBounds.right;
        int textStretchY = textBounds.bottom - textBounds.top;

        PointF newTextPoint = new PointF();
        newTextPoint.x = drawBounds.centerX() - (textStretchX / 2);
        newTextPoint.y = drawBounds.centerY() + (textStretchY / 2);
        return newTextPoint;
    }


}
