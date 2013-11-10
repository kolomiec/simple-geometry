package uk.ks.jarvis.simple.geometry.beans;

/**
 * Created by sayenko on 7/14/13.
 */
public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setXY(double x,double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Point that = (Point) obj;

        return this.getX()==that.getX() && this.getY()==that.getY();
    }

    public boolean nearlyEquals(Point point) {
        int delta = 8;
        return (point.getX() < this.getX() + delta) && (point.getX() > this.getX() - delta) &&
                (point.getY() < this.getY() + delta) && (point.getY() > this.getY() - delta);
    }
}
