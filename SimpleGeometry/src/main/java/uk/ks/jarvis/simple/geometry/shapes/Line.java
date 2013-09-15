package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.CoordinatePlane.SystemInformation;
import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLengthBetweenTwoPoints;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;

/**
 * Created by sayenko on 7/17/13.
 */

public class Line implements Shape {
    private final String label;
    public int color = 0;
    public int numberTouchedPoint = 0;
    Point lastTouchCoordinates = new Point(0f, 0f);
    Point deltaTouchCoordinates = new Point(0f, 0f);
    private Point point;
    private Float tgOfAngle, tgOfDrawedAngle;


    public Line(Point point, Float angle, String label) {
        this.point = point;
        this.label = label;
        this.tgOfAngle = angle;
        this.tgOfDrawedAngle = this.tgOfAngle;
        color = BaseHelper.getRandomColor();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);

        canvas.drawLine(getFirstPoint().getX(), getFirstPoint().getY(), getSecondPoint().getX(), getSecondPoint().getY(), paint);

        Float pointRadius = 5.0f;

        BaseHelper.drawTextWithShadow(canvas, "1", getFirstPoint().getX() + pointRadius, getFirstPoint().getY() - pointRadius / 2);
        BaseHelper.drawTextWithShadow(canvas, "2", getSecondPoint().getX() + pointRadius, getSecondPoint().getY() - pointRadius / 2);
    }

    private int calculateAngle() {
        Point coord = new Point(point.getX() - getFirstPoint().getX(), point.getY() - getFirstPoint().getY());
        Float delta = 90 / (coord.getX() + coord.getY());

        return Math.round(delta * coord.getY());
    }

    @Override
    public String toString() {
        return "Line " + label + " - x1: " + Math.round(point.getX()) + ", angle : " + calculateAngle();
    }

    public Point getFirstPoint() {
        Point p = new Point(0f, 0f);
        if (tgOfAngle >= 1) {
            p.setY(((SystemInformation.DISPLAY_WIDTH - point.getX()) / tgOfAngle) + point.getY());
            p.setX((float) SystemInformation.DISPLAY_WIDTH);
        } else if ((tgOfAngle < 1) && (tgOfAngle > -1)) {
            p.setX(((SystemInformation.DISPLAY_HEIGHT - point.getY()) * tgOfAngle) + point.getX());
            p.setY((float) SystemInformation.DISPLAY_HEIGHT);
        } else if (tgOfAngle < -1) {
            p.setY((-point.getX() / tgOfAngle) + point.getY());
            p.setX(0f);
        }
        return p;
    }

    public Point getSecondPoint() {
        Point p = new Point(0f, 0f);
        if (tgOfAngle >= 1) {
            p.setY(((-point.getX()) / tgOfAngle) + point.getY());
            p.setX(0f);
        } else if ((tgOfAngle < 1) && (tgOfAngle > -1)) {
            p.setX(((-point.getY()) * tgOfAngle) + point.getX());
            p.setY(0f);
        } else if (tgOfAngle < -1) {
            p.setY(((SystemInformation.DISPLAY_WIDTH - point.getX()) / tgOfAngle) + point.getY());
            p.setX((float) SystemInformation.DISPLAY_WIDTH);
        }
        return p;
    }

    @Override
    public void move(Point point, boolean onlyMove) {
        if (!onlyMove) {
        }
        if (numberTouchedPoint == 0) {
            changePointCoordinates(this.point, point);
        } else {
            tgOfAngle = (this.point.getX() - point.getX()) / (this.point.getY() - point.getY());
        }
    }

    @Override
    public boolean isTouched(Point point) {
        setPoint(lastTouchCoordinates, point);
        if (isDotTouched(this.point, point)) {
            numberTouchedPoint = 0;
            return true;
        } else {
            if (isLineTouched(this.point, getFirstPoint(), point)) {
                numberTouchedPoint = 1;
                return true;
            } else if (isLineTouched(this.point, getSecondPoint(), point)) {
                numberTouchedPoint = 2;
                return true;
            }
        }
        return false;
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
//        if (circle.isBorderTouched(this.getPoint1(), 20)) {
//            Point newCoordinates = circle.getNewCoordinates(this.drawedPoint1);
//            Point changedPoint1 = new Point(drawedPoint1);
//            Point changedPoint2 = new Point(drawedPoint2);
//            switch (this.getNumberOfSelectedPoint()) {
//                case 0:
//                    Point deltaCoordinates = new Point(this.point.getX() - this.point2.getX(), this.point.getY() - this.point2.getY());
//                    setPoint(changedPoint1, newCoordinates);
//                    setPoint(changedPoint2, changedPoint1.getX() - deltaCoordinates.getX(), changedPoint1.getY() - deltaCoordinates.getY());
//                    break;
//                case 1:
//                    setPoint(changedPoint1, newCoordinates);
//                    break;
//                case 2:
//                    break;
//            }
//            return new Point(drawedPoint1.getX() - changedPoint1.getX(), drawedPoint1.getY() - changedPoint1.getY());
//        } else if (circle.isBorderTouched(this.getPoint2(), 20)) {
//            Point newCoordinates = circle.getNewCoordinates(this.drawedPoint2);
//            Point changedPoint1 = new Point(drawedPoint1);
//            Point changedPoint2 = new Point(drawedPoint2);
//            switch (this.getNumberOfSelectedPoint()) {
//                case 0:
//                    Point deltaCoordinates = new Point(this.point.getX() - this.point2.getX(), this.point.getY() - this.point2.getY());
//                    setPoint(changedPoint2, newCoordinates);
//                    setPoint(changedPoint1, changedPoint2.getX() + deltaCoordinates.getX(), changedPoint2.getY() + deltaCoordinates.getY());
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    setPoint(changedPoint2, newCoordinates);
//                    break;
//            }
//            return new Point(drawedPoint2.getX() - changedPoint2.getX(), drawedPoint2.getY() - changedPoint2.getY());
//        } else {
//            Point p = this.getNewCoordinates(circle.getCoordinatesOfCenterPoint());
//            if (this.isLineTouched(p)) {
//                double length = getLengthBetweenTwoPoints(p, circle.getCoordinatesOfCenterPoint());
//                if (((length) < (circle.getRadius() + 15)) && ((length) > (circle.getRadius() - 15))) {
//                    Point delta = circle.getNewCoordinates(p);
//
//                    setPoint(delta, p.getX() - delta.getX(), p.getY() - delta.getY());
//                    return delta;
//                }
//            }
//        }
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
//        if (line.isLineTouched(this.getPoint1())) {
//
//            Point newCoordinates = line.getNewCoordinates(this.point);
//            Point changedPoint1 = new Point(drawedPoint1);
//            Point changedPoint2 = new Point(drawedPoint2);
//            switch (this.getNumberOfSelectedPoint()) {
//                case 0:
//                    Point deltaCoordinates = new Point(this.point.getX() - this.point2.getX(), this.point.getY() - this.point2.getY());
//                    setPoint(changedPoint1, newCoordinates);
//                    setPoint(changedPoint2, changedPoint1.getX() - deltaCoordinates.getX(), changedPoint1.getY() - deltaCoordinates.getY());
//                    break;
//                case 1:
//                    setPoint(changedPoint1, newCoordinates);
//                    break;
//                case 2:
//                    break;
//            }
//            return new Point(drawedPoint1.getX() - changedPoint1.getX(), drawedPoint1.getY() - changedPoint1.getY());
//        }
//        if (line.isLineTouched(this.getPoint2())) {
//            Point newCoordinates = line.getNewCoordinates(this.getPoint2());
//            Point changedPoint1 = new Point(drawedPoint1);
//            Point changedPoint2 = new Point(drawedPoint2);
//            switch (this.getNumberOfSelectedPoint()) {
//                case 0:
//                    Point deltaCoordinates = new Point(this.point.getX() - this.point2.getX(), this.point.getY() - this.point2.getY());
//                    setPoint(changedPoint2, newCoordinates);
//                    setPoint(changedPoint1, changedPoint2.getX() + deltaCoordinates.getX(), changedPoint2.getY() + deltaCoordinates.getY());
//                    break;
//                case 1:
//                    break;
//                case 2:
//                    setPoint(changedPoint2, newCoordinates);
//                    break;
//            }
//            return new Point(drawedPoint2.getX() - changedPoint2.getX(), drawedPoint2.getY() - changedPoint2.getY());
//        }
        return null;
    }

    @Override
    public void refreshCoordinates() {
    }

    public boolean isLineTouched(Point linePoint1, Point linePoint2, Point point) {
        return ((getLengthBetweenTwoPoints(linePoint1, point) + getLengthBetweenTwoPoints(linePoint2, point) -
                getLengthBetweenTwoPoints(linePoint1, linePoint2)) < 3);
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
    public Point setFigureThatItWillNotBeOutsideTheScreen(float maxX, float maxY) {
        return null;
    }

    @Override
    public void changeCoordinatesToDelta(Point delta) {
//        drawedPoint1.setX(drawedPoint1.getX() - delta.getX());
//        drawedPoint1.setY(drawedPoint1.getY() - delta.getY());
//        drawedPoint2.setX(drawedPoint2.getX() - delta.getX());
//        drawedPoint2.setY(drawedPoint2.getY() - delta.getY());
    }

    public void changePointCoordinates(Point point1, Point touchCoordinates) {
//        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
//        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, touchCoordinates);
    }

    public void changeLineCoordinates(Point point1, Point point2, Point touchCoordinates) {
        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, point1.getX() - deltaTouchCoordinates.getX(), point1.getY() - deltaTouchCoordinates.getY());
        setPoint(point2, point2.getX() - deltaTouchCoordinates.getX(), point2.getY() - deltaTouchCoordinates.getY());
    }

    public Point getNewCoordinates(Point point) {
//        float firstLineLength, secondLineLength, bigLineLength;
//        firstLineLength = (float) getLengthBetweenTwoPoints(this.point, point);
//        secondLineLength = (float) getLengthBetweenTwoPoints(this.point2, point);
//        bigLineLength = (float) getLengthBetweenTwoPoints(this.point, this.point2);
//
//        float ao = (float) ((Math.pow(firstLineLength, 2) - Math.pow(secondLineLength, 2) + Math.pow(bigLineLength, 2)) / (2 * bigLineLength));
//        float coefficient = bigLineLength / ao;
//        Point dotCoordinates = new Point(0f, 0f);
//        dotCoordinates.setX(((point2.getX() - this.point.getX()) / coefficient) + this.point.getX());
//        dotCoordinates.setY(((point2.getY() - this.point.getY()) / coefficient) + this.point.getY());
//        return dotCoordinates;
        return null;
    }
}