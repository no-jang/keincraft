package engine.graphics.instance.properties;

import engine.util.enums.HasValue;

public enum InstanceLayer implements HasValue<String> {
    KHRONOS_VALIDATION("VK_LAYER_KHRONOS_validation");

    private final String value;

    InstanceLayer(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}