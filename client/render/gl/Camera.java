package client.render.gl;

import client.render.util.Transformation;
import common.util.math.PosRot;
import org.joml.Matrix4f;

public class Camera extends PosRot {
    private Matrix4f projMatrix;
    private Matrix4f projViewMatrix;

    public void updateView(int width, int height) {
        projMatrix = Transformation.makeProjMatrix(width, height);
    }

    public void update() {
        projViewMatrix = Transformation.makeProjViewMatrix(projMatrix, position, rotation);
    }

    public Matrix4f getProjViewMatrix() {
        return projViewMatrix;
    }
}
