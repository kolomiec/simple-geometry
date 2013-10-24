package uk.ks.jarvis.simple.geometry.utils;

import uk.ks.jarvis.simple.geometry.beans.Point;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;

/**
 * Created by sayenko on 10/22/13.
 */
public class Zoom {
    private Point originalCoordinate2;
    private Point originalCoordinate1;
    private Point zoomPoint = new Point(0f, 0f);
    private double zoomRatio = 1;

    public void initZoom(Point firstCoordinate, Point secondCoordinate) {
        originalCoordinate1 = new Point(firstCoordinate);
        originalCoordinate2 = new Point(secondCoordinate);
    }

    public void zoom(Point firstCoordinate, Point secondCoordinate) {
        zoomPoint = new Point((firstCoordinate.getX() + secondCoordinate.getX()) / 2, (firstCoordinate.getY() + secondCoordinate.getY()) / 2);
        zoomRatio = getLength(firstCoordinate, secondCoordinate) / getLength(originalCoordinate1, originalCoordinate2);
        originalCoordinate1 = new Point(firstCoordinate);
        originalCoordinate2 = new Point(secondCoordinate);
    }

    public Point getZoomPoint() {
        return zoomPoint;
    }

    public double getZoomRatio() {
        return zoomRatio;
    }

    @Override
    public String toString() {
        return "ZoomRatio: " + zoomRatio + ", x:" + Math.round(zoomPoint.getX()) + ", y:" + Math.round(zoomPoint.getY());
    }
}
