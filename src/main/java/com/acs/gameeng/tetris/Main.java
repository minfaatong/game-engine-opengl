package com.acs.gameeng.tetris;

public class Main {
    public static void main(String[] args) {
        TetrisGame tetrisGame = new TetrisGame(160, 100, 8, 8, false);
        tetrisGame.start();
    }
}
