package com.acs.gameeng.pacman;

import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Pixel;
import com.acs.gameeng.base.Sprite;

public class Pacman extends ACSGameEngine {

  private char[][] board;

  //  = new char[27][32];
  protected Pacman(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean fullScreen, boolean useRetina) {
    super(screenWidth, screenHeight, pixelWidth, pixelHeight, "Pac-Man", fullScreen, useRetina);
    board = new char[][]{
        {'r', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'q', 'w', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 't'},
        {'{', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '|', '|', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '}'},
        {'{', '.', 's', '-', '-', 'd', '.', 's', '-', '-', '-', 'd', '.', '|', '|', '.', 's', '-', '-', '-', 'd', '.', 's', '-', '-', 'd', '.', '}'},
        {'{', '.', '|', ' ', ' ', '|', '.', '|', ' ', ' ', ' ', '|', '.', '|', '|', '.', '|', ' ', ' ', ' ', '|', '.', '|', ' ', ' ', '|', '.', '}'},
        {'{', '.', 's', '-', '-', 'd', '.', 's', '-', '-', '-', 'd', '.', '|', '|', '.', 's', '-', '-', '-', 'd', '.', 's', '-', '-', 'd', '.', '}'},
        {'{', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '.', '}'},
        {'{', '.', 's', '-', '-', 'd', '.', 's', 'd', '.', 's', '-', '-', '-', '-', '-', '-', 'd', '.', 's', 'd', '.', 's', '-', '-', 'd', '.', '}'},
        {'{', '.', 's', '-', '-', 'd', '.', '|', '|', '.', 's', '-', '-', '-', '-', '-', '-', 'd', '.', '|', '|', '.', 's', '-', '-', 'd', '.', '}'},
    };
  }

  @Override
  public boolean onUserCreate() {
    return true;
  }

  @Override
  public boolean onUserUpdate(float elapsedTime) {
    clear(Pixel.BLACK);

    drawBoard();
//    int size = 20;

//    drawCircle(100,100,size, Pixel.WHITE);

//    drawArc(20,20, 10,0, Math.toRadians(90), true);

    Sprite s = Sprite.load("/Users/peteriskandar/workspace/game-engine-opengl/sprites/pacman/corner.spr");
    drawSprite(0,0, s,1);
    return true;
  }



  private void drawBoard() {
    int x = 0;
    int y = 0;
    int size = 10;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        char item = board[i][j];
        x = (j * size);
        y = i * size;
        fillSquare(x, y, size, Pixel.BLACK);
        switch (item) {
          case '-':
            drawLine(x, y, x + 10, y, Pixel.BLUE);
            drawLine(x, y + (size / 2), x + 10, y + (size / 2), Pixel.BLUE);

            break;
          case 's':
          case 'd':
            drawSquare(x, y, size, Pixel.BLUE);
            break;
          case '{':
            drawLine(x, y, x, y + size, Pixel.BLUE);
            drawLine(x + (size / 2), y, x + (size / 2), y + size, Pixel.BLUE);

            break;
          case '}':
            drawLine(x + size, y, x + size, y + size, Pixel.BLUE);
            drawLine(x + (size / 2), y, x + (size / 2), y + size, Pixel.BLUE);

            break;
          case 'r':
            drawCircle(x + size, y + size, size, Pixel.BLUE);
            drawCircle(x + size + 4, y + size + 4, size, Pixel.BLUE);

//            drawLine(x + (size / 2), y + size, x + size, y + (size / 2), Pixel.BLUE);
            break;

          case 'q':
            drawSquare(x, y, size, Pixel.GREEN);
            break;
          case 'w':
            drawSquare(x, y, size, Pixel.DARK_YELLOW);
            break;
          case 't':
            drawLine(x, y, x + size, y + size, Pixel.BLUE);
            drawLine(x, y + (size / 2), x + (size / 2), y + size, Pixel.BLUE);
//
//            drawCircle(x, size, size, Pixel.BLUE);
//            drawCircle(size + 4, size + 4, size, Pixel.BLUE);

            break;
          case '.':
            fillSquare(x + ((size / 2) - 1), y + ((size / 2) - 1), 2, Pixel.WHITE);
            break;
        }
      }
    }
  }

  private void drawLeftCorner() {

  }

  @Override
  public boolean onUserDestroy() {
    return false;
  }

  public static void main(String[] args) {
    int pixelDim = 2;
    Pacman acsGameEngine = new Pacman(281, 360, pixelDim, pixelDim, false, true);

    acsGameEngine.start();


  }
}
