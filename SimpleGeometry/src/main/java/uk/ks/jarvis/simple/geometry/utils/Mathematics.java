package uk.ks.jarvis.simple.geometry.utils;

import static java.lang.Math.PI;

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
}
