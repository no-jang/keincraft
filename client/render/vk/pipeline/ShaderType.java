package client.render.vk.pipeline;

import org.lwjgl.vulkan.VK10;

public enum ShaderType {
    VERTEX_SHADER(VK10.VK_SHADER_STAGE_VERTEX_BIT),
    FRAGMENT_SHADER(VK10.VK_SHADER_STAGE_FRAGMENT_BIT);

    private final int index;

    ShaderType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
