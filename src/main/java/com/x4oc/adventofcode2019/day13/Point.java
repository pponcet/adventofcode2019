package com.x4oc.adventofcode2019.day13;

public class Point {
    public static final Point nullPoint = new Point(-1, -1);
    private final long x;
    private final long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;

        Point point = (Point) o;

        if (getX() != point.getX()) return false;
        return getY() == point.getY();

    }

    @Override
    public int hashCode() {
        int result = (int) (getX() ^ (getX() >>> 32));
        result = 31 * result + (int) (getY() ^ (getY() >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }
}
