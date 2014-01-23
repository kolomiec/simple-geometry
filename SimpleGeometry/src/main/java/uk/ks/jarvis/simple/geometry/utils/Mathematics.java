package uk.ks.jarvis.simple.geometry.utils;

import uk.ks.jarvis.simple.geometry.beans.Point;
import uk.ks.jarvis.simple.geometry.shapes.Dot;
import uk.ks.jarvis.simple.geometry.shapes.Line;

import static java.lang.Math.PI;
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

    public static Point getProjectionDotOnLine(Dot dot, Line line) {
        double x1 = dot.getPoint().getX();
        double y1 = dot.getPoint().getY();
        double x2 = line.getSecondPoint().getX();
        double y2 = line.getSecondPoint().getY();
        double x3 = line.getFirstPoint().getX();
        double y3 = line.getFirstPoint().getY();
        Point p1 = new Point(dot.getPoint().getX(), dot.getPoint().getY());
        Point p2 = new Point(line.getFirstPoint().getX(), line.getFirstPoint().getY());
        Point p3 = new Point(line.getSecondPoint().getX(), line.getSecondPoint().getY());
//        double delta1 = (x3 * (x3 - x2 - x1) + x1 * x2 + y3 * (y3 - y2 - y1) + y1 * y2) /
//                (sqr(x3 - x2) + sqr(y3 - y2)); //todo
        double newLength = (sqr(getLength(p2, p3) - sqr(getLength(p1, p3))) + sqr(getLength(p2, p1))) / (2 * (getLength(p2, p3)));
        double delta = newLength / getLength(p2, p3);
        Point answer = new Point((x3 - x2) * delta + x2, (y3 - y2) * delta + y2);
        return answer;
    }
}
