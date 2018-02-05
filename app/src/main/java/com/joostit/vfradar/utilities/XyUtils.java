package com.joostit.vfradar.utilities;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Joost on 5-2-2018.
 */

public final class XyUtils {

    public XyUtils() {
    }

    /**
     * Creates a bounding Rect for the two points, with correct side values and a non-negative width
     *
     * @param p1 Point 1
     * @param p2 Point 2
     * @return The bounding rect for both points
     */
    public static RectF createBoundsRect(PointF p1, PointF p2) {
        float largeX = p1.x > p2.x ? p1.x : p2.x;
        float largeY = p1.y > p2.y ? p1.y : p2.y;
        float smallX = p1.x <= p2.x ? p1.x : p2.x;
        float smallY = p1.y <= p2.y ? p1.y : p2.y;
        return new RectF(smallX, smallY, largeX, largeY);
    }


    public static PointF getLineIntersection(PointF lineA1, PointF lineA2, PointF lineB1, PointF lineB2) {
        PointF result = null;

        double s1_x = lineA2.x - lineA1.x;
        double s1_y = lineA2.y - lineA1.y;

        double s2_x = lineB2.x - lineB1.x;
        double s2_y = lineB2.y - lineB1.y;

        double s = (-s1_y * (lineA1.x - lineB1.x) + s1_x * (lineA1.y - lineB1.y)) / (-s2_x * s1_y + s1_x * s2_y);
        double t = (s2_x * (lineA1.y - lineB1.y) - s2_y * (lineA1.x - lineB1.x)) / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision detected
            result = new PointF(
                    (int) (lineA1.x + (t * s1_x)),
                    (int) (lineA1.y + (t * s1_y)));
        }

        return result;
    }

}
