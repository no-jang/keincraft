package client;

import client.render.RenderManager;
import client.render.Window;

public class MinecraftClient {
    private Window window;
    private RenderManager renderManager;

    public MinecraftClient() {

    }

    public void run() {
        init();
        loop();
        destroy();
    }

    private void init() {
        window = new Window(900, 900);
        //renderManager = new RenderManager();
    }

    private void loop() {
        while (!window.shouldClose()) {
            input();
            update();
            render();
        }
    }

    private void input() {
        window.input();
    }

    private void update() {

    }

    private void render() {
        //renderManager.render(window);
    }

    private void destroy() {
        //renderManager.destroy();
        window.destroy();
    }
}
