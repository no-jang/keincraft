package engine.graphics.vulkan.properties;

import engine.util.enums.Maskable;
import org.lwjgl.vulkan.VK10;

public enum SampleCount implements Maskable {
    ONE(VK10.VK_SAMPLE_COUNT_1_BIT),
    TWO(VK10.VK_SAMPLE_COUNT_2_BIT),
    FOUR(VK10.VK_SAMPLE_COUNT_4_BIT),
    EIGHT(VK10.VK_SAMPLE_COUNT_8_BIT),
    SIXTEEN(VK10.VK_SAMPLE_COUNT_16_BIT),
    THIRTY_TWO(VK10.VK_SAMPLE_COUNT_32_BIT),
    SIXTY_FOUR(VK10.VK_SAMPLE_COUNT_64_BIT);

    private final int bit;

    SampleCount(int bit) {
        this.bit = bit;
    }

    @Override
    public int getBit() {
        return bit;
    }
}
