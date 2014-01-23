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
    private Point zoomPoint = new Point(0f, 0f);
    private Point prvZoomPoint = new Point(0f, 0f);
    private double zoomRatio = 1;
    private double angle = 0;
    private double prvAngle = 0;
    private float currZoom = 100;

    public void initZoom(Point firstCoordinate, Point secondCoordinate) {
        prvCoordinate1 = new Point(firstCoordinate);
        prvCoordinate2 = new Point(secondCoordinate);
        zoomPoint = new Point((firstCoordinate.getX() + secondCoordinate.getX()) / 2, (firstCoordinate.getY() + secondCoordinate.getY()) / 2);
        angle = getAngleFrom2Points(zoomPoint, secondCoordinate);
    }

    public void zoom(Point firstCoordinate, Point secondCoordinate) {
        prvZoomPoint = zoomPoint;
        zoomPoint = new Point((firstCoordinate.getX() + secondCoordinate.getX()) / 2, (firstCoordinate.getY() + secondCoordinate.getY()) / 2);
        zoomRatio = getLength(firstCoordinate, secondCoordinate) / getLength(prvCoordinate1, prvCoordinate2);
        currZoom += zoomRatio - 1;

//        if (currZoom < 99) {
//            currZoom = 99;
//            zoomRatio = 1;
//        }
//        if (currZoom > 101) {
//            currZoom = 101;
//            zoomRatio = 1;
//        }

        prvCoordinate1 = new Point(firstCoordinate);
        prvCoordinate2 = new Point(secondCoordinate);
        prvAngle = angle;
        angle = getAngleFrom2Points(zoomPoint, secondCoordinate);
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

    public double getAngleDelta() {
        return (angle - prvAngle) % 360;
    }

    public float getCurrZoom() {
        return currZoom;
    }

    @Override
    public String toString() {
        return "currZoom " + currZoom + ", zoomRatio " + zoomRatio;
    }
}
