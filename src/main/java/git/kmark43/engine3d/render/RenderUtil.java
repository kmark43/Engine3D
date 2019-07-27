package git.kmark43.engine3d.render;

import git.kmark43.engine3d.camera.Camera;
import git.kmark43.engine3d.math.Matrix4f;
import git.kmark43.engine3d.math.MutableVector3f;
import git.kmark43.engine3d.math.Vector3f;
import git.kmark43.engine3d.math.Vector4f;
import git.kmark43.engine3d.model.Face;
import git.kmark43.engine3d.model.Mesh;
import git.kmark43.engine3d.model.Scene;
import git.kmark43.engine3d.model.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RenderUtil {
    public static void render(Graphics2D g, Rectangle bounds, Scene scene) {
        Camera camera = scene.getCamera();
        List<Mesh> objects = scene.getObjects();
        Matrix4f translation = Matrix4f.transformation(-camera.getLocation().getX(), -camera.getLocation().getY(), -camera.getLocation().getZ());
        Matrix4f rotation = Matrix4f.rotation(camera.getYaw(), -camera.getPitch(), 0);
        Matrix4f perspective = Matrix4f.projection((float)Math.toRadians(70), (float)bounds.width / bounds.height, 0.01f, 100f);

        Matrix4f transform = perspective.multiply(rotation.multiply(translation));

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bounds.width, bounds.height);
        g.setColor(Color.WHITE);
        for (Mesh object : objects) {
            renderObject(g, bounds, transform, camera.getForward(), object);
        }
    }

    private static int renderCount = 0;
    private static void renderObject(Graphics2D g, Rectangle bounds, Matrix4f transform, Vector3f cameraDir, Mesh object) {
        float[][] zBuf = new float[bounds.width][bounds.height];

        Matrix4f translation = Matrix4f.transformation(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ());
        Matrix4f rotation = Matrix4f.rotation(object.getRotation().getX(), object.getRotation().getY(), object.getRotation().getZ());
        Matrix4f scale = Matrix4f.scalar(object.getScale().getX(), object.getScale().getY(), object.getScale().getZ());
        Matrix4f objTransform = translation.multiply(rotation.multiply(scale));
        Matrix4f finalTransform = transform.multiply(objTransform);

        for (Face face : object.getFaces()) {
            renderFace(g, bounds, finalTransform, zBuf, object.getTexture(), face);
        }
        renderCount++;
    }

    private static void renderFace(Graphics2D g, Rectangle bounds, Matrix4f transform, float[][] zBuf, BufferedImage textureImage, Face face) {
        List<Vertex> faceVertices = face.getVertices();
        List<RenderReadyVertex> vertices = new ArrayList<>();
        for (int i = 0; i < faceVertices.size(); i++) {
            vertices.add(new RenderReadyVertex(faceVertices.get(i).getLoc().transform(transform, 1), faceVertices.get(i).getNormal(), faceVertices.get(i).getTexLoc()));
        }

        vertices.sort(Comparator.comparing(v -> v.getLoc().getY()));

        g.setColor(face.getColor());

        int x1 = (int)((vertices.get(0).getLoc().getX() + .5f) * bounds.getWidth());
        int y1 = (int)((vertices.get(0).getLoc().getY() + .5f) * bounds.getHeight());
        float z1 = vertices.get(0).getLoc().getZ();
        float w1 = 1 / vertices.get(0).getLoc().getW();
        float u1 = vertices.get(0).getTexLoc() == null ? 0 : vertices.get(0).getTexLoc().getX() * w1;
        float v1 = vertices.get(0).getTexLoc() == null ? 0 : vertices.get(0).getTexLoc().getY() * w1;

        int x2 = (int)((vertices.get(1).getLoc().getX() + .5f) * bounds.getWidth());
        int y2 = (int)((vertices.get(1).getLoc().getY() + .5f) * bounds.getHeight());
        float z2 = vertices.get(1).getLoc().getZ();
        float w2 = 1 / vertices.get(1).getLoc().getW();
        float u2 = vertices.get(1).getTexLoc() == null ? 0 : vertices.get(1).getTexLoc().getX() * w2;
        float v2 = vertices.get(1).getTexLoc() == null ? 0 : vertices.get(1).getTexLoc().getY() * w2;

        int x3 = (int)((vertices.get(2).getLoc().getX() + .5f) * bounds.getWidth());
        int y3 = (int)((vertices.get(2).getLoc().getY() + .5f) * bounds.getHeight());
        float z3 = vertices.get(2).getLoc().getZ();
        float w3 = 1 / vertices.get(2).getLoc().getW();
        float u3 = vertices.get(2).getTexLoc() == null ? 0 : vertices.get(2).getTexLoc().getX() * w3;
        float v3 = vertices.get(2).getTexLoc() == null ? 0 : vertices.get(2).getTexLoc().getY() * w3;

        if (y3 >= 0 && y1 < zBuf[0].length && y1 != y3) {
            if (y1 == y2) {
                drawFlatTopTriangle(g, zBuf,
                        x1, x2, x3,
                        z1, z2, z3,
                        w1, w2, w3,
                        u1, u2, u3,
                        v1, v2, v3,
                        y1, y3,
                        textureImage
                );
            } else if (y2 == y3) {
                drawFlatBottomTriangle(g, zBuf,
                        x1, x2, x3,
                        z1, z2, z3,
                        w1, w2, w3,
                        u1, u2, u3,
                        v1, v2, v3,
                        y1, y3,
                        textureImage
                );
            } else {
                float splitX = x1 + (y2 - y1) * (x3 - x1) / (y3 - y1);
                float splitZ = z1 + (y2 - y1) * (z3 - z1) / (y3 - y1);
                float splitW = w1 + (y2 - y1) * (w3 - w1) / (y3 - y1);
                float splitU = u1 + (y2 - y1) * (u3 - u1) / (y3 - y1);
                float splitV = v1 + (y2 - y1) * (v3 - v1) / (y3 - y1);
                drawFlatBottomTriangle(g, zBuf,
                        x1, x2, splitX,
                        z1, z2, splitZ,
                        w1, w2, splitW,
                        u1, u2, splitU,
                        v1, v2, splitV,
                        y1, y2,
                        textureImage
                );
                drawFlatTopTriangle(g, zBuf,
                        x2, splitX, x3,
                        z2, splitZ, z3,
                        w2, splitW, w3,
                        u2, splitU, u3,
                        v2, splitV, v3,
                        y2, y3,
                        textureImage
                );
            }
        }
    }

    private static void drawFlatBottomTriangle(Graphics2D g, float[][] zBuf,
                                               float x1, float x2, float x3,
                                               float z1, float z2, float z3,
                                               float w1, float w2, float w3,
                                               float u1, float u2, float u3,
                                               float v1, float v2, float v3,
                                               int y1, int y2,
                                               BufferedImage textureImage) {

        float dy = (float)(y2 - y1);

        float dx12 = (x2 - x1) / dy;  float dx13 = (x3 - x1) / dy;
        float dz12 = (z2 - z1) / dy;  float dz13 = (z3 - z1) / dy;
        float dw12 = (w2 - w1) / dy;  float dw13 = (w3 - w1) / dy;
        float du12 = (u2 - u1) / dy;  float du13 = (u3 - u1) / dy;
        float dv12 = (v2 - v1) / dy;  float dv13 = (v3 - v1) / dy;

        float sx = x1;
        float ex = x1;

        float sz = z1;
        float ez = z1;

        float sw = w1;
        float ew = w1;

        float su = u1;
        float eu = u1;

        float sv = v1;
        float ev = v1;

        int y = y1;
        if (x2 < x3) {
            for (; y < y2 && y < zBuf[0].length; y++,
                    sx += dx12, ex += dx13,
                    sz += dz12, ez += dz13,
                    sw += dw12, ew += dw13,
                    su += du12, eu += du13,
                    sv += dv12, ev += dv13) {
                drawHorizontalLine(g, zBuf, (int)sx, (int)ex, sz, ez, sw, ew, su, eu, sv, ev, y, textureImage);
            }
        } else {
            for (; y < y2 && y < zBuf[0].length; y++,
                    sx += dx13, ex += dx12,
                    sz += dz13, ez += dz12,
                    sw += dw13, ew += dw12,
                    su += du13, eu += du12,
                    sv += dv13, ev += dv12) {
                drawHorizontalLine(g, zBuf, (int)sx, (int)ex, sz, ez, sw, ew, su, eu, sv, ev, y, textureImage);
            }
        }
    }

    private static void drawFlatTopTriangle(Graphics2D g, float[][] zBuf,
                                            float x1, float x2, float x3,
                                            float z1, float z2, float z3,
                                            float w1, float w2, float w3,
                                            float u1, float u2, float u3,
                                            float v1, float v2, float v3,
                                            int y1, int y2,
                                            BufferedImage textureImage) {

        float dy = (float)(y1 - y2);

        float dx31 = (x1 - x3) / dy;  float dx32 = (x2 - x3) / dy;
        float dz31 = (z1 - z3) / dy;  float dz32 = (z2 - z3) / dy;
        float dw31 = (w1 - w3) / dy;  float dw32 = (w2 - w3) / dy;
        float du31 = (u1 - u3) / dy;  float du32 = (u2 - u3) / dy;
        float dv31 = (v1 - v3) / dy;  float dv32 = (v2 - v3) / dy;

        float sx = x3;
        float ex = x3;

        float sz = z3;
        float ez = z3;

        float sw = w3;
        float ew = w3;

        float su = u3;
        float eu = u3;

        float sv = v3;
        float ev = v3;

        int y = y2;
        if (x1 < x2) {
            for (; y >= y1 && y >= 0; y--,
                    sx -= dx31, ex -= dx32,
                    sz -= dz31, ez -= dz32,
                    sw -= dw31, ew -= dw32,
                    su -= du31, eu -= du32,
                    sv -= dv31, ev -= dv32) {
                drawHorizontalLine(g, zBuf, (int)sx, (int)ex, sz, ez, sw, ew, su, eu, sv, ev, y, textureImage);
            }
        } else {
            for (; y >= y1 && y >= 0; y--,
                    sx -= dx32, ex -= dx31,
                    sz -= dz32, ez -= dz31,
                    sw -= dw32, ew -= dw31,
                    su -= du32, eu -= du31,
                    sv -= dv32, ev -= dv31) {
                drawHorizontalLine(g, zBuf, (int)sx, (int)ex, sz, ez, sw, ew, su, eu, sv, ev, y, textureImage);
            }
        }
    }

    private static void drawHorizontalLine(Graphics2D g, float[][] zBuf, int sx, int ex,
                                           float sz, float ez,
                                           float sw, float ew,
                                           float su, float eu, float sv, float ev,
                                           int y, BufferedImage textureImage) {
        float dx = (float)(ex - sx);

        float z = sz;
        float w = sw;
        float u = su;
        float v = sv;

        float dw = (ew - sw) / dx;
        float dz = (ez - sz) / dx;
        float du = (eu - su) / dx;
        float dv = (ev - sv) / dx;

        int x = sx;
        if (sx < 0) {
            x = 0;
            z = sz - sx * dz;
            w = sw - sx * dw;
            u = su - sx * du;
            v = sv - sx * dv;
        }

        for (; x <= ex && x >= 0 && x < zBuf.length; x++, z += dz, w += dw, u += du, v += dv) {
            if (y >= 0 && y < zBuf[0].length && z > zBuf[x][y]) {
                if (textureImage != null) {
                    int uPrime = Math.min(textureImage.getWidth() - 1, Math.max(0, (int) (textureImage.getWidth() * u * (1 / w) + .5)));
                    int vPrime = Math.min(textureImage.getHeight() - 1, Math.max(0, (int) (textureImage.getHeight() * v * (1 / w) + .5)));
                    g.setColor(new Color(textureImage.getRGB(uPrime, vPrime)));
                }
                g.drawLine(x, y, x, y);
                zBuf[x][y] = z;
            }
        }
    }
}
