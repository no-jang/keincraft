package client.render.chunk;

import client.render.gl.Shader;

import java.util.ArrayList;
import java.util.List;

public class ChunkRenderer {
    private final List<ChunkMesh> chunks;

    private Shader shader;

    public ChunkRenderer() {
        this.chunks = new ArrayList<>();
    }

    public void init() {
        shader = new Shader("shaders/chunk.vs", "shaders/chunk.fs");
    }

    public void render() {
        shader.bind();
    }

    public void addChunk(ChunkMesh mesh) {
        chunks.add(mesh);
    }
}
