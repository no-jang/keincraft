package client.input;

import client.render.Window;
import client.render.gl.Camera;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseManager {
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private final Vector2f displVec;

    private final Window window;

    private boolean mouseLocked = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseManager(Window window) {
        this.window = window;

        this.previousPos = new Vector2d();
        this.currentPos = new Vector2d();
        this.displVec = new Vector2f();
    }

    public void init() {
        // Mouse position callback
        GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, x, y) -> {
            currentPos.x = x;
            currentPos.y = y;
        });

        // Mouse button pressed / released callback
        GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(Camera camera) {
        if(rightButtonPressed) {
            GLFW.glfwSetCursorPos(window.getWindowHandle(), window.getWidth() / 2.0f, window.getHeight() / 2.0f);
            GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

            mouseLocked = true;
        }

        displVec.x = 0;
        displVec.y = 0;

        if (previousPos.x > 0 && previousPos.y > 0 && mouseLocked) {
            double deltaX = currentPos.x - previousPos.x;
            double deltaY = currentPos.y - previousPos.y;

            if (deltaX != 0) {
                displVec.y = (float) deltaX;
            }

            if (deltaY != 0) {
                displVec.x = (float) deltaY;
            }
        }

        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;

        camera.moveRotation(displVec.x * MOUSE_SENSITIVITY, displVec.y * MOUSE_SENSITIVITY, 0);
    }

    public void unlockMouse() {
        GLFW.glfwSetInputMode(window.getWindowHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        mouseLocked = false;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
