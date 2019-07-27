package git.kmark43.engine3d.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Window {
    private JFrame frame;

    public Window(int width, int height) {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setBackground(Color.BLACK);
        frame.setVisible(true);
        frame.createBufferStrategy(2);
    }

    public Rectangle bounds() {
        return frame.getBounds();
    }

    public Graphics2D getBufferGraphics() {
        return (Graphics2D)frame.getBufferStrategy().getDrawGraphics();
    }

    public void flip() {
        frame.getBufferStrategy().show();
    }

    public void addKeyListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    public void addMouseListener(MouseListener listener) {
        frame.addMouseListener(listener);
    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        frame.addMouseMotionListener(listener);
    }
}
