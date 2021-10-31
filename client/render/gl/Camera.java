package client.render.gl;

import common.util.math.PosRot;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera extends PosRot {
    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    private final Matrix4f modelViewMatrix;

    public Camera() {
        projectionMatrix = new Matrix4f();
        viewMatrix = new Matrix4f();
        modelViewMatrix = new Matrix4f();
    }

    public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        return projectionMatrix.setPerspective(fov, width / height, zNear, zFar);
    }

    public Matrix4f getViewMatrix() {
        viewMatrix.identity()
                .rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .translate(-position.x, -position.y, -position.z);

        return viewMatrix;
    }

    public Matrix4f getModelViewMatrix(PosRot posRot, Matrix4f viewMatrix) {
        Vector3f rot = posRot.getRotation();

        modelViewMatrix.identity()
                .translate(posRot.getPosition())
                .rotateX((float) Math.toRadians(-rot.x))
                .rotateY((float) Math.toRadians(-rot.y))
                .rotateZ((float) Math.toRadians(-rot.z));

        Matrix4f viewCurrent = new Matrix4f(viewMatrix);
        return viewCurrent.mul(modelViewMatrix);
    }
}
