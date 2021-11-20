package client.render.vk.pipeline.shader;

import client.graphics.vk.device.Device;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;

import java.io.IOException;
import java.nio.LongBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static client.render.vk.Global.vkCheck;
import static org.lwjgl.vulkan.VK10.vkCreateShaderModule;
import static org.lwjgl.vulkan.VK10.vkDestroyShaderModule;

public class Shader {
    private final long handle;
    private final VkPipelineShaderStageCreateInfo shaderStageCreateInfo;

    public Shader(MemoryStack stack, Device device, ShaderType type, byte[] code) {
        VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pCode(stack.bytes(code));

        LongBuffer pShaderModule = stack.mallocLong(1);
        vkCheck(vkCreateShaderModule(device.getHandle(), createInfo, null, pShaderModule), "Failed to create shader module");
        handle = pShaderModule.get(0);

        shaderStageCreateInfo = VkPipelineShaderStageCreateInfo.calloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .module(handle)
                .stage(type.getIndex())
                .pName(stack.UTF8("main"));
    }

    public static Shader readFromFile(MemoryStack stack, Device device, ShaderType type, String path) {
        try {
            byte[] code = Files.readAllBytes(Path.of(path));
            return new Shader(stack, device, type, code);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader from file: " + path, e);
        }
    }

    public void destroy(Device device) {
        vkDestroyShaderModule(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }

    public VkPipelineShaderStageCreateInfo getShaderStageCreateInfo() {
        return shaderStageCreateInfo;
    }
}
