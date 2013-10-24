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
        canvas.drawLine(startPointYAxis.getX(), startPointYAxis.getY(), endPointYAxis.getX(), endPointYAxis.getY(), paint);
        drawLabelOnYAxis(canvas);
    }

    private void drawLabelOnYAxis(Canvas canvas) {
        int labelsCount = Math.round((startPointYAxis.getY()) / labelStep);
        SystemInformation.COUNT_LABEL_BY_Y_AXIS = labelsCount;
        int magicNumber = 5; // :) ToDo Rename
        Point cursorPos = new Point(startPointYAxis.getX(), startPointXAxis.getY() - labelStep);
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine(cursorPos.getX() - labelHeight, cursorPos.getY(), cursorPos.getX() + labelHeight, cursorPos.getY(), paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            canvas.drawText(getString(i + 1), cursorPos.getX() - labelHeight - textWidth, cursorPos.getY() + magicNumber, paint);
            cursorPos.setY(cursorPos.getY() - labelStep);
        }
    }

    private void drawXAxis(Canvas canvas) {
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(lineWidth);
        canvas.drawLine(startPointXAxis.getX(), startPointXAxis.getY(), endPointXAxis.getX(), endPointXAxis.getY(), paint);
        drawLabelOnXAxis(canvas);
    }

    private void drawLabelOnXAxis(Canvas canvas) {
        int labelsCount = Math.round((SystemInformation.DISPLAY_WIDTH - startPointXAxis.getX()) / labelStep);
        SystemInformation.COUNT_LABEL_BY_X_AXIS = labelsCount;
        int magicNumber = 7; // :) ToDo Rename
        Point cursorPos = new Point(startPointXAxis.getX() + labelStep, startPointXAxis.getY());
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine(cursorPos.getX(), cursorPos.getY() - labelHeight, cursorPos.getX(), cursorPos.getY() + labelHeight, paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            canvas.drawText(getString(i + 1), cursorPos.getX() - magicNumber, cursorPos.getY() + labelHeight + textWidth, paint);
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
