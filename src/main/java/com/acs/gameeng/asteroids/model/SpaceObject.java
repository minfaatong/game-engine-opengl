package com.acs.gameeng.asteroids.model;

public class SpaceObject {
    public float x;
    public float y;
    public float dx;
    public float dy;
    public int size;

    public SpaceObject(int x, int y, int dx, int dy, int size) {

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
    }
}
