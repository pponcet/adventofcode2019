package com.x4oc.adventofcode2019.day13;

public class Tile {
    private final Point position;
    private final long id;

    public Tile(Point position, long id) {
        this.position = position;
        this.id = id;
    }

    public Point getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (getId() != tile.getId()) return false;
        return getPosition() != null ? getPosition().equals(tile.getPosition()) : tile.getPosition() == null;

    }

    @Override
    public int hashCode() {
        int result = getPosition() != null ? getPosition().hashCode() : 0;
        result = 31 * result + (int) (getId() ^ (getId() >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "position=" + position +
                ", id=" + id +
                '}';
    }
}
