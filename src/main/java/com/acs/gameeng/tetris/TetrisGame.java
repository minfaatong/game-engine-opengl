package com.acs.gameeng.tetris;


import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Pixel;

public class TetrisGame extends ACSGameEngine {

    protected TetrisGame(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen) {
        super(screenWidth, screenHeight, pixelWidth, pixelHeight, fullScreen, false);
    }

    @Override
    public boolean onUserCreate() {
        return true;
    }

    int r = 0;
    int g = 0;
    int b = 0;

    @Override
    public boolean onUserUpdate(float fElapsedTime) {
        clear(Pixel.BLACK);

        draw(2,0, new Pixel(255,0,0,255));
        draw(3,0, new Pixel(0,255,0,255));
        draw(4,0, new Pixel(0,0,255,255));
        return true;
    }


    @Override
    public boolean onUserDestroy() {
        return false;
    }
}
