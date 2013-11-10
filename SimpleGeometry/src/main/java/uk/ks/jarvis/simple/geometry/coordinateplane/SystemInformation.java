package uk.ks.jarvis.simple.geometry.coordinateplane;

import uk.ks.jarvis.simple.geometry.holders.BaseHolder;

/**
 * Created by sayenko on 8/10/13.
 */
public class SystemInformation {
    public static int DISPLAY_WIDTH = 0;
    public static int DISPLAY_HEIGHT = 0;
    public static int COUNT_LABEL_BY_X_AXIS = 20;
    public static int COUNT_LABEL_BY_Y_AXIS = 20;

    public static void initSystemInformation(BaseHolder baseHolder) {
        SystemInformation.DISPLAY_WIDTH = baseHolder.getWidth();
        SystemInformation.DISPLAY_HEIGHT = baseHolder.getHeight();
    }

    public static void setDISPLAY_WIDTH(int width) {
        DISPLAY_WIDTH = width;
    }

    public static void setDISPLAY_HEIGHT(int height) {
        DISPLAY_HEIGHT = height;
    }
}
