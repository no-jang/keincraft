package client.render.gl;

import client.render.texture.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private final int vao;
    private final int verticesVBO;
    private final int texCoordsVBO;
    private final int indexVBO;
    private final int vertexCount;

    private final Texture texture;

    public Mesh(float[] vertices, float[] texCoords, int[] indices, Texture texture) {
        this.texture = texture;

        vertexCount = indices.length;

        // Create and bind vao for storing vbo, ebo and attribs
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Create vertex vbo, attribs for storing vertices positions
        verticesVBO = createFloatVBO(GL15.GL_ARRAY_BUFFER, vertices);
        addVertexAttrib(0, 3, GL11.GL_FLOAT);

        // Create tex coord vbo for storing texture coordinates
        texCoordsVBO = createFloatVBO(GL15.GL_ARRAY_BUFFER, texCoords);
        addVertexAttrib(1, 3, GL11.GL_FLOAT);

        // Create index vbo for storing indices
        indexVBO = createIntVBO(GL15.GL_ELEMENT_ARRAY_BUFFER, indices);
    }

    private static int createFloatVBO(int type, float[] data) {
        int vboId;
        FloatBuffer buffer = null;
        try {
            // Store data in unsafe allocated memory
            buffer = MemoryUtil.memAllocFloat(data.length);
            buffer.put(data).flip();

            // Generate buffer and set its data
            vboId = GL15.glGenBuffers();
            GL15.glBindBuffer(type, vboId);
            GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
        } finally {
            if (buffer != null) {
                // Important: Free the memory afterwards
                MemoryUtil.memFree(buffer);
            }
        }

        return vboId;
    }

    private static int createIntVBO(int type, int[] data) {
        int vboId;
        IntBuffer buffer = null;
        try {
            // Store data in unsafe allocated memory
            buffer = MemoryUtil.memAllocInt(data.length);
            buffer.put(data).flip();

            // Generate buffer and set its data
            vboId = GL15.glGenBuffers();
            GL15.glBindBuffer(type, vboId);
            GL15.glBufferData(type, buffer, GL15.GL_STATIC_DRAW);
        } finally {
            if (buffer != null) {
                // Important: Free the memory afterwards
                MemoryUtil.memFree(buffer);
            }
        }

        return vboId;
    }

    private static void addVertexAttrib(int index, int size, int type) {
        // Create vertex attribs for sending the vbo created before to the vertex shader
        GL20.glEnableVertexAttribArray(index);
        GL20.glVertexAttribPointer(index, size, type, false, 0, 0);
    }

    private static void destroyVBO(int id, int type) {
        // Destroy and unbind vbo
        GL15.glBindBuffer(type, 0);
        GL15.glDeleteBuffers(id);
    }

    public void draw() {
        // Bind texture
        texture.bind();

        // Bind vao and vertex shader attribs
        GL30.glBindVertexArray(vao);

        // Draw the mesh
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);

        // Restore state
        GL30.glBindVertexArray(0);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void destroy() {
        // Destroy data
        destroyVBO(verticesVBO, GL15.GL_ARRAY_BUFFER); // vertices
        destroyVBO(texCoordsVBO, GL15.GL_ARRAY_BUFFER); // texCords
        destroyVBO(indexVBO, GL15.GL_ELEMENT_ARRAY_BUFFER); // indices

        // Destroy texture
        texture.destroy();

        // Destroy vao
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vao);
    }

    public int getVAO() {
        return vao;
    }
}
