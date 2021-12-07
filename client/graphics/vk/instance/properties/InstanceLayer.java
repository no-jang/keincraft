package client.graphics.vk.instance.properties;

import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.HasValue;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VkLayerProperties;

import java.util.ArrayList;
import java.util.List;

public enum InstanceLayer implements HasValue<String> {
    KHRONOS_VALIDATION("VK_LAYER_KHRONOS_validation");

    private final String value;

    InstanceLayer(String value) {
        this.value = value;
    }

    public static List<InstanceLayer> fromVulkanExtensions(VkLayerProperties.Buffer pLayers) {
        List<InstanceLayer> layers = new ArrayList<>();

        for (int i = 0; i < pLayers.capacity(); i++) {
            InstanceLayer layer = HasValue.getByValue(pLayers.get(i).layerNameString(), InstanceLayer.class);
            if (layer != null) {
                layers.add(layer);
            }
        }

        return layers;
    }

    public static PointerBuffer toVulkanBuffer(List<InstanceLayer> extensions) {
        MemoryStack stack = MemoryContext.getStack();
        PointerBuffer pBuffer = stack.mallocPointer(extensions.size());

        for (int i = 0; i < extensions.size(); i++) {
            pBuffer.put(i, stack.ASCII(extensions.get(i).getValue()));
        }

        return pBuffer;
    }

    @Override
    public String getValue() {
        return value;
    }
}
