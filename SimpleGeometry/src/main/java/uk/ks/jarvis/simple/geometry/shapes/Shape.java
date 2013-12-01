package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;


/**
 * Created by sayenko on 7/14/13.
 */

public interface Shape {

    public void draw(Canvas c, Paint p);

    public void move(Point point, boolean onlyMove);

    public boolean isTouched(Point point);

    public Point checkTouchWithOtherFigure(Circle circle);

    public Point checkTouchWithOtherFigure(Line line);

    public int getColor();

    public void setColor(int color);

    public String getLabel();

    public void zoom(Point centralZoomPoint, float zoomRatio, Point moveDelta);

    public void turn(Point centralTurnPoint, double angle);

    void refreshPrvTouchPoint(Point newTouchPoint);
}
