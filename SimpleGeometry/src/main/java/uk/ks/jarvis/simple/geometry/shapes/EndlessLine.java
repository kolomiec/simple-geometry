package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.holders.BaseHolder;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLengthBetweenTwoPoints;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;

/**
 * Created by sayenko on 8/12/13.
 */
public class EndlessLine extends Line {
    private final String label;
    public int color = 0;
    public int numberTouchedPoint = 0;
    Point lastTouchCoordinates = new Point(0f, 0f);
    Point deltaTouchCoordinates = new Point(0f, 0f);
    private Point point1;
    private Point point2;
    private Point drawedPoint1 = new Point(0f, 0f);
    private Point drawedPoint2 = new Point(0f, 0f);
    private float radius;

    public EndlessLine(Point point1, Point point2, String label, BaseHolder baseHolder) {
        super(point1, point2, label, label);
        this.point1 = new Point(300f, 10F);
        this.point2 = point2;
        this.label = label;
        setPoint(drawedPoint1, point1);
        setPoint(drawedPoint2, point2);
        color = BaseHelper.getRandomColor();
        radius = (float) (1.1 * getLengthBetweenTwoPoints(new Point(0f, 0f), baseHolder.getFragmentWidthAndHeight()));
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);

        canvas.drawLine(drawedPoint1.getX(), drawedPoint1.getY(), drawedPoint2.getX(), drawedPoint2.getY(), paint);

