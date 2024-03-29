package com.acs.gameeng.base;

public class Matrix4x4 {
    public double[][] m = new double[4][4];

    public Matrix4x4() {
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                m[i][j] = 0;
            }
        }
    }

    public static Matrix4x4 newIdenity(){
        Matrix4x4 matrix4x4 = new Matrix4x4();
        matrix4x4.m[0][0] = 1.0;
        matrix4x4.m[1][1] = 1.0;
        matrix4x4.m[2][2] = 1.0;
        matrix4x4.m[3][3] = 1.0;
        return matrix4x4;
    }

    public static Matrix4x4 rotationZ(double angleRad){
        // Rotation Z
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = Math.cos(angleRad);
        matrix.m[0][1] = Math.sin(angleRad);
        matrix.m[1][0] = Math.sin(angleRad) * -1.0;
        matrix.m[1][1] = Math.cos(angleRad);
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 rotationX(double angleRad){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = Math.cos(angleRad);
        matrix.m[1][2] = Math.sin(angleRad);
        matrix.m[2][1] = Math.sin(angleRad) * -1.0;
        matrix.m[2][2] = Math.cos(angleRad);
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 rotationY(double angleRad){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = Math.cos(angleRad);
        matrix.m[0][2] = Math.sin(angleRad);
        matrix.m[2][0] = Math.sin(angleRad) * -1.0;
        matrix.m[1][1] = 1.0;
        matrix.m[2][2] = Math.cos(angleRad);
        matrix.m[3][3] = 1.0;
        return matrix;
    }

    public static Matrix4x4 translate(double x, double y, double z){
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1.0;
        matrix.m[1][1] = 1.0;
        matrix.m[2][2] = 1.0;
        matrix.m[3][3] = 1.0;
        matrix.m[3][0] = x;
        matrix.m[3][1] = y;
        matrix.m[3][2] = z;
        return matrix;
    }

    public static Matrix4x4 projection(double fov, double aspectRatio, double near, double far){

        double fovRad = 1.0 / Math.tan(fov * 0.5 / 180.0 * 3.14159);

        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = aspectRatio * fovRad;
        matrix.m[1][1] = fovRad;
        matrix.m[2][2] = far / (far - near);
        matrix.m[3][2] = (-far * near) / (far - near);
        matrix.m[2][3] = 1.0;
        matrix.m[3][3] = 0;

        return matrix;
    }

    public static Matrix4x4 multiply(Matrix4x4 m1, Matrix4x4 m2){
        Matrix4x4 matrix = new Matrix4x4();
        for (int c = 0; c < 4; c++)
            for (int r = 0; r < 4; r++)
                matrix.m[r][c] = m1.m[r][0] * m2.m[0][c] + m1.m[r][1] * m2.m[1][c] + m1.m[r][2] * m2.m[2][c] + m1.m[r][3] * m2.m[3][c];
        return matrix;
    }

}
