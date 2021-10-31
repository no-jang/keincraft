package client.render.util;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Transformation {
    public static final float FOV = (float) Math.toRadians(60.0f);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000.f;

    public static Matrix4f makeProjMatrix(int width, int height) {
        return new Matrix4f().setPerspective(FOV, (float) width / (float) height, Z_NEAR, Z_FAR);
    }

    public static Matrix4f makeViewMatrix(Vector3f position, Vector3f rotation) {
        return new Matrix4f()
                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1))
                .translate(-position.x, -position.y, -position.z);
    }

    public static Matrix4f makeModelMatrix(Vector3f position, Vector3f rotation) {
        return new Matrix4f()
                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1))
                .translate(position.x, position.y, position.z);
    }

    public static Matrix4f makeProjViewMatrix(Matrix4f projMatrix, Vector3f position, Vector3f rotation) {
        return new Matrix4f(projMatrix).mul(makeViewMatrix(position, rotation));
    }

    public static Matrix4f makeProjViewModelMatrix(Matrix4f projViewMatrix, Vector3f position, Vector3f rotation) {
        return new Matrix4f(projViewMatrix).mul(makeModelMatrix(position, rotation));
    }
}