        Float pointRadius = 5.0f;
        BaseHelper.drawTextWithShadow(canvas, label, (drawedPoint1.getX() + drawedPoint2.getX()) / 2 + pointRadius, (drawedPoint1.getY() + drawedPoint2.getY()) / 2 - pointRadius / 2);
    }

    @Override
    public String toString() {
        return "Line " + label + " - endless...";
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
                    point1 = changePointCoordinates(point);
                    point2 = new Point((2 * getCentralPoint().getX()) - point1.getX(), (2 * getCentralPoint().getY()) - point1.getY());


                    if (Math.abs(point1.getX() - getCentralPoint().getX()) < 90) {
                        point1.setX(getCentralPoint().getX());
                        point2.setX(getCentralPoint().getX());
                    }
                    if (Math.abs(point1.getY() - getCentralPoint().getY()) < 90) {
                        point1.setY(getCentralPoint().getY());
                        point2.setY(getCentralPoint().getY());
                    }
                    setPoint(drawedPoint1, point1);
                    setPoint(drawedPoint2, point2);
                    float lengthX = Math.abs(getCentralPoint().getX() - point1.getX());
                    float lengthY = Math.abs(getCentralPoint().getY() - point1.getY());
                    if ((lengthX + 70 > lengthY) && (lengthX - 70 < lengthY)) {
                        Point centralPoint = new Point(getCentralPoint());
                        float delta = (lengthX + lengthY) / 2;
                        if (point1.getX() > centralPoint.getX()) {
                            drawedPoint1.setX(centralPoint.getX() + delta);

                        } else {
                            drawedPoint1.setX(centralPoint.getX() - delta);
                        }
                        if (point1.getY() > centralPoint.getY()) {
                            drawedPoint1.setY(centralPoint.getY() + delta);
                        } else {
                            drawedPoint1.setY(centralPoint.getY() - delta);
                        }
                        drawedPoint2 = new Point((2 * centralPoint.getX()) - drawedPoint1.getX(), (2 * centralPoint.getY()) - drawedPoint1.getY());
                    }

                }
                break;
                case 2: {
                    point2 = changePointCoordinates(point);
                    point1 = new Point((2 * getCentralPoint().getX()) - point2.getX(), (2 * getCentralPoint().getY()) - point2.getY());

                    if (Math.abs(point2.getX() - getCentralPoint().getX()) < 90) {
                        point1.setX(getCentralPoint().getX());
                        point2.setX(getCentralPoint().getX());
                    }
                    if (Math.abs(point2.getY() - getCentralPoint().getY()) < 90) {
                        point1.setY(getCentralPoint().getY());
                        point2.setY(getCentralPoint().getY());
                    }
                    setPoint(drawedPoint1, point1);
                    setPoint(drawedPoint2, point2);
                    float lengthX = Math.abs(point2.getX() - getCentralPoint().getX());
                    float lengthY = Math.abs(point2.getY() - getCentralPoint().getY());
                    if ((lengthX + 70 > lengthY) && (lengthX - 70 < lengthY)) {
                        Point centralPoint = new Point(getCentralPoint());
                        float delta = (lengthX + lengthY) / 2;
                        if (point2.getX() > centralPoint.getX()) {
                            drawedPoint2.setX(centralPoint.getX() + delta);
                        } else {
                            drawedPoint2.setX(centralPoint.getX() - delta);
                        }
                        if (point2.getY() > centralPoint.getY()) {
                            drawedPoint2.setY(centralPoint.getY() + delta);
                        } else {
                            drawedPoint2.setY(centralPoint.getY() - delta);
                        }
                        drawedPoint1 = new Point((2 * centralPoint.getX()) - drawedPoint2.getX(), (2 * centralPoint.getY()) - drawedPoint2.getY());
                    }
                }
                break;
            }
        }
        setPoint(lastTouchCoordinates, point);
    }

    @Override
    public boolean isLineTouched(Point point) {
        setPoint(lastTouchCoordinates, point);
        if (isDotTouched(getCentralPoint(), point)) {
            numberTouchedPoint = 0;
            return true;
        } else if (isLineTouched(point1, getCentralPoint(), point)) {
            numberTouchedPoint = 1;
            return true;
        } else if (isLineTouched(point2, getCentralPoint(), point)) {
            numberTouchedPoint = 2;
            return true;
        }
        return false;
    }

    public Point changePointCoordinates(Point point) {
        double radius2 = getLengthBetweenTwoPoints(this.getCentralPoint(), point);
        Float ratioOfTheRadii = (float) (radius / radius2);

        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point.getX() - this.getCentralPoint().getX()) * ratioOfTheRadii) + this.getCentralPoint().getX());
        dotCoordinates.setY(((point.getY() - this.getCentralPoint().getY()) * ratioOfTheRadii) + this.getCentralPoint().getY());

        return dotCoordinates;
    }

    private Point getCentralPoint() {
        return new Point((drawedPoint1.getX() + drawedPoint2.getX()) / 2, (drawedPoint1.getY() + drawedPoint2.getY()) / 2);
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
            if (this.isTouched(p)) {
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
        if (line.isTouched(this.getPoint1())) {

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
        if (line.isTouched(this.getPoint2())) {
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

    public boolean isLineTouched(Point point1, Point point2, Point point) {
        return ((getLengthBetweenTwoPoints(point1, point) + getLengthBetweenTwoPoints(point2, point) - getLengthBetweenTwoPoints(point1, point2)) < 3);
    }

    @Override
    public boolean isDotTouched(Point p1, Point p2) {
        float delta = 20f;
        if ((p1.getX() < (p2.getX() + delta)) && (p1.getX() > (p2.getX() - delta)))
            if ((p1.getY() < (p2.getY() + delta)) && (p1.getY() > (p2.getY() - delta))) {
                return true;
            }
        return false;
    }

    @Override
    public Point setFigureThatItWillNotBeOutsideTheScreen(float maxX, float maxY) {
//        if (getNumberOfSelectedPoint() == 0) {
//            Point delta = setDotThatItWillNotBeOutsideTheScreen(getCentralPoint(), maxX, maxY);
//            setPoint(drawedPoint1, drawedPoint1.getX() - delta.getX(), drawedPoint1.getY() - delta.getY());
//            setPoint(drawedPoint2, drawedPoint2.getX() - delta.getX(), drawedPoint2.getY() - delta.getY());
//        }
        return null;
    }

    @Override
    public void changeCoordinatesToDelta(Point delta) {
        drawedPoint1.setX(drawedPoint1.getX() - delta.getX());
        drawedPoint1.setY(drawedPoint1.getY() - delta.getY());
        drawedPoint2.setX(drawedPoint2.getX() - delta.getX());
        drawedPoint2.setY(drawedPoint2.getY() - delta.getY());
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void refreshCoordinates() {
        setPoint(point1, drawedPoint1);
        setPoint(point2, drawedPoint2);
    }

    @Override
    public void changePointCoordinates(Point point1, Point touchCoordinates) {
        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, point1.getX() - deltaTouchCoordinates.getX(), point1.getY() - deltaTouchCoordinates.getY());
    }

    @Override
    public void changeLineCoordinates(Point point1, Point point2, Point touchCoordinates) {
        deltaTouchCoordinates.setX(lastTouchCoordinates.getX() - touchCoordinates.getX());
        deltaTouchCoordinates.setY(lastTouchCoordinates.getY() - touchCoordinates.getY());

        setPoint(point1, point1.getX() - deltaTouchCoordinates.getX(), point1.getY() - deltaTouchCoordinates.getY());
        setPoint(point2, point2.getX() - deltaTouchCoordinates.getX(), point2.getY() - deltaTouchCoordinates.getY());
    }

    @Override
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