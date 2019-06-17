package com.acs.gameeng.asteroids;

import com.acs.gameeng.asteroids.model.SpaceObject;
import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Pixel;

import java.util.ArrayList;
import java.util.List;

public class AsteroidsGame extends ACSGameEngine {

    List<SpaceObject> asteroids;


    private AsteroidsGame(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean screen, boolean useRetina) {
        super(screenWidth, screenHeight, pixelWidth, pixelHeight, screen, useRetina);
    }

    @Override
    public boolean onUserCreate() {
        asteroids = new ArrayList<>();
        asteroids.add(new SpaceObject(20, 20, 8, -6, 16));

        return true;
    }

    @Override
    public boolean onUserUpdate(float fElapsedTime) {
        clear(Pixel.BLACK);
        draw(0,0, Pixel.WHITE);
        for (SpaceObject a : asteroids) {
            a.x += a.dx * fElapsedTime;
            a.y += a.dy * fElapsedTime;

            for (int x = 0; x < a.size; x++) {
                for (int y = 0; y < a.size; y++) {
                    draw((int) a.x + x, (int) a.y + y, Pixel.RED);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onUserDestroy() {
        return false;
    }

    public static void main(String[] args) {
        boolean useRetina = false;
        for (String s : args) {
            if(s.startsWith("--useRetina")){
                String[] split = s.split("=");
                if(split[1].equals("true")){
                    useRetina = true;
                }
            }
        }
        AsteroidsGame tetrisGame = new AsteroidsGame(160, 100, 4, 4, false, useRetina);
        tetrisGame.start();
    }
}
