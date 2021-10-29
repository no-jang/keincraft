package client.render.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Mesh {
    private final int vao;
    private final int vbo;
    private final int vertexCount;

    public Mesh(float[] vertices) {
        vertexCount = vertices.length / 3;

        FloatBuffer verticesBuffer = null;
        try {
            // Allocate memory for vertices buffer
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
            verticesBuffer.put(vertices).flip();

            // Create and bind vao for storing vbo and attribs
            vao = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vao);

            // Create and bind vbo for storing vertices
            vbo = GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);

            // Create vertex attribs for sending them to vertex shader
            GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, MemoryUtil.NULL);

            // Unbind vao and vbo for not interfering with other opengl components
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
            GL30.glBindVertexArray(0);
        } finally {
            // Free memory of vertices buffer
            MemoryUtil.memFree(verticesBuffer);
        }
    }

    public void draw() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // Bind vao and vertex shader attribs
        GL30.glBindVertexArray(vao);
        GL20.glEnableVertexAttribArray(0);

        // Draw the mesh
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);

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
