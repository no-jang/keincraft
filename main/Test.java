package main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVulkan;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        GLFW.glfwInit();

        if (GLFWVulkan.glfwVulkanSupported()) {
            System.out.println("Vulkan is supported");
        } else {
            System.out.println("Vulkan IS NOT SUPPORTED");
        }

/*        Engine engine = new Engine();
        EntityRegistry entityRegistry = engine.getEntityRegistry();

        entityRegistry.addEntity(new MemoryContext(engine));

        Instance instance = entityRegistry.addEntity(Instance.builder(engine)
                .applicationName("test application")
                .engineName("test engine")
                .applicationVersion(new Version(1, 0, 0))
                .engineVersion(new Version(1, 0, 0))
                .vulkanVersion(new Version(1, 2, 0))
                .extensions(extensions -> extensions
                        .required(InstanceExtension.DEBUG_REPORT))
                .layers(layers -> layers
                        .required(InstanceLayer.KHRONOS_VALIDATION))
                .build());*/
    }
}
