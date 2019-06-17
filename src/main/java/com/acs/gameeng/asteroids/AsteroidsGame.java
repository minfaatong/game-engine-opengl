package com.acs.gameeng.asteroids;

import com.acs.gameeng.asteroids.model.SpaceObject;
import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Pixel;

import java.util.ArrayList;
import java.util.List;

public class AsteroidsGame extends ACSGameEngine {

    List<SpaceObject> asteroids;


    private AsteroidsGame(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen) {
        super(screenWidth, screenHeight, pixelWidth, pixelHeight, fullScreen);
    }

    @Override
    public boolean onUserCreate() {
        asteroids = new ArrayList<>();
        asteroids.add(new SpaceObject(20, 20, 8, -6, 16));

        return true;
    }

    @Override
    public boolean onUserUpdate(float fElapsedTime) {
        clear(Pixel.WHITE);
        draw(0,0, Pixel.WHITE);
//        System.out.println(fElapsedTime);
//        for (SpaceObject a : asteroids) {
//            a.x += a.dx * fElapsedTime;
//            a.y += a.dy * fElapsedTime;
//
//            for (int x = 0; x < a.size; x++) {
//                for (int y = 0; y < a.size; y++) {
//                    draw((int) a.x + x, (int) a.y + y, Pixel.RED);
//                }
//            }
//        }
        return true;
    }

    @Override
    public boolean onUserDestroy() {
        return false;
    }

    public static void main(String[] args) {
        AsteroidsGame tetrisGame = new AsteroidsGame(160, 100, 4, 4, false);
        tetrisGame.start();
    }
}
