package client.render.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Texture {
    private final int id;

    public Texture(String texturePath) {
        int width;
        int height;
        ByteBuffer buffer;

        // Load texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            buffer = STBImage.stbi_load(texturePath, widthBuffer, heightBuffer, channelsBuffer, 4);
            if (buffer == null) {
                throw new RuntimeException("Could not load image file " + texturePath + ": " + STBImage.stbi_failure_reason());
            }

            width = widthBuffer.get();
            height = heightBuffer.get();
        }

        // Create new texture object
        id = GL11.glGenTextures();

        // Bind the textures
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        // Tell opengl how to unpack RGBA bytes. Each component is one byte
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

        // Upload texture data
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Generate Mip Map
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        STBImage.stbi_image_free(buffer);
    }

    public void bind() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void destroy() {
        GL11.glDeleteTextures(id);
    }
}
