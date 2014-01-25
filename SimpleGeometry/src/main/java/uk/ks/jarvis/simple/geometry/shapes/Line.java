package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.coordinateplane.SystemInformation;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static java.lang.Math.abs;
import static java.lang.Math.round;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getAngleFrom2Points;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.arcsin;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.getLengthFromPointToLine;
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
    private Dot dot;


    public Line(Dot dot, Float angle, String label) {
        this.dot = dot;
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
        canvas.drawCircle((float) dot.getPoint().getX(), (float) dot.getPoint().getY(), pointRadius, paint);
        BaseHelper.drawTextWithShadow(canvas, label, (float) getFirstPoint(angle).getX() + pointRadius, (float) getFirstPoint(angle).getY() - pointRadius / 2);
        BaseHelper.drawTextWithShadow(canvas, label, (float) getSecondPoint(angle).getX() + pointRadius, (float) getSecondPoint(angle).getY() - pointRadius / 2);
    }

    @Override
    public String toString() {
        return "Line " + label + " - x: " + Math.round(dot.getPoint().getX()) + " - y: " + Math.round(dot.getPoint().getY()) +
                ", angle : " + round(angle) + " | " + Math.round(getFirstPoint(angle).getX()) + " " + Math.round(getFirstPoint(angle).getY()) + " " +
                Math.round(getSecondPoint(angle).getX()) + " " + Math.round(getSecondPoint(angle).getY());
    }

    public Point getFirstPoint() {
        return getFirstPoint(angle);
    }

    public Point getFirstPoint(double angle) {
        Point p = new Point(0f, 0f);
        if ((angle >= 0) && (angle < 45)) { // ok
            p.setX(SystemInformation.DISPLAY_WIDTH);
            p.setY(((-SystemInformation.DISPLAY_WIDTH + dot.getPoint().getX()) * tg(angle)) + dot.getPoint().getY());
        } else if ((angle >= 45) && (angle < 90)) { // ok
            p.setX(((dot.getPoint().getY()) / tg(angle)) + dot.getPoint().getX());
            p.setY(0f);
        } else if ((angle >= 90) && (angle < 135)) { // ok
            p.setX(((dot.getPoint().getY()) / tg(angle)) + dot.getPoint().getX());
            p.setY(0f);
        } else if ((angle >= 135) && (angle < 180)) { // ok
            p.setX(0f);
            p.setY(((dot.getPoint().getX()) * tg(angle)) + dot.getPoint().getY());
        } else if ((angle >= 180) && (angle < 225)) { // ok
            p.setX(0f);
            p.setY(((dot.getPoint().getX()) * tg(angle)) + dot.getPoint().getY());
        } else if ((angle >= 225) && (angle < 270)) { // ok
            p.setX(((-SystemInformation.DISPLAY_HEIGHT + dot.getPoint().getY()) / tg(angle)) + dot.getPoint().getX());
            p.setY(SystemInformation.DISPLAY_HEIGHT);
        } else if ((angle >= 270) && (angle < 315)) { // ok
            p.setX(((-SystemInformation.DISPLAY_HEIGHT + dot.getPoint().getY()) / tg(angle)) + dot.getPoint().getX());
            p.setY(SystemInformation.DISPLAY_HEIGHT);
        } else if ((angle >= 315) && (angle < 360)) { // ok
            p.setX(SystemInformation.DISPLAY_WIDTH);
            p.setY(((-SystemInformation.DISPLAY_WIDTH + dot.getPoint().getX()) * tg(angle)) + dot.getPoint().getY());
        }
        return p;
    }

    public Point getSecondPoint(double angle) {
        return getFirstPoint((180 + angle) % 360);
    }

    public Point getSecondPoint() {
        return getSecondPoint(angle);
    }

    @Override
    public void move(Point touchCoordinates, boolean onlyMove) {
        if (!onlyMove && numberTouchedPoint != 0) {
            setAngle(touchCoordinates);
        }
    }

    public void setAngle(Point point) {
        angle = bringToShapes(bringToAngles(getAngleFrom2Points(this.dot.getPoint(), point)));
        if (numberTouchedPoint == NUMBER_OF_SECOND_POINT) angle = (angle + 180) % 360;
    }

    private double bringToShapes(double angle) {
        List<Shape> shapeList = ShapeList.getShapeArray();

        for (Shape shape : shapeList) {
            if (shape.getClass() == Dot.class) {
                angle = bringToDot(angle, (Dot) shape);
            } else if (shape.getClass() == Circle.class) {
                double lengthToLine = getLengthFromPointToLine(((Circle) shape).getCenterPoint(), new Line(this.dot, (float) angle, label));
                if (abs(lengthToLine - ((Circle) shape).getRadius()) < 10) {
                    double angle1 = getAngleFrom2Points(this.getPoint(), ((Circle) shape).getCenterPoint());
                    double angle2 = arcsin(((Circle) shape).getRadius() / getLength(this.getPoint(), ((Circle) shape).getCenterPoint()));

                    double a1 = angle1 - angle2;
                    double a2 = angle1 + angle2;

                    if (abs(angle - a1) > abs(angle - a2)) {
                        angle = a2;
                    } else {
                        angle = a1;
                    }
                }
            }
        }

        return angle;
    }

    private double bringToDot(double angle, Dot dot) {
        double dotAngle = getAngleFrom2Points(this.dot.getPoint(), dot.getPoint());

        if (angle + 3 > dotAngle && angle - 3 < dotAngle) {
            angle = dotAngle;
        }

        return angle;
    }

    private double bringToAngles(double angle) {
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
        if (this.dot.getPoint().nearlyEquals(point)) {
            numberTouchedPoint = 0;
            return true;
        } else {
            if (isLineTouched(this.dot.getPoint(), getFirstPoint(angle), point)) {
                numberTouchedPoint = NUMBER_OF_FIRST_POINT;
                return true;
            } else if (isLineTouched(this.dot.getPoint(), getSecondPoint(angle), point)) {
                numberTouchedPoint = NUMBER_OF_SECOND_POINT;
                return true;
            }
        }
        return false;
    }

    public boolean isLineTouched(Point linePoint1, Point linePoint2, Point point) {
        return ((getLength(linePoint1, point) + getLength(linePoint2, point) -
                getLength(linePoint1, linePoint2)) < 3);
    }

    public Point getPoint() {
        return dot.getPoint();
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
    public String getLabel() {
        return this.label;
    }

    @Override
    public void zoom(Point centralZoomPoint, float zoomRatio, Point moveDelta) {
    }

    @Override
    public void turn(Point centralTurnPoint, double deltaAngle) {
        this.angle += deltaAngle;

        angle = (angle + 360) % 360;

        if (this.angle < 0) {
            this.angle += 360;
        }
        if (this.angle > 360) {
            this.angle %= 360;
        }
    }

    @Override
    public void refreshPrvTouchPoint(Point newTouchPoint) {
    }
}