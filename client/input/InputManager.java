package client.input;

import client.render.Window;
import client.render.gl.Camera;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class InputManager {
    private static final float CAMERA_POS_STEP = 0.05f;
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Window window;

    private final Vector3f cameraInc;

    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private final Vector2f displVec;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public InputManager(Window window) {
        this.window = window;

        this.previousPos = new Vector2d();
        this.currentPos = new Vector2d();
        this.displVec = new Vector2f();
        this.cameraInc = new Vector3f();
    }

    public void init(Window window) {
        // Mouse position callback
        GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, x, y) -> {
            currentPos.x = x;
            currentPos.y = y;
        });

        // Cursor is in window callback
        GLFW.glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> inWindow = entered);

        // Mouse button pressed / released callback
        GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(Camera camera) {
        // Camera rotation
        displVec.x = 0;
        displVec.y = 0;

        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltaX = currentPos.x - previousPos.x;
            double deltaY = currentPos.y - previousPos.y;

            if (deltaX != 0) {
                displVec.x = (float) deltaX;
            }

            if (deltaY != 0) {
                displVec.y = (float) deltaY;
            }
        }

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;

        // Camera position
        cameraInc.set(0, 0, 0);

        if (isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        }

        if (isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        }

        if (isKeyPressed(GLFW.GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (isKeyPressed(GLFW.GLFW_KEY_X)) {
            cameraInc.y = 1;
        }

        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        if (isRightButtonPressed()) {
            camera.movePosition(displVec.x * MOUSE_SENSITIVITY, displVec.y * MOUSE_SENSITIVITY, 0);
        }
    }

    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(window.getWindowHandle(), key) == GLFW.GLFW_PRESS;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
