package com.acs.gameeng.game;

import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Matrix4x4;
import com.acs.gameeng.base.Mesh;
import com.acs.gameeng.base.Pixel;
import com.acs.gameeng.base.Triangle;
import com.acs.gameeng.base.Vector3D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class ThreeDEngine2 extends ACSGameEngine {

    private Mesh meshCube;
    private Matrix4x4 projectionMatrix;
    private double theta = 0.0;
    private Vector3D camera;

    protected ThreeDEngine2(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen, boolean useRetina) {
        super(screenWidth, screenHeight, pixelWidth, pixelHeight, fullScreen, useRetina);
    }

    @Override
    public boolean onUserCreate() {
        camera = new Vector3D();
        meshCube = new Mesh("objects/ship.obj");

        // Projection Matrix
        double near = 0.1;
        double far = 1000.0;
        double fov = 90.0;
        double aspectRatio = (double)screenHeight() / (double)screenWidth();
        double fovRad = 1.0 / Math.tan(fov * 0.5 / 180.0 * Math.PI);

        projectionMatrix = new Matrix4x4();
        projectionMatrix.m[0][0] = aspectRatio * fovRad;
        projectionMatrix.m[1][1] = fovRad;
        projectionMatrix.m[2][2] = far / (far - near);
        projectionMatrix.m[3][2] = (-far * near) / (far - near);
        projectionMatrix.m[2][3] = 1.0;
        projectionMatrix.m[3][3] = 0.0;


        return true;
    }

    @Override
    public boolean onUserUpdate(double elapsedTime) {
        clear(Pixel.BLACK);

        Matrix4x4 matrixRotationZ = new Matrix4x4();
        Matrix4x4 matrixRotationX = new Matrix4x4();

        theta += 1.0f * elapsedTime;

        // Rotation Z
        matrixRotationZ.m[0][0] = Math.cos(theta);
        matrixRotationZ.m[0][1] = Math.sin(theta);
        matrixRotationZ.m[1][0] = -Math.sin(theta);
        matrixRotationZ.m[1][1] = Math.cos(theta);
        matrixRotationZ.m[2][2] = 1;
        matrixRotationZ.m[3][3] = 1;

        // Rotation X
        matrixRotationX.m[0][0] = 1;
        matrixRotationX.m[1][1] = Math.cos(theta * 0.5f);
        matrixRotationX.m[1][2] = Math.sin(theta * 0.5f);
        matrixRotationX.m[2][1] = -Math.sin(theta * 0.5f);
        matrixRotationX.m[2][2] = Math.cos(theta * 0.5f);
        matrixRotationX.m[3][3] = 1;

        for (Triangle tri : meshCube.tris) {

            Triangle triangleRotatedZ = new Triangle();
            triangleRotatedZ.points[0] = multiplyMatrixVector(matrixRotationZ, tri.points[0]);
            triangleRotatedZ.points[1] = multiplyMatrixVector(matrixRotationZ, tri.points[1]);
            triangleRotatedZ.points[2] = multiplyMatrixVector(matrixRotationZ, tri.points[2]);

            Triangle triangleRotatedZX = new Triangle();
            triangleRotatedZX.points[0] = multiplyMatrixVector(matrixRotationX, triangleRotatedZ.points[0]);
            triangleRotatedZX.points[1] = multiplyMatrixVector(matrixRotationX, triangleRotatedZ.points[1]);
            triangleRotatedZX.points[2] = multiplyMatrixVector(matrixRotationX, triangleRotatedZ.points[2]);

            Triangle triangleTranslated = Triangle.copy(triangleRotatedZX);

            triangleTranslated.points[0].z = triangleRotatedZX.points[0].z + 8.0;
            triangleTranslated.points[1].z = triangleRotatedZX.points[1].z + 8.0;
            triangleTranslated.points[2].z = triangleRotatedZX.points[2].z + 8.0;


            Vector3D line1 = subtactVector(triangleTranslated.points[1], triangleTranslated.points[0]);
            Vector3D line2 = subtactVector(triangleTranslated.points[2], triangleTranslated.points[0]);
            Vector3D normal = vectorCrossProduct(line1, line2);

            normal = vectorNormalize(normal);

            List<Triangle> trianglesToRaster = new ArrayList<>();

            if (normal.x * (triangleTranslated.points[0].x - camera.x) +
                normal.y * (triangleTranslated.points[0].y - camera.y) +
                normal.z * (triangleTranslated.points[0].z - camera.z) < 0) {


                Vector3D lightDirection = new Vector3D(0,0,-1);
                lightDirection = vectorNormalize(lightDirection);

                double dotProduct = vectorDotProduct(normal, lightDirection);

                triangleTranslated.pixel = getColour(dotProduct);

                Triangle triangleProjected = new Triangle();
                triangleProjected.points[0] = multiplyMatrixVector(projectionMatrix, triangleTranslated.points[0]);
                triangleProjected.points[1] = multiplyMatrixVector(projectionMatrix, triangleTranslated.points[1]);
                triangleProjected.points[2] = multiplyMatrixVector(projectionMatrix, triangleTranslated.points[2]);
                triangleProjected.pixel = triangleTranslated.pixel;
                // scale into view
                triangleProjected.points[0].x += 1.0f;
                triangleProjected.points[0].y += 1.0f;
                triangleProjected.points[1].x += 1.0f;
                triangleProjected.points[1].y += 1.0f;
                triangleProjected.points[2].x += 1.0f;
                triangleProjected.points[2].y += 1.0f;

                triangleProjected.points[0].x *= 0.5f * screenWidth();
                triangleProjected.points[0].y *= 0.5f * screenHeight();
                triangleProjected.points[1].x *= 0.5f * screenWidth();
                triangleProjected.points[1].y *= 0.5f * screenHeight();
                triangleProjected.points[2].x *= 0.5f * screenWidth();
                triangleProjected.points[2].y *= 0.5f * screenHeight();

                trianglesToRaster.add(triangleProjected);
            }

            trianglesToRaster.sort((t1, t2) -> {
                double z1 = (t1.points[0].z + t1.points[1].z + t1.points[2].z) / 3.0f;
                double z2 = (t2.points[0].z + t2.points[1].z + t2.points[2].z) / 3.0f;
                return Double.compare(z2, z1);
            });

            for (Triangle triangle : trianglesToRaster) {
                fillTriangle(triangle, triangle.pixel);
            }

        }
        return true;
    }

    private Pixel getColour(double luminance) {
        int value = (int) (255 * luminance);
        return new Pixel(value, value, value);
    }

    private Vector3D subtactVector(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    private Vector3D vectorCrossProduct(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    private double vectorDotProduct(Vector3D v1, Vector3D v2) {
        return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
    }

    private double vectorLength(Vector3D v) {
        return Math.sqrt(vectorDotProduct(v, v));
    }

    private Vector3D vectorNormalize(Vector3D v) {
        double l = vectorLength(v);
        return new Vector3D(v.x / l, v.y / l, v.z / l);
    }

    @Override
    public boolean onUserDestroy() {
        return false;
    }

    private Vector3D multiplyMatrixVector(Matrix4x4 matrix, Vector3D input) {
        Vector3D output = new Vector3D();


        output.x = input.x * matrix.m[0][0] + input.y * matrix.m[1][0] + input.z * matrix.m[2][0] + matrix.m[3][0];
        output.y = input.x * matrix.m[0][1] + input.y * matrix.m[1][1] + input.z * matrix.m[2][1] + matrix.m[3][1];
        output.z = input.x * matrix.m[0][2] + input.y * matrix.m[1][2] + input.z * matrix.m[2][2] + matrix.m[3][2];
        double w = input.x * matrix.m[0][3] + input.y * matrix.m[1][3] + input.z * matrix.m[2][3] + matrix.m[3][3];

        if (w > 0.0) {
            output.x /= w;
            output.y /= w;
            output.z /= w;
        }
        return output;
    }

    public static void main(String[] args) {
        int pixelDim = 4;
        ThreeDEngine2 acsGameEngine = new ThreeDEngine2(256, 240, pixelDim, pixelDim, false, false);
        acsGameEngine.start();
    }
}
