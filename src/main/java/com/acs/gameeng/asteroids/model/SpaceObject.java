package com.acs.gameeng.asteroids.model;

public class SpaceObject {
    public float x;
    public float y;
    public float dx;
    public float dy;
    public int size;
    public float angle;

    public SpaceObject(int x, int y, int dx, int dy, int size, float angle) {

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
    }

    @Override
    public String toString() {
        return "SpaceObject{" +
                "x=" + x +
                ", y=" + y +
                ", dx=" + dx +
                ", dy=" + dy +
                ", size=" + size +
                ", angle=" + angle +
                '}';
    }
}
