package client;

import client.render.Window;

public class MinecraftClient
{
    private final Window window;
    
    public MinecraftClient() {
        this.window = new Window();
    }
    
    public void run() {
        init();
        loop();
        destroy();
    }
    
    private void init() {
        window.init();
    }
    
    private void loop() {
        while(!window.shouldClose()){
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
        window.render();
    }
    
    private void destroy() {
        window.destroy();
    }
}
