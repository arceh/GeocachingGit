package hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten;

/**
 * Created by carsten on 03.11.16.
 */

public class Coordinate {

    public double x;
    public double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(String coord) {
        if(!coord.matches("[0-9]*.{1}[0-9]*,{1} [0-9]*.{1}[0-9]*")) {
            throw new IllegalArgumentException();
        }
        String[] coordinates;
        coordinates = coord.split(", ");
        this.x = Float.parseFloat(coordinates[0]);
        this.y = Float.parseFloat(coordinates[1]);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }
}
