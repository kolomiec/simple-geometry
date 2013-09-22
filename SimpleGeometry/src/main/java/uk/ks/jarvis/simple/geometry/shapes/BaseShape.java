package uk.ks.jarvis.simple.geometry.shapes;

/**
 * Created by oem on 9/14/13.
 */
public abstract class BaseShape implements Shape {

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Shape that = (Shape) obj;

        if (this.getLabel() != that.getLabel()) return false;

        return true;
    }

    @Override
    public String getLabel() {
        return this.getLabel();
    }
}
