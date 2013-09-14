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

    public void refreshCoordinates();

    public int getColor();

    public void setColor(int color);

    public Point setFigureThatItWillNotBeOutsideTheScreen(float maxX, float maxY);

    public void changeCoordinatesToDelta(Point delta);

    public String getLabel();
}
