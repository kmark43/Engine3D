package git.kmark43.engine3d.math;

public class MutableVector3f {
    private float x;
    private float y;
    private float z;

    public MutableVector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MutableVector3f(MutableVector3f o) {
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

    public double dot(MutableVector3f o) {
        return x * o.x + y * o.y + z * o.z;
    }

    public double lengthSquared() {
        return this.dot(this);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public MutableVector3f normalize() {
        return scale(1f / (float)length());
    }

    public MutableVector3f scale(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public MutableVector3f add(MutableVector3f o) {
        x += o.x;
        y += o.y;
        z += o.z;
        return this;
    }

    public MutableVector3f sub(MutableVector3f o) {
        return add(o.scale(-1f));
    }

    public MutableVector3f mul(MutableVector3f o) {
        x *= o.x;
        y *= o.y;
        z *= o.z;
        return this;
    }

    public MutableVector3f transform(Matrix4f m) {
        return transform(m, 1);
    }

    public MutableVector3f transform(Matrix4f m, float w) {
        float x_ = x * m.get(0, 0) + y * m.get(0, 1) + z * m.get(0, 2) + w * m.get(0, 3);
        float y_ = x * m.get(1, 0) + y * m.get(1, 1) + z * m.get(1, 2) + w * m.get(1, 3);
        float z_ = x * m.get(2, 0) + y * m.get(2, 1) + z * m.get(2, 2) + w * m.get(2, 3);
        float w_ = x * m.get(3, 0) + y * m.get(3, 1) + z * m.get(3, 2) + w * m.get(3, 3);
        if (w_ != 1) {
            x_ /= w_;
            y_ /= w_;
            z_ /= w_;
        }
        x = x_;
        y = y_;
        z = z_;
        return this;
    }

    public Vector3f cement() {
        return new Vector3f(x, y, z);
    }

    @Override
    public String toString() {
        return "MutableVector3f{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
