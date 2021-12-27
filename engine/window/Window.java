package engine.window;

import engine.test.ownable.DestroyableOwnableLongHandle;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

public class Window extends DestroyableOwnableLongHandle<Window, WindowContext> {
    private int width;
    private int height;
    private boolean isResized;

    public Window(WindowContext owner, long handle, int width, int height) {
        super(owner, handle);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void doDestroy() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
    }

    public boolean isCloseRequested() {
        throwIfDestroyed();
        return GLFW.glfwWindowShouldClose(handle);
    }

    public void resetResized() {
        throwIfDestroyed();
        isResized = false;
    }

    public int getWidth() {
        throwIfDestroyed();
        return width;
    }

    public int getHeight() {
        throwIfDestroyed();
        return height;
    }

    public boolean isResized() {
        throwIfDestroyed();
        return isResized;
    }

    void updateSize(int width, int height) {
        throwIfDestroyed();

        this.width = width;
        this.height = height;
        this.isResized = true;
    }
}
