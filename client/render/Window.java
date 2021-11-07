package client.render;

import org.lwjgl.bgfx.BGFXInit;
import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.bgfx.BGFX.*;

public class Window {
    private final long handle;

    private int width;
    private int height;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;

        if(!glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

        if(Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
        }

        handle = glfwCreateWindow(width, height, "minecraft-clone", 0, 0);
        if(handle == NULL) {
            throw new RuntimeException("Unable to create GLFW window");
        }

        glfwSetFramebufferSizeCallback(handle, (window, newWidth, newHeight) -> {
            setFramebufferSize(newWidth, newHeight);
        });

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(handle, true);
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        try(MemoryStack stack = stackPush()) {
            BGFXInit init = BGFXInit.mallocStack(stack);
            bgfx_init_ctor(init);
            init.type(BGFX_RENDERER_TYPE_OPENGL)
                    .resolution(it -> it
                            .width(width)
                            .height(height)
                            .reset(BGFX_RESET_VSYNC));

            switch (Platform.get()) {
                case LINUX -> init.platformData()
                        .ndt(GLFWNativeX11.glfwGetX11Display())
                        .nwh(GLFWNativeX11.glfwGetX11Window(handle));
                case MACOSX -> init.platformData()
                        .nwh(GLFWNativeCocoa.glfwGetCocoaWindow(handle));
                case WINDOWS -> init.platformData()
                        .nwh(GLFWNativeWin32.glfwGetWin32Window(handle));
            }

            if(!bgfx_init(init)) {
                throw new RuntimeException("Unable to initialize bgfx renderer");
            }
        }
    }

    public void input() {
        glfwPollEvents();
    }

    public void destroy() {
        bgfx_shutdown();
        glfwDestroyWindow(handle);
        glfwTerminate();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getHandle() {
        return handle;
    }

    private void setFramebufferSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
