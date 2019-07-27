package git.kmark43.engine3d.model;

import git.kmark43.engine3d.math.MutableVector3f;
import git.kmark43.engine3d.math.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Face {
    private final List<Vertex> vertices;
    private final Vector3f surfaceNormal;
    private Color color;

    public Face(List<Vertex> vertices) {
        if (vertices.size() != 3) {
            throw new UnsupportedOperationException("Non triangulated verticies not supported yet");
        }
        this.vertices = Objects.requireNonNull(vertices);
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());

        MutableVector3f normal = new MutableVector3f(0, 0, 0);
        for (int i = 0; i < vertices.size(); i++) {
            Vector3f cur = vertices.get(i).getLoc();
            Vector3f next = vertices.get((i + 1) % vertices.size()).getLoc();
            normal.add(new MutableVector3f(
                    (cur.getY() - next.getY()) * (cur.getZ() + next.getZ()),
                    (cur.getZ() - next.getZ()) * (cur.getX() + next.getX()),
                    (cur.getX() - next.getX()) * (cur.getY() + next.getY())
            ));
        }
        surfaceNormal = normal.cement();
    }

    public List<Vertex> getVertices() {
        return new ArrayList<>(vertices);
    }

    public Vector3f getSurfaceNormal() {
        return surfaceNormal;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Face{" +
                "vertices=" + vertices +
                ", surfaceNormal=" + surfaceNormal +
                '}';
    }
}
