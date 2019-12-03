package com.x4oc.adventofcode2019.day03;

public class Point implements Comparable<Point>{
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
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
        int result = getX();
        result = 31 * result + getY();
        return result;
    }

    @Override
    public String toString() {
        return "("+x+","+y+")";
    }

    @Override
    public int compareTo(Point p) {
        return this.manhattanDistance() - p.manhattanDistance();
    }

    public int manhattanDistance(){
        return Math.abs(this.getX()) + Math.abs(this.getY());
    }
}
