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
    List<Shape> shapeList = new ArrayList<>();
    private int color = 0;

    public ShapeList() {
        color = BaseHelper.getRandomColor();
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

    public void zoom(Point centralPoint, double ratio) {
        for (Shape shape : shapeList) {
            shape.zoom(centralPoint, (float) ratio);
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

    public void move(Point point) {
        boolean manyFigures = (shapeList.size() > 1);
        for (Shape shape : shapeList) {
            if (shape.getClass() == Dot.class)
                shape.move(point, manyFigures);
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
        for (Shape shape : shapeList) {
            if (shape.isTouched(point)) {
                return shape;
            }
        }
        return null;
    }

    public boolean checkTouchWithOtherFigure(ShapeList shapeList) {
        for (Shape shape1 : this.shapeList) {
            for (Shape shape2 : shapeList.getShapeArray()) {
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
        for (Shape shape1 : this.shapeList) {
            shape1.changeCoordinatesToDelta(delta);
        }
    }

    public void refreshCoordinates() {
        for (Shape shape : shapeList) {
            shape.refreshCoordinates();
        }
    }

    public List<Shape> getShapeArray() {
        return shapeList;
    }

    @Override
    public String toString() {
        return shapeList.get(0).toString();
    }
}
