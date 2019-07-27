package git.kmark43.engine3d.model;

import git.kmark43.engine3d.math.Vector2f;
import git.kmark43.engine3d.math.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mesh {
    private List<Face> faces;
    private Vector3f location;
    private Vector3f rotation;
    private Vector3f scale;
    private BufferedImage texture;

    public Mesh(List<Face> faces) {
        this(faces, new Vector3f(0, 0, 0));
    }

    public Mesh(List<Face> faces, Vector3f location) {
        this(faces, location, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
    }

    public Mesh(List<Face> faces, Vector3f location, Vector3f rotation, Vector3f scale) {
        this.faces = faces;
        this.location = location;
        this.rotation = rotation;
        this.scale = scale;
    }

    public List<Face> getFaces() {
        return new ArrayList<>(faces);
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void setTexture(BufferedImage texture) {
        this.texture = texture;
    }

    @Override
    public String toString() {
        return "Mesh{" +
                "faces=" + faces +
                '}';
    }

    public static Mesh fromObj(BufferedReader reader) throws IOException {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normalVertices = new ArrayList<>();
        List<Vector2f> textureCoords = new ArrayList<>();
        List<Face> faces = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("") || line.startsWith("#")) continue;
            if (line.startsWith("v ")) {
                String[] tokens = line.split(" ");
                vertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
            } else if (line.startsWith("f ")) {
                String[] tokens = line.split(" ");
                faces.add(parseFace(tokens, vertices, normalVertices, textureCoords));
            } else if (line.startsWith("vn ")) {
                String[] tokens = line.split(" ");
                normalVertices.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
            } else if (line.startsWith("vt ")) {
                String[] tokens = line.split(" ");
                textureCoords.add(new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2])));
            }
        }

        return new Mesh(faces);
    }

    private static Face parseFace(String[] tokens, List<Vector3f> vertices, List<Vector3f> normalVertices, List<Vector2f> textureCoords) {
        List<Vertex> vertexList = new ArrayList<>();
        for (int i = 1; i < tokens.length; i++) {
            String token = tokens[i];
            String[] subtokens = token.split("/");

            Integer vIndex = null;
            Integer nIndex = null;
            Integer tIndex = null;

            if (subtokens.length >= 1) {
                vIndex = attemptParse(subtokens[0]);
            }
            if (subtokens.length >= 2) {
                tIndex = attemptParse(subtokens[1]);
            }
            if (subtokens.length >= 3) {
                nIndex = attemptParse(subtokens[2]);
            }

            assert vIndex != null;

            if (nIndex != null && tIndex != null) {
                vertexList.add(new Vertex(vertices.get(vIndex - 1), normalVertices.get(nIndex - 1), textureCoords.get(tIndex - 1)));
            } else if (nIndex != null) {
                vertexList.add(new Vertex(vertices.get(vIndex - 1), normalVertices.get(nIndex - 1), null));
            } else if (tIndex != null) {
                vertexList.add(new Vertex(vertices.get(vIndex - 1), null, textureCoords.get(tIndex - 1)));
            } else {
                vertexList.add(new Vertex(vertices.get(vIndex - 1), null, null));
            }
        }
        return new Face(vertexList);
    }

    private static Integer attemptParse(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
