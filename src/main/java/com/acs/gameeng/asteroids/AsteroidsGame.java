package com.acs.gameeng.asteroids;

import com.acs.gameeng.asteroids.model.SpaceObject;
import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Key;
import com.acs.gameeng.base.Pixel;

import java.util.ArrayList;
import java.util.List;

public class AsteroidsGame extends ACSGameEngine {

  List<SpaceObject> asteroids;
  SpaceObject player;


  private AsteroidsGame(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean screen, boolean useRetina) {
    super(screenWidth, screenHeight, pixelWidth, pixelHeight, screen, useRetina);
  }

  @Override
  public boolean onUserCreate() {
    asteroids = new ArrayList<>();
    asteroids.add(new SpaceObject(20, 20, 8, -6, 16, 0));

    player = new SpaceObject(screenWidth() / 2, screenHeight() / 2, 0, 0, 10, 0f);

    return true;
  }

  @Override
  public boolean onUserUpdate(float fElapsedTime) {
    clear(Pixel.BLACK);

    if (getKey(Key.LEFT).held) {
      player.angle -= 5.0f * fElapsedTime;
    }

    if (getKey(Key.RIGHT).held) {
      player.angle += 5.0f * fElapsedTime;
    }

    if (getKey(Key.UP).held) {
      player.dx += Math.sin(player.angle) * 20f * fElapsedTime;
      player.dy += -Math.cos(player.angle) * 20f * fElapsedTime;
    }

    player.x += player.dx * fElapsedTime;
    player.y += player.dy * fElapsedTime;

    float[] wrappedPlayerCoords = wrapCoordinates(player.x, player.y);
    player.x = wrappedPlayerCoords[0];
    player.y = wrappedPlayerCoords[1];

    for (SpaceObject a : asteroids) {
      a.x += a.dx * fElapsedTime;
      a.y += a.dy * fElapsedTime;

      float[] wrapped = wrapCoordinates(a.x, a.y);
      a.x = wrapped[0];
      a.y = wrapped[1];

      for (int x = 0; x < a.size; x++) {
        for (int y = 0; y < a.size; y++) {
          draw((int) a.x + x, (int) a.y + y, Pixel.RED);
        }
      }
    }

    float[] mx = {0, -2.5f, 2.5f};
    float[] my = {-5.5f, 2.5f, 2.5f};

    float[] sx = new float[3];
    float[] sy = new float[3];

    for (int i = 0; i < 3; i++) {
      sx[i] = (float) (mx[i] * Math.cos(player.angle) - my[i] * Math.sin(player.angle));
      sy[i] = (float) (mx[i] * Math.sin(player.angle) + my[i] * Math.cos(player.angle));
    }

    //Translate
    for (int i = 0; i < 3; i++) {
      sx[i] = sx[i] + player.x;
      sy[i] = sy[i] + player.y;
    }

    // Draw Closed Poly
    for (int i = 0; i < 4; i++) {
      int j = (i + 1);
      drawLine((int) sx[i % 3], (int) sy[i % 3], (int) sx[j % 3], (int) sy[j % 3], Pixel.WHITE);
    }

    return true;
  }

//  private void DrawWireFrameModel(){
//
//    for (int i = 0; i < 3; i++) {
//      sx[i] = (float) (mx[i] * Math.cos(player.angle) - my[i] * Math.sin(player.angle));
//      sy[i] = (float) (mx[i] * Math.sin(player.angle) + my[i] * Math.cos(player.angle));
//    }
//
//    //Translate
//    for (int i = 0; i < 3; i++) {
//      sx[i] = sx[i] + player.x;
//      sy[i] = sy[i] + player.y;
//    }
//
//    // Draw Closed Poly
//    for (int i = 0; i < 4; i++) {
//      int j = (i + 1);
//      drawLine((int) sx[i % 3], (int) sy[i % 3], (int) sx[j % 3], (int) sy[j % 3], Pixel.WHITE);
//    }
//  }

  private float[] wrapCoordinates(float ix, float iy) {
    float[] outputs = new float[2];
    outputs[0] = ix;
    outputs[1] = iy;

    if (ix < 0.0f) {
      outputs[0] = ix + (float) screenWidth();
    }

    if (ix >= (float) screenWidth()) {
      outputs[0] = ix - (float) screenWidth();
    }

    if (iy < 0.0f) {
      outputs[1] = iy + (float) screenHeight();
    }

    if (iy >= (float) screenHeight()) {
      outputs[1] = iy - (float) screenHeight();
    }

    return outputs;
  }


  @Override
  public boolean onUserDestroy() {
    return false;
  }

  public static void main(String[] args) {
    boolean useRetina = false;
    for (String s : args) {
      if (s.startsWith("--useRetina")) {
        String[] split = s.split("=");
        if (split[1].equals("true")) {
          useRetina = true;
        }
      }
    }
    AsteroidsGame tetrisGame = new AsteroidsGame(160, 100, 4, 4, false, useRetina);
    tetrisGame.start();
  }


  @Override
  public boolean draw(int x, int y, Pixel p) {
    float fx, fy;
    float[] outputs = wrapCoordinates(x, y);
    fx = outputs[0];
    fy = outputs[1];

    return super.draw((int) fx, (int) fy, p);
  }
}
