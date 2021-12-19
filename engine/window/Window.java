package engine.window;

import engine.helper.pointer.DestroyablePointer;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

public class Window extends DestroyablePointer {
    private int width;
    private int height;
    private boolean isResized;

    public Window(long handle, int width, int height) {
        super(handle);

        this.width = width;
        this.height = height;
    }

    @Override
    protected void destroy(long handle) {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }

    public boolean isCloseRequested() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void resetResized() {
        isResized = false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return isResized;
    }

    void updateSize(int width, int height) {
        this.width = width;
        this.height = height;
        this.isResized = true;
    }
}
