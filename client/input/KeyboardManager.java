package client.input;

import client.render.Window;
import client.render.gl.Camera;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyboardManager {
    private static final float CAMERA_POS_STEP = 0.05f;

    private final List<Integer> keysPressed;

    private Window window;
    private MouseManager mouseManager;

    public KeyboardManager(Window window, MouseManager mouseManager) {
        this.window = window;
        this.mouseManager = mouseManager;

        this.keysPressed = new ArrayList<>();
    }

    public void init() {
        // Key pressed callback
        GLFW.glfwSetKeyCallback(window.getWindowHandle(), (windowHandle, key, scancode, action, mods) -> {
            if(action == GLFW.GLFW_PRESS) {
                keysPressed.add(key);
            } else if(action == GLFW.GLFW_RELEASE) {
                keysPressed.remove((Object) key);
            }
        });
    }

    public void input(Camera camera) {
        // Close window on esc
        if(isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
            mouseManager.unlockMouse();
        }

        Vector3f cameraInc = new Vector3f();

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
    }

    public boolean isKeyPressed(int key) {
        return keysPressed.contains(key);
    }
}
