package client.render.texture;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class TextureArray extends Texture {
    private final int textureSize;

    private int textureCount;

    public TextureArray(int textureCount, int textureSize) {
        super(null);
        this.textureSize = textureSize;

        // Create texture
        id = GL11.glGenTextures();

        // Bind texture
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bind();

        // Set texture attributes
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL30.GL_TEXTURE_2D_ARRAY, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        // Set texture data
        GL12.glTexImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, GL11.GL_RGBA, textureSize, textureSize, textureCount,
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, MemoryUtil.NULL);
    }

    public int addTexture(Image image) {
        // Set subimage data
        GL12.glTexSubImage3D(GL30.GL_TEXTURE_2D_ARRAY, 0, 0, 0, textureCount, textureSize, textureSize,
                1, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image.getBuffer());

        // Generate mipmap for performance
        GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D_ARRAY);

        // Set subimage attributes
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1);

        return textureCount++;
    }

    @Override
    public void bind() {
        GL11.glBindTexture(GL30.GL_TEXTURE_2D_ARRAY, id);
    }

    @Override
    public void destroy() {
        GL11.glDeleteTextures(id);
    }
}
