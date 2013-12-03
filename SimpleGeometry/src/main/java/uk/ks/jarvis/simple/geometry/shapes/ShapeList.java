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

    public ShapeList() {
        color = BaseHelper.getRandomColor();
    }

    public static List<Shape> getShapeArray() {
        return shapeList;
    }

    public void add(int number, Shape shape) {
        shapeList.add(number, shape);
    }

    public void add(Shape shape) {
        shapeList.add(shape);
    }

    public boolean contains(Shape shape) {
        return shapeList.contains(shape);
    }

    public void setRandomColor() {
        if (shapeList.size() == 1) {
            shapeList.get(0).setColor(BaseHelper.getRandomColor());
        } else
            color = BaseHelper.getRandomColor();
    }

    public void zoom(Point centralPoint, double ratio, Point moveDelta) {
        for (Shape shape : shapeList) {
            shape.zoom(centralPoint, (float) ratio, moveDelta);
        }
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
        for (Shape shape : shapeList) {
            shape.isTouched(touchPoint);
            shape.move(new Point(touchPoint.getX(), touchPoint.getY()), ONLY_MOVE);
        }
    }

    public void move(Point touchPoint, Shape shape) {
        try {
            shape.isTouched(touchPoint);
            shape.move(new Point(touchPoint.getX(), touchPoint.getY()), ONLY_CHANGE);
        } catch (Exception ignored) {

        }
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

    public void turn(Point centralTurnPoint, double angle) {
        for (Shape shape : shapeList) {
            shape.turn(centralTurnPoint, angle);
        }
    }

    @Override
    public String toString() {
        return shapeList.get(0).toString();
    }

    public int size() {
        return shapeList.size();
    }

    public void removeShape(Shape shape) {
        if (shapeList.contains(shape)) {
            shapeList.remove(shape);
        }
    }

    public void removeAll() {
        try {
            for (Shape shape : shapeList) {
                if (!shape.getLabel().equals("A")) {
                    removeShape(shape);
                }
            }
        } catch (Exception e) {
            removeAll();
        }
    }
}
