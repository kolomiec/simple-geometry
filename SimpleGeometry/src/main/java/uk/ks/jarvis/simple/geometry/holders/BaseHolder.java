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

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.coordinateplane.CoordinateSystem;
import uk.ks.jarvis.simple.geometry.fragments.ShapeDialog;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Line;
import uk.ks.jarvis.simple.geometry.shapes.Shape;
import uk.ks.jarvis.simple.geometry.shapes.ShapeList;
import uk.ks.jarvis.simple.geometry.utils.ColorTheme;
import uk.ks.jarvis.simple.geometry.utils.LettersGenerator;
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
    private ShapeList shapes = new ShapeList();
    private Paint paint = new Paint();
    private int NUMBER_OF_FIRST_SHAPE = 0;
    private boolean isLongClick;
    private boolean createFigureMode = true;
    private Shape createShape = new Dot(new Point(0d, 0d), LettersGenerator.getInstance().getNextUpperCaseName());
    private CoordinateSystem coordinateSystem;
    private boolean showScale = true;
    private boolean coordinateSystemCreated = false;
    private SharedPreferences sharedPrefs;
    private int pointerCount;
    private boolean scaleMode = false;


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
        Toast.makeText(context, "Touch the screen to draw a dot.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDraw(Canvas canvas) {
        refresh(canvas, paint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        pointerCount = motionEvent.getPointerCount();
        firstPointerCoordinates = new Point(motionEvent.getX(), motionEvent.getY());
        switch (motionEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downCoordinates.setXY(motionEvent.getX(), motionEvent.getY());

                if (createFigureMode) {
                    CreateNewFigureInCreateFigureMode(motionEvent);
                }
                shapes.refreshPrvTouchPoint(firstPointerCoordinates);
                isLongClick = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (createFigureMode) {
                    shapes.move(new Point(firstPointerCoordinates.getX(), firstPointerCoordinates.getY()), createShape);
                } else if (pointerCount > 1) {
                    secondPointerCoordinates.setXY(motionEvent.getX(1), motionEvent.getY(1));
                    if (!scaleMode) {
                        zoom.initZoom(firstPointerCoordinates, secondPointerCoordinates);
                        scaleMode = true;
                    }
                    zoom.zoom(firstPointerCoordinates, secondPointerCoordinates);
                    shapes.zoom(zoom.getZoomPoint(), zoom.getZoomRatio(), zoom.getMoveDelta());
                    shapes.turn(zoom.getZoomPoint(), zoom.getAngleDelta());
                    if (isLongClick) {
                        isLongClick = false;
                    }
                } else if (!scaleMode) {
                    shapes.move(new Point(firstPointerCoordinates.getX(), firstPointerCoordinates.getY()));
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
        } else if (((createShape.getClass()) == (Dot.class))) {
            ((Dot) createShape).getPoint().setX(motionEvent.getX());
            ((Dot) createShape).getPoint().setY(motionEvent.getY());
        }

        shapes.add(NUMBER_OF_FIRST_SHAPE, createShape);
        isTouchedShape = true;
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
                paint.setStrokeWidth(20);
                paint.setColor(Color.argb(50, 0, 250, 0));
                canvas.drawLine((float) firstPointerCoordinates.getX(), (float) firstPointerCoordinates.getY(), (float) secondPointerCoordinates.getX(), (float) secondPointerCoordinates.getY(), paint);
            }
        }

        paint.setStrokeWidth(2);
        shapes.draw(canvas, p);

        canvas.drawText(pointerCount + " pointers, " + Math.round(firstPointerCoordinates.getX()), 30, 10, p);

        if (shapes.size() != 0) {
            canvas.drawText(shapes.toString(), 30, 25, p);
        }

        if (sharedPrefs.getBoolean("checkBox", false)) {
            coordinateSystem.draw(canvas);
        }
    }

    public FragmentActivity getActivity() {
        return this.activity;
    }

    @Override
    public boolean onLongClick(View view) {
        if (isLongClick) {
            Shape clickedShape = getClickedShapeFromShapeList();
            if (clickedShape != null) {
                ShapeDialog c = new ShapeDialog(this, shapes, clickedShape);
                c.show(activity.getSupportFragmentManager(), "");
            }
            return true;
        }
        return false;
    }

    private Shape getClickedShapeFromShapeList() {
        if (shapes.isTouched(firstPointerCoordinates)) {
            return shapes.getTouchedFigureInList(firstPointerCoordinates);
        }
        return null;
    }

    public void setCreateFigureMode(Shape createShape) {
        createFigureMode = true;
        this.createShape = createShape;
    }
}