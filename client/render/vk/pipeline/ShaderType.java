package client.render.vk.pipeline;

import org.lwjgl.vulkan.VK10;

public enum ShaderType {
    VERTEX_SHADER(VK10.VK_PIPELINE_STAGE_VERTEX_SHADER_BIT),
    FRAGMENT_SHADER(VK10.VK_PIPELINE_STAGE_FRAGMENT_SHADER_BIT);

    private final int index;

    ShaderType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
