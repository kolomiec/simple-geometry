package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLengthBetweenTwoPoints;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;

/**
 * Created by sayenko on 7/17/13.
 */

public class Line implements Shape {
    private final String label1;
    private final String label2;
    public int color = 0;
    public int numberTouchedPoint = 0;
    Point lastTouchCoordinates = new Point(0f, 0f);
    Point deltaTouchCoordinates = new Point(0f, 0f);
    private Point point1;
    private Point point2;
    private Point drawedPoint1 = new Point(0f, 0f);
    private Point drawedPoint2 = new Point(0f, 0f);


    public Line(Point point1, Point point2, String label1, String label2) {
        this.point1 = point1;
        this.point2 = point2;
        this.label1 = label1;
        this.label2 = label2;

        setPoint(drawedPoint1, point1);
        setPoint(drawedPoint2, point2);
        color = BaseHelper.getRandomColor();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);

        canvas.drawLine(drawedPoint1.getX(), drawedPoint1.getY(), drawedPoint2.getX(), drawedPoint2.getY(), paint);

        Float pointRadius = 5.0f;
        canvas.drawCircle(drawedPoint1.getX(), drawedPoint1.getY(), pointRadius, paint);
        canvas.drawCircle(drawedPoint2.getX(), drawedPoint2.getY(), pointRadius, paint);

