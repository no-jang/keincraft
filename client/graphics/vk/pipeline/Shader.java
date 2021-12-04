package client.graphics.vk.pipeline;

import client.graphics.vk.device.Device;
import client.graphics.vk.util.Check;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkPipelineShaderStageCreateInfo;
import org.lwjgl.vulkan.VkShaderModuleCreateInfo;

import java.io.IOException;
import java.nio.LongBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Shader {
    private final long handle;
    private final int stage;

    public Shader(MemoryStack stack, Device device, Path file) {
        byte[] code;
        try {
            code = Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader code: " + file, e);
        }

        VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pCode(stack.bytes(code));

        LongBuffer pHandle = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateShaderModule(device.getHandle(), createInfo, null, pHandle), "Failed to create shader: " + file);
        handle = pHandle.get(0);

        stage = getShaderStage(file);
    }

    public void destroy(Device device) {
        VK10.vkDestroyShaderModule(device.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }

    public int getStage() {
        return stage;
    }

    private static int getShaderStage(Path path) {
        String fileName = path.getFileName()
                .toString()
                .replace(".spv", "");

        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (fileExtension) {
            case "vert":
                return VK10.VK_SHADER_STAGE_VERTEX_BIT;
            case "frag":
                return VK10.VK_SHADER_STAGE_FRAGMENT_BIT;
            default:
                throw new RuntimeException("Unknown shader stage " + fileExtension + " for " + path);
        }
    }
}
