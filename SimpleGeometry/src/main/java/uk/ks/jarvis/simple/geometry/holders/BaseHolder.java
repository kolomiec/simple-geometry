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
import uk.ks.jarvis.simple.geometry.coordinateplane.SystemInformation;
import uk.ks.jarvis.simple.geometry.fragments.CreateFigureDialog;
import uk.ks.jarvis.simple.geometry.fragments.ShapeDialog;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Line;
import uk.ks.jarvis.simple.geometry.shapes.Shape;
import uk.ks.jarvis.simple.geometry.shapes.ShapeList;
import uk.ks.jarvis.simple.geometry.utils.ColorTheme;
import uk.ks.jarvis.simple.geometry.utils.Zoom;

/**
 * Created by sayenko on 7/14/13.
 */
public class BaseHolder extends View implements View.OnTouchListener, View.OnLongClickListener {

    private final Context context;
    Zoom zoom = new Zoom();
    private Point firstPointerCoordinates = new Point(0f, 0f);
    private Point secondPointerCoordinates = new Point(0f, 0f);
    private Point downCoordinates = new Point(0f, 0f);
    private boolean thereAreTouchedFigures = false;
    private boolean isTouchedShape;
    private FragmentActivity activity;
    private List<ShapeList> shapes = new ArrayList<ShapeList>();
    private Paint paint = new Paint();
    private int FIRST_SHAPE_IN_LIST = 0;
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
                downCoordinates.setX(motionEvent.getX());
                downCoordinates.setY(motionEvent.getY());

                firstPointerCoordinates.setX(motionEvent.getX());
                firstPointerCoordinates.setY(motionEvent.getY());

                if (createFigureMode) {
                    CreateNewFigureInCreateFigureMode(motionEvent);
                }

                isLongClick = true;
                moveTouchedFigureToFirstPosition();

                break;
            case MotionEvent.ACTION_MOVE:
                firstPointerCoordinates.setX(motionEvent.getX());
                firstPointerCoordinates.setY(motionEvent.getY());
                if (pointerCount > 1) {
                    secondPointerCoordinates.setX(motionEvent.getX(1));
                    secondPointerCoordinates.setY(motionEvent.getY(1));
                    if (!scaleMode) {
                        zoom.initZoom(firstPointerCoordinates, secondPointerCoordinates);
                        scaleMode = true;
                    }
                    zoom.zoom(firstPointerCoordinates, secondPointerCoordinates);
                    for (ShapeList s : shapes) {
                        s.zoom(zoom.getZoomPoint(), zoom.getZoomRatio());
                    }
                    if (isLongClick) {
                        isLongClick = false;
                    }
                } else if (!scaleMode) {
                    if ((!downCoordinates.nearlyEquals(firstPointerCoordinates)) && isLongClick) {
                        isLongClick = false;
                    }

                    if (isTouchedShape) {
                        shapes.get(FIRST_SHAPE_IN_LIST).move(firstPointerCoordinates);
                        getFigureTouchedWithFirstFigure();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (scaleMode) {
                    scaleMode = false;
                }
                for (ShapeList s : shapes) {
                    s.refreshCoordinates();
                }
//                ShapeList figureTouchedWithFirstFigure = getFigureTouchedWithFirstFigure();
//                if (figureTouchedWithFirstFigure != null) {
//                    mergeShapeLists(shapes.get(FIRST_SHAPE_IN_LIST), figureTouchedWithFirstFigure);
//                    thereAreTouchedFigures = false;
//                }
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
            shapes.get(FIRST_SHAPE_IN_LIST).getShapeArray().add(createShape);
        }
        isTouchedShape = true;
        createFigureMode = false;
    }

    private ShapeList getFigureTouchedWithFirstFigure() {
        int count = 0;
        ShapeList shapeList = null;
        if (shapes.size() > 1) {
            for (ShapeList s : shapes) {
                if (count != 0) {
                    boolean touchFigures = shapes.get(FIRST_SHAPE_IN_LIST).checkTouchWithOtherFigure(s);
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
        int touchedShapePosition = 0;
        for (ShapeList s : shapes) {
            if (s.isTouched(firstPointerCoordinates)) {
                isTouchedShape = true;
                shapes.set(touchedShapePosition, shapes.set(FIRST_SHAPE_IN_LIST, shapes.get(touchedShapePosition)));
                break;
            }
            touchedShapePosition++;
        }
    }

    public void addShape(ShapeList shapeList) {
        shapes.add(FIRST_SHAPE_IN_LIST, shapeList);
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
        removeFirstShape();
    }

    public void removeFirstShape() {
        if (shapes.size() != 0) {
            shapes.remove(FIRST_SHAPE_IN_LIST);
            if (shapes.size() == 0) {
                Toast.makeText(context, "There are no figures anymore...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setSystemInformation() {
        SystemInformation.DISPLAY_HEIGHT = this.getHeight();
        SystemInformation.DISPLAY_WIDTH = this.getWidth();
    }

    private void refresh(Canvas canvas, Paint p) {
        String s = sharedPrefs.getString("list", "-1");
        if ((s.equals("2")) && (ColorTheme.isLightTheme)) {
            ColorTheme.setDarkTheme();
        } else if ((s.equals("1") && (ColorTheme.isDarkTheme))) {
            ColorTheme.setLightTheme();
        }
        if (!coordinateSystemCreated) {
            setSystemInformation();
            coordinateSystem = new CoordinateSystem();
            coordinateSystemCreated = true;
        }
        canvas.drawColor(ColorTheme.DARK_COLOR);

        if (pointerCount > 0) {
            paint.setColor(Color.BLUE);
            canvas.drawCircle(firstPointerCoordinates.getX(), firstPointerCoordinates.getY(), 40, paint);

            if (pointerCount > 1) {
                paint.setColor(Color.RED);
                canvas.drawCircle(secondPointerCoordinates.getX(), secondPointerCoordinates.getY(), 40, paint);
            }
        }

        paint.setStrokeWidth(2);
        for (ShapeList shape : shapes) {
            shape.draw(canvas, p);
        }

        canvas.drawText(pointerCount + " pointers, " + zoom.toString(), 30, 10, p);

        if (shapes.size() > 0) {
            canvas.drawText(shapes.get(FIRST_SHAPE_IN_LIST).toString(), 30, 25, p);
        }

        if (sharedPrefs.getBoolean("checkBox", false)) {
            coordinateSystem.draw(canvas);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (isLongClick) {
            if (isClickOnShape()) {
                Dot someDot = shapes.get(FIRST_SHAPE_IN_LIST).someDotTouched(firstPointerCoordinates);
                if (someDot != null) {
                    CreateFigureDialog c = new CreateFigureDialog(this, someDot.getPoint());
                    c.show(activity.getSupportFragmentManager(), "");
                } else {
                    ShapeDialog c = new ShapeDialog(this, shapes.get(FIRST_SHAPE_IN_LIST), getClickedShapeFromShapeList());
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