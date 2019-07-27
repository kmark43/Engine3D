package git.kmark43.engine3d;

import git.kmark43.engine3d.camera.Camera;
import git.kmark43.engine3d.control.Keyboard;
import git.kmark43.engine3d.math.Vector3f;
import git.kmark43.engine3d.model.Mesh;
import git.kmark43.engine3d.model.Scene;
import git.kmark43.engine3d.render.RenderUtil;
import git.kmark43.engine3d.window.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        Window window = new Window(800, 600);
        Keyboard keyboard = new Keyboard();
        window.addKeyListener(keyboard);
        Camera camera = new Camera(new Vector3f(0, 0, -20), 0f, 0);
        Scene scene = new Scene(camera);
        Mesh monkey = null;
        Mesh cube = null;
        try {
            monkey = Mesh.fromObj(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("monkey.obj")))));
            cube = Mesh.fromObj(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("cube.obj")))));
            try {
                cube.setTexture(ImageIO.read(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("tile.png"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        scene.addObject(monkey);
//        scene.addObject(cube);

        while (true) {
            updateCamera(camera, keyboard);
            scene.update();
            Graphics2D g = window.getBufferGraphics();
            RenderUtil.render(g, window.bounds(), scene);
            g.dispose();
            window.flip();
            try {
                Thread.sleep(13);
            } catch(InterruptedException e) {}
        }
    }

    private static void updateCamera(Camera camera, Keyboard keyboard) {
        float moveSpeed = .2f;
        float rotateSpeed = .03f;

        if (keyboard.isPressed(KeyEvent.VK_W)) {
            Vector3f dir = camera.getHorizontallyForward();
            camera.setLocation(camera.getLocation().add(dir.scale(moveSpeed)));
        }

        if (keyboard.isPressed(KeyEvent.VK_S)) {
            Vector3f dir = camera.getHorizontallyForward();
            camera.setLocation(camera.getLocation().add(dir.scale(-moveSpeed)));
        }

        if (keyboard.isPressed(KeyEvent.VK_A)) {
            Vector3f dir = camera.getRight();
            camera.setLocation(camera.getLocation().add(dir.scale(-moveSpeed)));
        }

        if (keyboard.isPressed(KeyEvent.VK_D)) {
            Vector3f dir = camera.getRight();
            camera.setLocation(camera.getLocation().add(dir.scale(moveSpeed)));
        }

        if (keyboard.isPressed(KeyEvent.VK_SPACE)) {
            camera.setLocation(camera.getLocation().add(new Vector3f(0, moveSpeed, 0)));
        }

        if (keyboard.isPressed(KeyEvent.VK_SHIFT)) {
            camera.setLocation(camera.getLocation().add(new Vector3f(0, -moveSpeed, 0)));
        }

        if (keyboard.isPressed(KeyEvent.VK_LEFT)) {
            camera.setYaw(camera.getYaw() - rotateSpeed);
        }

        if (keyboard.isPressed(KeyEvent.VK_RIGHT)) {
            camera.setYaw(camera.getYaw() + rotateSpeed);
        }

        if (keyboard.isPressed(KeyEvent.VK_UP)) {
            camera.setPitch(camera.getPitch() - rotateSpeed);
        }

        if (keyboard.isPressed(KeyEvent.VK_DOWN)) {
            camera.setPitch(camera.getPitch() + rotateSpeed);
        }
    }
}
