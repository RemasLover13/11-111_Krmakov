package org.example.display;

import org.example.IO.Input;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public abstract class Display {
    private static boolean isCreated = false;
    private static JFrame frame;
    private static Canvas content;
    private static BufferedImage bufferedImage;
    private static int[] bufferData;
    private static Graphics bufferGraphics;
    private static int clearColor;

    private static BufferStrategy bufferStrategy;

    public static void create(int width, int height, String title, int _clearColor, int numBuffers) {
        if (isCreated) {
            return;
        }
        frame = new JFrame(title);
        content = new Canvas();

        Dimension size = new Dimension(width, height);
        content.setPreferredSize(size);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        bufferData = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
        bufferGraphics = bufferedImage.getGraphics();
        ((Graphics2D) bufferGraphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearColor = _clearColor;

        content.createBufferStrategy(numBuffers);
        bufferStrategy = content.getBufferStrategy();

        isCreated = true;
    }

    public static void clear() {
        Arrays.fill(bufferData, clearColor);
    }



    public static void swapBuffers() {
        Graphics g = bufferStrategy.getDrawGraphics();
        g.drawImage(bufferedImage, 0, 0, null);
        bufferStrategy.show();
    }

    public static Graphics2D getGraphics() {
        return ((Graphics2D) bufferGraphics);
    }

    public static void destroy() {

        if (!isCreated) {
            return;
        }

        frame.dispose();
    }

    public static void setTitle(String title) {
        frame.setTitle(title);
    }

    public static void addInputListener(Input inputListener) {
        frame.add(inputListener);
    }

}
