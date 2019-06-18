package com.acs.gameeng.base;

public class Vector3D {
    public float x;
    public float y;
    public float z;
    public float w = 1;

    public Vector3D() {
    }

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
