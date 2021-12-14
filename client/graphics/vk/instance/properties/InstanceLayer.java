package client.graphics.vk.instance.properties;

import common.util.enums.HasValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.vulkan.VkLayerProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper enum for vulkan instance validation layers. Validation layers are used to validate the input given to Vulkan
 * and report errors if necessary.
 */
public enum InstanceLayer implements HasValue<String> {
    KHRONOS_VALIDATION("VK_LAYER_KHRONOS_validation");

    private final String value;

    InstanceLayer(String value) {
        this.value = value;
    }

    /**
     * Converts a {@link VkLayerProperties.Buffer} to {@link InstanceLayer}
     *
     * @param pLayers layer properties buffer
     * @return converted InstanceLayers
     * @see VkLayerProperties.Buffer
     */
    @NotNull
    public static List<InstanceLayer> fromBuffer(@NotNull VkLayerProperties.Buffer pLayers) {
        List<InstanceLayer> layers = new ArrayList<>();

        for (int i = 0; i < pLayers.capacity(); i++) {
            InstanceLayer layer = HasValue.getByValue(pLayers.get(i).layerNameString(), InstanceLayer.class);
            if (layer != null) {
                layers.add(layer);
            }
        }

        return layers;
    }

    @Override
    public String getValue() {
        return value;
    }
}
