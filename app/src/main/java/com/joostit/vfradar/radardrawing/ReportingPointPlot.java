package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.joostit.vfradar.site.ReportingPoint;

/**
 * Created by Joost on 24-1-2018.
 */

public class ReportingPointPlot extends DrawableItem {

    private final float symbolSize = 6;
    private Path symbolPath = new Path();
    private ReportingPoint source;
    private int symbolColor = 0xFFe6e600;
    private int textColor = 0xFFe6e600;
    private Paint symbolPaint;
    private Paint textPaint;
    private PointF screenPoint = null;
    private boolean doDraw;

    public ReportingPointPlot(ReportingPoint source) {
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
    public boolean updateDrawing(SphericalMercatorProjection projection, RectF bounds, ZoomLevelInfo zoomLevelInfo) {
        screenPoint = projection.toScreenPoint(source.position);

        doDraw = bounds.contains(screenPoint.x, screenPoint.y);

        if (doDraw) {
            Path newPath = new Path();

            newPath.moveTo(screenPoint.x - symbolSize, screenPoint.y + symbolSize);
            newPath.lineTo(screenPoint.x + symbolSize, screenPoint.y + symbolSize);
            newPath.lineTo(screenPoint.x, screenPoint.y - symbolSize);
            newPath.close();

            symbolPath = newPath;
        }

        return doDraw;
    }


    @Override
    public void draw(Canvas canvas) {

        if (!doDraw) {
            return;
        }

        Rect nameBounds = new Rect();
        textPaint.getTextBounds(source.name, 0, source.name.length(), nameBounds);

        canvas.drawPath(symbolPath, symbolPaint);
        canvas.drawText(source.name, screenPoint.x - (nameBounds.right / 2), screenPoint.y + (nameBounds.bottom - nameBounds.top) + 10, textPaint);
    }

}
