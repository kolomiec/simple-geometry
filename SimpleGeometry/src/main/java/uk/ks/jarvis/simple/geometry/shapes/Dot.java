package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;

import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getAngleFrom2Points;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.cos;
import static uk.ks.jarvis.simple.geometry.utils.Mathematics.sin;


/**
 * Created by sayenko on 7/14/13.
 */
public class Dot extends BaseShape {
    private final Float radius = 5.0f;
    private final String label;
    private int color = 0;
    private Point lastTouchCoordinates = new Point(0f, 0f);
    private Point deltaTouchCoordinates = new Point(0f, 0f);
//    private List<Shape> shapeList = new ArrayList<>();
    private Point point;
    private Point delta = new Point(15f, 15f);


    public Dot(Point point, String label) {
        this.point = point;
        this.label = label;
        color = BaseHelper.getRandomColor();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle((float) point.getX(), (float) point.getY(), radius, paint);

        BaseHelper.drawTextWithShadow(canvas, label, (float) point.getX() + radius, (float) point.getY() + radius / 2);
    }

    @Override
    public String toString() {
        return "Dot " + label + " - x:" + Math.round(point.getX()) + ", y:" + Math.round(point.getY());
    }

    @Override
    public void move(Point touchCoordinates, boolean onlyMove) {
        deltaTouchCoordinates = new Point(lastTouchCoordinates.getX() - touchCoordinates.getX(),
                lastTouchCoordinates.getY() - touchCoordinates.getY());
        this.point = new Point(this.point.getX() - deltaTouchCoordinates.getX(), this.point.getY() - deltaTouchCoordinates.getY());
        lastTouchCoordinates = new Point(touchCoordinates);
    }

    @Override
    public void refreshPrvTouchPoint(Point newTouchPoint) {
        lastTouchCoordinates = new Point(newTouchPoint);
    }

    public boolean isTouched(Point point) {
        boolean betweenDelta = (this.point.getX() < (point.getX() + delta.getX())) && (this.point.getX() > (point.getX() - delta.getX()));
        if (betweenDelta) {
            boolean betweenDelta2 = (this.point.getY() < (point.getY() + delta.getY())) && (this.point.getY() > (point.getY() - delta.getY()));
            if (betweenDelta2) {
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
    public void changeCoordinatesToDelta(Point delta) {
        point.setX(point.getX() - delta.getX());
        point.setY(point.getY() - delta.getY());
    }

    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public void zoom(Point centralZoomPoint, float zoomRatio, Point moveDelta) {
        point.setX(((-centralZoomPoint.getX() + this.point.getX()) * zoomRatio) + centralZoomPoint.getX());
        point.setY(((-centralZoomPoint.getY() + this.point.getY()) * zoomRatio) + centralZoomPoint.getY());
        point.setX(point.getX() + moveDelta.getX());
        point.setY(point.getY() + moveDelta.getY());
    }

    @Override
    public void turn(Point centralTurnPoint, double deltaAngle) {
        double angle = (getAngleFrom2Points(centralTurnPoint, point) + deltaAngle + 360) % 360;
        double hypotenuse = getLength(point, centralTurnPoint);
        point.setX(hypotenuse * cos(angle) + centralTurnPoint.getX());
        point.setY(-hypotenuse * sin(angle) + centralTurnPoint.getY());
    }

    public Point getPoint() {
        return this.point;
    }

    @Override
    public Point checkTouchWithOtherFigure(Circle circle) {
        return null;
    }

    @Override
    public Point checkTouchWithOtherFigure(Line line) {
        return null;
    }
}