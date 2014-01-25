package uk.ks.jarvis.simple.geometry.utils;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.shapes.Line;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static uk.ks.jarvis.simple.geometry.utils.BaseHelper.getLength;

/**
 * Created by sayenko on 11/10/13.
 */
public class Mathematics {
    public static double tg(double angle) {
        return Math.tan((angle * PI) / 180);
    }

    public static double sin(double angle) {
        return Math.sin((angle * PI) / 180);
    }

    public static double cos(double angle) {
        return Math.cos((angle * PI) / 180);
    }

    public static double arctg(double tg) {
        return 180 * Math.atan(tg) / PI;
    }

    public static double arcsin(double sin) {
        return 180 * Math.asin(sin) / PI;
    }

    public static double arccos(double cos) {
        return 180 * Math.acos(cos) / PI;
    }

    public static double sqr(double number) {
        return number * number;
    }

    public static long sqr(int number) {
        return number * number;
    }

    public static Point minus(Point A, Point B) {
        return new Point(A.getX() - B.getX(), A.getY() - B.getY());
    }

    public static Point plus(Point A, Point B) {
        return new Point(A.getX() + B.getX(), A.getY() + B.getY());
    }

    public static Point multiply(double k, Point A) {
        return new Point(k * A.getX(), k * A.getY());
    }

    /**
     * Скалярний добуток
     */
    public static double scal_prod(Point A, Point B) {
        return A.getX() * B.getX() + A.getY() * B.getY();
    }


    /**
     * Псевдоскалярний добуток
     */
    public static double cross_prod(Point A, Point B) {
        return A.getX() * B.getY() - A.getY() * B.getX();
    }


    public static double getLengthFromPointToLine(Point point, Line line) {
        Point vector1 = minus(line.getSecondPoint(), line.getFirstPoint());
        Point vector2 = minus(point, line.getFirstPoint());

        return abs(cross_prod(vector1, vector2)) / getLength(line.getSecondPoint(), line.getFirstPoint());
    }

    public static Point getProjectionPointOnLine(Point point, Line line) {//ToDo... :( doesn't work sometimes...
        double A = getLength(point, line.getFirstPoint());
        double B = getLengthFromPointToLine(point, line);

        double newLength = sqrt(sqr(A) - sqr(B));
        double delta = newLength / getLength(line.getFirstPoint(), line.getSecondPoint());

        double x = (line.getSecondPoint().getX() - line.getFirstPoint().getX()) * delta + line.getFirstPoint().getX();
        double y = (line.getSecondPoint().getY() - line.getFirstPoint().getY()) * delta + line.getFirstPoint().getY();
        return new Point(x, y);
    }
}
