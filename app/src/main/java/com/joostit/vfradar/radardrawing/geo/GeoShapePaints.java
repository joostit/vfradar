package com.joostit.vfradar.radardrawing.geo;

import android.graphics.Paint;

import com.joostit.vfradar.geo.GeoObjectTypes;

/**
 * Created by Joost on 5-2-2018.
 */

public final class GeoShapePaints {

    private GeoShapePaints() {
    }

    public static final int notificationAreaForeColor = 0xBBffff00;
    public static final int notificationAreaBackColor = 0x44ffff00;
    public static final int notificationAreaTextColor = 0xFFffff00;

    public static final int warningAreaForeColor = 0xBBff0000;
    public static final int warningAreaBackColor = 0x44ff0000;
    public static final int warningAreaTextColor = 0xFFff0000;

    public static final int genericPolygonForeColor = 0;
    public static final int genericPolygonBackColor = 0xFF404040;
    public static final int genericPolygonTextColor = 0xFF808080;

    public static final int strokeWidth = 2;
    public static final int nameTextSize = 25;


    public static Paint createPolygonForePaint(GeoObjectTypes type) {
        int color = 0;
        switch (type) {
            case GenericGeoArea:
                color = genericPolygonForeColor;
                break;

            case NotificationArea:
                color = notificationAreaForeColor;
                break;
            case WarningArea:
                color = warningAreaForeColor;
                break;
        }

        if (color == 0) {
            return null;
        } else {
            Paint retVal = new Paint(Paint.ANTI_ALIAS_FLAG);
            retVal.setStyle(Paint.Style.STROKE);
            retVal.setStrokeWidth(GeoShapePaints.strokeWidth);
            retVal.setColor(color);
            return retVal;
        }
    }


    public static Paint createPolygonBackPaint(GeoObjectTypes type) {
        int color = 0;
        switch (type) {
            case GenericGeoArea:
                color = genericPolygonBackColor;
                break;

            case NotificationArea:
                color = notificationAreaBackColor;
                break;
            case WarningArea:
                color = warningAreaBackColor;
                break;
        }

        if (color == 0) {
            return null;
        } else {
            Paint retVal = new Paint(Paint.ANTI_ALIAS_FLAG);
            retVal.setStyle(Paint.Style.FILL);
            retVal.setStrokeWidth(strokeWidth);
            retVal.setColor(color);
            return retVal;
        }
    }

    public static Paint createPolygonTextPaint(GeoObjectTypes type) {
        int color = 0;
        switch (type) {
            case GenericGeoArea:
                color = genericPolygonTextColor;
                break;

            case NotificationArea:
                color = notificationAreaTextColor;
                break;
            case WarningArea:
                color = warningAreaTextColor;
                break;
        }

        if (color == 0) {
            return null;
        } else {
            Paint retVal = new Paint(Paint.ANTI_ALIAS_FLAG);
            retVal.setStyle(Paint.Style.FILL);
            retVal.setColor(color);
            retVal.setTextSize(nameTextSize);
            return retVal;
        }
    }

}
