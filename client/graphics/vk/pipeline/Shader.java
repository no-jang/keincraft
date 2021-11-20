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

/**
 * Shaders are programmes that run on the graphic device. This Shader class creates one shader stage (for example vertex, fragment)
 */
public class Shader {
    private final long handle;
    private final VkPipelineShaderStageCreateInfo stageCreateInfo;

    /**
     * Loads and creates shader from file. Prepares VkPipelineShaderStageCreateInfo for pipeline creation.
     * File must end on .vert (vertex shader), .frag (fragment shader)
     *
     * @param stack  memory stack
     * @param device vulkan device
     * @param file   file to shader file. Must end on .vert, .frag
     */
    public Shader(MemoryStack stack, Device device, Path file) {
        // Load shader code from file
        byte[] code;
        try {
            code = Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load shader code " + file, e);
        }

        VkShaderModuleCreateInfo createInfo = VkShaderModuleCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .pCode(stack.bytes());

        // Create shader
        LongBuffer pShader = stack.mallocLong(1);
        Check.vkCheck(VK10.vkCreateShaderModule(device.getHandle(), createInfo, null, pShader), "Failed to create shader " + file);
        handle = pShader.get(0);

        // Prepare pipeline shader create info
        stageCreateInfo = VkPipelineShaderStageCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .module(handle)
                .stage(getShaderStage(file))
                .pName(stack.UTF8("main"));
    }

    /**
     * @param path path to shader file
     * @return shader type based on file ending
     */
    private static int getShaderStage(Path path) {
        String fileName = path.getFileName().toString();
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

    /**
     * Destroys the shader. Should be called after pipeline creation
     *
     * @param device vulkan device
     */
    public void destroy(Device device) {
        VK10.vkDestroyShaderModule(device.getHandle(), handle, null);
    }

    /**
     * @return internal shader vulkan handle
     */
    public long getHandle() {
        return handle;
    }

    /**
     * @return pipeline shader stage create info for pipeline creation
     * @see VkPipelineShaderStageCreateInfo
     */
    public VkPipelineShaderStageCreateInfo getStageCreateInfo() {
        return stageCreateInfo;
    }
}
