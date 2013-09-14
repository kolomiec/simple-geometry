package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;

/**
 * Created by sayenko on 7/26/13.
 */
public class Circle extends BaseShape {

    private final String label;
    public int color = 0;
    Point lastTouchCoordinates = new Point(0f, 0f);
    Point deltaTouchCoordinates = new Point(0f, 0f);
    private Point centerPoint;
    private Point drawedCenterPoint = new Point(0f, 0f);
    private double radius;
    private boolean radiusChangeMode;


    public Circle(Float radius, Point point, String label) {
        this.radius = radius;
        this.centerPoint = point;
        this.label = label;
        setPoint(drawedCenterPoint, point);
        color = BaseHelper.getRandomColor();
    }

    public static Point getCoordinatesOfBorderOfCircle(Point point, Point point2, double radius) {
        float radius2 = (float) BaseHelper.getLengthBetweenTwoPoints(point2, point);

        float ratioOfTheRadii = ((float) radius / radius2);

        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point.getX() - point2.getX()) * ratioOfTheRadii) + point2.getX());
        dotCoordinates.setY(((point.getY() - point2.getY()) * ratioOfTheRadii) + point2.getY());

        return dotCoordinates;
    }

    @Override
    public String toString() {
        return "Circle " + label + " - x: " + Math.round(drawedCenterPoint.getX()) + ", y: " + Math.round(drawedCenterPoint.getY()) + ", radius: " + Math.round(radius);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(drawedCenterPoint.getX(), drawedCenterPoint.getY(), (float) radius, paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(drawedCenterPoint.getX(), drawedCenterPoint.getY(), 2, paint);

        BaseHelper.drawTextWithShadow(canvas, label, drawedCenterPoint.getX() + 4, drawedCenterPoint.getY());
    }

    @Override
    public void move(Point touchCoordinates, boolean onlyMove) {
        if (radiusChangeMode && onlyMove) {
            changeRadius(touchCoordinates);
        } else {
            setPoint(deltaTouchCoordinates, lastTouchCoordinates.getX() - touchCoordinates.getX(), lastTouchCoordinates.getY() - touchCoordinates.getY());
            setPoint(centerPoint, centerPoint.getX() - deltaTouchCoordinates.getX(), centerPoint.getY() - deltaTouchCoordinates.getY());
        }
        setPoint(drawedCenterPoint, centerPoint);
        setPoint(lastTouchCoordinates, touchCoordinates);
    }

    public Point getCoordinatesOfCenterPoint() {
        return centerPoint;
    }

    public double getRadius() {
        return radius;
    }

    public Point getDrawedCenterPoint() {
        return drawedCenterPoint;
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
        Point initialPoint = new Point(drawedCenterPoint);
        Point changedPoint = new Point(drawedCenterPoint);
        if (radius * 2 > maxX) {
            radius = (maxX) / 2;
        } else if (radius * 2 > maxY - 2) {
            radius = (maxY) / 2;
        }
        if (drawedCenterPoint.getX() + radius > maxX) {
            changedPoint.setX(maxX - (float) radius);
        } else if (drawedCenterPoint.getX() - radius < 0) {
            changedPoint.setX((float) radius);
        }

        if (drawedCenterPoint.getY() + radius > maxY) {
            changedPoint.setY(maxY - (float) radius);
        } else if (drawedCenterPoint.getY() - radius < 0) {
            changedPoint.setY((float) radius);
        }
        return new Point(initialPoint.getX() - changedPoint.getX(), initialPoint.getY() - changedPoint.getY());
    }

    @Override
    public void changeCoordinatesToDelta(Point delta) {
        drawedCenterPoint.setX(drawedCenterPoint.getX() - delta.getX());
        drawedCenterPoint.setY(drawedCenterPoint.getY() - delta.getY());
    }

    @Override
    public String getLabel() {
        return this.getLabel();
    }

    @Override
    public boolean isTouched(Point point) {
        radiusChangeMode = false;
        setPoint(lastTouchCoordinates, point);

        if (isBorderTouched(point, 15)) {
            radiusChangeMode = true;
            return true;
        } else {
            double length = BaseHelper.getLengthBetweenTwoPoints(point, this.centerPoint);
            return (length < radius);
        }
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
        double length1 = BaseHelper.getLengthBetweenTwoPoints(this.getCoordinatesOfCenterPoint(), circle.getCoordinatesOfCenterPoint());
        double length2 = this.getRadius() + circle.getRadius();

        if (((length1) < (length2 + 15)) && ((length1) > (length2 - 15))) {
            this.getNewCoordinates(circle.getCoordinatesOfCenterPoint());
            Point newCoordinates = new Point(getCoordinatesOfBorderOfCircle(centerPoint, circle.getCoordinatesOfCenterPoint(), length2));
            return new Point(drawedCenterPoint.getX() - newCoordinates.getX(), drawedCenterPoint.getY() - newCoordinates.getY());
        }
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
        Point p = line.getNewCoordinates(this.getCoordinatesOfCenterPoint());
        if (line.isLineTouched(p)) {
            double length = BaseHelper.getLengthBetweenTwoPoints(p, this.getCoordinatesOfCenterPoint());
            if (((length) < (this.radius + 15)) && ((length) > (this.radius - 15))) {
                Point delta = new Point(this.getNewCoordinates(p));
                setPoint(delta, p.getX() - delta.getX(), p.getY() - delta.getY());
                return new Point(drawedCenterPoint.getX() - (centerPoint.getX() + delta.getX()), drawedCenterPoint.getY() - (centerPoint.getY() + delta.getY()));
            }
        }
        if (this.isBorderTouched(line.getPoint1(), 20)) {
            Point newCoordinates = getCoordinatesOfBorderOfCircle(centerPoint, line.getPoint1(), radius);
            return new Point(drawedCenterPoint.getX() - newCoordinates.getX(), drawedCenterPoint.getY() - newCoordinates.getY());
        }
        if (this.isBorderTouched(line.getPoint2(), 20)) {
            Point newCoordinates = getCoordinatesOfBorderOfCircle(centerPoint, line.getPoint2(), radius);
            return new Point(drawedCenterPoint.getX() - newCoordinates.getX(), drawedCenterPoint.getY() - newCoordinates.getY());
        }
        return null;
    }

    @Override
    public void refreshCoordinates() {
        setPoint(centerPoint, drawedCenterPoint);
    }

    public boolean isBorderTouched(Point point, int deltaRadius) {
        double length = BaseHelper.getLengthBetweenTwoPoints(point, this.centerPoint);
        return (length < radius + deltaRadius) && (length > radius - deltaRadius);
    }

    public Point getNewCoordinates(Point point) {
        double radius2 = BaseHelper.getLengthBetweenTwoPoints(this.centerPoint, point);
        Float ratioOfTheRadii = (float) (radius / radius2);

        Point dotCoordinates = new Point(0f, 0f);
        dotCoordinates.setX(((point.getX() - this.centerPoint.getX()) * ratioOfTheRadii) + this.centerPoint.getX());
        dotCoordinates.setY(((point.getY() - this.centerPoint.getY()) * ratioOfTheRadii) + this.centerPoint.getY());

        return dotCoordinates;
    }

    public void changeRadius(Point point) {
        radius = BaseHelper.getLengthBetweenTwoPoints(this.centerPoint, point);
    }

}