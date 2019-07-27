package git.kmark43.engine3d.math;

public class Vector3f {
    private final float x;
    private final float y;
    private final float z;

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f o) {
        this.x = o.x;
        this.y = o.y;
        this.z = o.z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public double dot(Vector3f o) {
        return x * o.x + y * o.y + z * o.z;
    }

    public double lengthSquared() {
        return this.dot(this);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector3f normalize() {
        return scale(1f / (float)length());
    }

    public Vector3f scale(float scalar) {
        return new Vector3f(x * scalar, y * scalar, z * scalar);
    }

    public Vector3f add(Vector3f o) {
        return new Vector3f(x + o.x, y + o.y, z + o.z);
    }

    public Vector3f sub(Vector3f o) {
        return add(o.scale(-1f));
    }

    public Vector3f mul(Vector3f o) {
        return new Vector3f(x * o.x, y * o.y, z * o.z);
    }

    public Vector4f transform(Matrix4f m) {
        return transform(m, 1);
    }

    public Vector4f transform(Matrix4f m, float w) {
        float x_ = x * m.get(0, 0) + y * m.get(0, 1) + z * m.get(0, 2) + w * m.get(0, 3);
        float y_ = x * m.get(1, 0) + y * m.get(1, 1) + z * m.get(1, 2) + w * m.get(1, 3);
        float z_ = x * m.get(2, 0) + y * m.get(2, 1) + z * m.get(2, 2) + w * m.get(2, 3);
        float w_ = x * m.get(3, 0) + y * m.get(3, 1) + z * m.get(3, 2) + w * m.get(3, 3);
        if (w_ != 1) {
            x_ /= w_;
            y_ /= w_;
            z_ /= w_;
        }
        return new Vector4f(x_, y_, z_, w_);
    }

    public MutableVector3f mutableCopy() {
        return new MutableVector3f(x, y, z);
    }

    @Override
    public String toString() {
        return "Vector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
