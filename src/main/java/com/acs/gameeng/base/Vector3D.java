package com.acs.gameeng.base;

public class Vector3D {
    public double x;
    public double y;
    public double z;
    public double w = 1;

    public Vector3D() {
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
