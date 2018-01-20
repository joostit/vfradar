package com.joostit.vfradar.radardrawing;

import android.graphics.Canvas;

/**
 * Created by Joost on 18-1-2018.
 */

public abstract class DrawableItem {


    public Boolean DoHitTest(float hitX, float hitY) {
        return false;
    }

    public abstract void Draw(Canvas canvas);


}
