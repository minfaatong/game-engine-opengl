package com.acs.gameeng.game;

import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Mesh;
import com.acs.gameeng.base.Pixel;

public class ThreeDEngine2 extends ACSGameEngine {

  private Mesh meshCube;


  protected ThreeDEngine2(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen, boolean useRetina) {
    super(screenWidth, screenHeight, pixelWidth, pixelHeight, fullScreen, useRetina);
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
