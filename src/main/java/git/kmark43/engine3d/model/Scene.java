package git.kmark43.engine3d.model;

import git.kmark43.engine3d.camera.Camera;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private Camera camera;
    private List<Mesh> objects = new ArrayList<>();

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public void addObject(Mesh mesh) {
        objects.add(mesh);
    }

    public void update() {

    }

    public Camera getCamera() {
        return camera;
    }

    public List<Mesh> getObjects() {
        return objects;
    }
}
