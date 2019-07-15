package com.acs.gameeng.base;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.abs;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DECAL;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_ENV_MODE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexEnvf;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTexSubImage2D;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class ACSGameEngine {

  private final int screenWidth;
  private final int screenHeight;
  private final int pixelWidth;
  private final int pixelHeight;
  private final boolean fullscreen;
  private final float pixelX;
  private final float pixelY;
  private final boolean useRetina;
  private Sprite defaultDrawTarget;
  private Sprite drawTarget;
  private boolean atomActive;
  private String title;

  //  static std::map<uint16_t, uint8_t> mapKeys;
  private static Map<Integer, Key> mapKeys = new HashMap<>();

  private long window;
  private int viewWidth;
  private int viewHeight;
  private int viewX;
  private int viewY;
  private int windowWidth;
  private int windowHeight;
  private float fSubPixelOffsetX = 0.0f;
  private float fSubPixelOffsetY = 0.0f;
  private int scaleFactor = 1;

  GLFWKeyCallback keyCallback;

  //    private boolean[] keyNewState = new boolean[300];
//    private boolean[] keyOldState = new boolean[300];
  private Map<Integer, Boolean> keyNewStateMap = new HashMap<>();
  private Map<Integer, Boolean> keyOldStateMap = new HashMap<>();
  private Map<Key, HardwareButton> keyHardwareButtonMap = new HashMap<>();

  public abstract boolean onUserCreate();

  public abstract boolean onUserUpdate(float fElapsedTime);

  public abstract boolean onUserDestroy();

  protected ACSGameEngine(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, String title, boolean fullScreen, boolean useRetina) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.pixelWidth = pixelWidth;
    this.pixelHeight = pixelHeight;
    this.title = title;
    this.fullscreen = fullScreen;

    pixelX = 2.0f / (float) (screenWidth);
    pixelY = 2.0f / (float) (screenHeight);
    this.useRetina = useRetina;
    this.scaleFactor = useRetina ? 2 : 1;

    if (pixelWidth == 0 || pixelHeight == 0 || screenWidth == 0 || screenHeight == 0)
      throw new IllegalArgumentException();

    // Load the default font sheet
//        olc_ConstructFontSheet();

    this.defaultDrawTarget = new Sprite(this.screenWidth, this.screenHeight);
    this.setDrawTarget(null);
  }


  public int start() {

    if (!windowCreate()) {
      return 1;
    }

    atomActive = true;
    loop();
    return 0;
  }

  void setSubPixelOffset(float ox, float oy) {
    fSubPixelOffsetX = ox * this.pixelX;
    fSubPixelOffsetY = oy * this.pixelY;
  }


  private void loop() {
    GL.createCapabilities();

    glViewport(this.viewX, this.viewY, this.viewWidth * this.scaleFactor, this.viewHeight * this.scaleFactor);

    glEnable(GL_TEXTURE_2D);
    int tex = glGenTextures();
    glBindTexture(GL_TEXTURE_2D, tex);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);

    // Copy pixel array into texture
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.screenWidth, this.screenHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, this.drawTarget.getData());

    glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

    // Create user resources as part of this thread
    if (!onUserCreate())
      atomActive = false;


    Instant tp1 = Instant.now();
    Instant tp2 = Instant.now();
    // Run the rendering loop until the user has attempted to close
    // the window or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window) && atomActive) {

      while (!glfwWindowShouldClose(window) && atomActive) {
        tp2 = Instant.now();
        long elapsedTime = ChronoUnit.MILLIS.between(tp1, tp2);

        tp1 = tp2;

        // Handle User Input - Keyboard
        for (int i = 0; i < Key.values().length; i++) {
          Key k = Key.values()[i];
          keyHardwareButtonMap.get(k).pressed = false;
          keyHardwareButtonMap.get(k).released = false;

          if (keyNewStateMap.get(k.getValue()) != keyOldStateMap.get(k.getValue())) {
            if (keyNewStateMap.get(k.getValue())) {
              keyHardwareButtonMap.get(k).pressed = !keyHardwareButtonMap.get(k).held;
              keyHardwareButtonMap.get(k).held = true;
            } else {
              keyHardwareButtonMap.get(k).released = true;
              keyHardwareButtonMap.get(k).held = false;
            }
          }

          keyOldStateMap.replace(k.getValue(), keyNewStateMap.get(k.getValue()));
        }

        if (!onUserUpdate(elapsedTime / 1000.0f))
          atomActive = false;

        glViewport(this.viewX, this.viewY, this.viewWidth * this.scaleFactor, this.viewHeight * this.scaleFactor);

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, this.screenWidth, this.screenHeight, GL_RGBA, GL_UNSIGNED_BYTE, this.drawTarget.getData());

        // Display texture on screen
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f);
        glVertex3f(-1.0f + (fSubPixelOffsetX), -1.0f + (fSubPixelOffsetY), 0.0f);
        glTexCoord2f(0.0f, 0.0f);
        glVertex3f(-1.0f + (fSubPixelOffsetX), 1.0f + (fSubPixelOffsetY), 0.0f);
        glTexCoord2f(1.0f, 0.0f);
        glVertex3f(1.0f + (fSubPixelOffsetX), 1.0f + (fSubPixelOffsetY), 0.0f);
        glTexCoord2f(1.0f, 1.0f);
        glVertex3f(1.0f + (fSubPixelOffsetX), -1.0f + (fSubPixelOffsetY), 0.0f);
        glEnd();

        glfwSwapBuffers(window); // swap the color buffers

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

        // Allow the user to free resources if they have overrided the destroy function
        if (onUserDestroy()) {
          // User has permitted destroy, so exit and clean up
        } else {
          // User denied destroy for some reason, so continue running
          atomActive = true;
        }

      }
    }
    keyCallback.free();
    glfwDestroyWindow(window);
    glfwTerminate();

  }


  private boolean windowCreate() {
    init();

    for (int i = 0; i < Key.values().length; i++) {
      Key value = Key.values()[i];
      keyHardwareButtonMap.put(value, new HardwareButton());
      keyNewStateMap.put(value.getValue(), false);
      keyOldStateMap.put(value.getValue(), false);

    }
    return true;
  }


  private void init() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set();

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW");

    // Configure GLFW
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

    this.windowWidth = this.screenWidth * this.pixelWidth;
    this.windowHeight = this.screenHeight * this.pixelHeight;
    this.viewWidth = this.windowWidth;
    this.viewHeight = this.windowHeight;

    updateViewport();
    window = glfwCreateWindow(this.windowWidth, this.windowHeight, this.title, NULL, NULL);
    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window");

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
          glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        } else {
          if (action == 1) {
            ACSGameEngine.this.keyNewStateMap.replace(key, true);
          } else if (action == 0) {
            ACSGameEngine.this.keyNewStateMap.replace(key, false);
          }
        }

      }
    });
    // Get the thread stack and push a new frame
    try (MemoryStack stack = stackPush()) {
      IntBuffer pWidth = stack.mallocInt(1); // int*
      IntBuffer pHeight = stack.mallocInt(1); // int*

      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(window, pWidth, pHeight);

      // Get the resolution of the primary monitor
      GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      // Center the window
      glfwSetWindowPos(
          window,
          (vidmode.width() - pWidth.get(0)) / 2,
          (vidmode.height() - pHeight.get(0)) / 2
      );
    } // the stack frame is popped automatically

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
  }

  public HardwareButton getKey(Key key) {
//    return keyboardState[key.getValue()];
    return keyHardwareButtonMap.get(key);
  }

  void updateViewport() {
    int ww = this.screenWidth * this.pixelWidth;
    int wh = this.screenHeight * this.pixelHeight;
    float wasp = (float) ww / (float) wh;

    viewWidth = windowWidth;
    viewHeight = (int) ((float) viewWidth / wasp);

    if (viewHeight > windowHeight) {
      viewHeight = windowHeight;
      viewWidth = (int) ((float) viewHeight * wasp);
    }

    viewX = (windowWidth - viewWidth) / 2;
    viewY = (windowHeight - viewHeight) / 2;
  }

  void updateWindowSize(int x, int y) {
    this.windowWidth = x;
    this.windowHeight = y;
    updateViewport();

  }

  public int screenWidth() {
    return screenWidth;
  }

  public int screenHeight() {
    return screenHeight;
  }

  public int getDrawTargetWidth() {
    return drawTarget.getWidth();
  }

  public int getDrawTargetHeight() {
    return drawTarget.getHeight();
  }

  public Sprite getDrawTarget() {
    return drawTarget;
  }

  public void setDrawTarget(Sprite target) {
    if (target != null)
      drawTarget = target;
    else
      drawTarget = defaultDrawTarget;
  }

  public boolean draw(int x, int y, Pixel p) {
    drawTarget.setPixel(x, y, p);
    return true;
  }

  protected void clear(Pixel color) {
    drawTarget.clear(color);
  }

  public void drawSquare(int x, int y, int size, Pixel pixel) {
    drawLine(x, y, x + size, y, pixel);
    drawLine(x + size, y, x + size, y + size, pixel);
    drawLine(x, y + size, x + size, y + size, pixel);
    drawLine(x, y, x, y + size, pixel);
  }

  public void fillSquare(int x, int y, int size, Pixel p) {
    int x2 = x + size;
    int y2 = y + size;

    if (x < 0) x = 0;
    if (x >= (int) screenWidth) x = (int) screenWidth;
    if (y < 0) y = 0;
    if (y >= (int) screenHeight) y = (int) screenHeight;

    if (x2 < 0) x2 = 0;
    if (x2 >= (int) screenWidth) x2 = (int) screenWidth;
    if (y2 < 0) y2 = 0;
    if (y2 >= (int) screenHeight) y2 = (int) screenHeight;

    for (int i = x; i < x2; i++)
      for (int j = y; j < y2; j++)
        draw(i, j, p);
  }

  public void drawLine(int x1, int y1, int x2, int y2, Pixel p) {
    int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
    dx = x2 - x1;
    dy = y2 - y1;

    // Line is vertical
    if (dx == 0) {
      if (y2 < y1) {
        int temp = y2;
        y2 = y1;
        y1 = temp;
      }

      for (y = y1; y <= y2; y++) {
        draw(x1, y, p);
      }

      return;
    }

    if (dy == 0) // Line is horizontal
    {
      if (x2 < x1) {
        int temp = x2;
        x2 = x1;
        x1 = temp;
      }

      for (x = x1; x <= x2; x++)
        draw(x, y1, p);
      return;
    }

    dx1 = abs(dx);
    dy1 = abs(dy);

    px = 2 * dy1 - dx1;
    py = 2 * dx1 - dy1;

    if (dy1 <= dx1) {
      if (dx >= 0) {
        x = x1;
        y = y1;
        xe = x2;
      } else {
        x = x2;
        y = y2;
        xe = x1;
      }

      draw(x, y, p);

      for (i = 0; x < xe; i++) {
        x = x + 1;
        if (px < 0)
          px = px + 2 * dy1;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) y = y + 1;
          else y = y - 1;
          px = px + 2 * (dy1 - dx1);
        }
        draw(x, y, p);
      }
    } else {
      if (dy >= 0) {
        x = x1;
        y = y1;
        ye = y2;
      } else {
        x = x2;
        y = y2;
        ye = y1;
      }

      draw(x, y, p);

      for (i = 0; y < ye; i++) {
        y = y + 1;
        if (py <= 0)
          py = py + 2 * dx1;
        else {
          if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) x = x + 1;
          else x = x - 1;
          py = py + 2 * (dx1 - dy1);
        }
        draw(x, y, p);
      }
    }
  }

  public void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Pixel p) {
    drawLine(x1, y1, x2, y2, p);
    drawLine(x2, y2, x3, y3, p);
    drawLine(x3, y3, x1, y1, p);

  }

  public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Pixel p) {
    drawTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3, p);
  }

  public void fillTriangle(float x1, float y1, float x2, float y2, float x3, float y3, Pixel p) {
    fillTriangle((int) x1, (int) y1, (int) x2, (int) y2, (int) x3, (int) y3, p);
  }

  //    http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html
  public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Pixel p) {
    if (y1 > y2) {
      int temp1 = y1;
      y1 = y2;
      y2 = temp1;

      int temp2 = x1;
      x1 = x2;
      x2 = temp2;
    }

    if (y1 > y3) {
      int temp1 = y1;
      y1 = y3;
      y3 = temp1;

      int temp2 = x1;
      x1 = x3;
      x3 = temp2;
    }

    if (y2 > y3) {
      int temp1 = y2;
      y2 = y3;
      y3 = temp1;

      int temp2 = x2;
      x2 = x3;
      x3 = temp2;
    }
    if (y2 == y3) {
      fillBottomFlatTriangle(x1, y1, x2, y2, x3, y3, p);
    } else if (y1 == y2) {
      fillTopFlatTriangle(x1, y1, x2, y2, x3, y3, p);
    } else {

      int x4 = (int) (x1 + ((float) (y2 - y1) / (float) (y3 - y1)) * (x3 - x1));
      int y4 = y2;

      fillBottomFlatTriangle(x1, y1, x2, y2, x4, y4, p);
      fillTopFlatTriangle(x2, y2, x4, y4, x3, y3, p);
    }

  }

  private void fillBottomFlatTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Pixel p) {
    float invslope1 = (float) (x2 - x1) / (float) (y2 - y1);
    float invslope2 = (float) (x3 - x1) / (float) (y3 - y1);

    float curx1 = x1;
    float curx2 = x1;

    for (int scanlineY = y1; scanlineY <= y2; scanlineY++) {
      drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, p);
      curx1 += invslope1;
      curx2 += invslope2;
    }
  }

  private void fillTopFlatTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Pixel p) {
    float invslope1 = (float) (x3 - x1) / (float) (y3 - y1);
    float invslope2 = (float) (x3 - x2) / (float) (y3 - y2);

    float curx1 = x3;
    float curx2 = x3;

    for (int scanlineY = y3; scanlineY > y1; scanlineY--) {
      drawLine((int) curx1, scanlineY, (int) curx2, scanlineY, p);
      curx1 -= invslope1;
      curx2 -= invslope2;
    }
  }

  private void eightWaySymmetricPlot(int xc, int yc, int x, int y, Pixel p) {
    draw(x + xc, y + yc, p);
    draw(x + xc, -y + yc, p);
    draw(-x + xc, -y + yc, p);
    draw(-x + xc, y + yc, p);
    draw(y + xc, x + yc, p);
    draw(y + xc, -x + yc, p);
    draw(-y + xc, -x + yc, p);
    draw(-y + xc, x + yc, p);
  }

  private void bresenhamCircle(int xc, int yc, int r, Pixel p) {
    int x = 0, y = r, d = 3 - (2 * r);
    eightWaySymmetricPlot(xc, yc, x, y, p);

    while (x <= y) {
      if (d <= 0) {
        d = d + (4 * x) + 6;
      } else {
        d = d + (4 * x) - (4 * y) + 10;
        y = y - 1;
      }
      x = x + 1;
      eightWaySymmetricPlot(xc, yc, x, y, p);
    }
  }

  public void drawCircle(int centerX, int centerY, int radius, Pixel p) {
    bresenhamCircle(centerX, centerY, radius, p);
  }
  public void drawSprite(int x, int y, Sprite sprite, int scale) {
    if (sprite == null) {
      return;
    }

    if (scale > 1)
    {
      for (int i = 0; i < sprite.getWidth(); i++)
        for (int j = 0; j < sprite.getHeight(); j++)
          for (int is = 0; is < scale; is++)
            for (int js = 0; js < scale; js++)
              draw(x + (i*scale) + is, y + (j*scale) + js, sprite.getPixel(i, j));
    }
    else
    {
      for (int i = 0; i < sprite.getWidth(); i++)
        for (int j = 0; j < sprite.getHeight(); j++)
          draw(x + i, y + j, sprite.getPixel(i, j));
    }
  }

  public void drawArc(int posX, int posY, int radius, double start, double end, boolean clockwise) {

    start = Math.toRadians(Math.round(Math.toDegrees(start)));
    end = Math.toRadians(Math.round(Math.toDegrees(end)));

    if (start >= Math.PI * 2) {
      start = Math.toRadians(Math.toDegrees(start) % 360);
    }

    if (end >= Math.PI * 2) {
      end = Math.toRadians(Math.toDegrees(end) % 360);
    }

    if (!clockwise) {
      double s = start;
      start = end;
      end = s;
    }

    int[][] xy = {{0, 0}, {0, 0}};

    List<Integer> q_x = new ArrayList<>();
    List<Integer> q_y = new ArrayList<>();

    List<Integer> q = new ArrayList<>();

    int j = 0;
    for (double i : new double[]{start, end}) {
      double angle = i;
      if (angle < Math.PI / 2) {
        q.add(1);

        q_x.add((int) (Math.round((Math.sin(angle) * radius))));
        q_y.add((int) (Math.round((Math.cos(angle) * radius))));

        if (j == 1 && angle == 0) {
          xy[1] = new int[]{0, -radius}; //# 90 degrees
        }
      } else if (angle >= Math.PI / 2 && angle < Math.PI) {
        q.add(2);

        q_x.add((int) (Math.round((Math.sin(angle - Math.PI / 2) * radius))));
        q_y.add((int) (Math.round((Math.cos(angle - Math.PI / 2) * radius))));

        if (j == 1 && angle == Math.PI / 2) {
          xy[1] = new int[]{radius, 0}; //# 90 degrees
        }
      } else if (angle >= Math.PI && angle < Math.PI / 2 * 3) {
        q.add(3);
        q_x.add((int) (Math.round((Math.sin(angle - Math.PI) * radius))));
        q_y.add((int) (Math.round((Math.cos(angle - Math.PI) * radius))));
        if (j == 1 && angle == Math.PI) {
          xy[1] = new int[]{0, radius};
        }
      } else {
        q.add(4);

        q_x.add((int) (Math.round((Math.sin(angle - Math.PI / 2 * 3) * radius))));
        q_y.add((int) (Math.round((Math.cos(angle - Math.PI / 2 * 3) * radius))));

        if (j == 1 && angle == Math.PI / 2 * 3) {
          xy[1] = new int[]{-radius, 0};
        }
      }
      j++;
    }

    List<Integer> quarters = new ArrayList<>();
    int sq = q.get(0);

    while (true) {
      quarters.add(sq);
      if (q.get(1) == sq && start < end || q.get(1) == sq && start > end && !q.get(0).equals(q.get(1))) {
        break; //# we reach the final end quarter
      } else if (q.get(1) == sq && start > end) {
//        quarters.extend([(sq + 1) % 5, (sq + 2) % 5, (sq + 3) % 5, (sq + 4) % 5])
        quarters.add((sq + 1) % 5);
        quarters.add((sq + 2) % 5);
        quarters.add((sq + 3) % 5);
        quarters.add((sq + 4) % 5);
        break;
      } else {
        sq++;
        if (sq > 4) {
          sq = 1;
        }
      }

    }

    int s = 3 - (2 * radius);

    List<Point> points = new ArrayList<>();
    Set<Point> points1 = new HashSet<>();
    Set<Point> points2 = new HashSet<>();
    Set<Point> points3 = new HashSet<>();
    Set<Point> points4 = new HashSet<>();

    int x = 0;
    int y = (int) (Math.round(radius));
    while (x <= y) {
      if (quarters.contains(1)) {
        if (!q.contains(1)) {
          // add all points of the quarter completely
          // first quarter first octant
          points1.add(new Point(x, -y));
          // first quarter 2nd octant
          points1.add(new Point(y, -x));
        } else {
          // start || end point in this quarter?
          if (q.get(0) == 1) {
            // start point
            if (q_x.get(0) <= x && q_y.get(0) >= abs(-y) && quarters.size() > 1 || q_x.get(0) <= x && q_x.get(1) >= x) {
              // first quarter first octant
              points1.add(new Point(x, -y));
              if (-y < xy[0][1]) {
                xy[0] = new int[]{x, -y};
              } else if (-y == xy[0][1]) {
                if (x < xy[0][0]) {
                  xy[0] = new int[]{x, -y};
                }
              }
            }
            if (q_x.get(0) <= y && q_y.get(0) >= x && quarters.size() > 1 || q_x.get(0) <= y && q_x.get(1) >= y && q_y.get(0) >= abs(-x) && q_y.get(1) <= abs(-x)) {
              // first quarter 2nd octant
              points1.add(new Point(y, -x));
              if (-x < xy[0][1]) {
                xy[0] = new int[]{y, -x};
              } else if (-x == xy[0][1]) {
                if (y < xy[0][0]) {
                  xy[0] = new int[]{y, -x};
                }
              }
            }
          }
          if (q.get(1) == 1) {
            // end point
            if (q_x.get(1) >= x && quarters.size() > 1 || q_x.get(0) <= x && q_x.get(1) >= x) {
              // first quarter first octant
              points1.add(new Point(x, -y));
              if (x > xy[1][0]) {
                xy[1] = new int[]{x, -y};
              } else if (x == xy[1][0]) {
                if (-y > xy[1][1]) {
                  xy[1] = new int[]{x, -y};
                }
              }
            }
            if (q_x.get(1) >= y && q_y.get(1) <= x && quarters.size() > 1 || q_x.get(0) <= y && q_x.get(1) >= y && q_y.get(0) >= abs(-x) && q_y.get(1) <= abs(-x)) {
              // first quarter 2nd octant
              points1.add(new Point(y, -x));
              if (y > xy[1][0]) {
                xy[1] = new int[]{y, -x};
              } else if (y == xy[1][0]) {
                if (-x > xy[1][1]) {
                  xy[1] = new int[]{y, -x};
                }
              }
            }
          }
        }
      }
      if (quarters.contains(2)) {
        if (!q.contains(2)) {
          // add all points of the quarter completely
          // second quarter 3rd octant
          points2.add(new Point(y, x));
          // second quarter 4.octant
          points2.add(new Point(x, y));
        } else {
          // start || end point in this quarter?
          if (q.get(0) == 2) {
            // start point
            if (q_x.get(0) >= y && q_y.get(0) <= x && quarters.size() > 1 || q_x.get(0) >= y && q_x.get(1) <= y && q_y.get(0) <= x && q_y.get(1) >= x) {
              // second quarter 3rd octant
              points2.add(new Point(y, x));
              if (y > xy[0][0]) {
                xy[0] = new int[]{y, x};
              } else if (y == xy[0][0]) {
                if (x < xy[0][1]) {
                  xy[0] = new int[]{y, x};
                }
              }
            }
            if (q_x.get(0) >= x && q_y.get(0) <= y && quarters.size() > 1 || q_x.get(0) >= x && q_x.get(1) <= x && q_y.get(0) <= y && q_y.get(1) >= y) {
              // second quarter 4.octant
              points2.add(new Point(x, y));
              if (x > xy[0][0]) {
                xy[0] = new int[]{x, y};
              } else if (x == xy[0][0]) {
                if (y < xy[0][1]) {
                  xy[0] = new int[]{x, y};
                }
              }
            }
          }
          if (q.get(1) == 2) {
            // end point
            if (q_x.get(1) <= y && q_y.get(1) >= x && quarters.size() > 1 || q_x.get(0) >= y && q_x.get(1) <= y && q_y.get(0) <= x && q_y.get(1) >= x) {
              // second quarter 3rd octant
              points2.add(new Point(y, x));
              if (x > xy[1][1]) {
                xy[1] = new int[]{y, x};
              } else if (x == xy[1][1]) {
                if (y < xy[1][0]) {
                  xy[1] = new int[]{y, x};
                }
              }
            }
            if (q_x.get(1) <= x && q_y.get(1) >= y && quarters.size() > 1 || q_x.get(0) >= x && q_x.get(1) <= x && q_y.get(0) <= y && q_y.get(1) >= y) {
              // second quarter 4.octant
              points2.add(new Point(x, y));
              if (y > xy[1][1]) {
                xy[1] = new int[]{x, y};
              } else if (x == xy[1][1]) {
                if (x < xy[1][0]) {
                  xy[1] = new int[]{x, y};
                }
              }
            }
          }
        }
      }
      if (quarters.contains(3)) {
        if (!q.contains(3)) {
          // add all points of the quarter completely
          // third quarter 5.octant
          points3.add(new Point(-x, y));
          // third quarter 6.octant
          points3.add(new Point(-y, x));
        } else {
          // start || end point in this quarter?
          if (q.get(0) == 3) {
            // start point
            if (q_x.get(0) <= x && q_y.get(0) >= abs(y) && quarters.size() > 1 || q_x.get(0) <= x && q_x.get(1) >= x) {
              // third quarter 5.octant
              points3.add(new Point(-x, y));
              if (y > xy[0][1]) {
                xy[0] = new int[]{-x, y};
              } else if (y == xy[0][1]) {
                if (-x > xy[0][0]) {
                  xy[0] = new int[]{-x, y};
                }
              }
            }
            if (q_x.get(0) <= y && q_y.get(0) >= x && quarters.size() > 1 || q_x.get(0) <= y && q_x.get(1) >= y && q_y.get(0) >= x && q_y.get(1) <= x) {
              // third quarter 6.octant
              points3.add(new Point(-y, x));
              if (x > xy[0][1]) {
                xy[0] = new int[]{-y, x};
              } else if (x == xy[0][1]) {
                if (-y > xy[0][0]) {
                  xy[0] = new int[]{-y, x};
                }
              }
            }
          }
          if (q.get(1) == 3) {
            // end point
            if (q_x.get(1) >= x && quarters.size() > 1 || q_x.get(0) <= x && q_x.get(1) >= x) {
              // third quarter 5.octant
              points3.add(new Point(-x, y));
              if (-x < xy[1][0]) {
                xy[1] = new int[]{-x, y};
              } else if (-x == xy[1][0]) {
                if (y < xy[1][1]) {
                  xy[1] = new int[]{-x, y};
                }
              }
            }
            if (q_x.get(1) >= y && q_y.get(1) <= x && quarters.size() > 1 || q_x.get(0) <= y && q_x.get(1) >= y && q_y.get(0) >= x && q_y.get(1) <= x) {
              // third quarter 6.octant
              points3.add(new Point(-y, x));
              if (-y < xy[1][0]) {
                xy[1] = new int[]{-y, x};
              } else if (-y == xy[1][0]) {
                if (x < xy[1][1]) {
                  xy[1] = new int[]{-y, x};
                }
              }
            }
          }
        }
      }
      if (quarters.contains(4)) {
        if (!q.contains(4)) {
          // add all points of the quarter completely
          // fourth quarter 7.octant
          points4.add(new Point(-y, -x));
          // fourth quarter 8.octant
          points4.add(new Point(-x, -y));
        } else {
          // start || end point in this quarter?
          if (q.get(0) == 4) {
            // start point
            if (q_x.get(0) >= y && q_y.get(0) <= x && quarters.size() > 1 || q_x.get(0) >= y && q_x.get(1) <= y && q_y.get(0) <= x && q_y.get(1) >= x) {
              // fourth quarter 7.octant
              points4.add(new Point(-y, -x));
              if (-y < xy[0][0]) {
                xy[0] = new int[]{-y, -x};
              } else if (-y == xy[0][0]) {
                if (-x > xy[0][1]) {
                  xy[0] = new int[]{-y, -x};
                }
              }
            }
            if (q_x.get(0) >= x && q_y.get(0) <= abs(-y) && quarters.size() > 1 || q_x.get(0) >= x && q_x.get(1) <= x && q_y.get(0) <= y && q_y.get(1) >= y) {
              // fourth quarter 8.octant
              points4.add(new Point(-x, -y));
              if (-x < xy[0][0]) {
                xy[0] = new int[]{-x, -y};
              } else if (-x == xy[0][0]) {
                if (-y > xy[0][1]) {
                  xy[0] = new int[]{-x, -y};
                }
              }
            }
          }
          if (q.get(1) == 4) {
            // end point
            if (q_x.get(1) <= y && q_y.get(1) >= x && quarters.size() > 1 || q_x.get(0) >= y && q_x.get(1) <= y && q_y.get(0) <= x && q_y.get(1) >= x) {
              // fourth quarter 7.octant
              points4.add(new Point(-y, -x));
              if (-x < xy[1][1]) {
                xy[1] = new int[]{-y, -x};
              } else if (-x == xy[1][1]) {
                if (-y > xy[1][0]) {
                  xy[1] = new int[]{-y, -x};
                }
              }
            }
            if (q_x.get(1) <= x && q_y.get(1) >= abs(-y) && quarters.size() > 1 || q_x.get(0) >= x && q_x.get(1) <= x && q_y.get(0) <= y && q_y.get(1) >= y) {
              // fourth quarter 8.octant
              points4.add(new Point(-x, -y));
              if (-y < xy[1][1]) {
                xy[1] = new int[]{-x, -y};
              } else if (-y == xy[1][1]) {
                if (-x > xy[1][0]) {
                  xy[1] = new int[]{-x, -y};
                }
              }
            }
          }
        }
      }
      if (s < 0) {
        s = s + (4 * x) + 6;
      } else {
        s = s + (4 * (x - y)) + 10;
        y = y - 1;
      }
      x = x + 1;
    }

    if (quarters.contains(1)) {
      //# points1_s.sort()// # if for some reason you need them sorted
      points.addAll(points1);
    }
    if (quarters.contains(2)) {
      //# points2_s.sort()// # if for some reason you need them sorted
      //# points2_s.reverse()// #// # if for some reason you need in right order
      points.addAll(points2);
    }
    if (quarters.contains(3)) {
      //# points3_s.sort()
      //# points3_s.reverse()
      points.addAll(points3);
    }
    if (quarters.contains(4)) {
      //# points4_s.sort()
      points.addAll(points4);
    }

//    System.out.println(points);
    for (Point point : points) {
      draw((int) point.x + posX, (int) point.y + posY, Pixel.WHITE);
    }
  }
}
