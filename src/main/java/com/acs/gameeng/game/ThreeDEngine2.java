package com.acs.gameeng.game;

import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Matrix4x4;
import com.acs.gameeng.base.Mesh;
import com.acs.gameeng.base.Pixel;

public class ThreeDEngine2 extends ACSGameEngine {

  private Mesh meshCube;
  private Matrix4x4 projectionMatrix;

  protected ThreeDEngine2(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen, boolean useRetina) {
    super(screenWidth, screenHeight, pixelWidth, pixelHeight,"", fullScreen, useRetina);
  }

  @Override
  public boolean onUserCreate() {
    meshCube = new Mesh(new float[][] {
        // SOUTH
        { 0.0f, 0.0f, 0.0f,    0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 0.0f },
        { 0.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 0.0f, 0.0f },

        // EAST
        { 1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f },
        { 1.0f, 0.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 0.0f, 1.0f },

        // NORTH
        { 1.0f, 0.0f, 1.0f,    1.0f, 1.0f, 1.0f,    0.0f, 1.0f, 1.0f },
        { 1.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 0.0f, 1.0f },

        // WEST
        { 0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 1.0f,    0.0f, 1.0f, 0.0f },
        { 0.0f, 0.0f, 1.0f,    0.0f, 1.0f, 0.0f,    0.0f, 0.0f, 0.0f },

        // TOP
        { 0.0f, 1.0f, 0.0f,    0.0f, 1.0f, 1.0f,    1.0f, 1.0f, 1.0f },
        { 0.0f, 1.0f, 0.0f,    1.0f, 1.0f, 1.0f,    1.0f, 1.0f, 0.0f },

        // BOTTOM
        { 1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f },
        { 1.0f, 0.0f, 1.0f,    0.0f, 0.0f, 0.0f,    1.0f, 0.0f, 0.0f },
    });

    // projection matrix
    double near = 0.1;
    double far = 1000.0;
    double fieldOfView = 90.0;
    double aspectRatio = screenHeight()/screenWidth();
    double fieldOfViewRadians = 1.0 / Math.tan(fieldOfView * 0.5 / 180.0 * Math.PI);

    projectionMatrix = new Matrix4x4();
    projectionMatrix.m[0][0] = aspectRatio * fieldOfViewRadians;
    projectionMatrix.m[1][1] = fieldOfViewRadians;
    //https://youtu.be/ih20l3pJoeU?t=1827

    return true;
  }

  @Override
  public boolean onUserUpdate(float fElapsedTime) {
    clear(Pixel.BLACK);
    return true;
  }

  @Override
  public boolean onUserDestroy() {
    return false;
  }

  public static void main(String[] args) {
    int pixelDim = 2;

    ThreeDEngine2 acsGameEngine = new ThreeDEngine2(256, 240, pixelDim, pixelDim, false, true);

    acsGameEngine.start();


  }
}
