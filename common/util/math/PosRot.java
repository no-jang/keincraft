package common.util.math;

import org.joml.Vector3f;

public class PosRot {
    protected final Vector3f position;
    protected final Vector3f rotation;

    public PosRot(int x, int y, int z, int rotX, int rotY, int rotZ) {
        position = new Vector3f(x, y, z);
        rotation = new Vector3f(rotX, rotY, rotZ);
    }

    public PosRot(int x, int y, int z) {
        this(x, y, z, 0, 0, 0);
    }

    public PosRot() {
        this(0, 0, 0);
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }

        if (offsetX != 0) {
            position.x += Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetZ;
        }

        position.y += offsetY;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }
}
