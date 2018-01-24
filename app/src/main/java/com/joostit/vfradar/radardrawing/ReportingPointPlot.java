package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

import com.joostit.vfradar.site.ReportingPoint;

/**
 * Created by Joost on 24-1-2018.
 */

public class ReportingPointPlot extends DrawableItem {

    private final float symbolSize = 6;
    private Path symbolPath = new Path();
    private ReportingPoint source;
    private int symbolColor = 0xFFb3b300;
    private int textColor = 0xFFb3b300;
    private Paint symbolPaint;
    private Paint textPaint;
    private PointF screenPoint = null;


    public ReportingPointPlot(ReportingPoint source){
        this.source = source;
        init();
    }

    private void init() {

        symbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        symbolPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        symbolPaint.setStrokeWidth(5);
        symbolPaint.setColor(symbolColor);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(20);

    }

    @Override
    public void updateDrawing(SphericalMercatorProjection projection) {
        screenPoint = projection.toScreenPoint(source.position);

        symbolPath.rewind();

        symbolPath.moveTo(screenPoint.x - symbolSize, screenPoint.y + symbolSize);
        symbolPath.lineTo(screenPoint.x + symbolSize, screenPoint.y + symbolSize);
        symbolPath.lineTo(screenPoint.x, screenPoint.y - symbolSize);
        symbolPath.close();
    }


    @Override
    public void draw(Canvas canvas) {

        if(screenPoint == null){
            return;
        }

        Rect nameBounds = new Rect();
        textPaint.getTextBounds(source.name, 0, source.name.length(), nameBounds);

        canvas.drawPath(symbolPath, symbolPaint);
        canvas.drawText(source.name, screenPoint.x - (nameBounds.right / 2), screenPoint.y + (nameBounds.bottom - nameBounds.top) + 8, textPaint);
    }

}
