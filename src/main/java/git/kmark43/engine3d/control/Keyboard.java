package git.kmark43.engine3d.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class Keyboard implements KeyListener {
    private HashSet<Integer> pressedKeys = new HashSet<>();

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean isPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public void clearPressedKeys() {
        pressedKeys.clear();
    }
}
