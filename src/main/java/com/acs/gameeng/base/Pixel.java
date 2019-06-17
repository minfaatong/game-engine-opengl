package com.acs.gameeng.base;

public class Pixel {

    private int value;

    public Pixel(int r, int g, int b) {
        this(r,g,b, 255);
    }
    public Pixel(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) |
                ((b & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((r & 0xFF) << 0);
    }

    public int getRGB() {
        return value;
    }

    public static final Pixel WHITE = new Pixel(255, 255, 255);
    public static final Pixel GREY = new Pixel(192, 192, 192);
    public static final Pixel DARK_GREY = new Pixel(128, 128, 128);
    public static final Pixel VERY_DARK_GREY = new Pixel(64, 64, 64);
    public static final Pixel RED = new Pixel(255, 0, 0);
    public static final Pixel DARK_RED = new Pixel(128, 0, 0);
    public static final Pixel VERY_DARK_RED = new Pixel(64, 0, 0);
    public static final Pixel YELLOW = new Pixel(255, 255, 0);
    public static final Pixel DARK_YELLOW = new Pixel(128, 128, 0);
    public static final Pixel VERY_DARK_YELLOW = new Pixel(64, 64, 0);
    public static final Pixel GREEN = new Pixel(0, 255, 0);
    public static final Pixel DARK_GREEN = new Pixel(0, 128, 0);
    public static final Pixel VERY_DARK_GREEN = new Pixel(0, 64, 0);
    public static final Pixel CYAN = new Pixel(0, 255, 255);
    public static final Pixel DARK_CYAN = new Pixel(0, 128, 128);
    public static final Pixel VERY_DARK_CYAN = new Pixel(0, 64, 64);
    public static final Pixel BLUE = new Pixel(0, 0, 255);
    public static final Pixel DARK_BLUE = new Pixel(0, 0, 128);
    public static final Pixel VERY_DARK_BLUE = new Pixel(0, 0, 64);
    public static final Pixel MAGENTA = new Pixel(255, 0, 255);
    public static final Pixel DARK_MAGENTA = new Pixel(128, 0, 128);
    public static final Pixel VERY_DARK_MAGENTA = new Pixel(64, 0, 64);
    public static final Pixel BLACK = new Pixel(0, 0, 0);
    public static final Pixel BLANK = new Pixel(0, 0, 0, 0);
}
