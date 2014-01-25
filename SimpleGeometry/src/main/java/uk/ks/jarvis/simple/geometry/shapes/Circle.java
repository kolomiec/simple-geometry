package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.List;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static java.lang.Math.abs;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.getLengthFromPointToLine;

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

                if (abs(length - newRadius) < 5) {
                    newRadius = length;
                }

            } else if (shape.getClass() == Line.class) {
                double lengthToLine = getLengthFromPointToLine(centerDot.getPoint(), (Line) shape);

                if (abs(lengthToLine - newRadius) < 10) {
                    newRadius = lengthToLine;
                }

            } else if (shape.getClass() == Circle.class) {
                double length1 = getLength(centerDot.getPoint(), ((Circle) shape).centerDot.getPoint());
                double length2 = ((Circle) shape).getRadius() + newRadius;

                if (abs(length1 - length2) < 5) {
                    newRadius = length1 - ((Circle) shape).getRadius();
                }
            }
        }
        return newRadius;
    }

    private double getNewRadius(Point touchCoordinates) {
        return getLength(this.centerDot.getPoint(), touchCoordinates);
    }

    public Point getCenterPoint() {
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

    public boolean isBorderTouched(Point point, int deltaRadius) {
        double length = getLength(point, this.centerDot.getPoint());
        return (length < radius + deltaRadius) && (length > radius - deltaRadius);
    }
}