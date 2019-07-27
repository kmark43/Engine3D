package git.kmark43.engine3d.math;

public class Matrix4f {
    private float[][] m = new float[4][4];

    private Matrix4f(float[][] m) {
        if (m.length != 4 || m[0].length != 4) {
            throw new IllegalArgumentException("Must be 4x4");
        }
        this.m = m;
    }

    public float get(int r, int c) {
        return m[r][c];
    }

    public void set(int r, int c, float value) {
        m[r][c] = value;
    }

    public Matrix4f multiply(Matrix4f other) {
        float[][] v = new float[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                v[i][j] = m[i][0] * other.m[0][j] +
                        m[i][1] * other.m[1][j] +
                        m[i][2] * other.m[2][j] +
                        m[i][3] * other.m[3][j];
            }
        }
        return new Matrix4f(v);
    }

    private static float[][] eyeM() {
        float[][] m = new float[4][4];
        for (int i = 0; i < 4; i++) {
            m[i][i] = 1;
        }
        return m;
    }

    public static Matrix4f eye() {
        return new Matrix4f(eyeM());
    }

    public static Matrix4f scalar(float x, float y, float z) {
        float[][] m = eyeM();
        m[0][0] = x;
        m[1][1] = y;
        m[2][2] = z;
        return new Matrix4f(m);
    }

    public static Matrix4f transformation(float x, float y, float z) {
        float[][] m = eyeM();
        m[0][3] = x;
        m[1][3] = y;
        m[2][3] = z;
        return new Matrix4f(m);
    }

    public static Matrix4f rotation(float yaw, float pitch, float roll) {
        Matrix4f yawM = new Matrix4f(new float[][] {
                {1, 0, 0, 0},
                {0, (float)Math.cos(pitch), (float)-Math.sin(pitch), 0},
                {0, (float)Math.sin(pitch), (float)Math.cos(pitch), 0},
                {0, 0, 0, 1}
        });

        Matrix4f pitchM = new Matrix4f(new float[][] {
                {(float)Math.cos(yaw), 0, (float)Math.sin(yaw), 0},
                {0, 1, 0, 0},
                {(float)-Math.sin(yaw), 0, (float)Math.cos(yaw), 0},
                {0, 0, 0, 1}
        });

        Matrix4f rollM = new Matrix4f(new float[][] {
                {(float)Math.cos(roll), (float)-Math.sin(roll), 0, 0},
                {(float)Math.sin(roll), (float)Math.cos(roll), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });

        return yawM.multiply(pitchM.multiply(rollM));
    }

    public static Matrix4f projection(float fov, float ar, float n, float f) {
        float top = (float)Math.tan(fov / 2) * n;
        float bottom = -top;
        float right = top * ar;
        float left = -top * ar;
        return new Matrix4f(new float[][]{
                {2 * n / (right - left), 0, (right + left) / (right - left), 0},
                {0, 2 * n / (top - bottom), (top + bottom) / (top - bottom), 0},
                {0, 0, -(f + n) / (f - n), -2 * f * n / (f - n)},
                {0, 0, -1, 0}
        });
    }
}
