package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static java.lang.Math.PI;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.setPoint;


/**
 * Created by sayenko on 7/14/13.
 */
public class Dot implements Shape {

    private final Float radius = 5.0f;
    private final Point point;
    private final String label;
    public int color = 0;
    Point lastTouchCoordinates = new Point(0f, 0f);
    Point deltaTouchCoordinates = new Point(0f, 0f);
    private Point delta = new Point(15f, 15f);


    public Dot(Point point, String label) {
        this.point = point;
        this.label = label;
        color = BaseHelper.getRandomColor();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point.getX(), point.getY(), radius, paint);

//        BaseHelper.drawTextWithShadow(canvas, label, point.getX() + radius, point.getY() + radius / 2);
        BaseHelper.drawTextWithShadow(canvas, String.valueOf((PI/6)), point.getX() + radius, point.getY() + radius / 2);

    }

    @Override
    public String toString() {
        return "Dot " + label + " - x:" + Math.round(point.getX()) + ", y:" + Math.round(point.getY());
    }

    @Override
    public void move(Point touchCoordinates, boolean onlyMove) {
        setPoint(deltaTouchCoordinates, lastTouchCoordinates.getX() - touchCoordinates.getX(), lastTouchCoordinates.getY() - touchCoordinates.getY());
        setPoint(this.point, this.point.getX() - deltaTouchCoordinates.getX(), this.point.getY() - deltaTouchCoordinates.getY());
        setPoint(lastTouchCoordinates, touchCoordinates);
    }

    public boolean isTouched(Point point) {
        boolean betweenDelta = (this.point.getX() < (point.getX() + delta.getX())) && (this.point.getX() > (point.getX() - delta.getX()));
        if (betweenDelta) {
            boolean betweenDelta2 = (this.point.getY() < (point.getY() + delta.getY())) && (this.point.getY() > (point.getY() - delta.getY()));
            if (betweenDelta2) {
                setPoint(lastTouchCoordinates, point);
                return true;
            }
        }
        return false;
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
        point.setX(point.getX() - delta.getX());
        point.setY(point.getY() - delta.getY());
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
        return null;
    }

    public Point getCoordinatesPoint() {
        return point;
    }

    @Override
    public void refreshCoordinates() {

    }
}
