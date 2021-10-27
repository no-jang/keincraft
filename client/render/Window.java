package client.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

public class Window
{
    private long windowHandle;
    
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        
        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }
        
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        
        windowHandle = GLFW.glfwCreateWindow(300, 300, "Minecraft School", MemoryUtil.NULL, MemoryUtil.NULL);
        if (windowHandle == MemoryUtil.NULL) {
            throw new IllegalStateException("Unable to create window");
        }

        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);

            GLFW.glfwGetWindowSize(windowHandle, width, height);
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            GLFW.glfwSetWindowPos(windowHandle, (vidMode.width() - width.get()) / 2, (vidMode.height() - height.get()) / 2);
        }

        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(windowHandle);
    }
    
    public void input() {
        GLFW.glfwPollEvents();
    }
    
    public void render() {
        GLFW.glfwSwapBuffers(windowHandle);
    }
    
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(windowHandle);
    }
    
    public long getWindowHandle() {
        return windowHandle;
    }
}
