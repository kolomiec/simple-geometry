package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.coordinateplane.SystemInformation;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.round;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getAngleFrom2Points;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.tg;

/**
 * Created by sayenko on 7/17/13.
 */

public class Line extends BaseShape {
    private static final int NUMBER_OF_FIRST_POINT = 1;
    private static final int NUMBER_OF_SECOND_POINT = 2;
    private final String label;
    private int color = 0;
    private int numberTouchedPoint = 0;
    private double angle;
    private Point lastTouchCoordinates = new Point(0f, 0f);
    private Point point;
    private Point deltaTouchCoordinates = new Point(0f, 0f);


    public Line(Point point, Float angle, String label) {
        this.point = point;
        this.label = label;
        this.angle = angle % 360;
        color = BaseHelper.getRandomColor();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        angle = angle % 360;
        paint.setStyle(Paint.Style.FILL);

        canvas.drawLine((float) getFirstPoint(angle).getX(), (float) getFirstPoint(angle).getY(), (float) getSecondPoint(angle).getX(), (float) getSecondPoint(angle).getY(), paint);

        Float pointRadius = 5.0f;
        canvas.drawCircle((float) point.getX(), (float) point.getY(), pointRadius, paint);
        BaseHelper.drawTextWithShadow(canvas, label, (float) getFirstPoint(angle).getX() + pointRadius, (float) getFirstPoint(angle).getY() - pointRadius / 2);
        BaseHelper.drawTextWithShadow(canvas, label, (float) getSecondPoint(angle).getX() + pointRadius, (float) getSecondPoint(angle).getY() - pointRadius / 2);
    }

    @Override
    public String toString() {
        return "Line " + label + " - x: " + Math.round(point.getX()) + " - y: " + Math.round(point.getY()) +
                ", angle : " + round(angle) + ", newAngle : " + (90 * (atan(tg(angle)) / atan(tg(90)))) + ", tg : " + tg(angle);
    }

    public Point getFirstPoint(double angle) {
        Point p = new Point(0f, 0f);
        if ((angle >= 0) && (angle < 45)) { // ok
            p.setX(SystemInformation.DISPLAY_WIDTH);
            p.setY(((-SystemInformation.DISPLAY_WIDTH + point.getX()) * tg(angle)) + point.getY());
        } else if ((angle >= 45) && (angle < 90)) { // ok
            p.setX(((point.getY()) / tg(angle)) + point.getX());
            p.setY(0f);
        } else if ((angle >= 90) && (angle < 135)) { // ok
            p.setX(((point.getY()) / tg(angle)) + point.getX());
            p.setY(0f);
        } else if ((angle >= 135) && (angle < 180)) { // ok
            p.setX(0f);
            p.setY(((point.getX()) * tg(angle)) + point.getY());
        } else if ((angle >= 180) && (angle < 225)) { // ok
            p.setX(0f);
            p.setY(((point.getX()) * tg(angle)) + point.getY());
        } else if ((angle >= 225) && (angle < 270)) { // ok
            p.setX(((-SystemInformation.DISPLAY_HEIGHT + point.getY()) / tg(angle)) + point.getX());
            p.setY(SystemInformation.DISPLAY_HEIGHT);
        } else if ((angle >= 270) && (angle < 315)) { // ok
            p.setX(((-SystemInformation.DISPLAY_HEIGHT + point.getY()) / tg(angle)) + point.getX());
            p.setY(SystemInformation.DISPLAY_HEIGHT);
        } else if ((angle >= 315) && (angle < 360)) { // ok
            p.setX(SystemInformation.DISPLAY_WIDTH);
            p.setY(((-SystemInformation.DISPLAY_WIDTH + point.getX()) * tg(angle)) + point.getY());
        }
        return p;
    }

    public Point getSecondPoint(double angle) {
        return getFirstPoint((180 + angle) % 360);
    }

    @Override
    public void move(Point point, boolean onlyMove) {
        if (onlyMove || numberTouchedPoint == 0) {
            changePointCoordinates(point);
        } else {
            setAngle(point);
        }
    }

    public void setAngle(Point point) {
        angle = bringToAngles(getAngleFrom2Points(this.point, point));
        if (numberTouchedPoint == NUMBER_OF_SECOND_POINT) angle = (angle + 180) % 360;
    }

    private float bringToAngles(float angle) {
        final int delta = 4;
        float angleOfAttraction = 0f;
        do {
            if (abs(angle - angleOfAttraction) < delta) angle = angleOfAttraction;
            angleOfAttraction += 45;
        } while (angleOfAttraction < 360);
        return angle;
    }

    @Override
    public boolean isTouched(Point point) {
        setPoint(lastTouchCoordinates, point);
        if (isDotTouched(this.point, point)) {
            numberTouchedPoint = 0;
            return true;
        } else {
            if (isLineTouched(this.point, getFirstPoint(angle), point)) {
                numberTouchedPoint = NUMBER_OF_FIRST_POINT;
                return true;
            } else if (isLineTouched(this.point, getSecondPoint(angle), point)) {
                numberTouchedPoint = NUMBER_OF_SECOND_POINT;
                return true;
            }
        }
        return false;
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
        return null;
    }

    public boolean isLineTouched(Point linePoint1, Point linePoint2, Point point) {
        return ((getLength(linePoint1, point) + getLength(linePoint2, point) -
                getLength(linePoint1, linePoint2)) < 3);
    }

    public boolean isDotTouched(Point p1, Point p2) {
        float delta = 20f;
        if ((p1.getX() < (p2.getX() + delta)) && (p1.getX() > (p2.getX() - delta)))
            if ((p1.getY() < (p2.getY() + delta)) && (p1.getY() > (p2.getY() - delta))) {
                return true;
            }
        return false;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void changeCoordinatesToDelta(Point delta) {

    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void zoom(Point centralZoomPoint, float zoomRatio, Point moveDelta) {

    }

    @Override
    public void turn(Point centralTurnPoint, double angle) {
        this.angle += angle;

//        angle = (angle + 360) / 360;

        if (this.angle < 0) {
            this.angle += 360;
        }
        if (this.angle > 360) {
            this.angle %= 360;
        }
    }

    public void changePointCoordinates(Point touchCoordinates) {
        setPoint(deltaTouchCoordinates, lastTouchCoordinates.getX() - touchCoordinates.getX(), lastTouchCoordinates.getY() - touchCoordinates.getY());
        setPoint(this.point, this.point.getX() - deltaTouchCoordinates.getX(), this.point.getY() - deltaTouchCoordinates.getY());
        setPoint(lastTouchCoordinates, touchCoordinates);
    }
}