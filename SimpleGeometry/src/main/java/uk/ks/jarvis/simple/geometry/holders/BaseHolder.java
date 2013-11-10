package uk.ks.jarvis.simple.geometry.holders;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.coordinateplane.CoordinateSystem;
import uk.ks.jarvis.simple.geometry.fragments.CreateFigureDialog;
import uk.ks.jarvis.simple.geometry.fragments.ShapeDialog;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Line;
import uk.ks.jarvis.simple.geometry.shapes.Shape;
import uk.ks.jarvis.simple.geometry.shapes.ShapeList;
import uk.ks.jarvis.simple.geometry.utils.ColorTheme;
import uk.ks.jarvis.simple.geometry.utils.Zoom;

import static uk.ks.jarvis.simple.geometry.coordinateplane.SystemInformation.initSystemInformation;

/**
 * Created by sayenko on 7/14/13.
 */
public class BaseHolder extends View implements View.OnTouchListener, View.OnLongClickListener {

    private final Context context;
    private Zoom zoom = new Zoom();
    private Point firstPointerCoordinates = new Point(0f, 0f);
    private Point secondPointerCoordinates = new Point(0f, 0f);
    private Point downCoordinates = new Point(0f, 0f);
    private boolean thereAreTouchedFigures = false;
    private boolean isTouchedShape;
    private FragmentActivity activity;
    private List<ShapeList> shapes = new ArrayList<>();
    private Paint paint = new Paint();
    private int NUMBER_OF_FIRST_SHAPE = 0;
    private boolean isLongClick;
    private boolean createFigureMode;
    private Shape createShape;
    private CoordinateSystem coordinateSystem;
    private boolean showScale = true;
    private boolean coordinateSystemCreated = false;
    private SharedPreferences sharedPrefs;
    private int pointerCount;
    private boolean scaleMode;


    public BaseHolder(Context context) {
        super(context);
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
        this.context = context;
    }

    public BaseHolder(Context context, FragmentActivity activity) {
        super(context);
        this.setOnTouchListener(this);
        this.setOnLongClickListener(this);
        this.setHorizontalFadingEdgeEnabled(true);
        this.setVerticalFadingEdgeEnabled(false);
        this.context = context;
        this.activity = activity;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        scaleMode = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        refresh(canvas, paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        pointerCount = motionEvent.getPointerCount();
        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downCoordinates.setXY(motionEvent.getX(), motionEvent.getY());

                firstPointerCoordinates.setXY(motionEvent.getX(), motionEvent.getY());
                if (createFigureMode) {
                    CreateNewFigureInCreateFigureMode(motionEvent);
                }

                isLongClick = true;
                moveTouchedFigureToFirstPosition();

                break;
            case MotionEvent.ACTION_MOVE:
                firstPointerCoordinates.setXY(motionEvent.getX(), motionEvent.getY());
                if (createFigureMode) {
                    createShape.move(firstPointerCoordinates, Shape.ONLY_CHANGE);
                } else if (pointerCount > 1) {
                    secondPointerCoordinates.setXY(motionEvent.getX(1), motionEvent.getY(1));
                    if (!scaleMode) {
                        zoom.initZoom(firstPointerCoordinates, secondPointerCoordinates);
                        scaleMode = true;
                    }
                    zoom.zoom(firstPointerCoordinates, secondPointerCoordinates);
                    for (ShapeList s : shapes) {
                        s.zoom(zoom.getZoomPoint(), zoom.getZoomRatio(), zoom.getMoveDelta());
                        s.turn(zoom.getZoomPoint(), zoom.getAngleDelta());
                    }
                    if (isLongClick) {
                        isLongClick = false;
                    }
                } else if (!scaleMode) {
                    if (shapes.size() != 0) {
                        shapes.get(NUMBER_OF_FIRST_SHAPE).move(firstPointerCoordinates);
                        getFigureTouchedWithFirstFigure();
                    }
                }
                if ((!downCoordinates.nearlyEquals(firstPointerCoordinates)) && isLongClick) {
                    isLongClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scaleMode) {
                    scaleMode = false;
                }
                if (createFigureMode) {
                    createFigureMode = false;
                }
                break;
            default:
                break;
        }
        invalidate();
        return false;
    }

    private void CreateNewFigureInCreateFigureMode(MotionEvent motionEvent) {
        if (((createShape.getClass()) == (Line.class))) {
            ((Line) createShape).setAngle(firstPointerCoordinates);
//        } else if (((createShape.getClass()) == (Circle.class))) {
//            ((Circle) createShape).getCoordinatesOfCenterPoint().setX(motionEvent.getX());
//            ((Circle) createShape).getCoordinatesOfCenterPoint().setY(motionEvent.getY());
        } else if (((createShape.getClass()) == (Dot.class))) {
            ((Dot) createShape).getPoint().setX(motionEvent.getX());
            ((Dot) createShape).getPoint().setY(motionEvent.getY());
        }

        if (shapes.size() == 0) {
            ShapeList listOfShapes = new ShapeList();
            listOfShapes.getShapeArray().add(createShape);
            addShape(listOfShapes);
        } else {
            shapes.get(NUMBER_OF_FIRST_SHAPE).getShapeArray().add(NUMBER_OF_FIRST_SHAPE, createShape);
        }
        isTouchedShape = true;
//        createFigureMode = false;
    }