        BaseHelper.drawTextWithShadow(canvas, label1, drawedPoint1.getX() + pointRadius, drawedPoint1.getY() - pointRadius / 2);
        BaseHelper.drawTextWithShadow(canvas, label2, drawedPoint2.getX() + pointRadius, drawedPoint2.getY() - pointRadius / 2);
    }

    @Override
    public String toString() {
        return "Line " + label1 + label2 + " - x1: " + Math.round(drawedPoint1.getX()) + ", y1: " + Math.round(drawedPoint1.getY()) +
                ", x2: " + Math.round(drawedPoint2.getX()) + ", y2: " + Math.round(drawedPoint2.getY());
    }

    @Override
    public void move(Point point, boolean onlyMove) {
        if (!onlyMove) {
            changeLineCoordinates(point1, point2, point);
            setPoint(drawedPoint1, point1);
            setPoint(drawedPoint2, point2);
        } else {
            switch (numberTouchedPoint) {
                case 0: {
                    changeLineCoordinates(point1, point2, point);
                    setPoint(drawedPoint1, point1);
                    setPoint(drawedPoint2, point2);
                }
                break;
                case 1: {
                    changePointCoordinates(point1, point);
                    setPoint(drawedPoint1, point1);
                    setPoint(drawedPoint2, point2);


                    if ((getLengthBetweenTwoPoints(point1, point2) > 20)) {
                        if (Math.abs(point1.getX() - point2.getX()) < 10) {
                            drawedPoint1.setX(point2.getX());
                        }
                        if (Math.abs(point1.getY() - point2.getY()) < 10) {
                            drawedPoint1.setY(point2.getY());
                        }
                    }
                    float lengthX = Math.abs(point2.getX() - point1.getX());
                    float lengthY = Math.abs(point2.getY() - point1.getY());
                    if ((lengthX + 15 > lengthY) && (lengthX - 15 < lengthY)) {
                        float delta = (lengthX + lengthY) / 2;
                        if (point1.getX() > point2.getX()) {
                            drawedPoint1.setX(point2.getX() + delta);
                        } else {
                            drawedPoint1.setX(point2.getX() - delta);
                        }
                        if (point1.getY() > point2.getY()) {
                            drawedPoint1.setY(point2.getY() + delta);
                        } else {
                            drawedPoint1.setY(point2.getY() - delta);
                        }
                    }
                }
                break;
                case 2: {
                    changePointCoordinates(point2, point);
                    setPoint(drawedPoint1, point1);
                    setPoint(drawedPoint2, point2);

                    if ((getLengthBetweenTwoPoints(point1, point2) > 20)) {
                        if (Math.abs(point1.getX() - point2.getX()) < 10) {
                            drawedPoint2.setX(point1.getX());
                        }
                        if (Math.abs(point1.getY() - point2.getY()) < 10) {
                            drawedPoint2.setY(point1.getY());
                        }
                    }
                    float lengthX = Math.abs(point2.getX() - point1.getX());
                    float lengthY = Math.abs(point2.getY() - point1.getY());
                    if ((lengthX + 15 > lengthY) && (lengthX - 15 < lengthY)) {
                        float delta = (lengthX + lengthY) / 2;
                        if (point2.getX() > point1.getX()) {
                            drawedPoint2.setX(point1.getX() + delta);
                        } else {
                            drawedPoint2.setX(point1.getX() - delta);
                        }
                        if (point2.getY() > point1.getY()) {
                            drawedPoint2.setY(point1.getY() + delta);
                        } else {
                            drawedPoint2.setY(point1.getY() - delta);
                        }
                    }
                }
                break;
            }
        }
        setPoint(lastTouchCoordinates, point);
    }

    @Override
    public boolean isTouched(Point point) {
        setPoint(lastTouchCoordinates, point);
        if (isDotTouched(point1, point)) {
            numberTouchedPoint = 1;
            return true;
        } else if (isDotTouched(point2, point)) {
            numberTouchedPoint = 2;
            return true;
        } else {
            if (isLineTouched(point)) {
                numberTouchedPoint = 0;
                return true;
            }
        }
        return false;
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
        if (circle.isBorderTouched(this.getPoint1(), 20)) {
            Point newCoordinates = circle.getNewCoordinates(this.drawedPoint1);
            Point changedPoint1 = new Point(drawedPoint1);
            Point changedPoint2 = new Point(drawedPoint2);
            switch (this.getNumberOfSelectedPoint()) {
                case 0:
                    Point deltaCoordinates = new Point(this.point1.getX() - this.point2.getX(), this.point1.getY() - this.point2.getY());
                    setPoint(changedPoint1, newCoordinates);
                    setPoint(changedPoint2, changedPoint1.getX() - deltaCoordinates.getX(), changedPoint1.getY() - deltaCoordinates.getY());
                    break;
                case 1:
                    setPoint(changedPoint1, newCoordinates);
                    break;
                case 2:
                    break;
            }
            return new Point(drawedPoint1.getX() - changedPoint1.getX(), drawedPoint1.getY() - changedPoint1.getY());
        } else if (circle.isBorderTouched(this.getPoint2(), 20)) {
            Point newCoordinates = circle.getNewCoordinates(this.drawedPoint2);
            Point changedPoint1 = new Point(drawedPoint1);
            Point changedPoint2 = new Point(drawedPoint2);
            switch (this.getNumberOfSelectedPoint()) {
                case 0:
                    Point deltaCoordinates = new Point(this.point1.getX() - this.point2.getX(), this.point1.getY() - this.point2.getY());
                    setPoint(changedPoint2, newCoordinates);
                    setPoint(changedPoint1, changedPoint2.getX() + deltaCoordinates.getX(), changedPoint2.getY() + deltaCoordinates.getY());
                    break;
                case 1:
                    break;
                case 2:
                    setPoint(changedPoint2, newCoordinates);
                    break;
            }
            return new Point(drawedPoint2.getX() - changedPoint2.getX(), drawedPoint2.getY() - changedPoint2.getY());
        } else {
            Point p = this.getNewCoordinates(circle.getCoordinatesOfCenterPoint());
            if (this.isLineTouched(p)) {
                double length = getLengthBetweenTwoPoints(p, circle.getCoordinatesOfCenterPoint());
                if (((length) < (circle.getRadius() + 15)) && ((length) > (circle.getRadius() - 15))) {
                    Point delta = circle.getNewCoordinates(p);

                    setPoint(delta, p.getX() - delta.getX(), p.getY() - delta.getY());
                    return delta;
                }
            }
        }
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
        if (line.isLineTouched(this.getPoint1())) {

            Point newCoordinates = line.getNewCoordinates(this.point1);
            Point changedPoint1 = new Point(drawedPoint1);
            Point changedPoint2 = new Point(drawedPoint2);
            switch (this.getNumberOfSelectedPoint()) {
                case 0:
                    Point deltaCoordinates = new Point(this.point1.getX() - this.point2.getX(), this.point1.getY() - this.point2.getY());
                    setPoint(changedPoint1, newCoordinates);
                    setPoint(changedPoint2, changedPoint1.getX() - deltaCoordinates.getX(), changedPoint1.getY() - deltaCoordinates.getY());
                    break;
                case 1:
                    setPoint(changedPoint1, newCoordinates);
                    break;
                case 2:
                    break;
            }
            return new Point(drawedPoint1.getX() - changedPoint1.getX(), drawedPoint1.getY() - changedPoint1.getY());
        }
        if (line.isLineTouched(this.getPoint2())) {
            Point newCoordinates = line.getNewCoordinates(this.getPoint2());
            Point changedPoint1 = new Point(drawedPoint1);
            Point changedPoint2 = new Point(drawedPoint2);
            switch (this.getNumberOfSelectedPoint()) {
                case 0:
                    Point deltaCoordinates = new Point(this.point1.getX() - this.point2.getX(), this.point1.getY() - this.point2.getY());
                    setPoint(changedPoint2, newCoordinates);
                    setPoint(changedPoint1, changedPoint2.getX() + deltaCoordinates.getX(), changedPoint2.getY() + deltaCoordinates.getY());
                    break;
                case 1:
                    break;
                case 2:
                    setPoint(changedPoint2, newCoordinates);
                    break;
            }
            return new Point(drawedPoint2.getX() - changedPoint2.getX(), drawedPoint2.getY() - changedPoint2.getY());
        }
        return null;
    }

    @Override
    public void refreshCoordinates() {
        setPoint(point1, drawedPoint1);
        setPoint(point2, drawedPoint2);
    }

    public boolean isLineTouched(Point point) {
        return ((getLengthBetweenTwoPoints(point1, point) + getLengthBetweenTwoPoints(point2, point) - getLengthBetweenTwoPoints(point1, point2)) < 3);
    }

    public boolean isDotTouched(Point p1, Point p2) {
        float delta = 20f;
        if ((p1.getX() < (p2.getX() + delta)) && (p1.getX() > (p2.getX() - delta)))
            if ((p1.getY() < (p2.getY() + delta)) && (p1.getY() > (p2.getY() - delta))) {
                return true;
            }
        return false;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public Point getDrawedPoint1() {
        return drawedPoint1;
    }

    public Point getDrawedPoint2() {
        return drawedPoint2;
    }

    public int getNumberOfSelectedPoint() {
        return numberTouchedPoint;
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
        switch (this.getNumberOfSelectedPoint()) {
            case 0:
                Point changedPoint1 = new Point(drawedPoint1);
                Point changedPoint2 = new Point(drawedPoint2);
                Point delta01 = setDotThatItWillNotBeOutsideTheScreen(changedPoint1, maxX, maxY);
                setPoint(changedPoint1, changedPoint1.getX() - delta01.getX(), changedPoint1.getY() - delta01.getY());
                setPoint(changedPoint2, changedPoint2.getX() - delta01.getX(), changedPoint2.getY() - delta01.getY());
                Point delta02 = setDotThatItWillNotBeOutsideTheScreen(changedPoint2, maxX, maxY);
                return new Point(delta01.getX() + delta02.getX(), delta01.getY() + delta02.getY());
            case 1:
                Point delta1 = setDotThatItWillNotBeOutsideTheScreen(drawedPoint1, maxX, maxY);
                setPoint(drawedPoint1, drawedPoint1.getX() - delta1.getX(), drawedPoint1.getY() - delta1.getY());
                return null;
            case 2:
                Point delta2 = setDotThatItWillNotBeOutsideTheScreen(drawedPoint2, maxX, maxY);
                setPoint(drawedPoint2, drawedPoint2.getX() - delta2.getX(), drawedPoint2.getY() - delta2.getY());
                return null;
        }
        return null;
    }

    public Point setDotThatItWillNotBeOutsideTheScreen(Point point, float maxX, float maxY) {
        Point pointForChange = new Point(point);
        if (pointForChange.getX() > maxX) {
            pointForChange.setX(maxX);
        } else if (pointForChange.getX() < 0) {
            pointForChange.setX(0f);
        }
        if (pointForChange.getY() > maxY) {
            pointForChange.setY(maxY);
        } else if (pointForChange.getY() < 0) {
            pointForChange.setY(0f);
        }
        return new Point(point.getX() - pointForChange.getX(), point.getY() - pointForChange.getY());
    }

    @Override
    public void changeCoordinatesToDelta(Point delta) {
        drawedPoint1.setX(drawedPoint1.getX() - delta.getX());
        drawedPoint1.setY(drawedPoint1.getY() - delta.getY());
        drawedPoint2.setX(drawedPoint2.getX() - delta.getX());
        drawedPoint2.setY(drawedPoint2.getY() - delta.getY());
    }

    public void changePointCoordinates(Point point1, Point touchCoordinates) {
        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, point1.getX() - deltaTouchCoordinates.getX(), point1.getY() - deltaTouchCoordinates.getY());
    }

    public void changeLineCoordinates(Point point1, Point point2, Point touchCoordinates) {
        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, point1.getX() - deltaTouchCoordinates.getX(), point1.getY() - deltaTouchCoordinates.getY());
        setPoint(point2, point2.getX() - deltaTouchCoordinates.getX(), point2.getY() - deltaTouchCoordinates.getY());
    }

    public Point getNewCoordinates(Point point) {
        float firstLineLength, secondLineLength, bigLineLength;
        firstLineLength = (float) getLengthBetweenTwoPoints(this.point1, point);
        secondLineLength = (float) getLengthBetweenTwoPoints(this.point2, point);
        bigLineLength = (float) getLengthBetweenTwoPoints(this.point1, this.point2);

        float ao = (float) ((Math.pow(firstLineLength, 2) - Math.pow(secondLineLength, 2) + Math.pow(bigLineLength, 2)) / (2 * bigLineLength));
        float coefficient = bigLineLength / ao;
        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point2.getX() - point1.getX()) / coefficient) + point1.getX());
        dotCoordinates.setY(((point2.getY() - point1.getY()) / coefficient) + point1.getY());
        return dotCoordinates;
    }
}