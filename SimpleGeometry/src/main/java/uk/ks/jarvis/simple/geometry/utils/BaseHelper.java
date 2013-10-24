package uk.ks.jarvis.simple.geometry.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import uk.ks.jarvis.simple.geometry.beans.Point;


/**
 * Created with IntelliJ IDEA.
 * User: ksk
 * Date: 17.03.13
 * Time: 18:45
 * To change this template use File | Settings | File Templates.
 */
public class BaseHelper {
    public static Paint getLabelPaint(int color) {
        Paint labelPaint = new Paint();
        labelPaint.setColor(color);
        labelPaint.setTextSize(35.0f);
        return labelPaint;
    }

    public static int getRandomColor() {
        Random rand = new Random();
        return Color.argb(250, rand.nextInt(156) + 50, rand.nextInt(156) + 50, rand.nextInt(156) + 50);// тгьиук to constant
    }

    public static double getLength(Point point1, Point point2) {
        return Math.sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
    }

    public static void drawTextWithShadow(Canvas canvas, String label, float x, float y) {
        canvas.drawText(label, x + 1, y + 2, BaseHelper.getLabelPaint(Color.argb(150, 0, 0, 0)));
        canvas.drawText(label, x, y, BaseHelper.getLabelPaint(ColorTheme.LIGHT_COLOR));
    }

    public static void setPoint(Point point, float x, float y) {
        point.setX(x);
        point.setY(y);
    }

    public static void setPoint(Point point1, Point point2) {
        point1.setX(point2.getX());
        point1.setY(point2.getY());
    }


}
