package com.joostit.vfradar.radardrawing;


import android.graphics.PointF;

import com.joostit.vfradar.geo.LatLon;

/**
 * Created by Joost on 16-1-2018.
 */
/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/


public class SphericalMercatorProjection {
    private double mWorldWidth;
    private double offsetX = 0;
    private double offsetY = 0;

    private final double WORLD_CIRCUMFERENCE = 40075000;    // Circumference of the earth in meters

    public SphericalMercatorProjection(final double worldWidth) {
        mWorldWidth = worldWidth;
    }

    public synchronized void setScreen(double screenSize, double screenWidth, double screenHeight, double screenViewRadius, LatLon centerPosition) {
        mWorldWidth = ((WORLD_CIRCUMFERENCE / 1.65) / (screenViewRadius * 2)) * screenSize;    // The word size in pixels

        PointF screenCenterpoint = toWorldPoint(centerPosition);

        offsetX = screenCenterpoint.x - (0.5 * screenWidth);
        offsetY = screenCenterpoint.y - (0.5 * screenHeight);
    }

    @SuppressWarnings("deprecation")
    public synchronized PointF toScreenPoint(final LatLon position) {
        return toScreenPoint(position.Latitude, position.Longitude);
    }

    @SuppressWarnings("deprecation")
    public synchronized PointF toScreenPoint(final double lat, final double lon) {
        final double x = lon / 360 + .5;
        final double siny = Math.sin(Math.toRadians(lat));
        final double y = 0.5 * Math.log((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5;

        PointF retVal = new PointF((float) (x * mWorldWidth), (float) (y * mWorldWidth));

        retVal.x -= offsetX;
        retVal.y -= offsetY;

        return retVal;
    }

    @SuppressWarnings("deprecation")
    public synchronized PointF toWorldPoint(final LatLon position) {
        final double x = position.Longitude / 360 + .5;
        final double siny = Math.sin(Math.toRadians(position.Latitude));
        final double y = 0.5 * Math.log((1 + siny) / (1 - siny)) / -(2 * Math.PI) + .5;
        PointF retVal = new PointF((float) (x * mWorldWidth), (float) (y * mWorldWidth));
        return retVal;
    }

//    public LatLng toLatLng(com.google.maps.android.geometry.Point point) {
//        final double x = point.x / mWorldWidth - 0.5;
//        final double lng = x * 360;
//
//        double y = .5 - (point.y / mWorldWidth);
//        final double lat = 90 - Math.toDegrees(Math.atan(Math.exp(-y * 2 * Math.PI)) * 2);
//
//        return new LatLng(lat, lng);
//    }
}
