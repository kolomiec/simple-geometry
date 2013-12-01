package uk.ks.jarvis.simple.geometry.shapes;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.BaseHelper;


/**
 * Created by sayenko on 8/3/13.
 */
public class ShapeList {
    private static List<Shape> shapeList = new ArrayList<>();
    boolean ONLY_CHANGE = false;
    boolean ONLY_MOVE = true;
    private int color = 0;
    private Point prvTouchPoint = new Point(0, 0);

    public ShapeList() {
        color = BaseHelper.getRandomColor();
    }

    public void add(int number, Shape shape) {
        shapeList.add(number, shape);
    }

    public void add(Shape shape) {
        shapeList.add(shape);
    }

    public void setRandomColor() {
        if (shapeList.size() == 1) {
            shapeList.get(0).setColor(BaseHelper.getRandomColor());
        } else
            color = BaseHelper.getRandomColor();
    }

    public Dot someDotTouched(Point point) {
        boolean isDot = false;
        int count = 0;
        for (Shape shape : shapeList) {
            if (shape.isTouched(point)) {
                if (shapeList.get(count).getClass() == Dot.class) {
                    isDot = true;
                    break;
                }
            }
            count++;
        }
        if (isDot) {
            return ((Dot) shapeList.get(count));
        } else return null;
    }

    public void zoom(Point centralPoint, double ratio, Point moveDelta) {
        for (Shape shape : shapeList) {
            shape.zoom(centralPoint, (float) ratio, moveDelta);
        }
    }

    public Point checkTouch(Shape shape1, Shape shape2) {
        if (((shape1.getClass()) == (Line.class)) && ((shape2.getClass()) == (Circle.class))) {
            return shape1.checkTouchWithOtherFigure((Circle) shape2);

//        } else if (((shape1.getClass()) == (Circle.class)) && ((shape2.getClass()) == (Circle.class))) {
//            return shape1.checkTouchWithOtherFigure((Circle) shape2);

//        } else if (((shape1.getClass()) == (Circle.class)) && (((shape2.getClass()) == (Line.class)) || ((shape2.getClass()) == (EndlessLine.class)))) {
//            return shape1.checkTouchWithOtherFigure((Line) shape2);
        }
        return null;
    }

    public void draw(Canvas canvas, Paint paint) {
        if (shapeList.size() == 1) {
            paint.setColor(shapeList.get(0).getColor());
        } else
            paint.setColor(color);
        for (Shape shape : shapeList) {
            shape.draw(canvas, paint);
        }
    }

    public void refreshPrvTouchPoint(Point touchCoordinates) {
        for (Shape shape : shapeList) {
            shape.refreshPrvTouchPoint(touchCoordinates);
        }
    }

    public void move(Point touchPoint) {

//        boolean manyFigures = (shapeList.size() > 1);
        for (Shape shape : shapeList) {
//            if (shape.getClass() == Line.class) shape.move(touchPoint, ONLY_CHANGE);
            shape.isTouched(touchPoint);
            shape.move(new Point(touchPoint.getX(), touchPoint.getY()), ONLY_MOVE);
        }
        prvTouchPoint = new Point(touchPoint);
    }

    public void move(Point touchPoint, Shape shape) {
//        boolean manyFigures = (shapeList.size() > 1);
        if (shapeList.contains(shape)) {
//            if (shape.getClass() == Line.class) shape.move(touchPoint, ONLY_MOVE);
//            shape.move(new Point(prvTouchPoint.getX() - touchPoint.getX(), prvTouchPoint.getY() - touchPoint.getY()), ONLY_MOVE);
            shape.isTouched(touchPoint);
            shape.move(new Point(touchPoint.getX(), touchPoint.getY()), ONLY_CHANGE);
        }
        prvTouchPoint = new Point(touchPoint);
    }

    public boolean isTouched(Point point) {
        boolean isTouch = false;
        for (Shape shape : shapeList) {
            if (shape.isTouched(point)) {
                isTouch = true;
            }
        }
        return isTouch;
    }

    public Shape getTouchedFigureInList(Point point) {
        Shape touchedShape = null;
        for (Shape shape : shapeList) {
            if (shape.isTouched(point)) {

                if (shape.getClass() == Dot.class)
                    return shape;

                else touchedShape = shape;
            }
        }
        return touchedShape;
    }

    public boolean checkTouchWithOtherFigure(ShapeList shapeList) {
        for (Shape shape1 : ShapeList.shapeList) {
            for (Shape shape2 : getShapeArray()) {
                Point delta = checkTouch(shape1, shape2);
                if (delta != null) {
                    changeCoordinatesToDelta(delta);
                    return true;
                }
            }
        }
        return false;
    }

    public void changeCoordinatesToDelta(Point delta) {
        for (Shape shape1 : shapeList) {
            shape1.changeCoordinatesToDelta(delta);
        }
    }

    public void turn(Point centralTurnPoint, double angle) {
        for (Shape shape1 : shapeList) {
            shape1.turn(centralTurnPoint, (float) angle);
        }
    }

    public static List<Shape> getShapeArray() {
        return shapeList;
    }

    @Override
    public String toString() {
        return shapeList.get(0).toString();
    }

    public int size() {
        return shapeList.size();
    }
}
