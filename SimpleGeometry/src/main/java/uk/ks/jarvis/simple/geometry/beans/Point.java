package uk.ks.jarvis.simple.geometry.beans;

/**
 * Created by sayenko on 7/14/13.
 */
public class Point {

    private Float x;
    private Float y;

    public Point(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Point that = (Point) obj;

        return this.getX().equals(that.getX()) && this.getY().equals(that.getY());
    }

    public boolean nearlyEquals(Point point) {
        int delta = 8;
        return (point.getX() < this.getX() + delta) && (point.getX() > this.getX() - delta) &&
                (point.getY() < this.getY() + delta) && (point.getY() > this.getY() - delta);
    }
}
