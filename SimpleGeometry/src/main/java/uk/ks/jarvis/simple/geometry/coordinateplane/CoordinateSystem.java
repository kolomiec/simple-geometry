package uk.ks.jarvis.simple.geometry.coordinateplane;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.holders.BaseHolder;
import uk.ks.jarvis.simple.geometry.utils.ColorTheme;
import uk.ks.jarvis.simple.geometry.utils.Zoom;


/**
 * Created by sayenko on 8/10/13.
 */
public class CoordinateSystem {
    private float labelStep = 20;
    private final int labelHeight = 3;
    private final int lineWidth = 1;
    private final int labelWidth = 2;
    private final int textWidth = 13;
    private Point startPointXAxis = new Point(30f, SystemInformation.DISPLAY_HEIGHT - 20f);
    private Point endPointXAxis = new Point(SystemInformation.DISPLAY_WIDTH - 1f, SystemInformation.DISPLAY_HEIGHT - 20f);
    private Point startPointYAxis = new Point(30f, (SystemInformation.DISPLAY_HEIGHT) - 20f);
    private Point endPointYAxis = new Point(30f, 1f);
    private Point originPoint = new Point(startPointXAxis);
    private Paint paint;
    private Zoom zoom;
    private int delta = 0;

    public CoordinateSystem(BaseHolder baseHolder, Zoom zoom) {
        paint = new Paint();
        paint.setColor(ColorTheme.LIGHT_COLOR);
        this.zoom = zoom;
        baseHolder.getHeight();
    }

    public void draw(Canvas canvas) {
        drawXAxis(canvas);
        drawYAxis(canvas);
    }

    public void changeZoom() {
        labelStep *= zoom.getZoomRatio();
        if (labelStep > 100) {
            labelStep = labelStep / 10;
            delta--;
        }
        if (labelStep < 15) {
            labelStep = labelStep * 10;
            delta++;
        }
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
        Point cursorPos = new Point(startPointYAxis.getX(), startPointXAxis.getY() - labelStep);
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine((float) cursorPos.getX() - labelHeight, (float) cursorPos.getY(), (float) cursorPos.getX() + labelHeight, (float) cursorPos.getY(), paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            String s = getString(((i + 1) * Math.pow(10, delta)));
            canvas.drawText(s, (float) cursorPos.getX() - textWidth * 2 - labelHeight, (float) cursorPos.getY() + 4, paint);
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
        Point cursorPos = new Point(startPointXAxis.getX() + labelStep, startPointXAxis.getY());
        paint.setStrokeWidth(labelWidth);
        paint.setTextSize(textWidth);
        for (int i = 0; i < labelsCount - 1; i++) {
            paint.setColor(Color.GRAY);
            canvas.drawLine((float) cursorPos.getX(), (float) cursorPos.getY() - labelHeight, (float) cursorPos.getX(), (float) cursorPos.getY() + labelHeight, paint);
            paint.setColor(ColorTheme.LIGHT_COLOR);
            String s = getString(((i + 1) * Math.pow(10, delta)));
            canvas.drawText(s, (float) cursorPos.getX() - textWidth, (float) cursorPos.getY() + labelHeight + textWidth, paint);
            cursorPos.setX(cursorPos.getX() + labelStep);
        }
    }

    private String getString(double count) {
        String s;
        if (count >= 1000) {
            s = Double.toString(count);
            s = s.toCharArray()[0] + "." + s.toCharArray()[1] + "E" + delta;
        } else if (count >= 10) {
            long value = Math.round(count);
            s = Long.toString(value);
        } else {
            count = (Math.round(count * 1e10)) / 1e10;
            s = Double.toString(count);
        }

        if (s.length() < 3) {
            s = " " + s;
        }
        if (s.length() < 4) {
            s = " " + s;
        }
        return s;
    }

    public Point getOriginPoint() {
        return originPoint;
    }

    public float getLabelStep() {
        return this.labelStep;
    }

}
