package com.acs.gameeng.asteroids;

import com.acs.gameeng.asteroids.model.SpaceObject;
import com.acs.gameeng.base.ACSGameEngine;
import com.acs.gameeng.base.Key;
import com.acs.gameeng.base.Pixel;
import com.acs.gameeng.base.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AsteroidsGame extends ACSGameEngine {

    private List<SpaceObject> asteroids;
    private SpaceObject player;
    private List<SpaceObject> bullets = new ArrayList<>();

    private List<Point> modelShip;
    private List<Point> modelAsteroid;

    private AsteroidsGame(int screenWidth, int screenHeight, int pixelWidth, int pixelHeight, boolean screen, boolean useRetina) {
        super(screenWidth, screenHeight, pixelWidth, pixelHeight, screen, useRetina);
    }

    @Override
    public boolean onUserCreate() {
        asteroids = new ArrayList<>();
        asteroids.add(new SpaceObject(20, 20, 8, -6, 16, 0));

        player = new SpaceObject(screenWidth() / 2, screenHeight() / 2, 0, 0, 10, 0f);

        modelShip = new ArrayList<>();
        modelShip.add(new Point(0, -5.5f));
        modelShip.add(new Point(-2.5f, 2.5f));
        modelShip.add(new Point(2.5f, 2.5f));

        int verts = 20;
        modelAsteroid = new ArrayList<>();
        for (int i = 0; i < verts; i++) {
            float radius = 1.0f;
            float a = ((float) i / (float)verts) * (float)(Math.PI * 2);
            Point point = new Point();
            point.x = (float) (radius * Math.sin(a));
            point.y = (float) (radius * Math.cos(a));
            modelAsteroid.add(point);
        }

        return true;
    }

    @Override
    public boolean onUserUpdate(double fElapsedTime) {
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

        if(getKey(Key.SPACE).released){
            SpaceObject spaceObject = new SpaceObject((int)player.x, (int)player.y, (int)(50.0f * Math.sin(player.angle)), (int)(-50 * Math.cos(player.angle)), 0, 0f);
            bullets.add(spaceObject);
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

            DrawWireFrameModel(modelAsteroid, a.x, a.y, a.angle, a.size, Pixel.BLUE);
        }

        List<SpaceObject> newAsteroids = new ArrayList<>();
        // Update and draw bullets
        for (SpaceObject bullet : bullets) {
            bullet.x += bullet.dx * fElapsedTime;
            bullet.y += bullet.dy * fElapsedTime;

            float[] wrapped = wrapCoordinates(bullet.x, bullet.y);
            bullet.x = wrapped[0];
            bullet.y = wrapped[1];

            draw((int)bullet.x, (int)bullet.y, Pixel.WHITE);

            for (SpaceObject asteroid :
                    asteroids) {
                if(isPointInsideCircle(asteroid.x, asteroid.y, asteroid.size, bullet.x, bullet.y)){
                    bullet.x = -100;
                    if(asteroid.size > 4){
                        //create two
                        double angle1 = Math.random() * (Math.PI * 2);
                        double angle2 = Math.random() * (Math.PI * 2);
                        newAsteroids.add(new SpaceObject((int)asteroid.x,(int) asteroid.y, (int)(10 * Math.sin(angle1)), (int)(10 * Math.cos(angle1)), asteroid.size / 2,0f));
                        newAsteroids.add(new SpaceObject((int)asteroid.x,(int) asteroid.y, (int)(10 * Math.sin(angle2)), (int)(10 * Math.cos(angle2)), asteroid.size / 2,0f));
                    }
                    asteroid.x = -100;
                }
            }
        }

        asteroids.addAll(newAsteroids);

        // remove off screen
        if(bullets.size() > 0){
            for (int i = bullets.size() - 1; i >= 0; i--) {
                SpaceObject bullet = bullets.get(i);
                if(bullet.x < 1 ||
                        bullet.y < 1 ||
                        bullet.x >= screenWidth() ||
                        bullet.y >= screenHeight()){
                    bullets.remove(bullet);
                }
            }
        }

        if(asteroids.size() > 0){
            for (int i = asteroids.size() - 1; i >= 0; i--) {
                SpaceObject asteroid = asteroids.get(i);
                if(asteroid.x < 0 ){
                    asteroids.remove(asteroid);
                }
            }
        }

        DrawWireFrameModel(modelShip, player.x, player.y, player.angle, 1, Pixel.WHITE);

        return true;
    }

    boolean isPointInsideCircle(float cx, float cy, float radius, float x, float y){
        return Math.sqrt((x-cx) * (x-cx) + (y-cy)*(y-cy)) < radius;
    }

    private void DrawWireFrameModel(List<Point> vecModelCoordinates, float x, float y, float rotation, float scaling, Pixel pixel) {

        int vertices = vecModelCoordinates.size();
        List<Point> vecTransformedCoordinates = new ArrayList<>(vertices);

        for (int i = 0; i < vertices; i++) {
            Point point = new Point();
            point.x = (float) (vecModelCoordinates.get(i).x * Math.cos(rotation) - vecModelCoordinates.get(i).y * Math.sin(rotation));
            point.y = (float) (vecModelCoordinates.get(i).x * Math.sin(rotation) + vecModelCoordinates.get(i).y * Math.cos(rotation));
            vecTransformedCoordinates.add(point);
        }

        //scale
        for (int i = 0; i < vertices; i++) {
            vecTransformedCoordinates.get(i).x = vecTransformedCoordinates.get(i).x * scaling;
            vecTransformedCoordinates.get(i).y = vecTransformedCoordinates.get(i).y * scaling;
        }
        //Translate
        for (int i = 0; i < vertices; i++) {
            vecTransformedCoordinates.get(i).x = vecTransformedCoordinates.get(i).x + x;
            vecTransformedCoordinates.get(i).y = vecTransformedCoordinates.get(i).y + y;
        }

        // Draw Closed Poly
        for (int i = 0; i < vertices + 1; i++) {
            int j = (i + 1);
            drawLine((int) vecTransformedCoordinates.get(i % vertices).x,
                    (int) vecTransformedCoordinates.get(i % vertices).y,
                    (int) vecTransformedCoordinates.get(j % vertices).x,
                    (int) vecTransformedCoordinates.get(j % vertices).y,
                    Pixel.WHITE);
        }
    }

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
