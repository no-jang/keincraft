package client.render.texture;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Image {
    private final int width;
    private final int height;
    private final ByteBuffer buffer;

    public Image(String filePath) {
        // Load texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            buffer = STBImage.stbi_load(filePath, widthBuffer, heightBuffer, channelsBuffer, 4);
            if (buffer == null) {
                throw new RuntimeException("Could not load image file " + filePath + ": " + STBImage.stbi_failure_reason());
            }

            width = widthBuffer.get();
            height = heightBuffer.get();
        }
    }

    public void destroy() {
        STBImage.stbi_image_free(buffer);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
