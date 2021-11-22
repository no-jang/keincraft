package client;

import client.graphics.Graphics;
import client.graphics.Window;

/**
 * Core class for keincraft
 */
public class KeincraftClient {
    private final Window window;
    private final Graphics graphics;

    /**
     * Initializes keincraft
     */
    public KeincraftClient() {
        window = new Window("keincraft", 900, 900);
        graphics = new Graphics(window);
    }

    /**
     * Starts the game loop for input, updating and rendering tasks
     */
    public void loop() {
        while (!window.shouldClose()) {
            input();
            update();
            render();
        }
    }

    /**
     * Everything regarded to player inputs like key presses, mouse clicks and movement, ...
     */
    private void input() {
        window.input();
    }

    /**
     * Everything regarded update the game logic like receiving packets, calculate entities, ...
     */
    private void update() {

    }

    /**
     * Everything regarded to rendering a frame
     */
    private void render() {
        //graphics.render();
    }

    /**
     * Destroys the game instance and unloads everything
     */
    public void destroy() {
        //graphics.destroy();
        window.destroy();
    }

    /**
     * @return game window
     * @see Window
     */
    public Window getWindow() {
        return window;
    }

    /**
     * @return graphics module
     * @see Graphics
     */
    //public Graphics getGraphics() {
    //return graphics;
    //}
}
