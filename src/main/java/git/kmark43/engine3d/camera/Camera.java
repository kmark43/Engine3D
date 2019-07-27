package git.kmark43.engine3d.camera;

import git.kmark43.engine3d.math.Vector3f;

public class Camera {
    private Vector3f location;
    private float yaw;
    private float pitch;

    public Camera() {
        this(new Vector3f(0, 0, 0), 0, 0);
    }

    public Camera(Vector3f location, float yaw, float pitch) {
        this.location = location;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Vector3f getLocation() {
        return location;
    }

    public void setLocation(Vector3f location) {
        this.location = location;
    }

    public Vector3f getHorizontallyForward() {
        float sy = (float) Math.sin(yaw);
        float cy = (float) Math.cos(yaw);
        return new Vector3f(-sy, 0, cy);
    }

    public Vector3f getForward() {
        float sp = (float) Math.sin(-pitch);
        float cp = (float) Math.cos(-pitch);
        float sy = (float) Math.sin(yaw);
        float cy = (float) Math.cos(yaw);
        return new Vector3f(-sy * cp, sp, cp * cy);
    }

    public Vector3f getRight() {
        float sy = (float) Math.sin(yaw + Math.PI / 2);
        float cy = (float) Math.cos(yaw + Math.PI / 2);
        return new Vector3f(-sy, 0, cy);
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
