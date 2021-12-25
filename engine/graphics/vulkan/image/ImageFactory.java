package engine.graphics.vulkan.image;

import engine.graphics.vulkan.device.Device;
import engine.graphics.vulkan.image.properties.ImageAspect;
import engine.graphics.vulkan.image.properties.ImageViewInfo;
import engine.graphics.vulkan.image.properties.ImageViewType;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import engine.util.enums.Maskable;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK10;
import org.lwjgl.vulkan.VkImageViewCreateInfo;

import java.nio.LongBuffer;
import java.util.Collection;
import java.util.List;

public class ImageFactory {
    public static ImageView createImageView(Device device, ImageBase image, ImageViewInfo info) {
        MemoryStack stack = MemoryContext.getStack();

        ImageViewType viewType = info.getViewType();
        if (viewType == null) {
            switch (image.getType()) {
                case ONE_DIMENSIONAL:
                    viewType = ImageViewType.ONE_DIMENSIONAL;
                    break;
                case TWO_DIMENSIONAL:
                    viewType = ImageViewType.TWO_DIMENSIONAL;
                    break;
                case THREE_DIMENSIONAL:
                    viewType = ImageViewType.THREE_DIMENSIONAL;
                    break;
            }
        }

        Collection<ImageAspect> aspects = info.getAspects();
        if (aspects == null || aspects.isEmpty()) {
            aspects = List.of(ImageAspect.COLOR);
        }

        VkImageViewCreateInfo createInfo = VkImageViewCreateInfo.malloc(stack)
                .sType$Default()
                .flags(0)
                .pNext(0)
                .image(image.getHandle())
                .viewType(viewType.getValue())
                .format(image.getFormat().getValue());

        createInfo.components()
                .r(VK10.VK_COMPONENT_SWIZZLE_IDENTITY)
                .g(VK10.VK_COMPONENT_SWIZZLE_IDENTITY)
                .b(VK10.VK_COMPONENT_SWIZZLE_IDENTITY)
                .a(VK10.VK_COMPONENT_SWIZZLE_IDENTITY);

        createInfo.subresourceRange()
                .aspectMask(Maskable.toBitMask(aspects))
                .baseMipLevel(0)
                .baseArrayLayer(0)
                .levelCount(image.getMipLevelCount())
                .layerCount(image.getArrayLayerCount());

        LongBuffer handleBuffer = stack.mallocLong(1);
        VkFunction.execute(() -> VK10.vkCreateImageView(device.getReference(), createInfo, null, handleBuffer));
        return new ImageView(handleBuffer.get(0), device, image, viewType, aspects);
    }
}
