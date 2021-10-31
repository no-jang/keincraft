package client;

import client.input.KeyboardManager;
import client.input.MouseManager;
import client.render.RenderManager;
import client.render.Window;

public class MinecraftClient {
    private final Window window;
    private final MouseManager mouseManager;
    private final KeyboardManager keyboardManager;
    private final RenderManager renderManager;

    public MinecraftClient() {
        this.window = new Window(800, 800);
        this.mouseManager = new MouseManager(window);
        this.keyboardManager = new KeyboardManager(window, mouseManager);
        this.renderManager = new RenderManager(this);
    }

    public void run() {
        init();
        loop();
        destroy();
    }

    private void init() {
        window.init();

        mouseManager.init();
        keyboardManager.init();

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
        mouseManager.input(renderManager.getCamera());
        keyboardManager.input(renderManager.getCamera());
    }

    private void update() {

    }

    private void render() {
        renderManager.render(window);
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
