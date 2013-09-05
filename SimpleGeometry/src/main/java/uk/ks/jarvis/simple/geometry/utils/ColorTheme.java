package uk.ks.jarvis.simple.geometry.utils;

import android.graphics.Color;

/**
 * Created by sayenko on 8/11/13.
 */
public class ColorTheme {
    public static boolean isDarkTheme = true;
    public static boolean isLightTheme = false;
    public static int DARK_COLOR = Color.BLACK;
    public static int LIGHT_COLOR = Color.WHITE;

    public static void setDarkTheme() {
        DARK_COLOR = Color.BLACK;
        LIGHT_COLOR = Color.WHITE;
        isDarkTheme = true;
        isLightTheme = false;
    }

    public static void setLightTheme() {
        DARK_COLOR = Color.WHITE;
        LIGHT_COLOR = Color.BLACK;
        isLightTheme = true;
        isDarkTheme = false;
    }

    public static void changeTheme() {
        if (isDarkTheme) {
            setLightTheme();
        } else if (isLightTheme) {
            setDarkTheme();
        }
    }
}
