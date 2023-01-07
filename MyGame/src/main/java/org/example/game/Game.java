package org.example.game;

import org.example.IO.Input;
import org.example.display.Display;
import org.example.graphics.Sprite;
import org.example.graphics.SpriteSheet;
import org.example.graphics.TextureAtlas;
import org.example.utils.Time;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Game implements Runnable {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final String TITLE = "World of Tanks";
    public static final int CLEAR_COLOR = 0xff000000;
    public static final int NUM_BUFFERS = 3;

    public static final float UPDATE_RATE = 60.0f;
    public static final float UPDATE_INTERVAL = Time.SECOND / UPDATE_RATE;
    public static final long IDOL_TIME = 1;
    public static final String ATLAS_FILE_NAME = "img.png";
    private Graphics2D graphics;
    private Input input;
    private TextureAtlas atlas;

    private boolean running;
    private Thread gameThread;
    private Player player;
    private Level level;




    public Game() {
        running = false;
        Display.create(WIDTH, HEIGHT, TITLE, CLEAR_COLOR, NUM_BUFFERS);
        graphics = Display.getGraphics();
        input = new Input();
        Display.addInputListener(input);
        atlas = new TextureAtlas(ATLAS_FILE_NAME);
        player = new Player(300,300,1, 3, atlas);
        level = new Level(atlas);

    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        player.update(input);
        level.update();
    }

    private void render() {
        Display.clear();
        level.render(graphics);
        player.render(graphics);
        Display.swapBuffers();

    }

    @Override
    public void run() {

        int fps = 0;
        int upd = 0;
        int updl = 0;

        long count = 0;

        float delta = 0;

        long lastTime = Time.get();
        while (running) {
            long now = Time.get();
            long elapsedTime = now - lastTime;
            lastTime = now;

            count += elapsedTime;

            boolean render = false;
            delta += elapsedTime / UPDATE_INTERVAL;
            while (delta > 1) {
                update();
                upd++;
                delta--;
                if (render) {
                    updl++;
                } else {
                    render = true;
                }
            }

            if (render) {
                render();
                fps++;
            } else {
                try {
                    Thread.sleep(IDOL_TIME);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (count >= Time.SECOND) {
                Display.setTitle(TITLE + " || fps: " + fps + " | upd: " + upd + " | updl: " + updl);
                upd = 0;
                fps = 0;
                updl = 0;
                count = 0;
            }
        }
    }

    private void cleanUp() {
        Display.destroy();
    }


}
