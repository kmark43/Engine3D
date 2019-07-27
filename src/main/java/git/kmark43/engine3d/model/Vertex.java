package git.kmark43.engine3d.model;

import git.kmark43.engine3d.math.Vector2f;
import git.kmark43.engine3d.math.Vector3f;

import java.util.Objects;

public class Vertex {
    private Vector3f loc;
    private Vector3f normal;
    private Vector2f texLoc;

    public Vertex(Vector3f loc) {
        this(loc, null, null);
    }

    public Vertex(Vector3f loc, Vector3f normal, Vector2f texLoc) {
        this.loc = Objects.requireNonNull(loc);
        this.normal = normal;
        this.texLoc = texLoc;
    }

    public Vector3f getLoc() {
        return loc;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector2f getTexLoc() {
        return texLoc;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "loc=" + loc +
                ", normal=" + normal +
                ", texLoc=" + texLoc +
                '}';
    }
}
