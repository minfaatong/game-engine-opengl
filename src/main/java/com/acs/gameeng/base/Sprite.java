package com.acs.gameeng.base;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
  private final int width;
  private final int height;
  private final int[] pixelData;
  private final Pixel[] pixelObjectData;

  public Sprite(int width, int height) {

    this.width = width;
    this.height = height;
    this.pixelData = new int[width * height];
    this.pixelObjectData = new Pixel[width * height];
  }

  public boolean setPixel(int x, int y, Pixel c) {
    if (x >= 0 && x < width && y >= 0 && y < height) {
      pixelData[y * width + x] = c.getRGB();
      pixelObjectData[y * width + x] = c;
      return true;
    } else
      return false;
  }

  public IntBuffer getData() {
    IntBuffer bb = BufferUtils.createIntBuffer(width * height);

    bb.put(pixelData).rewind();

    return bb;
  }

  public void clear(Pixel color) {
    for (int i = 0; i < this.pixelData.length; i++) {
      this.pixelData[i] = color.getRGB();
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public static Sprite load(String file) {

    try {
      List<String> lines = Files.readAllLines(Paths.get(file));
      String sizeLine = lines.get(0);
      String[] split = sizeLine.split(",");
      int width = Integer.parseInt(split[0]);
      int height = Integer.parseInt(split[1]);
      Sprite sprite = new Sprite(width, height);


      int y = 0;
      for (int i = 1; i < lines.size(); i++) {
        String line = lines.get(i);
        List<Pixel> pixels = processLine(line);
        for (int x = 0; x < width; x++) {
          sprite.setPixel(x, y, pixels.get(x));
        }
        y++;

      }

      return sprite;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static List<Pixel> processLine(String line) {

    List<Pixel> pixels = new ArrayList<>();
    String[] split = line.split(",");
    for (int i = 0; i < split.length; i++) {
      switch (split[i]) {
        case "x":
          pixels.add(Pixel.WHITE);
          break;
        case "r":
          pixels.add(Pixel.RED);
          break;
      }
    }

    return pixels;

  }

  public Pixel getPixel(int x, int y) {

    if (x >= 0 && x < width && y >= 0 && y < height)
      return pixelObjectData[y * width + x];
    else
      return new Pixel(0, 0, 0, 0);


  }
}
