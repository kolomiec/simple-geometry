package uk.ks.jarvis.simple.geometry.utils;

import uk.ks.jarvis.simple.geometry.beans.Point;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getAngleFrom2Points;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;

/**
 * Created by sayenko on 10/22/13.
 */
public class Zoom {
    private Point prvCoordinate2;
    private Point prvCoordinate1;
    private Point firstCoordinate;
    private Point secondCoordinate;
    private Point zoomPoint = new Point(0f, 0f);
    private Point prvZoomPoint = new Point(0f, 0f);
    private double zoomRatio = 1;
    private float angle = 0;
    private float previosAngle = 0;

    public void initZoom(Point firstCoordinate, Point secondCoordinate) {
        prvCoordinate1 = new Point(firstCoordinate);
        prvCoordinate2 = new Point(secondCoordinate);
        zoomPoint = new Point((firstCoordinate.getX() + secondCoordinate.getX()) / 2, (firstCoordinate.getY() + secondCoordinate.getY()) / 2);
    }

    public void zoom(Point coordinate1, Point coordinate2) {
        firstCoordinate = coordinate1;
        secondCoordinate = coordinate2;
        prvZoomPoint = zoomPoint;
        zoomPoint = new Point((firstCoordinate.getX() + secondCoordinate.getX()) / 2, (firstCoordinate.getY() + secondCoordinate.getY()) / 2);
        zoomRatio = getLength(firstCoordinate, secondCoordinate) / getLength(prvCoordinate1, prvCoordinate2);
        prvCoordinate1 = new Point(firstCoordinate);
        prvCoordinate2 = new Point(secondCoordinate);
        previosAngle = angle;
        angle = getAngleFrom2Points(firstCoordinate, secondCoordinate);
    }

    public Point getZoomPoint() {
        return zoomPoint;
    }

    public double getZoomRatio() {
        return zoomRatio;
    }

    public double getZoomAngle() {
        return angle;
    }

    public Point getMoveDelta() {
        return new Point(zoomPoint.getX() - prvZoomPoint.getX(), zoomPoint.getY() - prvZoomPoint.getY());
    }

    @Override
    public String toString() {
        return "ZoomRatio: " + zoomRatio + ", x:" + Math.round(zoomPoint.getX()) + ", y:" + Math.round(zoomPoint.getY());
    }
}
