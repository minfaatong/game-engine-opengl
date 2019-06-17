package com.acs.gameeng.base;

import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

public class Sprite {
    private final int width;
    private final int height;
    private final int[] pixelData;

    public Sprite(int width, int height) {

        this.width = width;
        this.height = height;
        this.pixelData = new int[width * height];
    }

    public boolean setPixel(int x, int y, Pixel c) {
        if (x >= 0 && x < width && y >= 0 && y < height)
        {
            pixelData[y*width + x] = c.getRGB();
            return true;
        }
        else
            return false;
    }

    public IntBuffer getData() {
        IntBuffer bb = BufferUtils.createIntBuffer(width * height);

        bb.put(pixelData).rewind();

        return bb;
    }

    public void clear(Pixel color){
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
}
