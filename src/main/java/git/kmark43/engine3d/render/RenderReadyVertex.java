package git.kmark43.engine3d.render;

import git.kmark43.engine3d.math.Vector2f;
import git.kmark43.engine3d.math.Vector3f;
import git.kmark43.engine3d.math.Vector4f;

import java.util.Objects;

class RenderReadyVertex {
    private Vector4f loc;
    private Vector3f normal;
    private Vector2f texLoc;

    public RenderReadyVertex(Vector4f loc) {
        this.loc = Objects.requireNonNull(loc);
    }

    public RenderReadyVertex(Vector4f loc, Vector3f normal, Vector2f texLoc) {
        this.loc = Objects.requireNonNull(loc);
        this.normal = normal;
        this.texLoc = texLoc;
    }

    public Vector4f getLoc() {
        return loc;
    }

    public Vector3f getNormal() {
        return normal;
    }

    public Vector2f getTexLoc() {
        return texLoc;
    }
}
