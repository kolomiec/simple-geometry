package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;

/**
 * Created by sayenko on 7/26/13.
 */
public class Circle extends BaseShape {
    private int color = 0;
    private Dot centerDot;
    private double radius = 0;
    private boolean radiusChangeMode = true;

    public Circle(Float radius, Dot dot) {
        this.radius = radius;
        this.centerDot = dot;
        color = BaseHelper.getRandomColor();
    }

    public static Point getCoordinatesOfBorderOfCircle(Point point, Point point2, double radius) {
        double radius2 = getLength(point2, point);

        double ratioOfTheRadii = (radius / radius2);

        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point.getX() - point2.getX()) * ratioOfTheRadii) + point2.getX());
        dotCoordinates.setY(((point.getY() - point2.getY()) * ratioOfTheRadii) + point2.getY());

        return dotCoordinates;
    }

    @Override
    public String toString() {
        return "Circle " + centerDot.getLabel() + " - x: " + Math.round(centerDot.getPoint().getX()) + ", y: " + Math.round(centerDot.getPoint().getY()) + ", radius: " + Math.round(radius);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle((float) centerDot.getPoint().getX(), (float) centerDot.getPoint().getY(), (float) radius, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float) centerDot.getPoint().getX(), (float) centerDot.getPoint().getY(), 2, paint);
    }

    @Override
    public void move(Point touchCoordinates, boolean onlyMove) {
        if (radiusChangeMode && !onlyMove) {
            radius = bringToShapes(getNewRadius(touchCoordinates));
        }
    }

    private double bringToShapes(double newRadius) {
        List<Shape> shapeList = ShapeList.getShapeArray();

        for (Shape shape : shapeList) {
            if (shape.getClass() == Dot.class) {
                double length = getLength(centerDot.getPoint(), ((Dot) shape).getPoint());

                if (length + 5 > newRadius && length - 5 < newRadius) {
                    newRadius = length;
                }

            } else if (shape.getClass() == Line.class) {

            } else if (shape.getClass() == Circle.class) {

            } //ToDo change radius
        }
        return newRadius;
    }

    private double getNewRadius(Point touchCoordinates) {
        return getLength(this.centerDot.getPoint(), touchCoordinates);
    }

    public Point getCoordinatesOfCenterPoint() {
        return centerDot.getPoint();
    }

    public double getRadius() {
        return radius;
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
        return "";
    }

    @Override
    public void zoom(Point centralZoomPoint, float zoomRatio, Point moveDelta) {
        radius = radius * zoomRatio;
    }

    @Override
    public void turn(Point centralTurnPoint, double deltaAngle) {
    }

    @Override
    public void refreshPrvTouchPoint(Point newTouchPoint) {
    }

    @Override
    public boolean isTouched(Point point) {
//        radiusChangeMode = false;
//        setPoint(lastTouchCoordinates, point);

        if (isBorderTouched(point, 15)) {
//            radiusChangeMode = true;
            return true;
        } else {
            double length = getLength(point, this.centerDot.getPoint());
            return (length < radius);
        }
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
//        double length1 = BaseHelper.getLength(this.getCoordinatesOfCenterPoint(), circle.getCoordinatesOfCenterPoint());
//        double length2 = this.getRadius() + circle.getRadius();
//
//        if (((length1) < (length2 + 15)) && ((length1) > (length2 - 15))) {
//            this.getNewCoordinates(circle.getCoordinatesOfCenterPoint());
//            Point newCoordinates = new Point(getCoordinatesOfBorderOfCircle(centerDot.getPoint(), circle.getCoordinatesOfCenterPoint(), length2));
//            return new Point(centerDot.getPoint().getX() - newCoordinates.getX(), centerDot.getPoint().getY() - newCoordinates.getY());
//        }
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
//        Point p = line.getNewCoordinates(this.getCoordinatesOfCenterPoint());
//        if (line.isLineTouched(p)) {
//            double length = BaseHelper.getLength(p, this.getCoordinatesOfCenterPoint());
//            if (((length) < (this.radius + 15)) && ((length) > (this.radius - 15))) {
//                Point delta = new Point(this.getNewCoordinates(p));
//                setPoint(delta, p.getX() - delta.getX(), p.getY() - delta.getY());
//                return new Point(drawedCenterPoint.getX() - (centerDot.getX() + delta.getX()), drawedCenterPoint.getY() - (centerDot.getY() + delta.getY()));
//            }
//        }
//        if (this.isBorderTouched(line.getPoint1(), 20)) {
//            Point newCoordinates = getCoordinatesOfBorderOfCircle(centerDot, line.getPoint1(), radius);
//            return new Point(drawedCenterPoint.getX() - newCoordinates.getX(), drawedCenterPoint.getY() - newCoordinates.getY());
//        }
//        if (this.isBorderTouched(line.getPoint2(), 20)) {
//            Point newCoordinates = getCoordinatesOfBorderOfCircle(centerDot, line.getPoint2(), radius);
//            return new Point(drawedCenterPoint.getX() - newCoordinates.getX(), drawedCenterPoint.getY() - newCoordinates.getY());
//        }
        return null;
    }

    public boolean isBorderTouched(Point point, int deltaRadius) {
        double length = getLength(point, this.centerDot.getPoint());
        return (length < radius + deltaRadius) && (length > radius - deltaRadius);
    }

    public Point getNewCoordinates(Point point) {
        double radius2 = getLength(this.centerDot.getPoint(), point);
        double ratioOfTheRadii = (radius / radius2);

        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point.getX() - this.centerDot.getPoint().getX()) * ratioOfTheRadii) + this.centerDot.getPoint().getX());
        dotCoordinates.setY(((point.getY() - this.centerDot.getPoint().getY()) * ratioOfTheRadii) + this.centerDot.getPoint().getY());

        return dotCoordinates;
    }
}