package client.render.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {
    private final int vao;
    private final int vbo;
    private final int ebo;
    private final int vertexCount;

    public Mesh(float[] vertices, int[] indices) {
        vertexCount = indices.length;

        // Create and bind vao for storing vbo, ebo and attribs
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // Create vbo, attribs for storing vertices positions
        vbo = createFloatVBO(GL15.GL_ARRAY_BUFFER, vertices);
        addVertexAttrib(0, 3, GL11.GL_FLOAT);

        // Create ebo for storing indices
        ebo = createIntVBO(GL15.GL_ELEMENT_ARRAY_BUFFER, indices);
    }

    public void draw() {
        // Bind vao and vertex shader attribs
        GL30.glBindVertexArray(vao);

        // Draw the mesh
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);

        // Restore state
        GL30.glBindVertexArray(0);
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

    public int getVertexCount() {
        return vertexCount;
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

    public void destroy() {
        // Delete data
        destroyVBO(vbo, GL15.GL_ARRAY_BUFFER); // vbo
        destroyVBO(ebo, GL15.GL_ELEMENT_ARRAY_BUFFER); // ebo

        // Delete vao
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vao);
    }

    public int getVAO() {
        return vao;
    }
}
