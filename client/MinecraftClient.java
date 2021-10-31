package client;

import client.input.InputManager;
import client.render.RenderManager;
import client.render.Window;

public class MinecraftClient {
    private final Window window;
    private final InputManager inputManager;
    private final RenderManager renderManager;

    public MinecraftClient() {
        this.window = new Window();
        this.inputManager = new InputManager(window);
        this.renderManager = new RenderManager(this);
    }

    public void run() {
        init();
        loop();
        destroy();
    }

    private void init() {
        window.init();
        inputManager.init(window);
        renderManager.init();
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
        inputManager.input(renderManager.getCamera());
    }

    private void update() {

    }

    private void render() {
        renderManager.render();
        window.render();
    }

    private void destroy() {
        renderManager.destroy();
        window.destroy();
    }

    public Window getWindow() {
        return window;
    }
}
