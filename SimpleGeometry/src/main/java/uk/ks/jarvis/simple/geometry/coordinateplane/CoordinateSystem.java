package uk.ks.jarvis.simple.geometry.coordinateplane;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.utils.ColorTheme;


/**
 * Created by sayenko on 8/10/13.
 */
public class CoordinateSystem {
    private final int labelStep = 20;
    private final int labelHeight = 3;
    private final int lineWidth = 1;
    private final int labelWidth = 2;
    private final int textWidth = 13;
    private Point startPointXAxis = new Point(20f, SystemInformation.DISPLAY_HEIGHT - 20f);
    private Point endPointXAxis = new Point(SystemInformation.DISPLAY_WIDTH - 1f, SystemInformation.DISPLAY_HEIGHT - 20f);
    private Point startPointYAxis = new Point(20f, (SystemInformation.DISPLAY_HEIGHT) - 20f);
    private Point endPointYAxis = new Point(20f, 1f);
    private Point originPoint = new Point(startPointXAxis);
    private Paint paint;


    public CoordinateSystem() {
        paint = new Paint();
        paint.setColor(ColorTheme.LIGHT_COLOR);
    }

    public void draw(Canvas canvas) {
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

    private void drawYAxis(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine((float) startPointYAxis.getX(), (float) startPointYAxis.getY(), (float) endPointYAxis.getX(), (float) endPointYAxis.getY(), paint);
        drawLabelOnYAxis(canvas);
    }

    private void drawLabelOnYAxis(Canvas canvas) {
        int labelsCount = (int) Math.round((startPointYAxis.getY()) / labelStep);
        SystemInformation.COUNT_LABEL_BY_Y_AXIS = labelsCount;
        int magicNumber = 5; // :) ToDo Rename
        Point cursorPos = new Point(startPointYAxis.getX(), startPointXAxis.getY() - labelStep);
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine((float) cursorPos.getX() - labelHeight, (float) cursorPos.getY(), (float) cursorPos.getX() + labelHeight, (float) cursorPos.getY(), paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            canvas.drawText(getString(i + 1), (float) cursorPos.getX() - labelHeight - textWidth, (float) cursorPos.getY() + magicNumber, paint);
            cursorPos.setY(cursorPos.getY() - labelStep);
        }
    }

    private void drawXAxis(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine((float) startPointXAxis.getX(), (float) startPointXAxis.getY(), (float) endPointXAxis.getX(), (float) endPointXAxis.getY(), paint);
        drawLabelOnXAxis(canvas);
    }

    private void drawLabelOnXAxis(Canvas canvas) {
        int labelsCount = (int) Math.round((SystemInformation.DISPLAY_WIDTH - startPointXAxis.getX()) / labelStep);
        SystemInformation.COUNT_LABEL_BY_X_AXIS = labelsCount;
        int magicNumber = 7; // :) ToDo Rename
        Point cursorPos = new Point(startPointXAxis.getX() + labelStep, startPointXAxis.getY());
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine((float) cursorPos.getX(), (float) cursorPos.getY() - labelHeight, (float) cursorPos.getX(), (float) cursorPos.getY() + labelHeight, paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            canvas.drawText(getString(i + 1), (float) cursorPos.getX() - magicNumber, (float) cursorPos.getY() + labelHeight + textWidth, paint);
            cursorPos.setX(cursorPos.getX() + labelStep);
        }
    }

    private String getString(int count) {
        String s = Integer.toString(count);
        if (count < 10) {
            s = " " + s;
        }
        return s;
    }

    public Point getOriginPoint() {
        return originPoint;
    }

    public int getLabelStep() {
        return this.labelStep;
    }

}
