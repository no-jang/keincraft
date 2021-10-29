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

        FloatBuffer verticesBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            // Allocate memory for vertices and indices buffer
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            verticesBuffer.put(vertices).flip();
            indicesBuffer.put(indices).flip();

            // Create and bind vao for storing vbo, ebo and attribs
            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);

            // Create and bind vbo for storing vertices
            vbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

            // Create vertex attribs for sending them to vertex shader
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, MemoryUtil.NULL);

            // Create and bind ebo for storing indices
            ebo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);

            // Unbind vao and vbo for not interfering with other opengl components
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        } finally {
            // Free memory of vbo and ebo
            MemoryUtil.memFree(verticesBuffer);
            MemoryUtil.memFree(indicesBuffer);
        }
    }

    public void draw() {
        // Bind vao and vertex shader attribs
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);

        // Draw the mesh
        // GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
        GL11.glDrawElements(GL11.GL_TRIANGLES, vertexCount, GL11.GL_UNSIGNED_INT, 0);

        // Restore state
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void destroy() {
        // Delete vertex shader attribs
        GL20.glDisableVertexAttribArray(0);

        // Delete vbo
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vbo);

        // Delete ebo
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(ebo);

        // Delete vao
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vao);
    }

    public int getVao() {
        return vao;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
