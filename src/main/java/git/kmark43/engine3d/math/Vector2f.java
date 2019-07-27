package git.kmark43.engine3d.math;

public class Vector2f {
    private float x;
    private float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double dot(Vector2f o) {
        return x * o.x + y * o.y;
    }

    public double lengthSquared() {
        return this.dot(this);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector2f scale(float scalar) {
        return new Vector2f(x * scalar, y * scalar);
    }

    public Vector2f add(Vector2f o) {
        return new Vector2f(x + o.x, y + o.y);
    }

    public Vector2f sub(Vector2f o) {
        return add(o.scale(-1f));
    }

    @Override
    public String toString() {
        return "Vector2f{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
