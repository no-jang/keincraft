package engine.window;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.helper.pointer.Destroyable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class WindowContext implements Destroyable {
    private final ImmutableList<Window> windows;

    public WindowContext() {
        windows = new DefaultImmutableList<>(new ArrayList<>());
    }

    void addWindow(Window window) {
        windows.toMutable().add(window);
    }

    public ImmutableList<Window> getWindows() {
        return windows;
    }

    @Override
    public void destroy() {
        GLFW.glfwTerminate();
    }

    public void input() {
        GLFW.glfwPollEvents();
    }
}
