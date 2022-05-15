package collec_class;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private long x;
    private double y;
    private static final long serialVersionUID = 101L;
    public long getX() {
        return x;
    }
    public void setX(long x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public String toString() {
        return("Coordinates = " + this.x + " "+ this.y );
    }


}
