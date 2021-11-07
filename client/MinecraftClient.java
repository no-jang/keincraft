package client;

import client.render.Window;

public class MinecraftClient {
    private Window window;

    public MinecraftClient() {

    }

    public void run() {
        init();
        loop();
        destroy();
    }

    private void init() {
        window = new Window(900, 900);
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

    }

    private void destroy() {

    }

    public Window getWindow() {
        return window;
    }
}
