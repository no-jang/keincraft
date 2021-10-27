import client.MinecraftClient;

public class Main {
    public static void main(String[] args) {
        boolean isServer = false;
        
        for(String arg : args) {
            if(arg == "--server") {
                isServer = true;
            }
        }
        
        if(isServer) {
            runServer();
        } else {
            runClient();
        }
    }
    
    public static void runClient() {
        MinecraftClient client = new MinecraftClient();
        client.run();
    }
    
    public static void runServer() {
        
    }
}