    private ShapeList getFigureTouchedWithFirstFigure() {
        int count = 0;
        ShapeList shapeList = null;
        if (shapes.size() > 1) {
            for (ShapeList s : shapes) {
                if (count != 0) {
                    boolean touchFigures = shapes.get(NUMBER_OF_FIRST_SHAPE).checkTouchWithOtherFigure(s);
                    if (touchFigures) {
                        shapeList = s;
                    }
                }
                count++;
            }
        }
        return shapeList;
    }

    private void moveTouchedFigureToFirstPosition() {
        isTouchedShape = false;
        for (ShapeList s : shapes) {
            if (s.isTouched(firstPointerCoordinates)) {
                isTouchedShape = true;
                shapes.set(shapes.indexOf(s), shapes.set(NUMBER_OF_FIRST_SHAPE, s));
                break;
            }
        }
    }

    public void addShape(ShapeList shapeList) {
        shapes.add(NUMBER_OF_FIRST_SHAPE, shapeList);
    }

    public void unMergeAllFigures(ShapeList shapeList) {
        for (Shape shape : shapeList.getShapeArray()) {
            ShapeList listOfShapes = new ShapeList();
            listOfShapes.getShapeArray().add(shape);
            addShape(listOfShapes);
        }
        shapes.remove(shapeList);
    }

    public void disconnectFigure(ShapeList shapeList, Shape touchedShape) {
        ShapeList listOfShapes = new ShapeList();
        listOfShapes.getShapeArray().add(touchedShape);
        addShape(listOfShapes);
        shapeList.getShapeArray().remove(touchedShape);
    }

    public void mergeShapeLists(ShapeList shapeList1, ShapeList shapeList2) {
        for (Shape shape : (shapeList1).getShapeArray()) {
            (shapeList2).getShapeArray().add(shape);
        }
        Toast.makeText(context, "Figures were merged.", Toast.LENGTH_SHORT).show();
        removeShape(NUMBER_OF_FIRST_SHAPE);
    }

    public void removeShape(int numberOfShapeToRemove) {
        if (shapes.size() != 0) {
            shapes.remove(numberOfShapeToRemove);
            if (shapes.size() == 0) {
                Toast.makeText(context, "There are no figures anymore...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void refresh(Canvas canvas, Paint p) {
        String s = sharedPrefs.getString("list", "-1");
        if ((s.equals("2")) && (ColorTheme.isLightTheme)) {
            ColorTheme.setDarkTheme();
        } else if ((s.equals("1") && (ColorTheme.isDarkTheme))) {
            ColorTheme.setLightTheme();
        }
        if (!coordinateSystemCreated) {
            initSystemInformation(this);
            coordinateSystem = new CoordinateSystem();
            coordinateSystemCreated = true;
        }
        canvas.drawColor(ColorTheme.DARK_COLOR);

        if (pointerCount > 0) {
            paint.setColor(Color.argb(50, 0, 0, 250));
            canvas.drawCircle((float) firstPointerCoordinates.getX(), (float) firstPointerCoordinates.getY(), 40, paint);

            if (pointerCount > 1) {
                paint.setColor(Color.argb(50, 250, 0, 0));
                canvas.drawCircle((float) secondPointerCoordinates.getX(), (float) secondPointerCoordinates.getY(), 40, paint);
            }
        }

        paint.setStrokeWidth(2);
        for (ShapeList shape : shapes) {
            shape.draw(canvas, p);
        }

        canvas.drawText(pointerCount + " pointers, " + zoom.toString(), 30, 10, p);

        if (shapes.size() > 0) {
            canvas.drawText(shapes.get(NUMBER_OF_FIRST_SHAPE).toString(), 30, 25, p);
        }

        if (sharedPrefs.getBoolean("checkBox", false)) {
            coordinateSystem.draw(canvas);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (isLongClick) {
            if (isClickOnShape()) {
                Dot someDot = shapes.get(NUMBER_OF_FIRST_SHAPE).someDotTouched(firstPointerCoordinates);
                if (someDot != null) {
                    CreateFigureDialog c = new CreateFigureDialog(this, someDot.getPoint());
                    c.show(activity.getSupportFragmentManager(), "");
                } else {
                    ShapeDialog c = new ShapeDialog(this, shapes.get(NUMBER_OF_FIRST_SHAPE), getClickedShapeFromShapeList());
                    c.show(activity.getSupportFragmentManager(), "");
                }
            } else if (shapes.size() == 0) {
                CreateFigureDialog c = new CreateFigureDialog(this);
                c.show(activity.getSupportFragmentManager(), "");
            }
            return true;
        }
        return false;
    }

    private boolean isClickOnShape() {
        for (ShapeList shape : shapes) {
            if (shape.isTouched(firstPointerCoordinates)) {
                return true;
            }
        }
        return false;
    }

    private Shape getClickedShapeFromShapeList() {
        for (ShapeList shape : shapes) {
            if (shape.isTouched(firstPointerCoordinates)) {
                return shape.getTouchedFigureInList(firstPointerCoordinates);
            }
        }
        return null;
    }

    public void setCreateFigureMode(Shape createShape) {
        createFigureMode = true;
        this.createShape = createShape;
    }
}